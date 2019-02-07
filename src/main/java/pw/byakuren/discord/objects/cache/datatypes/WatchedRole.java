package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

public class WatchedRole extends CacheDatatype {

    long serverid;
    Role role;

    public WatchedRole(long roleid, JDA jda) {
        this.role = jda.getRoleById(roleid);
        this.serverid = this.role.getGuild().getIdLong();
    }

    public WatchedRole(Role role) {
        this.role = role;
        this.serverid = role.getGuild().getIdLong();
    }
}
