package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;

public class WatchedUser extends CacheEntry {

    private long serverId;
    private long userId;

    public WatchedUser(long serverId, long uid) {
        this.serverId = serverId;
        this.userId = uid;
    }

    public WatchedUser(@NotNull Member user) {
        this(user.getGuild().getIdLong(), user.getIdLong());
    }

    public long getServerId() {
        return serverId;
    }

    public long getUserId() {
        return userId;
    }

    public @NotNull String getUserMention() {
        return "<@" + userId + ">";    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addWatchedUser(serverId, userId);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeWatchedUser(serverId, userId);
    }
}
