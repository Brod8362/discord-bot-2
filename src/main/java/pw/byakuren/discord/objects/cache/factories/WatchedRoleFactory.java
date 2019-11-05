package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.WatchedRole;

import java.util.ArrayList;
import java.util.List;

public class WatchedRoleFactory extends DatatypeFactory<WatchedRole> {

    JDA jda;

    public WatchedRoleFactory(long serverid, DatabaseManager dbmg, JDA jda) {
        super(serverid, dbmg);
        this.jda = jda;
    }

    @Override
    public List<WatchedRole> getAll() {
        List<Long> l = dbmg.getWatchedRoles(serverid);

        ArrayList<WatchedRole> rl = new ArrayList<>();
        for (long v: l) {
            rl.add(new WatchedRole(v, jda));
        }
        return rl;
    }

    @Override
    public WatchedRole get(Object... qualifiers) {
        throw new UnsupportedOperationException("unimplemented");
        //todo single role gathering
    }
}
