package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class SendPMAction extends MessageAction {

    private final @NotNull String msgContent;

    public SendPMAction(@NotNull String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            // TODO: blocking operation in a thread pool might exhaust threads
            obj.getAuthor().openPrivateChannel().complete().sendMessage(msgContent).complete();
            success = true;
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(success, getName(), ex);
    }

    @Override
    public @NotNull String getName() {
        return "sendPM";
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("message", ArgumentType.STRING, "the content of the message to PM")};
    }

    @Override
    protected @NotNull String @NotNull [] getArguments() {
        return new String[]{msgContent};
    }

    @Override
    protected @NotNull MessageAction parseFromString(@NotNull String s) {
        return new SendPMAction(s);
    }
}
