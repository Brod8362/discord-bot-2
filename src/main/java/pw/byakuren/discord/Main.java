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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pw.byakuren.discord.commands.*;
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
import java.util.stream.Collectors;


public class Main extends ListenerAdapter {
    private static String prefix = getPrefix();

    private CommandHelper cmdhelp = new CommandHelper();
    private ModuleHelper mdhelp = new ModuleHelper();
    private DatabaseManager dbmg;

    private Cache cache;

    public static void main(String[] args) {
        String token = getToken();
        try {
            JDA jda = new JDABuilder(token).build();
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

    private void loadCommands() {
        cmdhelp.registerCommand(new Stop(dbmg, cache));
        cmdhelp.registerCommand(new Invite());
        cmdhelp.registerCommand(new Modules(mdhelp));
        cmdhelp.registerCommand(new Help(cmdhelp, cache));
        cmdhelp.registerCommand(new SetModeratorRole(cache));
        cmdhelp.registerCommand(new UserInfo(cache));
        cmdhelp.registerCommand(new ServerInfo());
        cmdhelp.registerCommand(new RegexKeys(cache));
        cmdhelp.registerCommand(new ExcludedChannels(cache));
        cmdhelp.registerCommand(new WatchUser(cache));
        cmdhelp.registerCommand(new WatchRole(cache));
        cmdhelp.registerCommand(new SetLogChannel(cache));
        cmdhelp.registerCommand(new VoiceBanCommand(cache));
        cmdhelp.registerCommand(new EightBall());
        System.out.println(String.format("Loaded %s commands.", cmdhelp.getCommandSet().size()));
    }

    private void loadModules() {
        mdhelp.registerModule(new StatisticManager(cache));
        mdhelp.registerModule(new RegexChecker(cache));
        mdhelp.registerModule(new VoiceWatchReporter(cache));
        mdhelp.registerModule(new RoleWatchReporter(cache));
        mdhelp.registerModule(new VoiceBanWatcher(cache));
        System.out.println(String.format("Loaded %s modules.", mdhelp.getModules().size()));
    }

    @Override
    public void onReady(ReadyEvent event) {
        connectToDatabase(event.getJDA());
        cache = new Cache(dbmg, event.getJDA());
        loadCommands();
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
                new Thread(() -> md.run((Event)event)).start();
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
                new Thread(() -> md.run(message)).start();
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
                    new Thread(() -> {
                        try {
                            cmd.run(message, args);
                        } catch (Exception e) {
                            reportError(message, e);
                        }
                    }).start();
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

    public static void reportError(Message m, Exception e) {
        m.getChannel().sendMessage(BotEmbed.error(e).build()).queue();
    }
}
