package pw.byakuren.discord.objects.cache;

import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.*;

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

    ServerCache(long id, DatabaseManager dbmg) {
        this.id = id;
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
}
