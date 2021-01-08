package pw.byakuren.discord.util;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.filters.*;

import java.util.HashMap;

public abstract class MessageFilterParser {

    private static final MessageFilter[] exampleFilters = new MessageFilter[]{
            new AdminFilter(),
            new PingFilter(3),
            new InviteFilter(),
            new JoinTimeFilter(10),
            new PositiveRoleFilter(123456),
            new RegexFilter(".*"),
            new MessageLengthFilter(100)
    };

    private static final HashMap<String, MessageFilter> filterMap = new HashMap<>();

    static {
        for (MessageFilter example : exampleFilters) {
            filterMap.put(example.getName(), example);
        }
    }

    public static Filter<Message> fromString(String s) {
        if (!s.contains("(")) return null;
        String name = s.substring(0, s.indexOf("("));
        if (filterMap.containsKey(name)) {
            return filterMap.get(name).fromString(s);
        }
        return null; //the filter does not exist
    }

    public static MessageFilter[] getExamples() {
        return exampleFilters;
    }
}
