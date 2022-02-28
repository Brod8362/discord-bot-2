package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.ExcludedChannel;

import java.util.List;
import java.util.stream.Collectors;

import static pw.byakuren.discord.commands.permissions.CommandPermission.MOD_ROLE;

public class ExcludedChannels extends Command {

    private final @NotNull Cache c;

    public ExcludedChannels(@NotNull Cache c) {
        this.c=c;
        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"list","l"}, null, null, this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                list_cmd(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"here", "h"}, null, null, this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                add_cmd(message, args);
            }
        });

    }

    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"exclude"};
    }

    @Override
    public @NotNull String getHelp() {
        return "Exclude a channel from being flagged for regex keywords.";
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return MOD_ROLE;
    }

    private void list_cmd(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        List<ExcludedChannel> list = sc.getAllValidExcludedChannels();
        if (list.size() == 0) {
            message.reply("You don't have any excluded channels. Call this command with " +
                    "`here` to toggle the current channel as excluded.").mentionRepliedUser(false).queue();
            return;
        }
        String s = "Channels:\n" +
                list.stream().map(ExcludedChannel::getChannelMention).collect(Collectors.joining(", "));
        message.reply(s).mentionRepliedUser(false).queue();
    }

    private void add_cmd(@NotNull Message message, @NotNull List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        if (sc.channelIsExcluded(message.getTextChannel())) {
            sc.removeExcludedChannel(message.getTextChannel());
        } else {
            ExcludedChannel ex = new ExcludedChannel(message.getTextChannel());
            ex.write_state= WriteState.PENDING_WRITE;
            sc.getExcludedChannels().getData().add(ex);
        }
        message.addReaction("\uD83D\uDC4D").queue();
    }
}
