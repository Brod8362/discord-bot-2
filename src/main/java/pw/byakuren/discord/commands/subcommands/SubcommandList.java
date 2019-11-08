package pw.byakuren.discord.commands.subcommands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.Command;

import java.util.List;
import java.util.stream.Collectors;

public class SubcommandList extends Subcommand {

    public void run(Message message, List<String> args) {
        List<Subcommand> cmds = super.subcommands;
        String t = "";
        t+="Available options: `";
        t+=cmds.stream().map(Command::getPrimaryName).collect(Collectors.joining("`, `"));
        t+="`";
        message.getChannel().sendMessage(t).queue();
    }
}
