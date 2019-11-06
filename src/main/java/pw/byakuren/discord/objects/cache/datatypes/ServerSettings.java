package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import pw.byakuren.discord.DatabaseManager;

public class ServerSettings extends CacheEntry {

    private final long s;
    ServerParameter setting;
    long v;

    public ServerSettings(long s, ServerParameter setting, long v) {
        this.s = s;
        this.setting = setting;
        this.v = v;
    }

    public ServerSettings(Guild g, ServerParameter setting, long v) {
        s=g.getIdLong();
        this.setting=setting;
        this.v=v;
    }

    public ServerParameter getSetting() {
        return setting;
    }

    public long getValue() {
        return v;
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.editServerSetting(s, setting.string, v);
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        //TODO figure out delete situation  (-1 = delete?)
    }

}
