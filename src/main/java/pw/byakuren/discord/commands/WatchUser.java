package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.WatchedUser;

import java.util.List;
import java.util.stream.Collectors;

public class WatchUser extends Command {

    private Cache c;

    public WatchUser(Cache c) {
        names = new String[]{"voicewatch", "vw"};
        help = "Add a user to voicewatch.";
        minimum_permission=CommandPermission.MOD_ROLE;

        this.c = c;

        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"add","a"}, "Add a new watched role.", "@Role", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_add(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"del","d","remove","r"}, "Remove a watched role.", "@Role", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_del(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"list", "l", "all"}, "See existing watched roles.", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_list(message, args);
            }
        });
    }

    private void cmd_add(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        for (Member m: message.getMentionedMembers()) {
            if (!sc.userIsWatched(m))
                sc.getWatchedUsers().getData().add(new WatchedUser(m));
        }
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_del(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        for (Member m: message.getMentionedMembers()) {
            if (sc.userIsWatched(m)) {
                for (int i = 0; i < sc.getWatchedUsers().getData().size(); i++) {
                    if (sc.getWatchedUsers().getData().get(i).getUser().getUser().getIdLong()==
                            m.getUser().getIdLong()) {
                        sc.getWatchedUsers().getData().remove(i);
                        i--;
                    }
                }
            }
        }
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_list(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        List<WatchedUser> list = sc.getAllValidWatchedUsers();
        StringBuilder s = new StringBuilder();
        s.append("Watched users:\n");
        s.append(list.stream().map(WatchedUser::getUserMention).collect(Collectors.joining(", ")));
        message.getChannel().sendMessage(s.toString()).queue();
    }
}
