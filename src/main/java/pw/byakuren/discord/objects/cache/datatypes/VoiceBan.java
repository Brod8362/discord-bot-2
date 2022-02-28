package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;

import java.time.LocalDateTime;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_DELETE;

public class VoiceBan extends CacheEntry {

    private final long guild_id;
    private final long member_id;
    private final long mod_id;
    private final @NotNull LocalDateTime started;
    private final @NotNull LocalDateTime expires;
    private boolean canceled = false;
    private @Nullable String reason;

    public VoiceBan(long guild_id, long member_id, long mod_id, @NotNull LocalDateTime started, @NotNull LocalDateTime expired) {
        this.guild_id = guild_id;
        this.member_id = member_id;
        this.mod_id = mod_id;
        this.started = started;
        this.expires = expired;
    }

    public VoiceBan(long guild_id, long member_id, long mod_id, @NotNull LocalDateTime started, @NotNull LocalDateTime expired, @Nullable String reason) {
        this.guild_id = guild_id;
        this.member_id = member_id;
        this.mod_id = mod_id;
        this.started = started;
        this.expires = expired;
        this.reason=reason;
    }

    public VoiceBan(@NotNull Member banned, @NotNull Member moderator, @NotNull LocalDateTime started, @NotNull LocalDateTime expired) {
        this.guild_id = banned.getGuild().getIdLong();
        this.member_id = banned.getIdLong();
        this.mod_id = moderator.getIdLong();
        this.started = started;
        this.expires = expired;
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addVoiceBan(this);
    }

    /* delete does NOT delete the ban, rather this indicates it has been cancelled. */
    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
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

    public @NotNull LocalDateTime getStartTime() {
        return started;
    }

    public @NotNull LocalDateTime getExpireTime() {
        return expires;
    }

    public @Nullable String getReason() {
        return reason;
    }

    /* these two methods have no real reason to be here but they're here because i dont have anywhere else to put them
    * and they're somewhat related*/
    public static @NotNull String formatVoiceBan(@NotNull VoiceBan vb) {
        return String.format("[%s] U:<@%d> M:<@%d> S:%s E:%s R:%s", vb.getStringState(), vb.getMemberId(),
                vb.getModId(), formatDateTime(vb.getStartTime()), formatDateTime(vb.getExpireTime()), vb.getReason());
    }

    public static @NotNull String formatDateTime(@NotNull LocalDateTime dt) {
        return String.format("%s %d, %d %2d:%2d", dt.getMonth(), dt.getDayOfMonth(), dt.getYear(),
                dt.getHour(), dt.getMinute());
    }

    public @NotNull String getStringState() {
        if (isCanceled()) return "Canceled";
        if (!isValid()) return "Expired";
        return "Active";
    }

    @Override
    public @NotNull String toString() {
        return formatVoiceBan(this);
    }
}
