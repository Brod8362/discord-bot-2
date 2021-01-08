package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class BanAction extends MessageAction {

    private final int delDays;

    public BanAction(int delDays) {
        this.delDays = delDays;
    }

    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            obj.getMember().ban(delDays).complete();
            success=true;
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(success, getRepresentation(), ex);
    }

    @Override
    protected String getRepresentation() {
        return "kick";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("days", ArgumentType.NUMBER, "number of days of messages to delete")};
    }

    @Override
    protected String[] getArguments() {
        return new String[]{delDays+""};
    }
}
