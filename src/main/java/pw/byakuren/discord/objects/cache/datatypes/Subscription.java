package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class Subscription extends CacheEntry {

    private final Member moderator;
    private final Member user;

    public Subscription(Member moderator, Member user) {
        this.moderator = moderator;
        this.user = user;
    }

    public Member getModerator() {
        return moderator;
    }

    public Member getUser() {
        return user;
    }

    public Subscription clone() {
        return new Subscription(moderator, user);
    }

    public static List<Subscription> getAll(DatabaseManager dbmg, long serverid) {
        return dbmg.getModeratorSubscriptions(serverid);
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.addModeratorSubscription(moderator, user);
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        dbmg.removeModeratorSubscription(moderator, user);
    }
}
