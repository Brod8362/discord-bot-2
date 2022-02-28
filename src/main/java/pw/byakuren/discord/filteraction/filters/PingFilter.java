package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
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
    public @NotNull String getName() {
        return "hasPings";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[]{count+""};
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean trigger = obj.getMentionedUsers().size()>=count;
        String reason = trigger ? null : String.format("the user did not ping %d or more users", count);
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new PingFilter(Integer.parseInt(s));
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("pings", ArgumentType.NUMBER, "how many pings the message must have to trigger this filter")};
    }
}
