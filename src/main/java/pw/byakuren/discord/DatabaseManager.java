package pw.byakuren.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.Triple;
import pw.byakuren.discord.objects.cache.datatypes.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class DatabaseManager {

    private @NotNull SQLConnection sql;
    private @NotNull JDA jda;

    public DatabaseManager(@NotNull SQLConnection sql, @NotNull JDA jda) throws SQLException {
            this.sql = sql;
            this.jda = jda;
            sql.initialize();
    }

    public @NotNull SQLConnection getSQL() {
        return sql;
    }


    /* Methods for changing server settings and features. */

    public void addServerSetting(@NotNull Guild server, @NotNull String setting, long value) {
        addServerSetting(server.getIdLong(), setting, value);
    }

    public void addServerSetting(long server, @NotNull String setting, long value) {
        try {
            sql.addServerSetting(server, setting, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeServerSetting(@NotNull Guild server, @NotNull String setting) {
        //todo complete
    }

    public void editServerSetting(@NotNull Guild server, @NotNull String setting, long newvalue) {
        editServerSetting(server.getIdLong(), setting, newvalue);
    }

    public void editServerSetting(long serverid, @NotNull String setting, long newvalue) {
        try {
            sql.editServerSetting(serverid, setting, newvalue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @Nullable List<ServerSettings> getServerSettings(@NotNull Guild server) {
        return getServerSettings(server.getIdLong());
    }

    public @Nullable List<ServerSettings> getServerSettings(long serverid) {
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

    public void addUserChatData(@NotNull Member user, @NotNull String datapoint) {
        try {
            sql.executeCreateDatapoint(user.getGuild().getIdLong(), user.getUser().getIdLong(), datapoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editUserChatData(long server, long us, @NotNull String datapoint, int new_val) {
        try {
            sql.executeEditDatapoint(server, us, datapoint, new_val);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void editUserChatData(@NotNull Member user, @NotNull String datapoint, int new_val) {
        editUserChatData(user.getGuild().getIdLong(), user.getUser().getIdLong(), datapoint, new_val);
    }

    public void removeUserChatData(@NotNull Member user, @NotNull String datapoint) {
        //todo complete
        throw new UnsupportedOperationException("unimplemented");
    }

    public int getUserChatDatapoint(@NotNull Member user, @NotNull String datapoint) {
        try {
            return sql.getDatapoint(user.getGuild().getIdLong(), user.getUser().getIdLong(), datapoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public @Nullable UserStats getUserChatData(@NotNull Member user) {
        List<Pair<String, Integer>> data = null;
        try {
            data = sql.getAllDatapoints(user.getGuild().getIdLong(), user.getUser().getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data==null) return null;
        int rec_rx = 0, rec_tx = 0, msg_sd = 0, msg_dl = 0, atc_sd = 0;
        for (Pair<String,Integer> t: data) {
            switch (Objects.requireNonNull(Statistic.datapointToStatistic(t.getLeft()))) {
                case MESSAGES_SENT:
                    msg_sd = t.getRight();
                    break;
                case REACTIONS_SENT:
                    rec_tx = t.getRight();
                    break;
                case ATTACHMENTS_SENT:
                    atc_sd = t.getRight();
                    break;
                case MESSAGES_DELETED:
                    msg_dl = t.getRight();
                    break;
                case REACTIONS_RECEIVED:
                    rec_rx = t.getRight();
                    break;
            }
        }
        return new UserStats(user.getGuild().getIdLong(), user.getUser().getIdLong(),
                rec_tx, rec_rx, msg_sd, msg_dl, atc_sd);
    }

    public @NotNull List<UserStats> getAllChatDataForServer(long serverid) {
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

    public void addWatchedRole(@NotNull Role role) {
        addWatchedRole(role.getGuild().getIdLong(), role.getIdLong());
    }

    public void addWatchedRole(long server, long role) {
        try {
            sql.executeAddWatchedRole(server, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWatchedRole(@NotNull Role role) {
        removeWatchedRole(role.getGuild().getIdLong(), role.getIdLong());
    }

    public void removeWatchedRole(long server, long role) {
        try {
            sql.executeRemoveWatchedRole(server, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public @Nullable List<Long> getWatchedRoles(@NotNull Guild server) {
        return getWatchedRoles(server.getIdLong());
    }
    
    public @Nullable List<Long> getWatchedRoles(long serverid) {
        try {
            return sql.getWatchedRoles(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkWatchedRole(@NotNull Role role) {
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

    public void addWatchedUser(@NotNull Member user) {
        addWatchedUser(user.getGuild().getIdLong(), user.getUser().getIdLong());
    }

    public void addWatchedUser(long server, long user) {
        try {
            sql.executeAddWatchedUser(server, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWatchedUser(@NotNull Member user) {
        removeWatchedUser(user.getGuild().getIdLong(), user.getUser().getIdLong());
    }

    public void removeWatchedUser(long server, long user) {
        try {
            sql.executeRemoveWatchedUser(server, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @Nullable List<Long> getWatchedUsers(@NotNull Guild server) {
        return getWatchedUsers(server.getIdLong());
    }

    public @Nullable List<Long> getWatchedUsers(long serverid) {
        try {
            return sql.getWatchedUsers(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkWatchedUser(@NotNull Member user) {
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
    
    public void addExcludedChannel(@NotNull TextChannel channel) {
        try {
            sql.executeAddExcludedChannel(channel.getGuild().getIdLong(), channel.getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeExcludedChannel(@NotNull TextChannel channel) {
        try {
            sql.executeRemoveExcludedChannel(channel.getGuild().getIdLong(), channel.getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public @Nullable List<TextChannel> getExcludedChannels(@NotNull Guild server) {
        return getExcludedChannels(server.getIdLong());
    }
    
    public @Nullable List<TextChannel> getExcludedChannels(long serverid) {
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

    public @Nullable ExcludedChannel getExcludedChannel(long channelid) {
        try {
            if (sql.executeCheckExcludedChannel(channelid)) {
                return new ExcludedChannel(channelid, jda);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkExcludedChannel(@NotNull TextChannel channel) {
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
    
    public void addRegexKey(@NotNull Guild server, @NotNull String key) {
        addRegexKey(server.getIdLong(), key);
    }

    public void addRegexKey(long serverid, @NotNull String key) {
        try {
            sql.executeAddRegexKey(serverid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void removeRegexKey(@NotNull Guild server, @NotNull String key) {
        removeRegexKey(server.getIdLong(), key);
    }

    public void removeRegexKey(long serverid, @NotNull String key) {
        try {
            sql.executeRemoveRegexKey(serverid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @Nullable List<RegexKey> getRegexKeys(@NotNull Guild guild) {
        return getRegexKeys(guild.getIdLong());
    }

    public @Nullable List<RegexKey> getRegexKeys(long serverid) {
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

    public boolean checkRegexKey(long serverid, @NotNull String key) {
        try {
            return sql.checkRegexKey(serverid, key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkRegexKey(@NotNull RegexKey k) {
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

    public void updateLastMessage(@NotNull Message m) {
       updateLastMessage(m.getGuild().getIdLong(), m.getMember().getUser().getIdLong(),
               m.getContentDisplay(), m.getIdLong());
    }

    public void updateLastMessage(long server, long user, @NotNull String content, long id) {
        try {
            sql.executeUpdateLastMessage(server, user, content, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @Nullable LastMessage getLastMessage(@NotNull Member m) {
        return getLastMessage(m.getGuild().getIdLong(), m.getUser().getIdLong());
    }

    public @Nullable LastMessage getLastMessage(long serverid, long memberid) {
        try {
            return sql.executeGetLastMessage(serverid, memberid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public @Nullable List<LastMessage> getLastMessages(@NotNull Guild g) {
        return getLastMessages(g.getIdLong());
    }

    public @Nullable List<LastMessage> getLastMessages(long serverid) {
        try {
            return sql.executeGetLastMessages(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public @Nullable WatchedUser getWatchedUser(long serverid, long userid) {
        try {
            if (sql.checkWatchedUser(serverid, userid))
                return new WatchedUser(serverid, userid, jda);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public @Nullable List<VoiceBan> getAllVoiceBans(long serverid) {
        try {
            return sql.getAllVoiceBans(serverid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addVoiceBan(@NotNull VoiceBan voiceBan) {
        try {
            sql.addVoiceBan(voiceBan);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVoiceBan(@NotNull VoiceBan vb) {
        try {
            sql.updateVoiceBan(vb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFilterAction(long guild, @NotNull MessageFilterAction messageFilterAction) {
        try {
            sql.executeAddFilterAction(guild, messageFilterAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFilterAction(long guild, @NotNull MessageFilterAction messageFilterAction) {
        try {
            sql.executeRemoveFilterAction(guild, messageFilterAction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @Nullable MessageFilterAction getFilterAction(long guild, @NotNull String name) {
        try {
            return sql.getFilterAction(guild, name);
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public @Nullable List<MessageFilterAction> getAllFilterActions(long guild) {
        try {
            return sql.getAllFilterActions(guild);
        } catch (SQLException | ClassNotFoundException | IOException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
