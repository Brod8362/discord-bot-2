package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.UserStats;

import java.util.List;

public class UserStatsFactory extends DatatypeFactory<UserStats> {

    public UserStatsFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<UserStats> getAll() {
        return dbmg.getAllChatDataForServer(serverid);
    }

}
