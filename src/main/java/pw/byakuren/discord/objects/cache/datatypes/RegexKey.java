package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;

import java.util.regex.Pattern;

public class RegexKey extends CacheEntry {

    private final @NotNull Guild guild;
    private final @NotNull Pattern key;

    public RegexKey(@NotNull Guild guild, @NotNull Pattern key) {
        this.guild = guild;
        this.key = key;
    }

    public RegexKey(@NotNull Guild guild, @NotNull String key) {
        this(guild, Pattern.compile(key, Pattern.CASE_INSENSITIVE));
    }

    public @NotNull Guild getGuild() {
        return guild;
    }

    public String getKey() {
        return key.pattern();
    }

    public @NotNull Pattern getPattern() { return key; }

    public @NotNull RegexKey clone() {
        return new RegexKey(guild, key);
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addRegexKey(guild, getKey());
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeRegexKey(guild, getKey());
    }
}
