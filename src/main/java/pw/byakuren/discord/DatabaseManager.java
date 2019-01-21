package pw.byakuren.discord;

import net.dv8tion.jda.api.entities.*;
import pw.byakuren.discord.objects.ServerSettings;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private SQLConnection sql;

    public DatabaseManager(SQLConnection sql) throws SQLException {
            this.sql = sql;
            sql.initialize();
    }

    public SQLConnection getSQL() {
        return sql;
    }

    /* For handling moderator susbcriptions to actions on specific users. */

    public void addModeratorSubscription(Member moderator, Member user) {
        addModeratorSubscription(user.getGuild().getIdLong(), moderator.getUser().getIdLong(), user.getUser().getIdLong());
    }

    public void addModeratorSubscription(long server, long moderator, long user) {
        try {
            sql.executeAddSubscription(server, moderator, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeModeratorSubscription(Member moderator, Member user) {
        removeModeratorSubscription(user.getGuild().getIdLong(), moderator.getUser().getIdLong(), user.getUser().getIdLong());
    }

    public void removeModeratorSubscription(long server, long moderator, long user) {
        try {
            sql.executeRemoveSubscription(server, moderator, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkModeratorSubscription(Member moderator, Member user) {
        return checkModeratorSubscription(moderator.getGuild().getIdLong(), moderator.getUser().getIdLong(), user.getUser().getIdLong());
    }

    public boolean checkModeratorSubscription(long server, long moderator, long user) {
        try {
            return sql.executeCheckSubscribed(server, moderator, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Long> getModeratorSubscriptions(Member moderator) {
        return getModeratorSubscriptions(moderator.getGuild().getIdLong(), moderator.getUser().getIdLong());
    }

    public List<Long> getModeratorSubscriptions(long server, long moderator) {
        try {
            return sql.executeGetSubscriptions(server, moderator);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /* Methods for changing server settings and features. */

    public void addServerSetting(Guild server, String setting, long value) {

    }

    public void removeServerSetting(Guild server, String setting) {

    }

    public void editServerSetting(Guild server, String setting, long newvalue) {

    }

    public ServerSettings getServerSettings(Guild server) {
        return getServerSettings(server.getIdLong());
    }

    public ServerSettings getServerSettings(long serverid) {
        return new ServerSettings();
    }

    /* Methods for modifying user chat data/statistics.
    *   Valid datapoints: reactions_tx, reactions_rx, messages_sent, messages_deleted, attachments_sent
    * */

    public void addUserChatData(Member user, String datapoint) {

    }

    public void editUserChatData(Member user, String datapoint, long difference) {

    }

    public void removeUserChatData(Member user, String datapoint) {

    }

    public int getUserChatDatapoint(Member user, String datapoint) {
        return -1;
    }

    public Map<String, Integer> getUserChatData(Member user) {
        return new HashMap<String, Integer>();
    }

    /* Methods for modyfing watched roles. */

    public void addWatchedRole(Role role) {

    }

    public void removeWatchedRole(Role role) {

    }
    
    public Role[] getWatchedRoles(Guild server) {
        return getWatchedRoles(server.getIdLong());
    }
    
    public Role[] getWatchedRoles(long serverid) {
        return new Role[0];
    }

    /* Methods for handling excluded channels */ 
    
    public void addExcludedChannel(TextChannel channel) {
        try {
            sql.executeAddExcludedChannel(channel.getGuild().getIdLong(), channel.getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeExcludedChannel(TextChannel channel) {
        try {
            sql.executeRemoveExcludedChannel(channel.getGuild().getIdLong(), channel.getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Long> getExcludedChannels(Guild server) {
        return getExcludedChannels(server.getIdLong());
    }
    
    public List<Long> getExcludedChannels(long serverid) {
        try {
            return sql.executeGetExcludedChannels(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkExcludedChannel(TextChannel channel) {
        return checkExcludedChannel(channel.getGuild().getIdLong(), channel.getIdLong());
    }

    public boolean checkExcludedChannel(long serverid, long channel) {
        try {
            return sql.executeCheckExcludedChannel(serverid, channel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Methods for handling server regex keys */
    
    public void addRegexKey(Guild server, String key) {
        addRegexKey(server.getIdLong(), key);
    }

    public void addRegexKey(long serverid, String key) {
        try {
            sql.executeAddRegexKey(serverid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void removeRegexKey(Guild server, String key) {
        removeRegexKey(server.getIdLong(), key);
    }

    public void removeRegexKey(long serverid, String key) {
        try {
            sql.executeRemoveRegexKey(serverid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRegexKeys(Guild server) {
        return getRegexKeys(server.getIdLong());
    }

    public List<String> getRegexKeys(long serverid) {
        try {
            return sql.executeGetRegexKeys(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /* Miscellaneous methods. */

    public void createNeededTables() {
        try {
            sql.createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLastMessage(Message message) {
        try {
            sql.executeUpdateLastMessage(message.getGuild().getIdLong(), message.getMember().getUser().getIdLong(), message.getContentDisplay());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
