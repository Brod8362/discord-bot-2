package pw.byakuren.discord.commands.subcommands;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.Command;

import java.util.List;
import java.util.stream.Collectors;

public class SubcommandList extends Subcommand {

    private final @NotNull Command c;

    public SubcommandList(@NotNull Command c) {
        super(new String[]{""}, "" , "" , c);
        this.c = c;
    }

    public SubcommandList(@NotNull String @NotNull [] names, @NotNull String help, @NotNull String syntax, @NotNull Command c) {
        super(names, help, syntax, c);
        this.c=c;
    }

    public void run(@NotNull Message message, @NotNull List<String> args) {
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
