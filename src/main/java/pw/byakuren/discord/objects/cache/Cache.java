package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    private Map<Long, ServerCache> servers = new HashMap<>();
    private DatabaseManager dbmg;
    private JDA jda;

    public Cache(DatabaseManager dbmg, JDA jda) {
        this.dbmg = dbmg;
        this.jda = jda;
        System.out.println("Retrieving all server caches...");
        loadAllServerCaches();
        System.out.println("Cache retrieval complete.");
    }

    private void loadAllServerCaches() {
        for (Guild g: jda.getGuilds()) {
            loadServerCache(g);
        }
    }

    public ServerCache getServerCache(Guild g) {
        return getServerCache(g.getIdLong());
    }

    public ServerCache getServerCache(long serverid) {
        if (servers.get(serverid) == null) {
            loadServerCache(serverid);
        }
        return servers.get(serverid);
    }

    private void loadServerCache(long serverid) {
        ServerCache sc = new ServerCache(serverid, dbmg, jda);
        servers.put(serverid, sc);
    }

    private void loadServerCache(Guild g) {
        loadServerCache(g.getIdLong());
    }

    public void writeAllAndQuit() {
        for (ServerCache sc: servers.values()) {
            sc.write_thread.writeAllAndQuit();
        }
    }

}
