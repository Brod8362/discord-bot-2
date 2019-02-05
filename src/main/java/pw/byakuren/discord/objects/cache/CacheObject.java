package pw.byakuren.discord.objects.cache;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheDatatype;

import java.util.ArrayList;
import java.util.List;

public class CacheObject<E extends CacheDatatype> {

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

    public List<E> getAllFromDatabase() {
        //todo ???
        return new ArrayList<>();
    }
}