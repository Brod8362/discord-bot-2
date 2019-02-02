package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import pw.byakuren.discord.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class LastMessage extends CacheDatatype {

    private Message m;

    public LastMessage(Message m) {
        this.m = m;
    }

    /**
     *
     * @return The message object the instance represents.
     */
    public Message getMessage() {
        return m;
    }

    /**
     *
     * @param qualifiers Objects to compare with. Accepts one of: Long, String, Message, or User.
     *                   Long: compares message ID
     *                   String: compares message raw content
     *                   Message: compares message object
     *                   User: compares author
     * @return Whether or not the object matches with the given comparator.
     */
    @Override
    public boolean matches(Object... qualifiers) {
        switch (qualifiers.length) {
            case 1:
                Object o = qualifiers[0];
                if (o instanceof Long) {
                    return o.equals(m.getId());
                } else if (o instanceof String) {
                    return o.equals(m.getContentRaw());
                } else if (o instanceof User) {
                    return o.equals(m.getAuthor());
                } else if (o instanceof Message) {
                    return o.equals(m);
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    /**
     *
     * @param m The member whose last message you want to retrieve.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The Member if found, otherwise null.
     */
    public static LastMessage get(Member m, DatabaseManager dbmg) {
        return null; //TODO add methods in dbmg and here.
    }

    /**
     *
     * @param serverid The ID of the server you'd like to get all messages for.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The list of all LastMessages associated with the given server
     */
    public static List<LastMessage> getAll(long serverid, DatabaseManager dbmg) {
        return new ArrayList<>(); //TODO add methods in dbmg and here.
    }

    /**
     *
     * @param guild Guild object you'd like to get messages for.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The list of all LastMessages associated with the given server
     */
    public static List<LastMessage> getAll(Guild guild, DatabaseManager dbmg) {
        return getAll(guild.getIdLong(), dbmg);
    }
}
