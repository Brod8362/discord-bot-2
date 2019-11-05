package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;

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

    public static ExcludedChannel get(Object qualifier, DatabaseManager dbmg) {
        if (qualifier instanceof Long) {
            return dbmg.getExcludedChannel((long)qualifier);
        }
        return null;
    }



}
