package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.VoiceBan;

import java.util.List;

public class VoiceBanFactory extends DatatypeFactory<VoiceBan> {

    public VoiceBanFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<VoiceBan> getAll() {
        return dbmg.getAllVoiceBans(serverid);
    }

    @Override
    public VoiceBan get(Object... qualifiers) {
        if (qualifiers.length != 2) return null;
        throw new UnsupportedOperationException("flavor exception");
    }
}
