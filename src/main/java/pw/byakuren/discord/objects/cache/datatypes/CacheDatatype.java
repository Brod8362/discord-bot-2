package pw.byakuren.discord.objects.cache.datatypes;

import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class CacheDatatype {

    public <T> boolean matches(T... qualifier) {
        return false;
    }

    public static <T> CacheDatatype get(T qualifier, DatabaseManager dbmg) {
        return null;
    }

    public static <T> List<T> getAll(DatabaseManager dbmg, long serverid) {
        return new ArrayList<T>();
    }

}
