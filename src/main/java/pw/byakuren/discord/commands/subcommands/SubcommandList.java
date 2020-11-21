package pw.byakuren.discord.commands.subcommands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.Command;

import java.util.List;
import java.util.stream.Collectors;

public class SubcommandList extends Subcommand {

    private Command c;

    public SubcommandList(Command c) {
        super(new String[]{""}, "" , "" , c);
        this.c = c;
    }

    public SubcommandList(String[] names, String help, String syntax, Command c) {
        super(names, help, syntax, c);
        this.c=c;
    }

    public void run(Message message, List<String> args) {
        List<Subcommand> cmds = c.getSubcommands();
        String t = "";
        if (cmds.get(0).getPrimaryName().equals(""))
            cmds = cmds.subList(1, cmds.size());
        t+="Available options: `";
        t+=cmds.stream().map(Command::getPrimaryName).collect(Collectors.joining("`, `"));
        t+="`";
        message.reply(t).mentionRepliedUser(false).queue();
    }
}
