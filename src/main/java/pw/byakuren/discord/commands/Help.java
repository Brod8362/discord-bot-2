package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Help extends Command {

    CommandHelper cmdhelp;
    private Cache c;

    public Help(CommandHelper cmd, Cache c) {
        names=new String[]{"help", "h"};
        help="See command listing.";
        syntax="`@User` represents a user mention.\n" +
                "`#Channel` represents a channel mention.\n" +
                "`@Role` represents a role mention.\n" +
                "`[]` represents a required argument.\n" +
                "`<>` represents an optional argument.\n" +
                "You can view help about subcommands like this: `help command.subcommand`";
        minimum_permission=CommandPermission.REGULAR_USER;

        cmdhelp = cmd;
        this.c =c;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() == 0) {
            cmd_list(message, args, false);
        } else if (args.size() == 1 && args.get(0).equals("-a")) {
            cmd_list(message, args, true);
        } else {
            cmd_details(message, args);
        }
    }

    private void cmd_list(Message message, List<String> args, boolean showall) {
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Help listing");
        StringBuilder desc = new StringBuilder();

        for (Command cmd: cmdhelp.getCommandSet()) {
            if ( cmd.canRun(message.getMember(), c))
                desc.append(String.format("**%s** - %s\n", cmd.getNames()[0], cmd.getHelp()));
            else
                if ( showall )
                    desc.append(String.format("~~%s~~ - %s\n", cmd.getNames()[0], cmd.getHelp()));
        }
        b.setDescription(desc);
        b.setFooter("Use the help command with a command name to see more information. " +
                "Run with -a to see all commands.", null);
        message.getChannel().sendMessage(b.build()).queue();
    }

    private void cmd_details(Message message, List<String> args) {
        String[] cmds = args.get(0).split("\\.");
        EmbedBuilder b = new EmbedBuilder();
        if (cmds.length == 1) {
            Command c = cmdhelp.getCommand(args.get(0));
            if (c == null) {
               cmd_not_found(message, args.get(0));
            } else {
                /* Command found */
                build_help_embed(message, c.getPrimaryName(), c);
            }
            return;
        }

        //info about subcommands
        Command c = cmdhelp.getCommand(cmds[0]);
        Subcommand d = null;
        String name = c.getPrimaryName();
        for (int i = 1; i < cmds.length; i++) {
            name+="."+cmds[i];
            Subcommand e = c.getSubcommand(cmds[i]);
            if (e == null)
                cmd_not_found(message, name);
            d = e;
        }
        if (d == null) return;
        build_help_embed(message, name, d);
    }

    private void cmd_not_found(Message message, String t) {
        /* Command not found */
        EmbedBuilder b = new EmbedBuilder();
        b.setDescription("Unknown command '"+t+"'");
        b.setFooter("Use the help command with no arguments to see all commands.", null);
        message.getChannel().sendMessage(b.build()).queue();
    }

    private void build_help_embed(Message message, String name, Command c) {
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("**"+name+"** ("+c.minimumPermission().name+")");
        String help = c.getHelp();
        String syntax = c.getSyntax();
        if (help == null) help = "No help defined.";
        if (syntax == null) syntax = "No syntax defined.";
        b.addField("Help", help, false);
        b.addField("Syntax", syntax, false);
        List<Subcommand> sub_cmds= c.getSubcommands();
        String subcmds = "";
        if (sub_cmds != null && sub_cmds.size() > 0 && sub_cmds.get(0).getPrimaryName().equals("")) {
            sub_cmds = sub_cmds.subList(1, sub_cmds.size());
            subcmds = sub_cmds.stream().map(Command::getPrimaryName).collect(Collectors.joining(", "));
        }
        if (subcmds.isEmpty()) subcmds = "None";
        b.addField("Subcommands", subcmds, false);
        b.setFooter("Aliases: "+String.join(", ", Arrays.asList(c.getNames())), null);
        message.getChannel().sendMessage(b.build()).queue();
    }
}
