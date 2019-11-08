package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
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
    }

    @Override
    public void run(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        if (args.size() < 1) return;
        switch (args.get(0)) {
            case "add":
                for (Member m: message.getMentionedMembers()) {
                    if (!sc.userIsWatched(m))
                        sc.getWatchedUsers().getData().add(new WatchedUser(m));
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "remove":
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
                break;
            case "list":
                List<WatchedUser> list = sc.getAllValidWatchedUsers();
                StringBuilder s = new StringBuilder();
                s.append("Watched users:\n");
                s.append(list.stream().map(WatchedUser::getUserMention).collect(Collectors.joining(", ")));
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            default:
                message.getChannel().sendMessage("Available arguments: `add [mention users]`, `remove [mention users]`, `list`").queue();
        }
    }
}
