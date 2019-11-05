package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.DatabaseManager;

public class WatchedRole extends CacheEntry {

    private long serverid;
    private Role role;

    public WatchedRole(long roleid, JDA jda) {
        this.role = jda.getRoleById(roleid);
        this.serverid = this.role.getGuild().getIdLong();
    }

    public WatchedRole(Role role) {
        this.role = role;
        this.serverid = role.getGuild().getIdLong();
    }

    public long getServerId() {
        return serverid;
    }

    public Role getRole() {
        return role;
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.addWatchedRole(role);
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        dbmg.removeWatchedRole(role);
    }
}
