package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;
import pw.byakuren.discord.objects.cache.ServerCache;

public class AlertAction extends MessageAction {

    private final ServerCache sc;

    public AlertAction(ServerCache sc) {
        this.sc = sc;
    }

    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;

        TextChannel lc = sc.getLogChannel(obj.getJDA());
        if (lc == null) {
            return new ActionResult(false, getRepresentation(), new NullPointerException("text channel not defined"));
        }
        try {
            //todo update this lol
            lc.sendMessage("a bad happened").complete();
            success=true;
        } catch (Exception e) {
            ex =e;
        }
        return new ActionResult(success, getRepresentation(), ex);
    }

    @Override
    protected String getRepresentation() {
        return "alert";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[0];
    }

    @Override
    protected String[] getArguments() {
        return new String[0];
    }
}
