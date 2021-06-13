package pw.byakuren.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.*;
import pw.byakuren.discord.commands.CompatabilityCommand;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.modules.Module;
import pw.byakuren.discord.modules.*;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.util.BotEmbed;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends ListenerAdapter {
    private static String prefix = getPrefix();

    private CommandHelper cmdhelp = new CommandHelper();
    private ModuleHelper mdhelp = new ModuleHelper();
    private DatabaseManager dbmg;
    private ExecutorService threadPool = Executors.newFixedThreadPool(32);

    private Cache cache;

    public static void main(String[] args) {
        String token = getToken();
        try {
            JDA jda = JDABuilder.createLight(token).build();
            jda.addEventListener(new Main());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private static String getPrefix() {
        String next;
        String dir = System.getProperty("user.dir");

        try {
            Scanner file = new Scanner(new File(dir+"/prefix"));
            next = file.next();
            System.out.println("Prefix set to " + next);
        } catch (FileNotFoundException e) {
            next = "j["; //default prefix
            System.out.println("Prefix not found, using default prefix "+next);
        }
        return next;
    }

    private static String getToken() {
        String next;
        String dir = System.getProperty("user.dir");
        try {
            Scanner file = new Scanner(new File(dir+"/token"));
            next = file.next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Token file not found.");
        }
        return next;
    }

    private void connectToDatabase(JDA jda) {
        try {
            dbmg = new DatabaseManager(new SQLConnection(), jda);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCommands(JDA jda) {
        cmdhelp.registerCommand(jda, new Stop(dbmg, cache));
        cmdhelp.registerCommand(jda, new Invite());
        cmdhelp.registerCommand(jda, new Modules(mdhelp));
        cmdhelp.registerCommand(jda, new Help(cmdhelp, cache));
        cmdhelp.registerCommand(jda, new SetModeratorRole(cache));
        cmdhelp.registerCommand(jda, new UserInfo(cache));
        cmdhelp.registerCommand(jda, new ServerInfo());
        cmdhelp.registerCommand(jda, new RegexKeys(cache));
        cmdhelp.registerCommand(jda, new ExcludedChannels(cache));
        cmdhelp.registerCommand(jda, new WatchUser(cache));
        cmdhelp.registerCommand(jda, new WatchRole(cache));
        cmdhelp.registerCommand(jda, new SetLogChannel(cache));
        cmdhelp.registerCommand(jda, new VoiceBanCommand(cache));
        cmdhelp.registerCommand(jda, new EightBall());
        cmdhelp.registerCommand(jda, new CompatabilityCommand());
        cmdhelp.registerCommand(jda, new FilterActionCommand(cache));
        System.out.printf("Loaded %d commands.\n", cmdhelp.getCommandSet().size());
    }

    private void loadModules() {
        mdhelp.registerModule(new StatisticManager(cache));
        mdhelp.registerModule(new RegexChecker(cache));
        mdhelp.registerModule(new VoiceWatchReporter(cache));
        mdhelp.registerModule(new RoleWatchReporter(cache));
        mdhelp.registerModule(new VoiceBanWatcher(cache));
        mdhelp.registerModule(new FilterActionHandler(cache));
        System.out.printf("Loaded %s modules.\n", mdhelp.getModules().size());
    }

    @Override
    public void onReady(ReadyEvent event) {
        connectToDatabase(event.getJDA());
        cache = new Cache(dbmg, event.getJDA());
        loadCommands(event.getJDA());
        loadModules();
        for (Module md: mdhelp.getModules().keySet()) {
            if (md.getInfo().type==ModuleType.COMMAND_MODULE) {
                md.run(cmdhelp);
            }
        }
    }

    @Override
    public void onGenericEvent(GenericEvent event) {
        for (Module md: mdhelp.getModules().keySet()) {
            if (md.getInfo().type== ModuleType.EVENT_MODULE && mdhelp.isEnabled(md)) {
                threadPool.execute(() -> md.run((Event)event));
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor(); //get message author
        Message message = event.getMessage(); //get message object
        String msg = message.getContentDisplay(); //get message content

        if (author.isBot()) {
            return;
        } //i dont like bots

        dbmg.updateLastMessage(event.getMessage());

        for (Module md: mdhelp.getModules().keySet()) {
            if (md.getInfo().type==ModuleType.MESSAGE_MODULE && mdhelp.isEnabled(md)) {
                threadPool.execute(() -> md.run(message));
            }
        }

        if (msg.startsWith(prefix)) {
            List<String> args_raw = Arrays.asList(msg.split(" "));
            ArrayList<String> args = new ArrayList<>(args_raw);
            Command cmd = cmdhelp.getCommand(args.remove(0).substring(prefix.length()));
            if (cmd == null) {
                message.addReaction("❔").queue();
                return;
            }
            /* Command permission checking */
            if (cmd.canRun(message.getMember(), cache)) {
                try {
                    threadPool.execute(() -> {
                        try {
                            cmd.run(message, args);
                        } catch (Exception e) {
                            reportError(message, e);
                        }
                    });
                } catch (Exception e) {
                    reportError(message, e);
                }
                return;
            }
            message.addReaction("❌").queue();
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild g =event.getGuild();
        System.out.println("Joined new server "+g.getName());
        cache.getServerCache(g);
        System.out.println("Loaded cache for "+g.getName());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        String cmd_name = event.getName();
        Command cmd = cmdhelp.getCommand(cmd_name);
        if (cmd instanceof RichCommand) {
            if (cmd.canRun(event.getMember(), cache)) {
                threadPool.execute(() -> {
                    try {
                        ((RichCommand) cmd).runSlash(event);
                    } catch (Exception e) {
                        reportErrorPrivate(event.getUser(), e);
                    }
                });

            } else {
                event.reply("You don't have permission to run that").setEphemeral(true).queue();
            }
        } else {
            event.reply("If you see this message, please report a bug. :)").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        Command cmd = cmdhelp.resolveButtonID(event.getComponentId());
        if (cmd == null) {
            //unregistered button ID
            System.out.printf("Unregistered button ID %s triggered", event.getComponentId());
        } else {
            ((RichCommand) cmd).onButtonClick(event);
        }
    }


    public static void reportError(Message m, Exception e) {
        m.reply(BotEmbed.error(e).build())
                .setActionRow(Button.link("https://github.com/Brod8362/discord-bot-2/issues", "Report a bug"))
                .queue();
    }

    public static void reportErrorPrivate(User u, Exception e) {
        u.openPrivateChannel().complete().sendMessage("There was an exception while running your command.\n" +
                "Please try again. If the problem persists, please report a bug.")
                .embed(BotEmbed.error(e).build());

    }
}
