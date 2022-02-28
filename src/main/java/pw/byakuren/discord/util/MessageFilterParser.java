package pw.byakuren.discord.util;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.filters.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MessageFilterParser {

    private static final @NotNull MessageFilter[] exampleFilters = new MessageFilter[]{
            new AdminFilter(),
            new ContainsFilter("stuff"),
            new EveryoneFilter(),
            new MentionsRoleFilter(123456),
            new PingFilter(3),
            new InviteFilter(),
            new JoinTimeFilter(10),
            new PositiveRoleFilter(123456),
            new RegexFilter(".*"),
            new MessageLengthFilter(100),
            new TrueFilter(),
            new RolePingFilter(3),
            new ChannelFilter(0)
    };

    private static final HashMap<String, MessageFilter> filterMap = new HashMap<>();

    static {
        for (MessageFilter example : exampleFilters) {
            filterMap.put(example.getName(), example);
        }
    }

    public static @Nullable Filter<Message> fromString(@NotNull String s) {
        if (!s.contains("(")) return null;
        String name = s.substring(0, s.indexOf("("));
        boolean not = s.startsWith("!");
        if (not) {
            name=name.substring(1);
            s=s.substring(1);
        }
        if (filterMap.containsKey(name)) {
            MessageFilter mf = filterMap.get(name).fromString(s);
            mf.setInverted(not);
            return mf;
        }
        return null; //the filter does not exist
    }

    public static @NotNull MessageFilter @NotNull [] getExamples() {
        return exampleFilters;
    }

    public static List<Filter<Message>> parseMany(@NotNull List<String> strs) {
        return strs.stream().map(MessageFilterParser::fromString).collect(Collectors.toList());
    }
}
