package pw.byakuren.discord.filteraction.result;

public class ActionResult {

    public final boolean success;
    public final String name;
    public final Exception exception;

    public ActionResult(boolean success, String name, Exception exception) {
        this.success = success;
        this.name = name;
        this.exception = exception;
    }
}
