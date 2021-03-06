package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class InviteFilter extends MessageFilter {

    @Override
    public String getName() {
        return "hasInvite";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean trigger = !obj.getInvites().isEmpty();
        String reason = trigger ? null : "the message does not have an invite in it";
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new InviteFilter();
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[0];
    }
}
