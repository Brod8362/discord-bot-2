package pw.byakuren.discord.filteraction.result;

import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.util.MiscUtil;

public class FilterResult {

    public final boolean triggered;
    private final String name;
    public final String reason;

    public FilterResult(boolean triggered, String filter, String reason) {
        this.triggered = triggered;
        this.name = filter;
        this.reason = reason;
    }

    public String toString() {
        return String.format("%s %s : %s", MiscUtil.booleanToEmoji(triggered), name, reason == null ? "OK" : reason);
    }
}
