package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.Triple;

import java.util.*;

public class Cache {

    private final @NotNull Map<Long, ServerCache> servers = new HashMap<>();
    private final @NotNull DatabaseManager dbmg;
    private final @NotNull JDA jda;
    public final @NotNull User owner;
    private final @NotNull List<Triple<Long, Long, Long>> deleted_message_authors;

    public Cache(@NotNull DatabaseManager dbmg, @NotNull JDA jda) {
        this.dbmg = dbmg;
        this.jda = jda;
        owner = jda.retrieveApplicationInfo().complete().getOwner();
        System.out.println("Retrieving all server caches...");
        loadAllServerCaches();
        System.out.println("Cache retrieval complete.");
        deleted_message_authors=new ArrayList<>();
    }

    private void loadAllServerCaches() {
        for (Guild g: jda.getGuilds()) {
            loadServerCache(g);
        }
    }

    public @NotNull ServerCache getServerCache(@NotNull Guild g) {
        return getServerCache(g.getIdLong());
    }

    public @NotNull ServerCache getServerCache(long serverid) {
        if (servers.get(serverid) == null) {
            loadServerCache(serverid);
        }
        // Should not be null at this point; loadServerCache() should have created it.
        return Objects.requireNonNull(servers.get(serverid));
    }

    private void loadServerCache(long serverid) {
        ServerCache sc = new ServerCache(serverid, dbmg, jda);
        servers.put(serverid, sc);
    }

    private void loadServerCache(@NotNull Guild g) {
        loadServerCache(g.getIdLong());
    }

    public void writeAllAndQuit() {
        for (ServerCache sc: servers.values()) {
            sc.write_thread.disableRun();
        } //first loop prevents concurrent modification exceptions
        for (ServerCache sc: servers.values()) {
            sc.write_thread.writeAllAndQuit();
        }
    }

    public void addMessageReference(@NotNull Message m) {
        if (deleted_message_authors.size() > m.getJDA().getGuilds().size()*1000) {
            System.out.printf("Trimming message cache from %d to %d\n", deleted_message_authors.size(), deleted_message_authors.size() - 500);
            deleted_message_authors.subList(0, 500).clear();
        } //prevent excessive memory usage
        deleted_message_authors.add(new Triple<>(m.getIdLong(), m.getGuild().getIdLong(), m.getAuthor().getIdLong()));
    }

    public @Nullable Triple<Long, Long, Long> seeDeletedMessageAuthor(long id) {
        for (Triple<Long, Long, Long> t: deleted_message_authors)
            if (t.a == id)
                return t;
        return null;
    }

}
