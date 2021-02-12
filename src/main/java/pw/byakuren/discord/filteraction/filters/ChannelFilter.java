package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
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
    public String getName() {
        return "inChannel";
    }

    @Override
    public String[] getArguments() {
        return new String[]{""+channelId};
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean res = obj.getTextChannel().getIdLong()==channelId;
        String reason = res ? null : "The message was not in channel <#"+channelId+">";
        return new FilterResult(res, inverted, getDisplay(), reason);
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new ChannelFilter(Long.parseLong(s));
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("channelId", ArgumentType.CHANNEL_ID, "ID of channel the message must be in")};
    }
}
