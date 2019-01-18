package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class Test implements Command {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getSyntax() {
        return null;
    }




    @Override
    public String getHelp() {
        return "Testing command.";
    }

    @Override
    public void run(Message message, List<String> args) {
        message.getChannel().sendMessage("this is a test command.").queue();
    }
}
