package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.User;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class Subscription extends CacheDatatype {

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

    @Override
    public <T> boolean matches(T... qualifier) {
        if (qualifier.length != 2) return false;
        if (!(qualifier[0] instanceof User) || !(qualifier[1] instanceof User)) return false;
        User u1 = (User)qualifier[0];
        User u2 = (User)qualifier[1];
        return (u1 == moderator && u2 == user);
    }
}
