package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

public class ServerSettings extends CacheEntry {

    private final long s;
    private final @NotNull ServerParameter setting;
    long v;

    public ServerSettings(long s, @NotNull ServerParameter setting, long v) {
        this.s = s;
        this.setting = setting;
        this.v = v;
    }

    public ServerSettings(@NotNull Guild g, @NotNull ServerParameter setting, long v) {
        s=g.getIdLong();
        this.setting=setting;
        this.v=v;
    }

    public @NotNull ServerParameter getSetting() {
        return setting;
    }

    public long getValue() {
        return v;
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.editServerSetting(s, setting.string, v);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        //TODO figure out delete situation  (-1 = delete?)
    }

}
