package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.LastMessage;

import java.util.List;

public class LastMessageFactory extends DatatypeFactory<LastMessage> {

    public LastMessageFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<LastMessage> getAll() {
        return dbmg.getLastMessages(serverid);
    }

    @Override
    public @Nullable LastMessage get(Object @NotNull ... qualifiers) {
        if (qualifiers.length != 1) return null;
        if (qualifiers[0] instanceof Long) {
            return dbmg.getLastMessage(serverid, (long)qualifiers[0]);
        }
        return null;
    }
}
