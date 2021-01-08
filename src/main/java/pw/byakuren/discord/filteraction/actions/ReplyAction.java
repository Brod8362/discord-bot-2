package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class ReplyAction extends MessageAction {

    private final String replyMessage;

    public ReplyAction(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            obj.reply(replyMessage).complete();
            success = true;
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(success, getDisplay(), ex);
    }

    @Override
    protected String getRepresentation() {
        return "reply";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("reply message", ArgumentType.STRING, "the content of the reply")};
    }

    @Override
    protected String[] getArguments() {
        return new String[]{replyMessage};
    }

}
