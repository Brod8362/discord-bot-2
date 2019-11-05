package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.JDA;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.*;
import pw.byakuren.discord.objects.cache.factories.*;

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

    ServerCache(long id, DatabaseManager dbmg, JDA jda) {
        this.id = id;
        userdata = new CacheObject<>(id, dbmg, new UserStatsFactory(id, dbmg));
        settings = new CacheObject<>(id, dbmg, new ServerSettingsFactory(id, dbmg));
        regex_keys = new CacheObject<>(id, dbmg, new RegexKeyFactory(id, dbmg));
        watched_users = new CacheObject<>(id, dbmg, new WatchedUserFactory(id, dbmg, jda));
        watched_roles = new CacheObject<>(id, dbmg, new WatchedRoleFactory(id, dbmg, jda));
        excluded_channels = new CacheObject<>(id, dbmg, new ExcludedChannelFactory(id, dbmg));
        last_messages = new CacheObject<>(id, dbmg, new LastMessageFactory(id, dbmg));
        moderator_subscriptions = new CacheObject<>(id, dbmg, new SubscriptionFactory(id, dbmg));
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
