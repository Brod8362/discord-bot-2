package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;

public class WatchedRole extends CacheEntry {

    private final long serverId;
    private final long roleId;

    public WatchedRole(long serverId, long roleId) {
        this.serverId = serverId;
        this.roleId = roleId;
    }

    public WatchedRole(@NotNull Role role) {
        this(role.getGuild().getIdLong(), role.getIdLong());
    }

    public long getServerId() {
        return serverId;
    }

    public long getRoleId() {
        return roleId;
    }

    public @NotNull String getRoleMention() {
        return "<@&" + roleId + ">";
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addWatchedRole(serverId, roleId);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeWatchedRole(serverId, roleId);
    }
}
