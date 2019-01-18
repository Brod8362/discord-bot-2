package pw.byakuren.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pw.byakuren.discord.commands.*;
import pw.byakuren.discord.modules.Module;
import pw.byakuren.discord.modules.ModuleHelper;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends ListenerAdapter {
    private static String prefix = getPrefix();

    private CommandHelper cmdhelp = new CommandHelper();
    private ModuleHelper mdhelp = new ModuleHelper();

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

    private void loadCommands() {
        cmdhelp.registerCommand(new Stop());
        cmdhelp.registerCommand(new Test());
        cmdhelp.registerCommand(new Invite());
        cmdhelp.registerCommand(new Modules(mdhelp));
        cmdhelp.registerCommand(new Help(cmdhelp));


        System.out.println(String.format("Loaded %s commands.", cmdhelp.getCommands().size()));
    }

    private void loadModules(JDA jda) {
        System.out.println(String.format("Loaded %s modules.", mdhelp.getModules().size()));
    }

    @Override
    public void onReady(ReadyEvent event) {
        loadCommands();
        loadModules(event.getJDA());
        for (Module md: mdhelp.getModules().keySet()) {
            if (md.isExtension()) {
                md.run(cmdhelp);
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

        for (Module md: mdhelp.getModules().keySet()) {
            if (!md.isExtension() && mdhelp.isEnabled(md)) {
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
                        cmd.run(message, args); //run given command w/ args
                }
            }
        }
    }
}
