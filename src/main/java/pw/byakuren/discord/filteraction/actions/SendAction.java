package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class SendAction extends MessageAction {

    private final long channelId;
    private final String content;

    public SendAction(long channelId, String content) {
        this.channelId = channelId;
        this.content = content;
    }

    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            TextChannel tc = obj.getGuild().getTextChannelById(channelId);
            if (tc == null) {
                throw new RuntimeException("channel not found");
            } else {
                tc.sendMessage(content).queue();
                success=true;
            }
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(success, getDisplay(), ex);
    }

    @Override
    public String getName() {
        return "send";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("channelId", ArgumentType.CHANNEL_ID, "ID of channel to send message to"),
                new Argument("content", ArgumentType.STRING, "message content to send")
        };
    }

    @Override
    protected String[] getArguments() {
        return new String[]{channelId + "", content};
    }

    @Override
    protected MessageAction parseFromString(String s) {
        String[] split = s.split(",");
        return new SendAction(Long.parseLong(split[0]), split[1]);
    }
}
