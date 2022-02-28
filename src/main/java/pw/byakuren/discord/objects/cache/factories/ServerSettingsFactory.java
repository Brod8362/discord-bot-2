package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.ServerSettings;

import java.util.List;

public class ServerSettingsFactory extends DatatypeFactory<ServerSettings> {

    public ServerSettingsFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<ServerSettings> getAll() {
        return dbmg.getServerSettings(serverid);
    }

}
