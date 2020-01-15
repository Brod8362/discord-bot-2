package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import pw.byakuren.discord.DatabaseManager;

import java.util.regex.Pattern;

public class RegexKey extends CacheEntry {

    private final Guild guild;
    private final Pattern key;

    public RegexKey(Guild guild, Pattern key) {
        this.guild = guild;
        this.key = key;
    }

    public RegexKey(Guild guild, String key) {
        this(guild, Pattern.compile(key, Pattern.CASE_INSENSITIVE));
    }

    public Guild getGuild() {
        return guild;
    }

    public String getKey() {
        return key.pattern();
    }

    public Pattern getPattern() { return key; }

    public RegexKey clone() {
        return new RegexKey(guild, key);
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.addRegexKey(guild, getKey());
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        dbmg.removeRegexKey(guild, getKey());
    }
}
