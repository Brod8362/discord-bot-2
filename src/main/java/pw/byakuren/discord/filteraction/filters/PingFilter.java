package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;

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
    public boolean apply(Message obj) {
        return obj.getMentionedRoles().size()>=count;
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("pings", ArgumentType.NUMBER, "how many pings the message must have to trigger this filter")};
    }
}
