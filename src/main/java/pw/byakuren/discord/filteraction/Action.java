package pw.byakuren.discord.filteraction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.result.ActionResult;

/**
 * Represents a single action that can be performed on object T.
 * @param <T> The type of object the action can be performed on
 */
public interface Action<T> {

    public @NotNull ActionResult execute(@NotNull T obj);

    public @NotNull String getName();

}
