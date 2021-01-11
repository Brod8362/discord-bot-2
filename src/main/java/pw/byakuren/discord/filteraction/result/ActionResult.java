package pw.byakuren.discord.filteraction.result;

import pw.byakuren.discord.util.MiscUtil;

public class ActionResult {

    public final boolean success;
    public final String name;
    public final Exception exception;

    public ActionResult(boolean success, String name, Exception exception) {
        this.success = success;
        this.name = name;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return String.format("%s %s : %s", MiscUtil.booleanToEmoji(success), name, exception == null ? "OK" : exception.getMessage());
    }
}
