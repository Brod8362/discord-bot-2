package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.objects.cache.datatypes.ExcludedChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExcludedChannelFactory extends DatatypeFactory<ExcludedChannel> {

    public ExcludedChannelFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<ExcludedChannel> getAll() {
        return dbmg.getExcludedChannels(serverid)
                .stream()
                .map(ExcludedChannel::new)
                .collect(Collectors.toList());
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
