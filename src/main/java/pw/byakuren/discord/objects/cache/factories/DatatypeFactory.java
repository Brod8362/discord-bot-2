package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public abstract class DatatypeFactory<E> {

    protected long serverid;
    protected @NotNull DatabaseManager dbmg;

    public DatatypeFactory(long serverid, @NotNull DatabaseManager dbmg) {
        this.serverid = serverid;
        this.dbmg = dbmg;
    }

    public abstract @NotNull List<E> getAll();

}
