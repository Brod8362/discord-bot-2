package pw.byakuren.discord;

import pw.byakuren.discord.objects.cache.datatypes.LastMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {

    private Connection connection = null;
    private Statement statement = null;

    private PreparedStatement incrementDatapoint;
    private PreparedStatement createDatapoint;

    private PreparedStatement addSubscription;
    private PreparedStatement removeSubscription;
    private PreparedStatement getSubscriptions;
    private PreparedStatement checkSubscribed;

    private PreparedStatement addRegexKey;
    private PreparedStatement removeRegexKey;
    private PreparedStatement getRegexKeys;

    private PreparedStatement addExcludedChannel;
    private PreparedStatement removeExcludedChannel;
    private PreparedStatement getExcludedChannels;
    private PreparedStatement checkExcludedChannel;
    private PreparedStatement checkExcludedChannelSingle;

    private PreparedStatement addWatchedUser;
    private PreparedStatement removeWatchedUser;
    private PreparedStatement checkWatchedUser;
    private PreparedStatement getwatchedUser;
    private PreparedStatement getWatchedUsers;

    private PreparedStatement addWatchedRole;
    private PreparedStatement removeWatchedRole;
    private PreparedStatement checkWatchedRole;
    private PreparedStatement getWatchedRoles;

    private PreparedStatement modifyServerSetting;

    private PreparedStatement updateLastMessage;
    private PreparedStatement getLastMessage;
    private PreparedStatement getLastMessages;

    public void initialize() throws SQLException {
        String dir = System.getProperty("user.dir");
        // intialize DB connection
        connection = DriverManager.getConnection("jdbc:sqlite:"+dir+"/database.db");
        statement = connection.createStatement();
        System.out.println("Connected to SQL");
        if (!verifyTables()) {
            System.out.println("Tables need to be created.");
            createTables();
        }
        incrementDatapoint = connection.prepareStatement("UPDATE user_chat_data SET count=count+1 WHERE server=? AND user=? AND datapoint=?");
        createDatapoint = connection.prepareStatement("INSERT INTO user_chat_data VALUES (?, ?, ?, 1)");

        updateLastMessage = connection.prepareStatement("REPLACE INTO last_messages (server, user, content, id, date_sent) VALUES (?, ?, ?, ?, ?)");
        getLastMessage = connection.prepareStatement("SELECT 1 FROM last_messages WHERE server=? AND user=?");
        getLastMessages = connection.prepareStatement("SELECT * FROM last_messages WHERE server=?");

        addRegexKey = connection.prepareStatement("INSERT INTO server_regex_keys VALUES (?, ?)");
        removeRegexKey = connection.prepareStatement("DELETE FROM server_regex_keys WHERE server=? AND regex_key=?");
        getRegexKeys = connection.prepareStatement("SELECT * FROM server_regex_keys WHERE server=?");

        addExcludedChannel = connection.prepareStatement("INSERT INTO excluded_channels VALUES (?, ?)");
        removeExcludedChannel = connection.prepareStatement("DELETE FROM excluded_channels WHERE server=? AND channel=?");
        getExcludedChannels = connection.prepareStatement("SELECT * FROM excluded_channels WHERE server=?");
        checkExcludedChannel = connection.prepareStatement("SELECT 1 FROM excluded_channels WHERE server=? AND channel=?");
        checkExcludedChannelSingle = connection.prepareStatement("SELECT 1 FROM excluded_channels WHERE channel=?");

        addSubscription = connection.prepareStatement("INSERT INTO moderator_subscriptions VALUES (?, ?, ?, ?)");
        removeSubscription = connection.prepareStatement("DELETE FROM moderator_subscriptions WHERE server=? AND moderator=? AND user=?");
        getSubscriptions = connection.prepareStatement("SELECT * FROM moderator_subscriptions WHERE server=? AND moderator=?");
        checkSubscribed = connection.prepareStatement("SELECT 1 FROM moderator_subscriptions WHERE server=? AND moderator=? AND user=?");

        addWatchedUser = connection.prepareStatement("INSERT INTO watched_users VALUES (?, ?, ?)");
        removeWatchedUser = connection.prepareStatement("DELETE FROM watched_users WHERE server=? AND user=?");
        getWatchedUsers = connection.prepareStatement("SELECT * FROM watched_users WHERE server=?");
        getwatchedUser = connection.prepareStatement("SELECT 1 FROM watched_users WHERE server=? AND user=?");
        checkWatchedUser = connection.prepareStatement("SELECT 1 FROM watched_users WHERE server=? AND user=?");

        addWatchedRole = connection.prepareStatement("INSERT INTO watched_roles VALUES (?, ?, ?)");
        removeWatchedRole = connection.prepareStatement("DELETE FROM watched_roles WHERE server=? AND role=?");
        getWatchedRoles = connection.prepareStatement("SELECT * FROM watched_roles WHERE server=?");
        checkWatchedRole = connection.prepareStatement("SELECT 1 FROM watched_roles WHERE server=? AND role=?");
    }

    private boolean verifyTables() {
        return (getTables().size() == 8);
    }


    public ResultSet runQuery(String query)  {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getTables() {
        try {
            ResultSet results = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            ArrayList<String> list = new ArrayList<>();
            while (results.next()) {
                list.add(results.getString("name"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTables() throws RuntimeException {
        String[] queries = new String[]{
                "CREATE TABLE last_messages (server INTEGER NOT NULL, user INTEGER NOT NULL, content TEXT NOT NULL, id INTEGER NOT NULL, date_sent TIMESTAMP NOT NULL, PRIMARY KEY(server, user))",
                "CREATE INDEX msg_idx ON last_messages(server)",
                "CREATE TABLE moderator_subscriptions (server INTEGER NOT NULL, moderator INTEGER NOT NULL, user INTEGER NOT NULL, date_added TIMESTAMP NOT NULL)",
                "CREATE INDEX mod_idx ON moderator_subscriptions(server)",
                "CREATE TABLE excluded_channels (server INTEGER NOT NULL, channel INTEGER NOT NULL)",
                "CREATE INDEX exclude_idx ON excluded_channels(server)",
                "CREATE TABLE watched_users (server INTEGER NOT NULL, user INTEGER NOT NULL, date_added TIMESTAMP NOT NULL, PRIMARY KEY(server,user))",
                "CREATE INDEX watched_users_idx ON watched_users(server)",
                "CREATE TABLE watched_roles (server INTEGER NOT NULL, role INTEGER NOT NULL, date_added TIMESTAMP NOT NULL, PRIMARY KEY(server,role))",
                "CREATE INDEX watched_roles_idx ON watched_roles(server)",
                "CREATE TABLE server_regex_keys (server INTEGER NOT NULL, regex_key TEXT NOT NULL)",
                "CREATE INDEX server_regex_idx ON server_regex_keys(server)",
                "CREATE TABLE user_chat_data (server INTEGER NOT NULL, user INTEGER NOT NULL, datapoint VARCHAR(50) NOT NULL, count INT NOT NULL, PRIMARY KEY(server, user, datapoint))",
                "CREATE INDEX chat_idx ON user_chat_data(server)",
                "CREATE TABLE server_settings (server INTEGER NOT NULL, setting VARCHAR(50) NOT NULL, value INTEGER NOT NULL)",
                "CREATE INDEX settings_idx ON server_settings(server)"
        };
        for (String s: queries) {
            try {
                statement.execute(s);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Table already exists or invalid syntax.");
            }
        }
    }

    public void dropTable(String s) throws SQLException {
        statement.execute("DROP TABLE "+s);
    }

    public void executeUpdateLastMessage(long server, long user, String message, long messageid) throws SQLException {
        updateLastMessage.setLong(1, server);
        updateLastMessage.setLong(2, user);
        updateLastMessage.setString(3, message);
        updateLastMessage.setLong(4, messageid);
        updateLastMessage.setDate(5, new Date(System.currentTimeMillis()));
        updateLastMessage.executeUpdate();
        updateLastMessage.clearParameters();
    }

    public long executeGetLastMessageId(long server, long user) throws SQLException {
        getLastMessage.setLong(1, server);
        getLastMessage.setLong(2, user);
        ResultSet set = getLastMessage.executeQuery();
        getLastMessage.clearParameters();
        return set.getLong("id");
    }

    public LastMessage executeGetLastMessage(long server, long user) throws SQLException {
        getLastMessage.setLong(1, server);
        getLastMessage.setLong(2, user);
        ResultSet set = getLastMessage.executeQuery();
        getLastMessage.clearParameters();
        return setToLastMessage(set);
    }

    public List<LastMessage> executeGetLastMessages(long server) throws SQLException {
        getLastMessages.setLong(1, server);
        ResultSet set = getLastMessages.executeQuery();
        List<LastMessage> l = new ArrayList<>();
        while (set.next()) {
            l.add(setToLastMessage(set));
        }
        return l;
    }

    private LastMessage setToLastMessage(ResultSet set) throws SQLException {
        long serverid = set.getLong(1);
        long userid =set.getLong(2);
        long messageid = set.getLong(3);
        String content = set.getString(4);
        Date date = set.getDate(5);
        return new LastMessage(serverid, userid, messageid, content, date);
    }

    public void executeAddRegexKey(long server, String regex) throws SQLException {
        addRegexKey.setLong(1, server);
        addRegexKey.setString(2, regex);
        addRegexKey.executeUpdate();
        addRegexKey.clearParameters();
    }

    public void executeRemoveRegexKey(long server, String regex) throws SQLException {
        removeRegexKey.setLong(1, server);
        removeRegexKey.setString(2, regex);
        removeRegexKey.executeUpdate();
        removeRegexKey.clearParameters();
    }

    public List<String> executeGetRegexKeys(long server) throws SQLException {
        getRegexKeys.setLong(1, server);
        ResultSet set =  getRegexKeys.executeQuery();
        getRegexKeys.clearParameters();
        ArrayList<String> list = new ArrayList<>();
        while (set.next()) {
            list.add(set.getString("regex_key"));
        }
        return list;
    }

    public void executeAddExcludedChannel(long server, long channel) throws SQLException {
        addExcludedChannel.setLong(1, server);
        addExcludedChannel.setLong(2, channel);
        addExcludedChannel.executeUpdate();
        addExcludedChannel.clearParameters();
    }

    public void executeRemoveExcludedChannel(long server, long channel) throws SQLException {
        removeExcludedChannel.setLong(1, server);
        removeExcludedChannel.setLong(2, channel);
        removeExcludedChannel.executeUpdate();
        removeExcludedChannel.clearParameters();
    }

    public boolean executeCheckExcludedChannel(long server, long channel) throws SQLException {
        checkExcludedChannel.setLong(1, server);
        checkExcludedChannel.setLong(2, channel);
        return checkExcludedChannel.executeQuery().next();
    }

    public boolean executeCheckExcludedChannel(long channel) throws SQLException {
        checkExcludedChannelSingle.setLong(1, channel);
        return checkExcludedChannel.executeQuery().next();
    }

    public List<Long> executeGetExcludedChannels(long server) throws SQLException {
        getExcludedChannels.setLong(1, server);
        ResultSet set = getExcludedChannels.executeQuery();
        getExcludedChannels.clearParameters();
        ArrayList<Long> list = new ArrayList<>();
        while (set.next()) {
            list.add(set.getLong("channel"));
        }
        return list;
    }

    public void executeAddSubscription(long server, long moderator, long user) throws SQLException {
        addSubscription.setLong(1, server);
        addSubscription.setLong(2, moderator);
        addSubscription.setLong(3, user);
        addSubscription.setDate(4, new Date(System.currentTimeMillis()));
        addSubscription.executeUpdate();
        addSubscription.clearParameters();
    }

    public void executeRemoveSubscription(long server, long moderator, long user) throws SQLException {
        removeSubscription.setLong(1, server);
        removeSubscription.setLong(2, moderator);
        removeSubscription.setLong(3, user);
        removeSubscription.executeUpdate();
        removeSubscription.clearParameters();
    }

    public List<Long> executeGetSubscriptions(long server, long moderator) throws SQLException {
        getSubscriptions.setLong(1, server);
        getSubscriptions.setLong(2, moderator);
        ResultSet set = getSubscriptions.executeQuery();
        getSubscriptions.clearParameters();
        ArrayList<Long> list = new ArrayList<>();
        while (set.next()) {
            list.add(set.getLong("user"));
        }
        return list;
    }

    public boolean executeCheckSubscribed(long server, long moderator, long user) throws SQLException {
        checkSubscribed.setLong(1, server);
        checkSubscribed.setLong(2, moderator);
        checkSubscribed.setLong(3, user);
        return checkSubscribed.executeQuery().next();
    }

    public void executeAddWatchedUser(long server, long user) throws SQLException {
        addWatchedUser.setLong(1, server);
        addWatchedUser.setLong(2, user);
        addWatchedUser.setDate(3, new Date(System.currentTimeMillis()));
        addWatchedUser.executeUpdate();
        addWatchedUser.clearParameters();
    }

    public void executeRemoveWatchedUser(long server, long user) throws SQLException {
        removeWatchedUser.setLong(1, server);
        removeWatchedUser.setLong(2, user);
        removeWatchedUser.executeUpdate();
        removeWatchedUser.clearParameters();
    }

    public List<Long> getWatchedUsers(long server) throws SQLException {
        getWatchedUsers.setLong(1, server);
        ResultSet set = getWatchedUsers.executeQuery();
        getWatchedUsers.clearParameters();
        ArrayList<Long> list = new ArrayList<>();
        while (set.next()) {
            list.add(set.getLong("user"));
        }
        return list;
    }

    public boolean checkWatchedUser(long server, long user) throws SQLException {
        checkWatchedUser.setLong(1, server);
        checkWatchedUser.setLong(2, user);
        return checkWatchedUser.executeQuery().next();
    }

    public void executeAddWatchedRole(long server, long role) throws SQLException {
        addWatchedRole.setLong(1, server);
        addWatchedRole.setLong(2, role);
        addWatchedRole.setDate(3, new Date(System.currentTimeMillis()));
        addWatchedRole.executeUpdate();
        addWatchedRole.clearParameters();
    }

    public void executeRemoveWatchedRole(long server, long role) throws SQLException {
        removeWatchedRole.setLong(1, server);
        removeWatchedRole.setLong(2, role);
        removeWatchedRole.executeUpdate();
        removeWatchedRole.clearParameters();
    }

    public List<Long> getWatchedRoles(long server) throws SQLException {
        getWatchedRoles.setLong(1, server);
        ResultSet set = getWatchedRoles.executeQuery();
        getWatchedRoles.clearParameters();
        ArrayList<Long> list = new ArrayList<>();
        while (set.next()) {
            list.add(set.getLong("role"));
        }
        return list;
    }

    public boolean checkWatchedRole(long server, long role) throws SQLException {
        checkWatchedRole.setLong(1, server);
        checkWatchedRole.setLong(2, role);
        return checkWatchedRole.executeQuery().next();
    }

}