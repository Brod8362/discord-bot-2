package pw.byakuren.discord.objects.cache.datatypes;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.WriteState;

import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.NEUTRAL;

public abstract class CacheEntry {

    public WriteState s = NEUTRAL;

    public boolean write_check(DatabaseManager dbmg) {
        switch (s) {
            case PENDING_WRITE:
                write(dbmg);
                s = NEUTRAL;
                return true;
            case PENDING_DELETE:
                delete(dbmg);
                s = NEUTRAL;
                return true;
            default:
                break;
        }
        return false;
    }
    protected abstract void write(DatabaseManager dbmg);

    protected abstract void delete(DatabaseManager dbmg);
}
