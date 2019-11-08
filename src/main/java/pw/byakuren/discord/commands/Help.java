package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Help extends Command {

    CommandHelper cmdhelp;
    private Cache c;

    public Help(CommandHelper cmd, Cache c) {
        names=new String[]{"help", "h"};
        help="See command listing";
        minimum_permission=CommandPermission.REGULAR_USER;

        cmdhelp = cmd;
        this.c =c;
    }

    @Override
    public void run(Message message, List<String> args) {
        EmbedBuilder b = new EmbedBuilder();
        if (args.size()==0) {
            /* Full comamnd listing */
            b.setTitle("Help listing");
            StringBuilder desc = new StringBuilder();

            for (Command cmd: cmdhelp.getCommandSet()) {
                if ( cmd.canRun(message.getMember(), c))
                    desc.append(String.format("**%s** - %s\n", cmd.getNames()[0], cmd.getHelp()));
            }
            b.setDescription(desc);
            b.setFooter("Use the help command with a command name to see more information.", null);
        } else {
            /* Details about specific command */
            Command c = cmdhelp.getCommand(args.get(0));
            if (c == null) {
                /* Command not found */
                b.setDescription("Unknown command '"+args.get(0)+"'");
                b.setFooter("Use the help command with no arguments to see all commands.", null);
            } else {
                /* Command found */
                b.setTitle("**"+c.getNames()[0]+"** ("+c.minimumPermission().name+")");
                String help = c.getHelp();
                String syntax = c.getSyntax();
                if (help == null) help = "No help defined.";
                if (syntax == null) syntax = "No syntax defined.";
                b.addField("Help", help, false);
                b.addField("Syntax", syntax, false);
                b.setFooter("Aliases: "+String.join(", ", Arrays.asList(c.getNames())), null);
            }
        }
        message.getChannel().sendMessage(b.build()).queue();
    }
}
