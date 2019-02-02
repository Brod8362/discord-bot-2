package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cache {

    private Map<Long, ServerCache> servers;
    private DatabaseManager dbmg;
    private JDA jda;

    public Cache(DatabaseManager dbmg, JDA jda) {
        this.dbmg = dbmg;
        this.jda = jda;
    }

    public ServerCache getServerCache(long serverid) {
        if (servers.get(serverid) == null) {
            loadServerCache(serverid);
        }
        return servers.get(serverid);
    }

    private void loadServerCache(long serverid) {
        ServerCache sc = new ServerCache(serverid, dbmg);
        servers.put(serverid, sc);
    }


    private List<Subscription> getSubscriptionsAsObjects(long serverid) {
        List<Subscription> subs = new ArrayList<>();
        for (Member m: jda.getGuildById(serverid).getMembers()) {
            if (m.hasPermission(Permission.ADMINISTRATOR)) {
                for (long l: dbmg.getModeratorSubscriptions(m)) {
                    subs.add(new Subscription(m.getUser(), jda.getUserById(l)));
                }
            }
        }
        return subs;
    }
}
