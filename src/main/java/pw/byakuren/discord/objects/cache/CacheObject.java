package pw.byakuren.discord.objects.cache;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.objects.cache.factories.DatatypeFactory;

import java.util.List;

public class CacheObject<E extends CacheEntry> {

    private DatabaseManager dbmg;
    private DatatypeFactory<E> factory;

    private final long id;

    private List<E> data;


    public CacheObject(long serverid, DatabaseManager dbmg, DatatypeFactory<E> factory) {
        data = factory.getAll();
        this.dbmg=dbmg;
        id = serverid;
        this.factory=factory;
    }

    public List<E> getData() {
        return data;
    }

    /**
     * @return Number of items written to database.
     */
    public int write() {
        int c = 0;
        for (E e: data) {
            if (e.write_check(dbmg)) c++;
        }
        return c;
    }

}