package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Map;

public class Help implements Command {

    CommandHelper cmdhelp;

    public Help(CommandHelper cmd) {
        cmdhelp = cmd;
    }

    public Help() {
    }



    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "See command listing.";
    }

    @Override
    public boolean needsBotOwner() {
        return false;
    }

    @Override
    public void run(Message message, List<String> args) {
        EmbedBuilder embed = new EmbedBuilder();

        try {
            args.get(0); //TODO make this less retarded
        } catch (IndexOutOfBoundsException ignored) {
            // iterate over commands and get their help
            Map<String, Command> commands = cmdhelp.getCommands();
            for (String name : commands.keySet()) {
                embed.addField(name, cmdhelp.getCommandHelp(name), false);
            }
            message.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        String name = args.get(0);
        try {
            cmdhelp.getCommandSyntax(name);
            embed.setTitle(name);
            embed.addField("Help", cmdhelp.getCommandHelp(name), false);
            embed.addField("Syntax", cmdhelp.getCommandSyntax(name), false);
            message.getChannel().sendMessage(embed.build()).queue();
        } catch (NullPointerException ignored) {
            message.getChannel().sendMessage("Command not found.").queue();
        }
    }
}
