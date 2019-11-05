package pw.byakuren.discord.objects;

public enum Statistic {

    REACTIONS_SENT("reactions_tx"), REACTIONS_RECEIVED("reactions_rx"),
    MESSAGES_SENT("messages_sent"), MESSAGES_DELETED("messages_deleted"),
    ATTACHMENTS_SENT("attachments_sent");

    public final String datapoint_name;

    Statistic(String s) {
        datapoint_name = s;
    }

    public static Statistic datapointToStatistic(String str) {
        for (Statistic s: Statistic.values()) {
            if (s.datapoint_name.equals(str)) return s;
        }
        return null;
    }

}