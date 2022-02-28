package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class ChannelFilter extends MessageFilter {
    
    private final long channelId;

    public ChannelFilter(long channelId) {
        this.channelId = channelId;
    }

    @Override
    public @NotNull String getName() {
        return "inChannel";
    }

    @Override
    public String @NotNull [] getArguments() {
        return new String[]{""+channelId};
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean res = obj.getTextChannel().getIdLong()==channelId;
        String reason = res ? null : "The message was not in channel <#"+channelId+">";
        return new FilterResult(res, inverted, getDisplay(), reason);
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new ChannelFilter(Long.parseLong(s));
    }

    @Override
    public Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("channelId", ArgumentType.CHANNEL_ID, "ID of channel the message must be in")};
    }
}
