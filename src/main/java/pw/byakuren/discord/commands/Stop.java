package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.List;

public class Stop extends Command {

    DatabaseManager dbmg = null;
    Cache c;

    public Stop(DatabaseManager dbmg, Cache c) {
        names=new String[]{"stop"};
        help="Stop the bot.";
        minimum_permission=CommandPermission.BOT_OWNER;

        this.dbmg = dbmg;
        this.c = c;
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
