package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class SendAction extends MessageAction {

    private final long channelId;
    private final @NotNull String content;

    public SendAction(long channelId, @NotNull String content) {
        this.channelId = channelId;
        this.content = content;
    }

    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        Exception ex = null;
        try {
            TextChannel tc = obj.getGuild().getTextChannelById(channelId);
            if (tc == null) {
                throw new RuntimeException("channel not found");
            }
            tc.sendMessage(content).queue();
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(getDisplay(), ex);
    }

    @Override
    public @NotNull String getName() {
        return "send";
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("channelId", ArgumentType.CHANNEL_ID, "ID of channel to send message to"),
                new Argument("content", ArgumentType.STRING, "message content to send")
        };
    }

    @Override
    protected @NotNull String @NotNull [] getArguments() {
        return new String[]{channelId + "", content};
    }

    @Override
    protected @NotNull MessageAction parseFromString(@NotNull String s) {
        String[] split = s.split(",");
        return new SendAction(Long.parseLong(split[0]), split[1]);
    }
}
