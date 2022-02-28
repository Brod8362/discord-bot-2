package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

public class WatchedRole extends CacheEntry {

    private long serverid;
    private @NotNull Role role;

    public WatchedRole(long roleid, @NotNull JDA jda) {
        this.role = jda.getRoleById(roleid);
        this.serverid = this.role.getGuild().getIdLong();
    }

    public WatchedRole(@NotNull Role role) {
        this.role = role;
        this.serverid = role.getGuild().getIdLong();
    }

    public long getServerId() {
        return serverid;
    }

    public @NotNull Role getRole() {
        return role;
    }

    public @NotNull String getRoleMention() {
        return role.getAsMention();
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addWatchedRole(role);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeWatchedRole(role);
    }
}
