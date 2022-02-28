package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class ExcludedChannel extends CacheEntry {

    private long serverid;
    private @NotNull TextChannel channel;

    public ExcludedChannel(@NotNull TextChannel channel) {
        this.channel = channel;
        this.serverid = channel.getGuild().getIdLong();
    }

    public ExcludedChannel(long channelid, @NotNull JDA jda) {
        this.channel = jda.getTextChannelById(channelid);
        this.serverid = channel.getGuild().getIdLong();
    }

    public static @Nullable CacheEntry get(Object qualifier, @NotNull DatabaseManager dbmg) {
        if (qualifier instanceof Long) {
            return dbmg.getExcludedChannel((long)qualifier);
        }
        return null;
    }

    public long getServerid() {
        return serverid;
    }

    public @NotNull TextChannel getChannel() {
        return channel;
    }

    public @NotNull String getChannelMention() {
        return channel.getAsMention();
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addExcludedChannel(channel);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeExcludedChannel(channel);
    }
}
