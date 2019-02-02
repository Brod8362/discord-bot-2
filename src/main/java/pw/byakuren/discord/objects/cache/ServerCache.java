package pw.byakuren.discord.objects.cache;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.ServerSettings;
import pw.byakuren.discord.objects.Subscription;
import pw.byakuren.discord.objects.UserStats;

import java.util.List;
import java.util.Map;

public class ServerCache {

    private long id;

    private DatabaseManager dbmg;

    private Map<Long, UserStats> userdata;
    private ServerSettings settings;
    private List<String> regex_keys;
    private List<Long> watched_users;
    private List<Long> watched_roles;
    private List<Long> excluded_channels;
    private Map<Long, Message> last_messages;
    private List<Subscription> moderator_subscriptions;

    public ServerCache(long id, DatabaseManager dbmg) {
        this.id = id;
        this.dbmg = dbmg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<Long, UserStats> getUserdata() {
        return userdata;
    }

    void setUserData(Map<Long, UserStats> userdata) {
        this.userdata = userdata;
    }

    public ServerSettings getSettings() {
        return settings;
    }

    void setSettings(ServerSettings settings) {
        this.settings = settings;
    }

    public List<String> getRegexKeys() {
        return regex_keys;
    }

    void setRegexKeys(List<String> regex_keys) {
        this.regex_keys = regex_keys;
    }

    public List<Long> getWatchedUsers() {
        return watched_users;
    }

    void setWatchedUsers(List<Long> watched_users) {
        this.watched_users = watched_users;
    }

    public List<Long> getWatchedRoles() {
        return watched_roles;
    }

    void setWatchedRoles(List<Long> watched_roles) {
        this.watched_roles = watched_roles;
    }

    public List<Long> getExcludedChannels() {
        return excluded_channels;
    }

    void setExcludedChannels(List<Long> excluded_channels) {
        this.excluded_channels = excluded_channels;
    }

    public Map<Long, Message> getLastMessages() {
        return last_messages;
    }

    void setLastMessages(Map<Long, Message> last_messages) {
        this.last_messages = last_messages;
    }

    public List<Subscription> getModeratorSubscriptions() {
        return moderator_subscriptions;
    }

    void setModeratorSubscriptions(List<Subscription> moderator_subscriptions) {
        this.moderator_subscriptions = moderator_subscriptions;
    }
}
