package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
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
            ul.add(new WatchedUser(serverid, v));
        }
        return ul;
    }

}
