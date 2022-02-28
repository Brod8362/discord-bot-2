package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;

public class ExcludedChannel extends CacheEntry {

    private final long serverId;
    private final long channelId;

    public ExcludedChannel(@NotNull TextChannel channel) {
        this(channel.getGuild().getIdLong(), channel.getIdLong());
    }

    public ExcludedChannel(long guildId, long channelId) {
        this.serverId = guildId;
        this.channelId = channelId;
    }

    public long getServerid() {
        return serverId;
    }

    public long getChannelId() {
        return channelId;
    }

    public @NotNull String getChannelMention() {
        return "<#" + channelId + ">";
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addExcludedChannel(serverId, channelId);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeExcludedChannel(serverId, channelId);
    }
}
