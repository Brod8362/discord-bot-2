package pw.byakuren.discord;

import com.sun.tools.javac.util.Pair;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.Triple;
import pw.byakuren.discord.objects.cache.datatypes.*;

import java.sql.SQLException;
import java.util.*;

public class DatabaseManager {

    private SQLConnection sql;
    private JDA jda;

    public DatabaseManager(SQLConnection sql, JDA jda) throws SQLException {
            this.sql = sql;
            this.jda = jda;
            sql.initialize();
    }

    public SQLConnection getSQL() {
        return sql;
    }


    /* Methods for changing server settings and features. */

    public void addServerSetting(Guild server, String setting, long value) {
        addServerSetting(server.getIdLong(), setting, value);
    }

    public void addServerSetting(long server, String setting, long value) {
        try {
            sql.addServerSetting(server, setting, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeServerSetting(Guild server, String setting) {
        //todo complete
    }

    public void editServerSetting(Guild server, String setting, long newvalue) {
        editServerSetting(server.getIdLong(), setting, newvalue);
    }

    public void editServerSetting(long serverid, String setting, long newvalue) {
        try {
            sql.editServerSetting(serverid, setting, newvalue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ServerSettings> getServerSettings(Guild server) {
        return getServerSettings(server.getIdLong());
    }

    public List<ServerSettings> getServerSettings(long serverid) {
        try {
            return sql.getAllServerSettings(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Methods for modifying user chat data/statistics.
    *   Valid datapoints: reactions_tx, reactions_rx, messages_sent, messages_deleted, attachments_sent
    * */

    public void addUserChatData(Member user, String datapoint) {
        try {
            sql.executeCreateDatapoint(user.getGuild().getIdLong(), user.getUser().getIdLong(), datapoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editUserChatData(long server, long us, String datapoint, int new_val) {
        try {
            sql.executeEditDatapoint(server, us, datapoint, new_val);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void editUserChatData(Member user, String datapoint, int new_val) {
        editUserChatData(user.getGuild().getIdLong(), user.getUser().getIdLong(), datapoint, new_val);
    }

    public void removeUserChatData(Member user, String datapoint) {
        //todo complete
        throw new UnsupportedOperationException("unimplemented");
    }

    public int getUserChatDatapoint(Member user, String datapoint) {
        try {
            return sql.getDatapoint(user.getGuild().getIdLong(), user.getUser().getIdLong(), datapoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public UserStats getUserChatData(Member user) {
        List<Pair<String, Integer>> data = null;
        try {
            data = sql.getAllDatapoints(user.getGuild().getIdLong(), user.getUser().getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data==null) return null;
        int rec_rx = 0, rec_tx = 0, msg_sd = 0, msg_dl = 0, atc_sd = 0;
        for (Pair<String,Integer> t: data) {
            switch (Objects.requireNonNull(Statistic.datapointToStatistic(t.fst))) {
                case MESSAGES_SENT:
                    msg_sd = t.snd;
                    break;
                case REACTIONS_SENT:
                    rec_tx = t.snd;
                    break;
                case ATTACHMENTS_SENT:
                    atc_sd = t.snd;
                    break;
                case MESSAGES_DELETED:
                    msg_dl = t.snd;
                    break;
                case REACTIONS_RECEIVED:
                    rec_rx = t.snd;
                    break;
            }
        }
        return new UserStats(user.getGuild().getIdLong(), user.getUser().getIdLong(),
                rec_tx, rec_rx, msg_sd, msg_dl, atc_sd);
    }

    public List<UserStats> getAllChatDataForServer(long serverid) {
        List<Triple<Long, String, Integer>> raw = null;
        try {
            raw = sql.getAllDatapointsServer(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map<Long, UserStats> users = new HashMap<>();
        for (Triple<Long, String, Integer> t: raw) {
            UserStats s = users.getOrDefault(t.a, new UserStats(serverid, t.a));
            s.setStatistic(Objects.requireNonNull(Statistic.datapointToStatistic(t.b)), t.c);
            users.put(t.a, s);
        }
        return new ArrayList<>(users.values());
    }

    /* Methods for modifying watched roles. */

    public void addWatchedRole(Role role) {
        addWatchedRole(role.getGuild().getIdLong(), role.getIdLong());
    }

    public void addWatchedRole(long server, long role) {
        try {
            sql.executeAddWatchedRole(server, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWatchedRole(Role role) {
        removeWatchedRole(role.getGuild().getIdLong(), role.getIdLong());
    }

    public void removeWatchedRole(long server, long role) {
        try {
            sql.executeRemoveWatchedRole(server, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Long> getWatchedRoles(Guild server) {
        return getWatchedRoles(server.getIdLong());
    }
    
    public List<Long> getWatchedRoles(long serverid) {
        try {
            return sql.getWatchedRoles(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkWatchedRole(Role role) {
        return checkWatchedRole(role.getGuild().getIdLong(), role.getIdLong());
    }

    public boolean checkWatchedRole(long server, long role) {
        try {
            return sql.checkWatchedRole(server, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Methods for handling watched users */

    public void addWatchedUser(Member user) {
        addWatchedUser(user.getGuild().getIdLong(), user.getUser().getIdLong());
    }

    public void addWatchedUser(long server, long user) {
        try {
            sql.executeAddWatchedUser(server, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWatchedUser(Member user) {
        removeWatchedUser(user.getGuild().getIdLong(), user.getUser().getIdLong());
    }

    public void removeWatchedUser(long server, long user) {
        try {
            sql.executeRemoveWatchedUser(server, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getWatchedUsers(Guild server) {
        return getWatchedUsers(server.getIdLong());
    }

    public List<Long> getWatchedUsers(long serverid) {
        try {
            return sql.getWatchedUsers(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkWatchedUser(Member user) {
        return checkWatchedUser(user.getGuild().getIdLong(), user.getUser().getIdLong());
    }

    public boolean checkWatchedUser(long server, long user) {
        try {
            return sql.checkWatchedUser(server, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
    
    public List<TextChannel> getExcludedChannels(Guild server) {
        return getExcludedChannels(server.getIdLong());
    }
    
    public List<TextChannel> getExcludedChannels(long serverid) {
        try {
            ArrayList<TextChannel> gs = new ArrayList<>();
            List<Long> s = sql.executeGetExcludedChannels(serverid);
            for (long l: s) {
                gs.add(jda.getTextChannelById(l));
            }
            return gs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ExcludedChannel getExcludedChannel(long channelid) {
        try {
            if (sql.executeCheckExcludedChannel(channelid)) {
                return new ExcludedChannel(channelid, jda);
            }
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

    public List<RegexKey> getRegexKeys(Guild guild) {
        return getRegexKeys(guild.getIdLong());
    }

    public List<RegexKey> getRegexKeys(long serverid) {
        try {
            Guild g = jda.getGuildById(serverid);
            if (g == null) {
                //todo handle null case
            }
            List<String> keys_raw = sql.executeGetRegexKeys(serverid);
            List<RegexKey> keys = new ArrayList<>();
            for (String k: keys_raw) {
                keys.add(new RegexKey(g, k));
            }
            return keys;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkRegexKey(long serverid, String key) {
        try {
            return sql.checkRegexKey(serverid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkRegexKey(RegexKey k) {
        return checkRegexKey(k.getGuild().getIdLong(), k.getKey());
    }
    
    /* Miscellaneous methods. */

    public void createNeededTables() {
        try {
            sql.createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLastMessage(Message m) {
       updateLastMessage(m.getGuild().getIdLong(), m.getMember().getUser().getIdLong(),
               m.getContentDisplay(), m.getIdLong());
    }

    public void updateLastMessage(long server, long user, String content, long id) {
        try {
            sql.executeUpdateLastMessage(server, user, content, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LastMessage getLastMessage(Member m) {
        return getLastMessage(m.getGuild().getIdLong(), m.getUser().getIdLong());
    }

    public LastMessage getLastMessage(long serverid, long memberid) {
        try {
            return sql.executeGetLastMessage(serverid, memberid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LastMessage> getLastMessages(Guild g) {
        return getLastMessages(g.getIdLong());
    }

    public List<LastMessage> getLastMessages(long serverid) {
        try {
            return sql.executeGetLastMessages(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WatchedUser getWatchedUser(long serverid, long userid) {
        try {
            if (sql.checkWatchedUser(serverid, userid))
                return new WatchedUser(serverid, userid, jda);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
