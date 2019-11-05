package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.User;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class Subscription extends CacheEntry {

    private final User moderator;
    private final User user;

    public Subscription(User moderator, User user) {
        this.moderator = moderator;
        this.user = user;
    }

    public User getModerator() {
        return moderator;
    }

    public User getUser() {
        return user;
    }

    public Subscription clone() {
        return new Subscription(moderator, user);
    }

    public static List<Subscription> getAll(DatabaseManager dbmg, long serverid) {
        return dbmg.getModeratorSubscriptions(serverid);
    }

}
