package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.WatchedRole;

import java.util.ArrayList;
import java.util.List;

public class WatchedRoleFactory extends DatatypeFactory<WatchedRole> {

    JDA jda;

    public WatchedRoleFactory(long serverid, @NotNull DatabaseManager dbmg, @NotNull JDA jda) {
        super(serverid, dbmg);
        this.jda = jda;
    }

    @Override
    public @NotNull List<WatchedRole> getAll() {
        List<Long> l = dbmg.getWatchedRoles(serverid);

        ArrayList<WatchedRole> rl = new ArrayList<>();
        for (long v: l) {
            rl.add(new WatchedRole(v, jda));
        }
        return rl;
    }

    @Override
    public @Nullable WatchedRole get(Object @NotNull ... qualifiers) {
        if (qualifiers.length != 1) return null;
        if (qualifiers[0] instanceof Long) {
            boolean res = dbmg.checkWatchedRole(serverid, (long)qualifiers[0]);
            if (res) {
                return new WatchedRole((long)qualifiers[0], jda);
            }
            return null;
        }
        return null;
    }
}
