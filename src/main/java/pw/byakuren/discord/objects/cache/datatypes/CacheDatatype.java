package pw.byakuren.discord.objects.cache.datatypes;

import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public abstract class CacheDatatype {

    /**
     *
     * @param qualifier What to use in getting the requested datatype. This method will be overridden by it's subclasses.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The requested object if found, otherwise null.
     */
    public static <T> T get(Object qualifier, DatabaseManager dbmg) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static <T> List<T> getAll(long serverid, DatabaseManager dbmg) {
           throw new UnsupportedOperationException("Method not implemented");
    }

}
