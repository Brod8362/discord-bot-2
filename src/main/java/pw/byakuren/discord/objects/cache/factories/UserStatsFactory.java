package pw.byakuren.discord.objects.cache.factories;

import net.dv8tion.jda.api.entities.Member;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.UserStats;

import java.util.List;

public class UserStatsFactory extends DatatypeFactory<UserStats> {

    public UserStatsFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<UserStats> getAll() {
        return dbmg.getAllChatDataForServer(serverid);
    }

    @Override
    public UserStats get(Object... qualifiers) {
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
