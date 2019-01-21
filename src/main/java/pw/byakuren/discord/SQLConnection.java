package pw.byakuren.discord;

import java.sql.*;
import java.util.ArrayList;

public class SQLConnection {

    private String dbname;
    private String dbuser;
    private String dbpassword;


    private Connection connection = null;
    private Statement statement = null;

    private PreparedStatement updateDatapoint;
    private PreparedStatement createDatapoint;

    private PreparedStatement addSubscription;
    private PreparedStatement deleteSubscription;

    private PreparedStatement addRegexKey;
    private PreparedStatement deleteRegexKey;

    private PreparedStatement addExcludedChannel;
    private PreparedStatement removeExcludedChannel;

    private PreparedStatement addWatchedUser;
    private PreparedStatement removeWatchedUser;

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
        updateLastMessage.executeQuery();
        updateLastMessage.clearParameters();
    }
}