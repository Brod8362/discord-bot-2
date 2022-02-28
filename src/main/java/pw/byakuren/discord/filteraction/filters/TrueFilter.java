package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class TrueFilter extends MessageFilter {
    @Override
    public @NotNull String getName() {
        return "true";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[0];
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        return new FilterResult(true, inverted, getDisplay(), null);
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new TrueFilter();
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[0];
    }
}
