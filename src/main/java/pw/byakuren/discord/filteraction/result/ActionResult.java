package pw.byakuren.discord.filteraction.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.util.MiscUtil;

public class ActionResult {

    public final boolean success;
    public final @NotNull String name;
    public final @Nullable Exception exception;

    public ActionResult(boolean success, @NotNull String name, @Nullable Exception exception) {
        this.success = success;
        this.name = name;
        this.exception = exception;
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s %s : %s", MiscUtil.booleanToEmoji(success), name, exception == null ? "OK" : exception.getMessage());
    }
}
