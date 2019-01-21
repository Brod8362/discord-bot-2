package pw.byakuren.discord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {

    private String dbname;
    private String dbuser;
    private String dbpassword;


    private Connection connection = null;
    private Statement statement = null;

    private PreparedStatement updateDatapoint;
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

    private PreparedStatement addWatchedUser;
    private PreparedStatement removeWatchedUser;
    private PreparedStatement checkWatchedUser;
    private PreparedStatement getWatchedUsers;

    private PreparedStatement addWatchedRole;
    private PreparedStatement removeWatchedRole;
    private PreparedStatement checkWatchedRole;
    private PreparedStatement getWatchedRoles;

    private PreparedStatement modifyServerSetting;

    private PreparedStatement updateLastMessage;





    public SQLConnection(String db, String user, String password) {
        dbname=db;
        dbuser=user;
        dbpassword=password;
    }

    public void initialize() throws SQLException {
        // intialize DB connection
        connection = DriverManager.getConnection(String.format("jdbc:mysql://localhost/%s?user=%s&password=%s", dbname, dbuser, dbpassword));
        statement = connection.createStatement();
        System.out.println("Connected to SQL server.");
        updateDatapoint = connection.prepareStatement("UPDATE user_chat_data SET count=count+1 WHERE server=? AND user=? AND datapoint=?");
        createDatapoint = connection.prepareStatement("INSERT INTO user_chat_data VALUES (?, ?, ?, 1)");

        updateLastMessage = connection.prepareStatement("REPLACE INTO last_messages (server, user, content, date_sent) VALUES (?, ?, ?, ?)");

        addRegexKey = connection.prepareStatement("INSERT INTO server_regex_keys VALUES (?, ?)");
        removeRegexKey = connection.prepareStatement("DELETE FROM server_regex_keys WHERE server=? AND regex_key=?");
        getRegexKeys = connection.prepareStatement("SELECT * FROM server_regex_keys WHERE server=?");

        addExcludedChannel = connection.prepareStatement("INSERT INTO excluded_channels VALUES (?, ?)");
        removeExcludedChannel = connection.prepareStatement("DELETE FROM excluded_channels WHERE server=? AND channel=?");
        getExcludedChannels = connection.prepareStatement("SELECT * FROM excluded_channels WHERE server=?");
        checkExcludedChannel = connection.prepareStatement("SELECT 1 FROM excluded_channels WHERE server=? AND channel=?");

        addSubscription = connection.prepareStatement("INSERT INTO moderator_subscriptions VALUES (?, ?, ?, ?)");
        removeSubscription = connection.prepareStatement("DELETE FROM moderator_subscriptions WHERE server=? AND moderator=? AND user=?");
        getSubscriptions = connection.prepareStatement("SELECT * FROM moderator_subscriptions WHERE server=? AND moderator=?");
        checkSubscribed = connection.prepareStatement("SELECT 1 FROM moderator_subscriptions WHERE server=? AND moderator=? AND user=?");

        addWatchedUser = connection.prepareStatement("INSERT INTO watched_users VALUES (?, ?, ?)");
        removeWatchedUser = connection.prepareStatement("DELETE FROM watched_users WHERE server=? AND user=?");
        getWatchedUsers = connection.prepareStatement("SELECT * FROM watched_users WHERE server=?");
        checkWatchedUser = connection.prepareStatement("SELECT 1 FROM watched_users WHERE server=? AND user=?");

        addWatchedRole = connection.prepareStatement("INSERT INTO watched_roles VALUES (?, ?, ?)");
        removeWatchedRole = connection.prepareStatement("DELETE FROM watched_roles WHERE server=? AND role=?");
        getWatchedRoles = connection.prepareStatement("SELECT * FROM watched_roles WHERE server=?");
        checkWatchedRole = connection.prepareStatement("SELECT 1 FROM watched_roles WHERE server=? AND role=?");
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
            ResultSet results = statement.executeQuery("SHOW TABLES;");
            ArrayList<String> list = new ArrayList<>();
            while (results.next()) {
                list.add(results.getString("Tables_in_"+dbname));
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
        String[] tables = new String[]{
                "CREATE TABLE last_messages (server BIGINT NOT NULL, user BIGINT NOT NULL, content TEXT NOT NULL, date_sent DATETIME NOT NULL, INDEX(server), PRIMARY KEY(server, user))",
                "CREATE TABLE moderator_subscriptions (server BIGINT NOT NULL, moderator BIGINT NOT NULL, user BIGINT NOT NULL, date_added DATETIME NOT NULL, INDEX(server))",
                "CREATE TABLE excluded_channels (server BIGINT NOT NULL, channel BIGINT NOT NULL, INDEX(server))",
                "CREATE TABLE watched_users (server BIGINT NOT NULL, user BIGINT NOT NULL, date_added DATETIME NOT NULL, INDEX(server))",
                "CREATE TABLE watched_roles (server BIGINT NOT NULL, role BIGINT NOT NULL, date_added DATETIME NOT NULL, INDEX(server))",
                "CREATE TABLE server_regex_keys (server BIGINT NOT NULL, regex_key TEXT NOT NULL, INDEX(server))",
                "CREATE TABLE user_chat_data (server BIGINT NOT NULL, user BIGINT NOT NULL, datapoint VARCHAR(50) NOT NULL, count INT NOT NULL, INDEX(server), PRIMARY KEY(server, user, datapoint))",
                "CREATE TABLE server_settings (server BIGINT NOT NULL, setting VARCHAR(50) NOT NULL, value BIGINT NOT NULL, INDEX(server))"
        };
        for (String s: tables) {
            try {
                statement.execute(s);
            } catch (SQLException e) {
                throw new RuntimeException("Table already exists or invalid syntax.");
            }
        }
    }

    public void dropTable(String s) throws SQLException {
        statement.execute("DROP TABLE "+s);
    }

    public void executeUpdateLastMessage(long server, long user, String message) throws SQLException {
        updateLastMessage.setLong(1, server);
        updateLastMessage.setLong(2, user);
        updateLastMessage.setString(3, message);
        updateLastMessage.setDate(4, new Date(System.currentTimeMillis()));
        updateLastMessage.executeUpdate();
        updateLastMessage.clearParameters();
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