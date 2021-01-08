package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class SendPMAction extends MessageAction {

    private final String msgContent;

    public SendPMAction(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            obj.getAuthor().openPrivateChannel().complete().sendMessage(msgContent).complete();
            success = true;
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(success, getRepresentation(), ex);
    }

    @Override
    public String getRepresentation() {
        return "sendPM";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("message", ArgumentType.STRING, "the content of the message to PM")};
    }

    @Override
    protected String[] getArguments() {
        return new String[]{};
    }

    @Override
    protected MessageAction parseFromString(String s) {
        return new SendPMAction(s);
    }
}
