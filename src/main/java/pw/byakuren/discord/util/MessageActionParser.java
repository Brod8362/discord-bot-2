package pw.byakuren.discord.util;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.actions.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MessageActionParser {

    public static MessageAction[] exampleActions = new MessageAction[]{
            new BanAction(0),
            new DeleteAction(),
            new KickAction(),
            new PinAction(),
            new ReplyAction("content"),
            new SendPMAction("content"),
            new NullAction(),
            new AssignRoleAction(0L),
            new SendAction(12345L, "content"),
            new RevokeRoleAction(0L)
    };

    private static final HashMap<String, MessageAction> actionMap = new HashMap<>();

    static {
        for (MessageAction example: exampleActions) {
            actionMap.put(example.getName(), example);
        }
    }

    public static Action<Message> fromString(String s) {
        if (!s.contains("<")) return null;
        String name = s.substring(0, s.indexOf("<"));
        if (actionMap.containsKey(name)) {
            return actionMap.get(name).fromString(s);
        }
        return null; //the filter does not exist
    }

    public static MessageAction[] getExamples() {
        return exampleActions;
    }

    public static List<Action<Message>> parseMany(List<String> strs) {
        return strs.stream().map(MessageActionParser::fromString).collect(Collectors.toList());
    }
}
