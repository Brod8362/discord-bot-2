package pw.byakuren.discord.util;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.actions.*;

import java.util.HashMap;

public abstract class MessageActionParser {

    public static MessageAction[] exampleActions = new MessageAction[]{
            new BanAction(0),
            new DeleteAction(),
            new KickAction(),
            new PinAction(),
            new ReplyAction(""),
            new SendPMAction("")
    };

    private static final HashMap<String, MessageAction> actionMap = new HashMap<>();

    static {
        for (MessageAction example: exampleActions) {
            actionMap.put(example.getRepresentation(), example);
        }
    }

    public static Action<Message> fromString(String s) {
        String name = s.substring(0, s.indexOf("<"));
        if (actionMap.containsKey(name)) {
            return actionMap.get(name);
        }
        return null; //the filter does not exist
    }

    public static MessageAction[] getExamples() {
        return exampleActions;
    }
}
