package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Help extends Command {

    private final @NotNull CommandHelper cmdhelp;
    private final @NotNull Cache c;

    public Help(@NotNull CommandHelper cmd, @NotNull Cache c) {
        names=new String[]{"help", "h"};
        help="See command listing.";
        syntax="`@User` represents a user mention.\n" +
                "`#Channel` represents a channel mention.\n" +
                "`@Role` represents a role mention.\n" +
                "`[]` represents a required argument.\n" +
                "`<>` represents an optional argument.\n" +
                "\n`T` means a command is traditional, and uses the chat prefix.\n`S` means the command supports slash commands.\n" +
                "It possible for a command to support one or both.\n"+
                "\nYou can view help about subcommands like this: `help command.subcommand`";
        minimum_permission=CommandPermission.REGULAR_USER;

        cmdhelp = cmd;
        this.c =c;
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        if (args.size() == 0) {
            cmd_list(message, args, false);
        } else if (args.size() == 1 && args.get(0).equals("-a")) {
            cmd_list(message, args, true);
        } else {
            cmd_details(message, args);
        }
    }

    private void cmd_list(@NotNull Message message, @NotNull List<String> args, boolean showall) {
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Help listing");
        StringBuilder desc = new StringBuilder();

        for (Command cmd: cmdhelp.getCommandSet()) {
            if ( cmd.canRun(message.getMember(), c))
                desc.append(String.format("**%s** (%s) - %s\n", cmd.getNames()[0], cmd.getTypeAbbreviation(), cmd.getHelp()));
            else
                if ( showall )
                    desc.append(String.format("~~%s~~ (%s) - %s\n", cmd.getNames()[0], cmd.getTypeAbbreviation(), cmd.getHelp()));
        }
        b.setDescription(desc);
        b.setFooter("Use the help command with a command name to see more information. " +
                "Run with -a to see all commands.", null);
        message.reply(b.build()).mentionRepliedUser(false).queue();
    }

    private void cmd_details(@NotNull Message message, @NotNull List<String> args) {
        String[] cmds = args.get(0).split("\\.");
        EmbedBuilder b = new EmbedBuilder();
        if (cmds.length == 1) {
            Command c = cmdhelp.getCommand(args.get(0));
            if (c == null) {
               cmd_not_found(message, args.get(0));
            } else {
                /* Command found */
                sendHelpEmbed(message, c.getPrimaryName(), c);
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
        sendHelpEmbed(message, name, d);
    }

    private void cmd_not_found(@NotNull Message message, @NotNull String t) {
        /* Command not found */
        EmbedBuilder b = new EmbedBuilder();
        b.setDescription("Unknown command '"+t+"'");
        b.setFooter("Use the help command with no arguments to see all commands.", null);
        message.reply(b.build()).mentionRepliedUser(false).queue();
    }

    private void sendHelpEmbed(@NotNull Message message, @NotNull String name, @NotNull Command c) {
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle(String.format("**%s** (%s, %s)", name, c.minimumPermission().name, c.getTypeAbbreviation()));
        String help = c.getHelp();
        String syntax = c.getSyntax();
        if (help == null) help = "No help defined.";
        if (syntax == null) syntax = "No syntax defined.";
        b.addField("Help", help, false);
        b.addField("Syntax", syntax, false);
        List<Subcommand> sub_cmds_raw= c.getSubcommands();
        String sub_commands_text = "";
        if (sub_cmds_raw != null && sub_cmds_raw.size() > 0 && sub_cmds_raw.get(0).getPrimaryName().equals("")) {
            sub_cmds_raw = sub_cmds_raw.subList(1, sub_cmds_raw.size());
            sub_commands_text = sub_cmds_raw.stream().map(Command::getPrimaryName).collect(Collectors.joining(", "));
        }
        if (sub_commands_text.isEmpty()) sub_commands_text = "None";
        b.addField("Subcommands", sub_commands_text, false);
        b.setFooter("Aliases: "+String.join(", ", Arrays.asList(c.getNames())), null);
        message.reply(b.build()).mentionRepliedUser(false).queue();
    }
}
