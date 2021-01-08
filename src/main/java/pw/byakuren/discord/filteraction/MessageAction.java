package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;

public abstract class MessageAction implements Action<Message> {

    public abstract String getRepresentation();

    // todo include arguments
    public String getDisplay() {
        return getRepresentation()+"<>";
    }

    public abstract Argument[] getExpectedArguments();

    protected abstract String[] getArguments();

    protected abstract MessageAction parseFromString(String s);

    public final MessageAction fromString(String s) {
        return parseFromString(s.substring(getRepresentation().length()+1, s.lastIndexOf(">")));
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
