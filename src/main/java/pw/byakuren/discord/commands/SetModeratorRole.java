package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.ServerParameter;
import pw.byakuren.discord.objects.cache.datatypes.ServerSettings;

import java.util.List;

public class SetModeratorRole extends Command {

    Cache c;

    public SetModeratorRole(Cache c) {
        names=new String[]{"setmodrole", "modrole"};
        help="Sets the moderator role for the server. Role must be pingable.";
        minimum_permission=CommandPermission.SERVER_ADMIN;

        this.c = c;
    }

    @Override
    public void run(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        Role r = message.getMentionedRoles().get(0);
        Role modr = sc.getModeratorRole(message.getJDA());
        ServerSettings setting = new ServerSettings(message.getGuild(), ServerParameter.SERVER_MODERATOR_ROLE, r.getIdLong());
        setting.write_state= WriteState.PENDING_WRITE;
        if (modr==null) {
            sc.getSettings().getData().add(setting);
        } else {
            sc.removeModeratorRole();
            sc.getSettings().getData().add(setting);
        }
        message.getChannel().sendMessage("Moderator role set to "+r.getAsMention()).queue();
    }
}
