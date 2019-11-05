package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import pw.byakuren.discord.DatabaseManager;

public class RegexKey extends CacheEntry {

    private final Guild guild;
    private final String key;

    public RegexKey(Guild guild, String key) {
        this.guild = guild;
        this.key = key;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getKey() {
        return key;
    }

    public RegexKey clone() {
        return new RegexKey(guild, key);
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.addRegexKey(guild, key);
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        dbmg.removeRegexKey(guild, key);
    }
}
