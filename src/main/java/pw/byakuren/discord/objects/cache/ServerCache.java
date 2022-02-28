package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.*;
import pw.byakuren.discord.objects.cache.factories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_DELETE;
import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class ServerCache {

    private final long id;

    private final @NotNull CacheObject<UserStats> userdata;
    private final @NotNull CacheObject<ServerSettings> settings;
    private final @NotNull CacheObject<RegexKey> regex_keys;
    private final @NotNull CacheObject<WatchedUser> watched_users;
    private final @NotNull CacheObject<WatchedRole> watched_roles;
    private final @NotNull CacheObject<ExcludedChannel> excluded_channels;
    private final @NotNull CacheObject<LastMessage> last_messages;
    private final @NotNull CacheObject<VoiceBan> voice_bans;
    private final @NotNull CacheObject<MessageFilterAction> message_filters;

    @NotNull ServerWriteThread write_thread;
    private final @NotNull CacheObject @NotNull [] objects;

    ServerCache(long id, @NotNull DatabaseManager dbmg, @NotNull JDA jda) {
        this.id = id;
        userdata = new CacheObject<>(id, dbmg, new UserStatsFactory(id, dbmg));
        settings = new CacheObject<>(id, dbmg, new ServerSettingsFactory(id, dbmg));

        regex_keys = new CacheObject<>(id, dbmg, new RegexKeyFactory(id, dbmg));
        watched_users = new CacheObject<>(id, dbmg, new WatchedUserFactory(id, dbmg, jda));
        watched_roles = new CacheObject<>(id, dbmg, new WatchedRoleFactory(id, dbmg, jda));
        excluded_channels = new CacheObject<>(id, dbmg, new ExcludedChannelFactory(id, dbmg));
        last_messages = new CacheObject<>(id, dbmg, new LastMessageFactory(id, dbmg));
        voice_bans = new CacheObject<>(id, dbmg, new VoiceBanFactory(id, dbmg));
        message_filters = new CacheObject<>(id, dbmg, new MessageFilterActionFactory(id, dbmg));
        objects = new CacheObject[]{userdata, settings, regex_keys, watched_roles, watched_users, excluded_channels,
                last_messages, voice_bans, message_filters};
        write_thread = new ServerWriteThread(id, objects);
    }

    public long getId() {
        return id;
    }

    public @NotNull CacheObject<UserStats> getUserStats() {
        return userdata;
    }

    public @NotNull CacheObject<ServerSettings> getSettings() {
        return settings;
    }

    public @NotNull CacheObject<RegexKey> getRegexKeys() {
        return regex_keys;
    }

    public @NotNull CacheObject<WatchedUser> getWatchedUsers() {
        return watched_users;
    }

    public @NotNull CacheObject<WatchedRole> getWatchedRoles() {
        return watched_roles;
    }

    public @NotNull CacheObject<ExcludedChannel> getExcludedChannels() {
        return excluded_channels;
    }

    public @NotNull CacheObject<LastMessage> getLastMessages() {
        return last_messages;
    }

    public @NotNull CacheObject<VoiceBan> getVoiceBans() { return voice_bans; }

    public @NotNull CacheObject<MessageFilterAction> getMessageFilterActions() { return message_filters; }

    public @NotNull UserStats getStatsForUser(@NotNull Member m) {
        return getStatsForUser(m.getGuild().getIdLong(), m.getUser());
    }

    public @NotNull UserStats getStatsForUser(long serverid, @NotNull User u) {
        return getStatsForUser(serverid, u.getIdLong());
    }

    public @NotNull UserStats getStatsForUser(long serverid, long id) {
        for (UserStats s: userdata.getData()) {
            if (s.getUser()==id) return s;
        }
        UserStats s = new UserStats(serverid, id);
        s.write_state=PENDING_WRITE;
        userdata.getData().add(s);
        return s;
    }

    public @NotNull List<RegexKey> getAllValidRegexKeys() {
        List<RegexKey> keys = new ArrayList<>(regex_keys.getData());
        for (int i =0; i < keys.size(); i++) {
            if (keys.get(i).write_state==PENDING_DELETE) {
                keys.remove(i);
                i--;
            }
        }
        return keys;
    }

    public @Nullable TextChannel getLogChannel(@NotNull JDA jda) {
        for (ServerSettings s: settings.getData()) {
            if (s.getSetting()==ServerParameter.SERVER_LOG_CHANNEL) {
                return jda.getTextChannelById(s.getValue());
            }
        }
        return null;
    }

    public @NotNull List<WatchedRole> getAllValidWatchedRoles() {
        List<WatchedRole> a = new ArrayList<>(watched_roles.getData());
        for (int i =0; i < a.size(); i++) {
            if (a.get(i).write_state==PENDING_DELETE) {
                a.remove(i);
                i--;
            }
        }
        return a;
    }

    public boolean roleIsWatched(@NotNull Role r) {
        for (WatchedRole wr: getAllValidWatchedRoles()) {
            if (wr.getRole().getIdLong()==r.getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public @NotNull List<WatchedUser> getAllValidWatchedUsers() {
        List<WatchedUser> a = new ArrayList<>(watched_users.getData());
        for (int i =0; i < a.size(); i++) {
            if (a.get(i).write_state==PENDING_DELETE) {
                a.remove(i);
                i--;
            }
        }
        return a;
    }

    public boolean userIsWatched(@NotNull Member m) {
        for (WatchedUser wu: getAllValidWatchedUsers()) {
            if (wu.getUser().getUser().getIdLong()==m.getUser().getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public @Nullable Role getModeratorRole(@NotNull JDA jda) {
        for (ServerSettings s: settings.getData()) {
            if (s.getSetting()==ServerParameter.SERVER_MODERATOR_ROLE && s.write_state!=PENDING_DELETE) {
                return jda.getRoleById(s.getValue());
            }
        }
        return null;
    }

    public void removeModeratorRole() {
        for (ServerSettings s: settings.getData()) {
            if (s.getSetting()==ServerParameter.SERVER_MODERATOR_ROLE) {
                s.write_state=PENDING_DELETE;
            }
        }
    }

    public @NotNull List<ExcludedChannel> getAllValidExcludedChannels() {
        List<ExcludedChannel> a = new ArrayList<>(excluded_channels.getData());
        for (int i =0; i < a.size(); i++) {
            if (a.get(i).write_state==PENDING_DELETE) {
                a.remove(i);
                i--;
            }
        }
        return a;
    }

    public boolean channelIsExcluded(@NotNull TextChannel c) {
        for (ExcludedChannel ex: getAllValidExcludedChannels()) {
            if (ex.getChannel().getIdLong()==c.getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public void removeExcludedChannel(@NotNull TextChannel c) {
        for (ExcludedChannel ex: excluded_channels.getData()) {
            if (ex.getChannel().getIdLong()==c.getIdLong()) {
                ex.write_state=PENDING_DELETE;
            }
        }
    }

    public @NotNull List<VoiceBan> getValidVoiceBans() {
        List<VoiceBan> a = new ArrayList<>(voice_bans.getData());
        for (int i =0; i < a.size(); i++) {
            if (!a.get(i).isValid()) {
                a.remove(i);
                i--;
            }
        }
        return a;
    }

    public boolean userIsVoiceBanned(@NotNull Member m) {
        for (VoiceBan vb: voice_bans.getData()) {
            if ((vb.getMemberId()==m.getIdLong()) && vb.isValid()) return true;
        }
        return false;
    }

    public @Nullable VoiceBan getValidVoiceBan(@NotNull Member m) {
        for (VoiceBan vb: voice_bans.getData()) {
            if ((vb.getMemberId()==m.getIdLong()) && vb.isValid()) return vb;
        }
        return null;
    }

    public @NotNull List<VoiceBan> getPrevVoiceBans(int c) {
        List<VoiceBan> l = new ArrayList<>();
        for (int i = 0; i < c && i < voice_bans.getData().size(); i++) {
            l.add(voice_bans.getData().get(i));
        }
        return l;
    }

    public @Nullable MessageFilterAction getFilterActionByName(String name) {
        for (MessageFilterAction mfa: getAllFilterActions()) {
            if (mfa.getName().equals(name)) {
                return mfa;
            }
        }
        return null;
    }

    public List<MessageFilterAction> getAllFilterActions() {
        return message_filters.getData().stream().filter(CacheEntry::exists).collect(Collectors.toList());
    }
}
