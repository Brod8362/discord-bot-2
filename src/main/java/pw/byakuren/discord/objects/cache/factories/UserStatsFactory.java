package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @Override
    public @Nullable UserStats get(Object @NotNull ... qualifiers) {
        switch (qualifiers.length) {
            case 1:
                if (qualifiers[0] instanceof Member) {
                    return dbmg.getUserChatData((Member)qualifiers[0]);
                }
                break;
            case 2:
                //todo server/user id combo here
                break;
            default:
                return null;
        }
        return null;
    }
}
