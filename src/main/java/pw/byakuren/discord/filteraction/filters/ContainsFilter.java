package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class ContainsFilter extends MessageFilter {

    private final @NotNull String containText;

    public ContainsFilter(@NotNull String containText) {
        this.containText = containText;
    }

    @Override
    public @NotNull String getName() {
        return "contains";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[]{containText};
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean apply = obj.getContentRaw().contains(containText);
        String reason = apply ? null : "the message does not contain the sequence specified";
        return new FilterResult(apply, inverted, getDisplay(), reason);
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new ContainsFilter(s);
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("containText", ArgumentType.STRING, "Text the message should contain to trigger this filter")};
    }
}
