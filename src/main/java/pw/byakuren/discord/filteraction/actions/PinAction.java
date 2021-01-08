package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class PinAction extends MessageAction {
    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            obj.pin().complete();
            success = true;
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(success, getDisplay(), ex);
    }

    @Override
    public String getRepresentation() {
        return "pin";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[0];
    }

    @Override
    protected String[] getArguments() {
        return new String[0];
    }

    @Override
    protected MessageAction parseFromString(String s) {
        return new PinAction();
    }
}
