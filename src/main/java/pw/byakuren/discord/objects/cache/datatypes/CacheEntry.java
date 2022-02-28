package pw.byakuren.discord.objects.cache.datatypes;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.WriteState;

import static pw.byakuren.discord.objects.cache.WriteState.NEUTRAL;

public abstract class CacheEntry {

    public @NotNull WriteState write_state = NEUTRAL;

    public boolean write_check(@NotNull DatabaseManager dbmg) {
        switch (write_state) {
            case PENDING_WRITE:
                write(dbmg);
                write_state = NEUTRAL;
                return true;
            case PENDING_DELETE:
                delete(dbmg);
                write_state = NEUTRAL;
                return true;
            default:
                break;
        }
        return false;
    }
    protected abstract void write(@NotNull DatabaseManager dbmg);

    protected abstract void delete(@NotNull DatabaseManager dbmg);

    public boolean exists() {
        return write_state!=WriteState.PENDING_DELETE;
    }
}
