package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.WatchedRole;
import pw.byakuren.discord.objects.cache.datatypes.WatchedUser;

import java.util.ArrayList;
import java.util.List;

public class WatchedUserFactory extends DatatypeFactory<WatchedUser> {

    @NotNull JDA jda;

    public WatchedUserFactory(long serverid, @NotNull DatabaseManager dbmg, @NotNull JDA jda) {
        super(serverid, dbmg);
        this.jda = jda;
    }

    @Override
    public @NotNull List<WatchedUser> getAll() {
        List<Long> l = dbmg.getWatchedUsers(serverid);

        ArrayList<WatchedUser> ul = new ArrayList<>();
        for (long v: l) {
            ul.add(new WatchedUser(serverid, v, jda));
        }
        return ul;
    }

    @Override
    public @Nullable WatchedUser get(Object @NotNull ... qualifiers) {
        switch (qualifiers.length) {
            case 1:
                if (qualifiers[0] instanceof Member) {
                    boolean res = dbmg.checkWatchedUser((Member) qualifiers[0]);
                    if (res) {
                        return new WatchedUser((Member) qualifiers[0]);
                    }
                    return null;
                }
                break;
            case 2:
                // 0 = server, 1 = user
                if (qualifiers[0] instanceof Long && qualifiers[1] instanceof Long) {
                    boolean res = dbmg.checkWatchedUser((long)qualifiers[0], (long)qualifiers[1]);
                    if (res) {
                        return new WatchedUser((long)qualifiers[0], (long)qualifiers[1], jda);
                    }
                    return null;
                }
                break;
            default:
                break;
        }
        return null;
    }
}
