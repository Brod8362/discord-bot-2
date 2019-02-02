package pw.byakuren.discord.objects.cache.datatypes;

import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public interface CacheDatatype {

    <T> boolean matches(T... qualifier);

    static <T> CacheDatatype get(T qualifier, DatabaseManager dbmg) {
        return null;
    }

    static List<CacheDatatype> getAll(DatabaseManager dbmg) {
        return new ArrayList<CacheDatatype>();
    }

}
