package pw.byakuren.discord.objects.cache.factories;

import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public abstract class DatatypeFactory<E> {

    protected long serverid;
    protected DatabaseManager dbmg;

    public DatatypeFactory(long serverid, DatabaseManager dbmg) {
        this.serverid = serverid;
        this.dbmg = dbmg;
    }

    public abstract List<E> getAll();

    public abstract E get(Object... qualifiers);

}
