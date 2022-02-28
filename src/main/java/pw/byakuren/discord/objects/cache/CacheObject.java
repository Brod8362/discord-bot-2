package pw.byakuren.discord.objects.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.objects.cache.factories.DatatypeFactory;

import java.util.ArrayList;
import java.util.List;

public class CacheObject<E extends CacheEntry> {

    private final @NotNull DatabaseManager dbmg;
    private final @NotNull DatatypeFactory<E> factory;

    private final long id;

    private final @NotNull List<E> data;


    public CacheObject(long serverid, @NotNull DatabaseManager dbmg, @NotNull DatatypeFactory<E> factory) {
        // Create defensive copy (source may be unmodifiable & our list is mutable)
        data = new ArrayList<>(factory.getAll());
        this.dbmg=dbmg;
        id = serverid;
        this.factory=factory;
    }

    public @NotNull List<E> getData() {
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