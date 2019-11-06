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

public class WatchRole implements Command {

    private Cache c;

    public WatchRole(Cache c) {
        this.c = c;
    }

    @Override
    public String[] getNames() {
        return new String[]{"watchrole", "role", "rw"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Manage watched pingable roles.";
    }

    @Override
    public CommandPermission minimumPermission() {
        return CommandPermission.MOD_ROLE;
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

                for (WatchedRole rw: list) {
                    s.append(rw.getRole().getAsMention()).append(" ");
                }
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
