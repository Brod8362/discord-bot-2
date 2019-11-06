package pw.byakuren.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pw.byakuren.discord.commands.*;
import pw.byakuren.discord.modules.Module;
import pw.byakuren.discord.modules.ModuleHelper;
import pw.byakuren.discord.modules.ModuleType;
import pw.byakuren.discord.modules.StatisticManager;
import pw.byakuren.discord.objects.cache.Cache;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends ListenerAdapter {
    private static String prefix = getPrefix();

    private CommandHelper cmdhelp = new CommandHelper();
    private ModuleHelper mdhelp = new ModuleHelper();
    private DatabaseManager dbmg;
    private User owner;

    Cache cache;

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
        cmdhelp.registerCommand(new Test());
        cmdhelp.registerCommand(new Invite());
        cmdhelp.registerCommand(new Modules(mdhelp));
        cmdhelp.registerCommand(new Help(cmdhelp));
        cmdhelp.registerCommand(new UserInfo(cache));
        cmdhelp.registerCommand(new ServerInfo());
        cmdhelp.registerCommand(new SQL(dbmg));
        cmdhelp.registerCommand(new RegexKeys(cache));
        cmdhelp.registerCommand(new ExcludedChannels(dbmg));
        cmdhelp.registerCommand(new Subscribe(dbmg));
        cmdhelp.registerCommand(new WatchUser(dbmg));
        cmdhelp.registerCommand(new WatchRole(dbmg));
        cmdhelp.registerCommand(new SetLogChannel(cache));

        System.out.println(String.format("Loaded %s commands.", cmdhelp.getCommands().size()));
    }

    private void loadModules(JDA jda) {
        mdhelp.registerModule(new StatisticManager(cache));
        System.out.println(String.format("Loaded %s modules.", mdhelp.getModules().size()));
    }

    private boolean isBotOwner(User u) {
      return u.equals(owner);
    }

    @Override
    public void onReady(ReadyEvent event) {
        connectToDatabase(event.getJDA());
        cache = new Cache(dbmg, event.getJDA());
        loadCommands();
        loadModules(event.getJDA());
        try {
            owner = event.getJDA().getApplicationInfo().complete(true).getOwner();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
        for (Module md: mdhelp.getModules().keySet()) {
            if (md.getInfo().type==ModuleType.COMMAND_MODULE) {
                md.run(cmdhelp);
            }
        }


    }

    @Override
    public void onGenericEvent(Event event) {
        //todo run as separate thread
        for (Module md: mdhelp.getModules().keySet()) {
            if (md.getInfo().type== ModuleType.EVENT_MODULE && mdhelp.isEnabled(md)) {
                md.run(event);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor(); //get message author
        Message message = event.getMessage(); //get message object
        String msg = message.getContentDisplay(); //get message content

        if (author.isBot()) {
            return;
        } //i dont like bots

        dbmg.updateLastMessage(event.getMessage());

        for (Module md: mdhelp.getModules().keySet()) {
            if (md.getInfo().type==ModuleType.MESSAGE_MODULE && mdhelp.isEnabled(md)) {
                md.run(message);
            }
        }

        String arg = "";
        try {
            arg = msg.substring(prefix.length()); //this is meant to grab the command name
        } catch (StringIndexOutOfBoundsException ignored) {} //this is called when the message is too short or there is no message body
        if (msg.startsWith(prefix)) {
            for (String key : cmdhelp.getCommands().keySet()) { //check all commands
                Command cmd = cmdhelp.getCommands().get(key);
                if (arg.startsWith(cmd.getName())) { //check to see if called command matches a registered one
                    List<String> args = new ArrayList<>(); //instantiate list for args
                    Scanner parse = new Scanner(msg.substring(prefix.length() + cmd.getName().length())); //remove command
                    while (parse.hasNext()) {
                        args.add(parse.next()); //put all args into list
                    }
                    /* For commands that require the user to be the bot hoster */
                    if (cmd.needsBotOwner()) {
                        if (isBotOwner(message.getAuthor())) {
                            cmd.run(message, args);
                        } else {
                            message.addReaction("❌").queue();
                            return;
                        }
                    }
                    cmd.run(message, args); //run given command w/ args
                }
            }
        }
    }
}
