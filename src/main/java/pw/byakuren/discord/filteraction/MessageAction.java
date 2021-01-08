package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;

public abstract class MessageAction implements Action<Message> {

    protected abstract String getRepresentation();

    public String getDisplay() {
        return getRepresentation()+"<>";
    }

    public abstract Argument[] getExpectedArguments();

    protected abstract String[] getArguments();
}
