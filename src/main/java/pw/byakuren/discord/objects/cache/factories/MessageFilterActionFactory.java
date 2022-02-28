package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;

import java.util.List;

public class MessageFilterActionFactory extends DatatypeFactory<MessageFilterAction> {
    public MessageFilterActionFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<MessageFilterAction> getAll() {
        return dbmg.getAllFilterActions(serverid);
    }

}
