package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.objects.cache.datatypes.ExcludedChannel;

import java.util.ArrayList;
import java.util.List;

public class ExcludedChannelFactory implements DatatypeFactory<ExcludedChannel> {

    private DatabaseManager dbmg;
    private long serverid;

    public ExcludedChannelFactory(DatabaseManager dbmg, long serverid) {
        this.dbmg = dbmg;
        this.serverid = serverid;
    }

    @Override
    public List<ExcludedChannel> getAll() {
        List<TextChannel> l = dbmg.getExcludedChannels(serverid);
        ArrayList<ExcludedChannel> c = new ArrayList<>();
        for (TextChannel t: l) {
            c.add(new ExcludedChannel(t));
        }
        return c;
    }

    @Override
    public ExcludedChannel get(Object... qualifiers) {
        if (qualifiers.length != 1) return null;
        if (qualifiers[0] instanceof Long) {
            return dbmg.getExcludedChannel((long)qualifiers[0]);
        }
        return null;
    }
}
