package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.ExcludedChannel;

import java.util.List;
import java.util.stream.Collectors;

import static pw.byakuren.discord.commands.permissions.CommandPermission.MOD_ROLE;

public class ExcludedChannels extends Command {

    private Cache c;

    public ExcludedChannels(Cache c) {
        names=new String[]{"exclude"};
        minimum_permission=MOD_ROLE;
        help="Exclude a channel from being flagged for regex keywords.";
        this.c=c;
    }

    @Override
    public void run(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        if (args.size() == 0) return;
        switch (args.get(0)) {
            case "list":
                List<ExcludedChannel> list = sc.getAllValidExcludedChannels();
                if (list.size() == 0) {
                    message.getChannel().sendMessage("You don't have any excluded channels. Call this command with " +
                            "`here` to toggle the current channel as excluded.").queue();
                    return;
                }
                StringBuilder s = new StringBuilder();
                s.append("Channels:\n");
                s.append(list.stream().map(ExcludedChannel::getChannelMention).collect(Collectors.joining(", ")));
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            case "here":
                if (sc.channelIsExcluded(message.getTextChannel())) {
                    sc.removeExcludedChannel(message.getTextChannel());
                } else {
                    ExcludedChannel ex = new ExcludedChannel(message.getTextChannel());
                    ex.write_state= WriteState.PENDING_WRITE;
                    sc.getExcludedChannels().getData().add(ex);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            default:
                message.getChannel().sendMessage("Available options: `here`, `list`").queue();
        }
    }
}
