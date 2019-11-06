package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.WatchRole;
import pw.byakuren.discord.objects.cache.datatypes.*;
import pw.byakuren.discord.objects.cache.factories.*;

import java.util.ArrayList;
import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_DELETE;
import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class ServerCache {

    private final long id;

    private CacheObject<UserStats> userdata;
    private CacheObject<ServerSettings> settings;
    private CacheObject<RegexKey> regex_keys;
    private CacheObject<WatchedUser> watched_users;
    private CacheObject<WatchedRole> watched_roles;
    private CacheObject<ExcludedChannel> excluded_channels;
    private CacheObject<LastMessage> last_messages;

    ServerWriteThread write_thread;
    CacheObject[] objects;

    ServerCache(long id, DatabaseManager dbmg, JDA jda) {
        this.id = id;
        userdata = new CacheObject<>(id, dbmg, new UserStatsFactory(id, dbmg));
        settings = new CacheObject<>(id, dbmg, new ServerSettingsFactory(id, dbmg));

        regex_keys = new CacheObject<>(id, dbmg, new RegexKeyFactory(id, dbmg));
        watched_users = new CacheObject<>(id, dbmg, new WatchedUserFactory(id, dbmg, jda));
        watched_roles = new CacheObject<>(id, dbmg, new WatchedRoleFactory(id, dbmg, jda));
        excluded_channels = new CacheObject<>(id, dbmg, new ExcludedChannelFactory(id, dbmg));
        last_messages = new CacheObject<>(id, dbmg, new LastMessageFactory(id, dbmg));
        objects = new CacheObject[]{userdata, settings, regex_keys, watched_roles, watched_users, excluded_channels, last_messages};
        write_thread = new ServerWriteThread(id, objects);
    }

    public long getId() {
        return id;
    }

    public CacheObject<UserStats> getUserStats() {
        return userdata;
    }

    public CacheObject<ServerSettings> getSettings() {
        return settings;
    }

    public CacheObject<RegexKey> getRegexKeys() {
        return regex_keys;
    }

    public CacheObject<WatchedUser> getWatchedUsers() {
        return watched_users;
    }

    public CacheObject<WatchedRole> getWatchedRoles() {
        return watched_roles;
    }

    public CacheObject<ExcludedChannel> getExcludedChannels() {
        return excluded_channels;
    }

    public CacheObject<LastMessage> getLastMessages() {
        return last_messages;
    }

    public UserStats getStatsForUser(Member m) {
        return getStatsForUser(m.getGuild().getIdLong(), m.getUser());
    }

    public UserStats getStatsForUser(long serverid, User u) {
        return getStatsForUser(serverid, u.getIdLong());
    }

    public UserStats getStatsForUser(long serverid,long id) {
        for (UserStats s: userdata.getData()) {
            if (s.getUser()==id) return s;
        }
        UserStats s = new UserStats(serverid, id);
        s.write_state=PENDING_WRITE;
        userdata.getData().add(s);
        return s;
    }

    public List<RegexKey> getAllValidRegexKeys() {
        List<RegexKey> keys = new ArrayList<>(regex_keys.getData());
        for (int i =0; i < keys.size(); i++) {
            if (keys.get(i).write_state==PENDING_DELETE) {
                keys.remove(i);
                i--;
            }
        }
        return keys;
    }

    public TextChannel getLogChannel(JDA jda) {
        for (ServerSettings s: settings.getData()) {
            if (s.getSetting()==ServerParameter.SERVER_LOG_CHANNEL) {
                return jda.getTextChannelById(s.getValue());
            }
        }
        return null;
    }

    public List<WatchedRole> getAllValidWatchedRoles() {
        List<WatchedRole> a = new ArrayList<>(watched_roles.getData());
        for (int i =0; i < a.size(); i++) {
            if (a.get(i).write_state==PENDING_DELETE) {
                a.remove(i);
                i--;
            }
        }
        return a;
    }

    public boolean roleIsWatched(Role r) {
        for (WatchedRole wr: getAllValidWatchedRoles()) {
            if (wr.getRole().getIdLong()==r.getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public List<WatchedUser> getAllValidWatchedUsers() {
        List<WatchedUser> a = new ArrayList<>(watched_users.getData());
        for (int i =0; i < a.size(); i++) {
            if (a.get(i).write_state==PENDING_DELETE) {
                a.remove(i);
                i--;
            }
        }
        return a;
    }

    public boolean userIsWatched(Member m) {
        for (WatchedUser wu: getAllValidWatchedUsers()) {
            if (wu.getUser().getUser().getIdLong()==m.getUser().getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public Role getModeratorRole(JDA jda) {
        for (ServerSettings s: settings.getData()) {
            if (s.getSetting()==ServerParameter.SERVER_MODERATOR_ROLE) {
                return jda.getRoleById(s.getValue());
            }
        }
        return null;
    }
}
