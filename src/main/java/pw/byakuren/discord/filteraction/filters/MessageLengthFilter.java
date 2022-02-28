package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class MessageLengthFilter extends MessageFilter {

    private final int length;

    public MessageLengthFilter(int length) {
        this.length = length;
    }

    @Override
    public @NotNull String getName() {
        return "msgLength";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[]{length+""};
    }

    @Override
    public @NotNull String getArgumentsDisplay() {
        return length+"";
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new MessageLengthFilter(Integer.parseInt(s));
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("length",  ArgumentType.NUMBER, "minimum length of message needed to trigger")};
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean trigger = obj.getContentRaw().length() >= length;
        String reason = trigger ? null : "the message is not at least "+length+" characters long";
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }
}
