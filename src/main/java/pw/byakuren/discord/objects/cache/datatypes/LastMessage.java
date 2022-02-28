package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

import java.sql.Date;

public class LastMessage extends CacheEntry {

    public final long serverid;
    public final long userid;
    public final long messageid;
    public final @NotNull String content;
    public final @NotNull Date date;

    public LastMessage(@NotNull Message m) {
        serverid = m.getGuild().getIdLong();
        userid = m.getAuthor().getIdLong();
        messageid = m.getIdLong();
        content = m.getContentRaw();
        date = new Date(m.getTimeCreated().toEpochSecond());
    }

    public LastMessage(long serverid, long userid, long messageid, @NotNull String content, @NotNull Date date) {
        this.serverid = serverid;
        this.userid = userid;
        this.messageid = messageid;
        this.content = content;
        this.date = date;
    }

    /**
     *
     * @param m The member whose last message you want to retrieve.
     * @param dbmg DatabaseManager to be used for attempting to get the requested object.
     * @return The Member if found, otherwise null.
     */
    public static @Nullable LastMessage get(@NotNull Member m, @NotNull DatabaseManager dbmg) {
        return dbmg.getLastMessage(m.getGuild().getIdLong(), m.getUser().getIdLong());
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.updateLastMessage(serverid, userid, content, messageid);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {}
}
