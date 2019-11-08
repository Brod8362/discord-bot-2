package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.WatchedRole;

import java.util.List;
import java.util.stream.Collectors;

public class WatchRole extends Command {

    private Cache c;

    public WatchRole(Cache c) {
        names=new String[]{"watchrole", "role", "rw"};
        help="Manage watched pingable roles";
        minimum_permission=CommandPermission.MOD_ROLE;

        this.c = c;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() < 1) return;
        ServerCache sc = c.getServerCache(message.getGuild());
        switch (args.get(0)) {
            case "add":
                for (Role r: message.getMentionedRoles()) {
                    if (!sc.roleIsWatched(r)) {
                        WatchedRole rol = new WatchedRole(r);
                        rol.write_state = WriteState.PENDING_WRITE;
                        sc.getWatchedRoles().getData().add(rol);
                    }
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "remove":
                for (Role r: message.getMentionedRoles()) {
                    if (sc.roleIsWatched(r))
                        for (int i = 0; i < sc.getWatchedRoles().getData().size(); i++) {
                            if (sc.getWatchedRoles().getData().get(i).getRole().equals(r)) {
                                sc.getWatchedRoles().getData().remove(i);
                                break;
                            }
                        }
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "list":
                List<WatchedRole> list = sc.getAllValidWatchedRoles();
                StringBuilder s = new StringBuilder();
                s.append("Watched roles:\n");
                s.append(list.stream().map(WatchedRole::getRoleMention).collect(Collectors.joining(", ")));
                if (list.size() == 0) {
                    s = new StringBuilder();
                    s.append("No watched roles. Use 'add' to add some.");
                }
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            default:
                message.getChannel().sendMessage("Available arguments: `add [mention role]`, `remove [mention roles]`, `list`").queue();
        }
    }
}
