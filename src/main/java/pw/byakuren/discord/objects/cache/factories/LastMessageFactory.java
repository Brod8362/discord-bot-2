package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.LastMessage;

import java.util.List;

public class LastMessageFactory extends DatatypeFactory<LastMessage> {

    public LastMessageFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<LastMessage> getAll() {
        return dbmg.getLastMessages(serverid);
    }

    @Override
    public LastMessage get(Object... qualifiers) {
        if (qualifiers.length != 1) return null;
        if (qualifiers[0] instanceof Long) {
            return dbmg.getLastMessage(serverid, (long)qualifiers[0]);
        }
        return null;
    }
}
