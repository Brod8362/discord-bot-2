package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.RegexKey;

import java.util.List;

public class RegexKeyFactory extends DatatypeFactory<RegexKey> {

    public RegexKeyFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<RegexKey> getAll() {
        return dbmg.getRegexKeys(serverid);
    }

}
