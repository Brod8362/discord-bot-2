package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class Stop implements Command {

    DatabaseManager dbmg = null;

    public Stop(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }

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
    public boolean needsBotOwner() {
        return true;
    }


    @Override
    public void run(Message message, List<String> args) {
        JDA jda = message.getJDA();
        message.addReaction("😢").queue();
        dbmg.getSQL().disconnect();
        jda.shutdown();
    }
}
