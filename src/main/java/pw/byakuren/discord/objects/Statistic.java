package pw.byakuren.discord.objects;

import org.jetbrains.annotations.Nullable;

public enum Statistic {

    REACTIONS_SENT("reactions_tx", "Reactions Sent"),
    REACTIONS_RECEIVED("reactions_rx", "Reactions Received"),
    MESSAGES_SENT("messages_sent", "Messages Sent"),
    MESSAGES_DELETED("messages_deleted", "Messaged Deleted"),
    ATTACHMENTS_SENT("attachments_sent", "Attachments Sent");

    public final String datapoint_name;
    public final String nice_name;

    Statistic(String s, String n) {
        datapoint_name = s;
        nice_name = n;
    }

    public static @Nullable Statistic datapointToStatistic(String str) {
        for (Statistic s: Statistic.values()) {
            if (s.datapoint_name.equals(str)) return s;
        }
        return null;
    }

}