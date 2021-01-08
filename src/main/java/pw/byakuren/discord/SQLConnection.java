package pw.byakuren.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.objects.Triple;
import pw.byakuren.discord.objects.cache.datatypes.*;
import pw.byakuren.discord.util.MessageActionParser;
import pw.byakuren.discord.util.MessageFilterParser;
import pw.byakuren.discord.util.MiscUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {

    private Connection connection = null;
    private Statement statement = null;

    private PreparedStatement incrementDatapoint;
    private PreparedStatement createDatapoint;
    private PreparedStatement editDatapoint;
    private PreparedStatement getDatapoint;
    private PreparedStatement getAllDatapoints;
    private PreparedStatement getAllDatapointsServer;
    private PreparedStatement checkDatapointExists;

    private PreparedStatement addRegexKey;
    private PreparedStatement removeRegexKey;
    private PreparedStatement getRegexKeys;
    private PreparedStatement checkRegexKey;

    private PreparedStatement addExcludedChannel;
    private PreparedStatement removeExcludedChannel;
    private PreparedStatement getExcludedChannels;
    private PreparedStatement checkExcludedChannel;
    private PreparedStatement checkExcludedChannelSingle;

    private PreparedStatement addWatchedUser;
    private PreparedStatement removeWatchedUser;
    private PreparedStatement checkWatchedUser;
    private PreparedStatement getWatchedUsers;

    private PreparedStatement addWatchedRole;
    private PreparedStatement removeWatchedRole;
    private PreparedStatement checkWatchedRole;
    private PreparedStatement getWatchedRoles;

    private PreparedStatement modifyServerSetting;
    private PreparedStatement checkServerSetting;
    private PreparedStatement addServerSetting;
    private PreparedStatement getServerSetting;
    private PreparedStatement getAllServerSettings;

    private PreparedStatement updateLastMessage;
    private PreparedStatement getLastMessage;
    private PreparedStatement getLastMessages;

    private PreparedStatement getVoiceBans;
    private PreparedStatement addVoiceBan;
    private PreparedStatement getMemberVoiceBans;
    private PreparedStatement updateVoiceBan;

    private PreparedStatement getFilterAction;
    private PreparedStatement getAllFilterActions;
    private PreparedStatement addFilterAction;
    private PreparedStatement removeFilterAction;

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
        editDatapoint = connection.prepareStatement("UPDATE user_chat_data SET count=? WHERE server=? AND user=? AND datapoint=?");
        getDatapoint = connection.prepareStatement("SELECT count FROM user_chat_data WHERE server=? AND user=? AND datapoint=?");
        getAllDatapoints = connection.prepareStatement("SELECT datapoint,count FROM user_chat_data WHERE server=? AND user=?");
        getAllDatapointsServer = connection.prepareStatement("SELECT * FROM user_chat_data WHERE server=?");
        checkDatapointExists = connection.prepareStatement("SELECT 1 FROM user_chat_data WHERE server=? AND user=? AND datapoint=?");

        updateLastMessage = connection.prepareStatement("REPLACE INTO last_messages (server, user, content, id, date_sent) VALUES (?, ?, ?, ?, ?)");
        getLastMessage = connection.prepareStatement("SELECT 1 FROM last_messages WHERE server=? AND user=?");
        getLastMessages = connection.prepareStatement("SELECT * FROM last_messages WHERE server=?");

        addRegexKey = connection.prepareStatement("INSERT INTO server_regex_keys VALUES (?, ?)");
        removeRegexKey = connection.prepareStatement("DELETE FROM server_regex_keys WHERE server=? AND regex_key=?");
        getRegexKeys = connection.prepareStatement("SELECT * FROM server_regex_keys WHERE server=?");
        checkRegexKey = connection.prepareStatement("SELECT 1 FROM server_regex_keys WHERE server=? AND regex_key=?");

        addExcludedChannel = connection.prepareStatement("INSERT INTO excluded_channels VALUES (?, ?)");
        removeExcludedChannel = connection.prepareStatement("DELETE FROM excluded_channels WHERE server=? AND channel=?");
        getExcludedChannels = connection.prepareStatement("SELECT * FROM excluded_channels WHERE server=?");
        checkExcludedChannel = connection.prepareStatement("SELECT 1 FROM excluded_channels WHERE server=? AND channel=?");
        checkExcludedChannelSingle = connection.prepareStatement("SELECT 1 FROM excluded_channels WHERE channel=?");

        addWatchedUser = connection.prepareStatement("INSERT INTO watched_users VALUES (?, ?, ?)");
        removeWatchedUser = connection.prepareStatement("DELETE FROM watched_users WHERE server=? AND user=?");
        getWatchedUsers = connection.prepareStatement("SELECT * FROM watched_users WHERE server=?");
        checkWatchedUser = connection.prepareStatement("SELECT 1 FROM watched_users WHERE server=? AND user=?");

        addWatchedRole = connection.prepareStatement("INSERT INTO watched_roles VALUES (?, ?, ?)");
        removeWatchedRole = connection.prepareStatement("DELETE FROM watched_roles WHERE server=? AND role=?");
        getWatchedRoles = connection.prepareStatement("SELECT * FROM watched_roles WHERE server=?");
        checkWatchedRole = connection.prepareStatement("SELECT 1 FROM watched_roles WHERE server=? AND role=?");

        modifyServerSetting = connection.prepareStatement("UPDATE server_settings SET value=? WHERE server=? AND setting=?");
        addServerSetting = connection.prepareStatement("INSERT INTO server_settings VALUES (?, ?, ?)");
        checkServerSetting = connection.prepareStatement("SELECT 1 FROM server_settings WHERE server=? AND setting=?");
        getServerSetting = connection.prepareStatement("SELECT value FROM server_settings WHERE server=? AND setting=?");
        getAllServerSettings = connection.prepareStatement("SELECT setting, value FROM server_settings WHERE server=?");

        addVoiceBan = connection.prepareStatement("INSERT INTO voice_bans VALUES (?, ?, ?, ?, ?, ?, 0)");
        getVoiceBans = connection.prepareStatement("SELECT * FROM voice_bans WHERE server=? ORDER BY date(end) DESC");
        updateVoiceBan = connection.prepareStatement("UPDATE voice_bans SET canceled=1 WHERE server=? AND user=? AND mod=? AND start=?");

        getFilterAction = connection.prepareStatement("SELECT * FROM filter_actions WHERE server=? AND name=?");
        getAllFilterActions = connection.prepareStatement("SELECT * FROM filter_actions WHERE server=?");
        addFilterAction = connection.prepareStatement("INSERT INTO filter_actions VALUES (?,?,?,?)");
        removeFilterAction = connection.prepareStatement("DELETE FROM filter_actions WHERE server=? AND name=?");
    }

    private boolean verifyTables() {
        return (getTables().size() == 9);
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
                "CREATE INDEX settings_idx ON server_settings(server)",
                "CREATE TABLE voice_bans (server INTEGER NOT NULl, user INTEGER NOT NULL, mod INTEGER NOT NULL, reason TEXT, start DATETIME NOT NULL, end DATETIME, canceled TINYINT, PRIMARY KEY(server, user, mod, start, end))",
                "CREATE INDEX ban_idx ON voice_bans(server)",
                "CREATE TABLE filter_actions (server INTEGER NOT NULL, name STRING NOT NULL, filters BLOB, actions BLOB, PRIMARY KEY(server,name))",
                "CREATE INDEX filter_action_idx ON filter_actions(server)"
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

    public boolean checkRegexKey(long server, String key) throws SQLException {
        checkRegexKey.setLong(1, server);
        checkRegexKey.setString(2, key);
        ResultSet r = checkRegexKey.executeQuery();
        checkRegexKey.clearParameters();
        return r.next();
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
        addWatchedRole.execute();
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

    public void executeIncrementDatapoint(long server, long user, String datapoint) throws SQLException {
        if (!checkDatapointExists(server, user, datapoint)) {
            executeCreateDatapoint(server, user, datapoint);
            return;
        }
        incrementDatapoint.setLong(1, server);
        incrementDatapoint.setLong(2, user);
        incrementDatapoint.setString(3, datapoint);
        incrementDatapoint.executeUpdate();
        incrementDatapoint.clearParameters();
    }

    public void executeCreateDatapoint(long server, long user, String datapoint) throws SQLException {
        createDatapoint.setLong(1, server);
        createDatapoint.setLong(2, user);
        createDatapoint.setString(3, datapoint);
        createDatapoint.execute();
        createDatapoint.clearParameters();
    }

    public void executeEditDatapoint(long server, long user, String datapoint, int val) throws SQLException {
        if (!checkDatapointExists(server, user, datapoint)) {
            executeCreateDatapoint(server,user, datapoint);
        }
        editDatapoint.setInt(1, val);
        editDatapoint.setLong(2, server);
        editDatapoint.setLong(3, user);
        editDatapoint.setString(4, datapoint);
        editDatapoint.executeUpdate();
        editDatapoint.clearParameters();
    }

    public int getDatapoint(long server, long user, String datapoint) throws SQLException {
        getDatapoint.setLong(1, server);
        getDatapoint.setLong(2, user);
        getDatapoint.setString(3, datapoint);
        ResultSet r = getDatapoint.executeQuery();
        return r.getInt(1);
    }

    public List<Pair<String,Integer>> getAllDatapoints(long server, long user) throws SQLException {
        getAllDatapoints.setLong(1, server);
        getAllDatapoints.setLong(2, user);
        ResultSet r = getAllDatapoints.executeQuery();
        List<Pair<String,Integer>> ps = new ArrayList<>();
        while (r.next()) {
            ps.add(Pair.of(r.getString(3), r.getInt(4)));
        }
        return ps;
    }

    public List<Triple<Long, String, Integer>> getAllDatapointsServer(long server) throws SQLException {
        getAllDatapointsServer.setLong(1, server);
        ResultSet r = getAllDatapointsServer.executeQuery();
        List<Triple<Long,String,Integer>> ts = new ArrayList<>();
        while (r.next()) {
            ts.add(new Triple<>(r.getLong(2), r.getString(3), r.getInt(4)));
        }
        return ts;
    }

    public boolean checkDatapointExists(long server, long user, String datapoint) throws SQLException {
        checkDatapointExists.setLong(1, server);
        checkDatapointExists.setLong(2, user);
        checkDatapointExists.setString(3, datapoint);
        ResultSet r = checkDatapointExists.executeQuery();
        checkDatapointExists.clearParameters();
        return r.next();
    }

    public boolean checkServerSettingExists(long server, String setting) throws SQLException {
        checkServerSetting.setLong(1, server);
        checkServerSetting.setString(2, setting);
        ResultSet r = checkServerSetting.executeQuery();
        checkServerSetting.clearParameters();
        return r.next();
    }

    public long getServerSetting(long server, String setting) throws SQLException {
        getServerSetting.setLong(1, server);
        getServerSetting.setString(2, setting);
        ResultSet r = getServerSetting.executeQuery();
        checkServerSetting.clearParameters();
        return r.getLong(1);
    }

    public void editServerSetting(long server, String setting, long val) throws SQLException {
        if (!checkServerSettingExists(server, setting)) {
            addServerSetting(server, setting, val);
            return;
        }
        modifyServerSetting.setLong(1, val);
        modifyServerSetting.setLong(2, server);
        modifyServerSetting.setString(3, setting);
        modifyServerSetting.executeUpdate();
        modifyServerSetting.clearParameters();
    }

    public void addServerSetting(long server, String setting, long val) throws SQLException {
        addServerSetting.setLong(1, server);
        addServerSetting.setString(2, setting);
        addServerSetting.setLong(3, val);
        addServerSetting.execute();
        addServerSetting.clearParameters();
    }

    public List<ServerSettings> getAllServerSettings(long server) throws SQLException {
        getAllServerSettings.setLong(1, server);
        ResultSet r = getAllServerSettings.executeQuery();
        getAllServerSettings.clearParameters();
        List<ServerSettings> l = new ArrayList<>();
        while (r.next()) {
            l.add(new ServerSettings(server, ServerParameter.stringToSetting(r.getString(1)), r.getLong(2)));
        }
        return l;
    }

    public void addVoiceBan(VoiceBan vb) throws SQLException {
        addVoiceBan.setLong(1, vb.getGuildId());
        addVoiceBan.setLong(2, vb.getMemberId());
        addVoiceBan.setLong(3, vb.getModId());
        addVoiceBan.setString(4, vb.getReason());
        addVoiceBan.setTimestamp(5, Timestamp.from(vb.getStartTime().toInstant(ZoneOffset.UTC)));
        addVoiceBan.setTimestamp(6, Timestamp.from(vb.getExpireTime().toInstant(ZoneOffset.UTC)));
        addVoiceBan.execute();
        addVoiceBan.clearParameters();
    }

    public List<VoiceBan> getAllVoiceBans(long serverid) throws SQLException {
        getVoiceBans.setLong(1, serverid);
        ResultSet r = getVoiceBans.executeQuery();
        List<VoiceBan> l = new ArrayList<>();
        while (r.next()) {
            l.add(new VoiceBan(
                    r.getLong("server"), r.getLong("user"), r.getLong("mod"),
                    r.getTimestamp("start").toLocalDateTime(),
                    r.getTimestamp("end").toLocalDateTime(), r.getString("reason")
            ));
        }
        return l;
    }

    public void updateVoiceBan(VoiceBan vb) throws SQLException {
        updateVoiceBan.setLong(1, vb.getGuildId());
        updateVoiceBan.setLong(2, vb.getMemberId());
        updateVoiceBan.setLong(3, vb.getModId());
        updateVoiceBan.setTimestamp(4, Timestamp.from(vb.getStartTime().toInstant(ZoneOffset.UTC)));
        updateVoiceBan.executeUpdate();
        updateVoiceBan.clearParameters();
    }

    public void executeAddFilterAction(long guild, MessageFilterAction messageFilterAction) throws SQLException, IOException {
        addFilterAction.setLong(1, guild);
        addFilterAction.setString(2, messageFilterAction.getName());
        //TODO serialize the arraylists as strings
        addFilterAction.setBytes(3, MiscUtil.serializeList(messageFilterAction.getFilters()));
        addFilterAction.setBytes(4, MiscUtil.serializeList(messageFilterAction.getActions()));
        addFilterAction.execute();
        addFilterAction.clearParameters();
    }

    public void executeRemoveFilterAction(long guild, MessageFilterAction messageFilterAction) throws SQLException {
        removeFilterAction.setLong(1, guild);
        removeFilterAction.setString(2, messageFilterAction.getName());
        removeFilterAction.executeUpdate();
        removeFilterAction.clearParameters();
    }

    public MessageFilterAction getFilterAction(long guild, String name) throws SQLException, IOException, ClassNotFoundException {
        getFilterAction.setLong(1, guild);
        getFilterAction.setString(2, name);
        ResultSet r = getFilterAction.executeQuery();
        getFilterAction.clearParameters();
        return new MessageFilterAction(guild, name, MiscUtil.deserializeList(r.getBytes(3)), MiscUtil.deserializeList(r.getBytes(4)));
    }

    public List<MessageFilterAction> getAllFilterActions(long guild) throws SQLException, IOException, ClassNotFoundException {
        getAllFilterActions.setLong(1, guild);
        ResultSet r = getAllFilterActions.executeQuery();
        ArrayList<MessageFilterAction> ar = new ArrayList<>();
        while (r.next()) {
            List<Filter<Message>> filters = MiscUtil.deserializeList(r.getBytes(3));
            List<Action<Message>> actions = MiscUtil.deserializeList(r.getBytes(4));
            ar.add(new MessageFilterAction(guild, r.getString(2), filters, actions));
        }
        return ar;
    }
}