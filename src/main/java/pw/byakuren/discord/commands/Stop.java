package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.List;

public class Stop implements Command {

    DatabaseManager dbmg = null;
    Cache c;

    public Stop(DatabaseManager dbmg, Cache c) {
        this.dbmg = dbmg;
        this.c = c;
    }

    @Override
    public String[] getNames() {
        return new String[]{"stop"};
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
        Message m = message.getChannel().sendMessage("Waiting for write threads...").complete();
        c.writeAllAndQuit();
        m.editMessage("Shutting down. \uD83D\uDE34").complete();
        dbmg.getSQL().disconnect();
        jda.shutdown();
    }
}
