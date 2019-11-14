package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Member;
import pw.byakuren.discord.DatabaseManager;

import java.time.LocalDateTime;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_DELETE;

public class VoiceBan extends CacheEntry {

    private final long guild_id;
    private final long member_id;
    private final long mod_id;
    private final LocalDateTime started;
    private final LocalDateTime expires;
    private boolean canceled = false;
    private String reason;

    public VoiceBan(long guild_id, long member_id, long mod_id, LocalDateTime started, LocalDateTime expired) {
        this.guild_id = guild_id;
        this.member_id = member_id;
        this.mod_id = mod_id;
        this.started = started;
        this.expires = expired;
    }

    public VoiceBan(long guild_id, long member_id, long mod_id, LocalDateTime started, LocalDateTime expired, String reason) {
        this.guild_id = guild_id;
        this.member_id = member_id;
        this.mod_id = mod_id;
        this.started = started;
        this.expires = expired;
        this.reason=reason;
    }

    public VoiceBan(Member banned, Member moderator, LocalDateTime started, LocalDateTime expired) {
        this.guild_id = banned.getGuild().getIdLong();
        this.member_id = banned.getIdLong();
        this.mod_id = moderator.getIdLong();
        this.started = started;
        this.expires = expired;
    }

    @Override
    protected void write(DatabaseManager dbmg) {
        dbmg.addVoiceBan(this);
    }

    /* delete does NOT delete the ban, rather this indicates it has been cancelled. */
    @Override
    protected void delete(DatabaseManager dbmg) {
        dbmg.updateVoiceBan(this);
    }

    public void cancel() {
        canceled = true;
        write_state = PENDING_DELETE;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean isValid() {
        return (!canceled && (expires == null || LocalDateTime.now().isBefore(expires)));
    }

    public long getGuildId() {
        return guild_id;
    }

    public long getMemberId() {
        return member_id;
    }

    public long getModId() {
        return mod_id;
    }

    public LocalDateTime getStartTime() {
        return started;
    }

    public LocalDateTime getExpireTime() {
        return expires;
    }

    public String getReason() {
        return reason;
    }

    /* these two methods have no real reason to be here but they're here because i dont have anywhere else to put them
    * and they're somewhat related*/
    public static String formatVoiceBan(VoiceBan vb) {
        return String.format("[%s] U:<@%d> M:<@%d> S:%s E:%s R:%s", vb.getStringState(), vb.getMemberId(),
                vb.getModId(), formatDateTime(vb.getStartTime()), formatDateTime(vb.getExpireTime()), vb.getReason());
    }

    public static String formatDateTime(LocalDateTime dt) {
        return String.format("%s %d, %d %2d:%2d", dt.getMonth(), dt.getDayOfMonth(), dt.getYear(),
                dt.getHour(), dt.getMinute());
    }

    public String getStringState() {
        if (isCanceled()) return "Canceled";
        if (!isValid()) return "Expired";
        return "Active";
    }

    @Override
    public String toString() {
        return formatVoiceBan(this);
    }
}
