package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.Subscription;

import java.util.List;

public class SubscriptionFactory extends DatatypeFactory<Subscription> {

    public SubscriptionFactory(long serverid, DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public List<Subscription> getAll() {
        return dbmg.getModeratorSubscriptions(serverid);
    }

    @Override
    public Subscription get(Object... qualifiers) {
        throw new UnsupportedOperationException("unimplemented");
    }
}
