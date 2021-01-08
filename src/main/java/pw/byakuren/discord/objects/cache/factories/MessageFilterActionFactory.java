package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;

import java.util.ArrayList;
import java.util.List;

public class MessageFilterActionFactory extends DatatypeFactory<MessageFilterAction> {
    public MessageFilterActionFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<MessageFilterAction> getAll() {
        // TODO implement this
        return new ArrayList<MessageFilterAction>();
    }

    @Override
    public MessageFilterAction get(Object... qualifiers) {
        // TODO implement this
        return null;
    }
}
