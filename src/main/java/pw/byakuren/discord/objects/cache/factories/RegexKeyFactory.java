package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.RegexKey;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public class RegexKeyFactory extends DatatypeFactory<RegexKey> {

    public RegexKeyFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<RegexKey> getAll() {
        return dbmg.getRegexKeys(serverid);
    }

    @Override
    public RegexKey get(Object... qualifiers) {
        throw new UnsupportedOperationException("unimplemented");
    }
}
