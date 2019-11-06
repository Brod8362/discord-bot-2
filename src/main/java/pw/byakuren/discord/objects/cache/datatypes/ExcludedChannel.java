package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ExcludedChannel extends CacheEntry {

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

    public static CacheEntry get(Object qualifier, DatabaseManager dbmg) {
        if (qualifier instanceof Long) {
            return dbmg.getExcludedChannel((long)qualifier);
        }
        return null;
    }

    public long getServerid() {
        return serverid;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public String getChannelMention() {
        return channel.getAsMention();
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.addExcludedChannel(channel);
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        dbmg.removeExcludedChannel(channel);
    }
}
