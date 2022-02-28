package pw.byakuren.discord.filteraction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.result.FilterResult;

public interface Filter<T> {

    public @NotNull String getName();

    public @NotNull String @NotNull [] getArguments();

    public @NotNull FilterResult apply(@NotNull T obj);

    boolean isInverted();

}
