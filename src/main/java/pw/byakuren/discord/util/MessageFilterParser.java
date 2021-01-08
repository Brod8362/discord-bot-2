package pw.byakuren.discord.util;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.filters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class MessageFilterParser {

    public static Filter<Message> fromString(String s) {
        String name = s.substring(0, s.indexOf("("));
        String inner = s.substring(name.length()+1, s.lastIndexOf(")"));
        switch (name) {
            case "isAdmin" : return new AdminFilter();
            case "hasPings" : return new PingFilter(Integer.parseInt(inner));
            case "hasInvite" : return new InviteFilter();
            case "hereFor": return new JoinTimeFilter(Integer.parseInt(inner));
            case "hasRole": return new PositiveRoleFilter(Long.parseLong(inner));
            case "matchesRegex": return new RegexFilter(inner);
            default: return null;
        }
    }
}
