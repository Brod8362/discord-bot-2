package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;

import java.sql.Date;
import java.util.List;

public class LastMessage extends CacheDatatype {

    private long serverid;
    private long userid;
    private long messageid;
    private String content;
    private Date date;

    public LastMessage(Message m) {
        serverid = m.getGuild().getIdLong();
        userid = m.getAuthor().getIdLong();
        messageid = m.getIdLong();
        content = m.getContentRaw();
        date = new Date(m.getTimeCreated().toEpochSecond());
    }

    public LastMessage(long serverid, long userid, long messageid, String content, Date date) {
        this.serverid = serverid;
        this.userid = userid;
        this.messageid = messageid;
        this.content = content;
        this.date = date;
    }

    /**
     *
     * @param qualifiers Objects to compare with. Accepts one of: Long, String
     *                   Long: compares message ID
     *                   String: compares message raw content
     * @return Whether or not the object matches with the given comparator.
     */
    @Override
    public boolean matches(Object... qualifiers) {
        if (qualifiers.length > 1) return false;
        Object o = qualifiers[0];
        if (o instanceof Long) {
            return o.equals(messageid);
        } else if (o instanceof String) {
            return o.equals(content);
        } else {
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
        return dbmg.getLastMessage(m.getGuild().getIdLong(), m.getUser().getIdLong());
    }

    /**
     *
     * @param serverid The ID of the server you'd like to get all messages for.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The list of all LastMessages associated with the given server
     */
    public static List<LastMessage> getAll(long serverid, DatabaseManager dbmg) {
        return dbmg.getLastMessages(serverid);
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
