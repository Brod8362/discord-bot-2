package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class EveryoneFilter extends MessageFilter {
    @Override
    public @NotNull String getName() {
        return "pingsEveryone";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[0];
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        return new FilterResult(obj.mentionsEveryone(), inverted, getDisplay(), obj.mentionsEveryone() ? null : "the message does not mention everyone");
    }

    @Override
    protected @NotNull MessageFilter parseFromString(String s) {
        return new EveryoneFilter();
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[0];
    }
}
