package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;

import java.util.List;

public interface Command {

    String[] getNames();

    String getSyntax();

    String getHelp();

    CommandPermission minimumPermission();

    void run(Message message, List<String> args);

}
