package pw.byakuren.discord.objects.cache;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;

import java.util.List;

public class CacheObject<E extends CacheEntry> {

    DatabaseManager dbmg;

    private final long id;

    private List<E> data;


    public CacheObject(long serverid) {
        data = getAllFromDatabase();
        id = serverid;
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
        return E.getAll(id, dbmg);
    }
}