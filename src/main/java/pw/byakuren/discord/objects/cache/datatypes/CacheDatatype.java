package pw.byakuren.discord.objects.cache.datatypes;

import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class CacheDatatype {

    public boolean matches(Object... qualifier) {
        return false;
    }

    /**
     *
     * @param qualifier What to use in getting the requested datatype. This method will be overridden by it's subclasses.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The requested object if found, otherwise null.
     */
    public static <T> T get(Object qualifier, DatabaseManager dbmg) {
        return null;
    }


    public static <T> List<T> getAll(long serverid, DatabaseManager dbmg) {
        return new ArrayList<>();
    }

}
