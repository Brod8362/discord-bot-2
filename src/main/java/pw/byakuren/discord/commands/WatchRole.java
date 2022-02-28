package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.WatchedRole;

import java.util.List;
import java.util.stream.Collectors;

public class WatchRole extends Command {

    private final @NotNull Cache c;

    public WatchRole(@NotNull Cache c) {
        names=new String[]{"watchrole", "role", "rw"};
        help="Manage watched pingable roles";
        minimum_permission=CommandPermission.MOD_ROLE;

        this.c = c;

        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"add","a"}, "Add a new watched role.", "@Role", this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                cmd_add(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"del","d","remove","r"}, "Remove a watched role.", "@Role", this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                cmd_del(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"list", "l", "all"}, "See existing watched roles.", null, this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                cmd_list(message, args);
            }
        });
    }

    private void cmd_add(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        for (Role r: message.getMentionedRoles()) {
            if (!sc.roleIsWatched(r)) {
                WatchedRole rol = new WatchedRole(r);
                rol.write_state = WriteState.PENDING_WRITE;
                sc.getWatchedRoles().getData().add(rol);
            }
        }
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_del(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
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
    }

    private void cmd_list(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        List<WatchedRole> list = sc.getAllValidWatchedRoles();
        StringBuilder s = new StringBuilder();
        s.append("Watched roles:\n");
        s.append(list.stream().map(WatchedRole::getRoleMention).collect(Collectors.joining(", ")));
        if (list.size() == 0) {
            s = new StringBuilder();
            s.append("No watched roles. Use 'add' to add some.");
        }
        message.reply(s.toString()).mentionRepliedUser(false).queue();
    }
}
