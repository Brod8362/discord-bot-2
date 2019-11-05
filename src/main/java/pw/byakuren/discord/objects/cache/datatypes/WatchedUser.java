package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import pw.byakuren.discord.DatabaseManager;

public class WatchedUser extends CacheEntry {

    private long serverid;
    private Member user;

    public WatchedUser(long serverid, long uid, JDA jda) {
        this.serverid = serverid;
        this.user = jda.getGuildById(serverid).getMemberById(uid);
    }

    public WatchedUser(Member user) {
        this.user = user;
        this.serverid = user.getGuild().getIdLong();
    }

    public static WatchedUser get(Object qualifier, DatabaseManager dbmg) {
        //todo ???
        return null;
    }
}
