package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public interface Command {
    String getName();

    String getSyntax();

    String getHelp();

    void run(Message message, List<String> args);


}
