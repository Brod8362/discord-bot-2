package pw.byakuren.discord;

import net.dv8tion.jda.api.entities.*;
import pw.byakuren.discord.objects.ServerSettings;

import java.sql.SQLException;
import java.util.HashMap;
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

    public void addModeratorSubscription(Member user, Member moderator) {

    }

    public void removeModeratorSubscription(Member user, Member moderator) {

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
        
    }
    
    public void removeExcludedChannel(TextChannel channel) {
        
    }
    
    public TextChannel[] getExcludedChannels(Guild server) {
        return getExcludedChannels(server.getIdLong());
    }
    
    public TextChannel[] getExcludedChannels(long serverid) {
        return new TextChannel[0];
    }

    /* Methods for handling server regex keys */
    
    public void addRegexKey(Guild server, String key) {
        addRegexKey(server.getIdLong(), key);
    }

    public void addRegexKey(long serverid, String key) {

    }
    
    public void removeRegexKey(Guild server, String key) {
        removeRegexKey(server.getIdLong(), key);
    }

    public void removeRegexKey(long serverid, String key) {

    }

    public String[] getRegexKeys(Guild server) {
        return getRegexKeys(server.getIdLong());
    }

    public String[] getRegexKeys(long serverid) {
        return new String[0];
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
