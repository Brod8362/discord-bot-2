package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

import java.util.List;

public class Stop implements Command {


    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Stop the bot.";
    }


    @Override
    public void run(Message message, List<String> args) {
        JDA jda = message.getJDA();
        User owner = null;
        try {
            owner = jda.getApplicationInfo().complete(true).getOwner();
        } catch (RateLimitedException ignored) { } //i don't care about this error
        if (message.getAuthor() != owner) { message.addReaction("❌").queue(); return; }
        message.addReaction("😢").queue();
        jda.shutdown();
    }
}
