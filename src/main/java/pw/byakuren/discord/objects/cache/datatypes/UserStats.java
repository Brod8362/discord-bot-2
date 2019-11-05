package pw.byakuren.discord.objects.cache.datatypes;

import pw.byakuren.discord.objects.Statistic;

public class UserStats extends CacheEntry {

    private long server;
    private long user;
    private int reactions_sent = 0;
    private int reactions_received = 0;
    private int messages_sent = 0;
    private int messages_deleted = 0;
    private int attachments_sent = 0;

    public UserStats(long server, long user, int reactions_sent, int reactions_received, int messages_sent, int messages_deleted, int attachments_sent) {
        this.server = server;
        this.user = user;
        this.reactions_sent = reactions_sent;
        this.reactions_received = reactions_received;
        this.messages_sent = messages_sent;
        this.messages_deleted = messages_deleted;
        this.attachments_sent = attachments_sent;
    }

    public UserStats(long server, long user) {
        this.server = server;
        this.user = user;
    }

    public long getServer() {
        return server;
    }

    public long getUser() {
        return user;
    }

    public int getStatistic(Statistic e) {
        switch (e) {
            case REACTIONS_SENT:
                return reactions_sent;
            case REACTIONS_RECEIVED:
                return reactions_received;
            case MESSAGES_SENT:
                return messages_sent;
            case MESSAGES_DELETED:
                return messages_deleted;
            case ATTACHMENTS_SENT:
                return attachments_sent;
            default:
                return -1;
        }
    }

    public boolean incrementStatistic(Statistic e) {
        switch (e) {
            case REACTIONS_SENT:
                reactions_sent++;
                break;
            case REACTIONS_RECEIVED:
                reactions_received++;
                break;
            case MESSAGES_SENT:
                messages_sent++;
                break;
            case MESSAGES_DELETED:
                messages_deleted++;
                break;
            case ATTACHMENTS_SENT:
                attachments_sent++;
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean decrementStatistic(Statistic e) {
        switch (e) {
            case REACTIONS_SENT:
                reactions_sent--;
                break;
            case REACTIONS_RECEIVED:
                reactions_received--;
                break;
            case MESSAGES_SENT:
                messages_sent--;
                break;
            case MESSAGES_DELETED:
                messages_deleted--;
                break;
            case ATTACHMENTS_SENT:
                attachments_sent--;
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean setStatistic(Statistic e, int v) {
        switch (e) {
            case REACTIONS_SENT:
                reactions_sent=v;
                break;
            case REACTIONS_RECEIVED:
                reactions_received=v;
                break;
            case MESSAGES_SENT:
                messages_sent=v;
                break;
            case MESSAGES_DELETED:
                messages_deleted=v;
                break;
            case ATTACHMENTS_SENT:
                attachments_sent=v;
                break;
            default:
                return false;
        }
        return true;
    }
}
