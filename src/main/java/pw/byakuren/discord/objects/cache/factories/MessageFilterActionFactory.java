package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;

import java.util.ArrayList;
import java.util.List;

public class MessageFilterActionFactory extends DatatypeFactory<MessageFilterAction> {
    public MessageFilterActionFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<MessageFilterAction> getAll() {
        return dbmg.getAllFilterActions(serverid);
    }

    @Override
    public @Nullable MessageFilterAction get(Object @NotNull ... qualifiers) {
        if (qualifiers.length != 2 || !(qualifiers[0] instanceof Long) || !(qualifiers[1] instanceof String)) {
            return null;
        }
        return dbmg.getFilterAction((long)qualifiers[0], (String)qualifiers[1]);
    }
}
