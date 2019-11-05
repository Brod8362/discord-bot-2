package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ExcludedChannel extends CacheDatatype {


    private long serverid;
    private TextChannel channel;

    public ExcludedChannel(TextChannel channel) {
        this.channel = channel;
        this.serverid = channel.getGuild().getIdLong();
    }

    public ExcludedChannel(long channelid, JDA jda) {
        this.channel = jda.getTextChannelById(channelid);
        this.serverid = channel.getGuild().getIdLong();
    }

    public static CacheDatatype get(Object qualifier, DatabaseManager dbmg) {
        if (qualifier instanceof Long) {
            return dbmg.getExcludedChannel((long)qualifier);
        }
        return null;
    }

    public static List<CacheDatatype> getAll(long serverid, DatabaseManager dbmg) {
        List<TextChannel> l = dbmg.getExcludedChannels(serverid);
        ArrayList<CacheDatatype> c = new ArrayList<>();
        for (TextChannel t: l) {
            c.add(new ExcludedChannel(t));
        }
        return c;
    }

}
