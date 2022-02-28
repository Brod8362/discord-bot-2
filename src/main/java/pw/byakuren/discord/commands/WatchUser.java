package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
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

    private final @NotNull Cache c;

    public WatchUser(@NotNull Cache c) {
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

    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"voicewatch", "vw"};
    }

    @Override
    public @NotNull String getHelp() {
        return "Add a user to voicewatch.";
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return CommandPermission.MOD_ROLE;
    }

    private void cmd_add(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        for (Member m: message.getMentionedMembers()) {
            if (!sc.userIsWatched(m))
                sc.getWatchedUsers().getData().add(new WatchedUser(m));
        }
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_del(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        for (Member m: message.getMentionedMembers()) {
            if (sc.userIsWatched(m)) {
                final List<WatchedUser> watchedUsers = sc.getWatchedUsers().getData();
                for (int i = 0; i < watchedUsers.size(); i++) {
                    if (watchedUsers.get(i).getUserId() ==
                            m.getUser().getIdLong()) {
                        watchedUsers.remove(i);
                        i--;
                    }
                }
            }
        }
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_list(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        List<WatchedUser> list = sc.getAllValidWatchedUsers();
        StringBuilder s = new StringBuilder();
        s.append("Watched users:\n");
        s.append(list.stream().map(WatchedUser::getUserMention).collect(Collectors.joining(", ")));
        message.reply(s.toString()).mentionRepliedUser(false).queue();
    }
}
