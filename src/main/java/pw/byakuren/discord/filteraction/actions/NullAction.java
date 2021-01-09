package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class NullAction extends MessageAction {
    @Override
    public ActionResult execute(Message obj) {
        return new ActionResult(true, getDisplay(), null);
    }

    @Override
    public String getName() {
        return "nothing";
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
        return new NullAction();
    }
}
