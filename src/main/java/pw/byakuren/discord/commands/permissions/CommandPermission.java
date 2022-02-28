package pw.byakuren.discord.commands.permissions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.objects.cache.Cache;

public enum CommandPermission {

    REGULAR_USER("Anybody"), MOD_ROLE("Server Moderators"), SERVER_ADMIN("Server Admins"), BOT_OWNER("Bot Owner");

    public final @NotNull String name;

    CommandPermission(@NotNull String name) {
        this.name = name;
    }

    public static @NotNull CommandPermission getPermission(@NotNull Member m, @NotNull Cache c) {
        if (m.getUser().getIdLong()==c.owner.getIdLong()) return BOT_OWNER;
        if (m.getPermissions().contains(Permission.ADMINISTRATOR)) return SERVER_ADMIN;
        Role r = c.getServerCache(m.getGuild()).getModeratorRole(m.getJDA());
        if (r != null && m.getRoles().contains(r)) return MOD_ROLE;
        return REGULAR_USER;
    }

}
