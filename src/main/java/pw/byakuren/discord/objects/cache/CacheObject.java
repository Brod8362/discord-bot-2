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
        data = getAllFromDatabase();
        id = serverid;
        this.factory=factory;
    }

    public E get(Object... qualifiers) {
        //todo re-implement later
        return null;
    }

    public List<E> getAll() {
        return data;
    }

    private E getFromDatabase() {
        //todo database getting here
        return null;
    }

    private List<E> getAllFromDatabase() {
        throw new UnsupportedOperationException("unimplemented");
    }
}