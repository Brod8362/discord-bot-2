package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

public class WatchedUser extends CacheEntry {

    private long serverid;
    private @NotNull Member user;

    public WatchedUser(long serverid, long uid, @NotNull JDA jda) {
        this.serverid = serverid;
        this.user = jda.getGuildById(serverid).getMemberById(uid);
    }

    public WatchedUser(@NotNull Member user) {
        this.user = user;
        this.serverid = user.getGuild().getIdLong();
    }

    public long getServerId() {
        return serverid;
    }

    public @NotNull Member getUser() {
        return user;
    }

    public @NotNull String getUserMention() {
        return user.getAsMention();
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addWatchedUser(user);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeWatchedUser(user);
    }
}
