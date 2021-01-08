package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class PingFilter extends MessageFilter {

    private final int count;

    public PingFilter(int count) {
        this.count = count;
    }

    @Override
    public String getRepresentation() {
        return "hasPings";
    }

    @Override
    public String[] getArguments() {
        return new String[]{count+""};
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean trigger = obj.getMentionedRoles().size()>=count;
        String reason = trigger ? null : String.format("the user did not ping %d or more users", count);
        return new FilterResult(trigger, getDisplay(), reason);
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new PingFilter(Integer.parseInt(s));
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("pings", ArgumentType.NUMBER, "how many pings the message must have to trigger this filter")};
    }
}
