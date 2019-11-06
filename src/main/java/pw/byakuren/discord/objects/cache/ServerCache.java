package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import pw.byakuren.discord.DatabaseManager;
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
    private CacheObject<Subscription> moderator_subscriptions;

    ServerWriteThread write_thread;
    CacheObject[] objects;

    ServerCache(long id, DatabaseManager dbmg, JDA jda) {
        this.id = id;
        userdata = new CacheObject<>(id, dbmg, new UserStatsFactory(id, dbmg));
//        settings = new CacheObject<>(id, dbmg, new ServerSettingsFactory(id, dbmg));
        //todo implement settings cache
        regex_keys = new CacheObject<>(id, dbmg, new RegexKeyFactory(id, dbmg));
        watched_users = new CacheObject<>(id, dbmg, new WatchedUserFactory(id, dbmg, jda));
        watched_roles = new CacheObject<>(id, dbmg, new WatchedRoleFactory(id, dbmg, jda));
        excluded_channels = new CacheObject<>(id, dbmg, new ExcludedChannelFactory(id, dbmg));
        last_messages = new CacheObject<>(id, dbmg, new LastMessageFactory(id, dbmg));
        moderator_subscriptions = new CacheObject<>(id, dbmg, new SubscriptionFactory(id, dbmg));
        objects = new CacheObject[]{userdata, /*settings,*/ regex_keys, watched_roles, watched_users, excluded_channels, last_messages, moderator_subscriptions};
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

    public CacheObject<Subscription> getModeratorSubscriptions() {
        return moderator_subscriptions;
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
}
