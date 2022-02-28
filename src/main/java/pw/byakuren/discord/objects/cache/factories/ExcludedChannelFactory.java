package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.objects.cache.datatypes.ExcludedChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExcludedChannelFactory extends DatatypeFactory<ExcludedChannel> {

    public ExcludedChannelFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<ExcludedChannel> getAll() {
        return dbmg.getExcludedChannels(serverid)
                .stream()
                .map(ExcludedChannel::new)
                .collect(Collectors.toList());
    }
}
