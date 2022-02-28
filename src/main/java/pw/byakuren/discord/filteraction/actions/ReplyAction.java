package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class ReplyAction extends MessageAction {

    private final @NotNull String replyMessage;

    public ReplyAction(@NotNull String replyMessage) {
        this.replyMessage = replyMessage;
    }

    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
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
    public @NotNull String getName() {
        return "reply";
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("reply message", ArgumentType.STRING, "the content of the reply")};
    }

    @Override
    protected @NotNull String @NotNull [] getArguments() {
        return new String[]{replyMessage};
    }

    @Override
    protected @NotNull MessageAction parseFromString(@NotNull String s) {
        return new ReplyAction(s);
    }

}
