package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.ServerSettings;

import java.util.List;

public class ServerSettingsFactory extends DatatypeFactory<ServerSettings> {

    public ServerSettingsFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<ServerSettings> getAll() {
        return dbmg.getServerSettings(serverid);
    }

    @Override
    public ServerSettings get(Object... qualifiers) {
        throw new UnsupportedOperationException("unimplemented");
    }
}
