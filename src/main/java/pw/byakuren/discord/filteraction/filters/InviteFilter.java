package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class InviteFilter extends MessageFilter {

    @Override
    public @NotNull String getName() {
        return "hasInvite";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[0];
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean trigger = !obj.getInvites().isEmpty();
        String reason = trigger ? null : "the message does not have an invite in it";
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }

    @Override
    protected @NotNull MessageFilter parseFromString(String s) {
        return new InviteFilter();
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[0];
    }
}
