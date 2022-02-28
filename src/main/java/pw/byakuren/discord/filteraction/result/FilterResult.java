package pw.byakuren.discord.filteraction.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.util.MiscUtil;

public class FilterResult {

    public final boolean triggered;
    public final boolean inverted;
    private final @NotNull String name;
    public final @Nullable String reason;

    public FilterResult(boolean triggered, boolean inverted, @NotNull String filter, @Nullable String reason) {
        this.triggered = triggered;
        this.inverted = inverted;
        this.name = filter;
        this.reason = reason;
    }

    //todo im really tired and can't think of what to call this, do it later
    public boolean functionName() {
        return MiscUtil.flip(triggered, inverted);
    }

    public String toString() {
        return String.format("%s %s : %s", MiscUtil.booleanToEmoji(functionName()), name, reason == null ? "OK" : reason);
    }
}
