package pw.byakuren.discord.objects;

public class UserStats {

    private int server;
    private int user;
    private int reactions_sent = 0;
    private int reactions_received = 0;
    private int messages_sent = 0;
    private int messages_deleted = 0;
    private int attachments_sent = 0;

    public UserStats(int server, int user, int reactions_sent, int reactions_received, int messages_sent, int messages_deleted, int attachments_sent) {
        this.server = server;
        this.user = user;
        this.reactions_sent = reactions_sent;
        this.reactions_received = reactions_received;
        this.messages_sent = messages_sent;
        this.messages_deleted = messages_deleted;
        this.attachments_sent = attachments_sent;
    }

    public UserStats(int server, int user) {
        this.server = server;
        this.user = user;
    }

    public int getServer() {
        return server;
    }

    public int getUser() {
        return user;
    }

    public int getReactions_sent() {
        return reactions_sent;
    }

    public int getReactions_received() {
        return reactions_received;
    }

    public int getMessages_sent() {
        return messages_sent;
    }

    public int getMessages_deleted() {
        return messages_deleted;
    }

    public int getAttachments_sent() {
        return attachments_sent;
    }

    public void incrementReactionsSent() {
        reactions_sent++;
    }

    public void incrementReactionsReceived() {
        reactions_received++;
    }

    public void incrementMessagesSent() {
        messages_sent++;
    }

    public void incrementAttachmentsSent() {
        attachments_sent++;
    }

    public void incrementMessagesDeleted() {
        messages_deleted++;
    }

    public void decrementAttachementsSent() {
        attachments_sent--;
    }

    public void decrementReactionsSent() {
        reactions_sent--;
    }

    public void decrementReactionsReceived() {
        reactions_received--;
    }

    public void decrementMessagesSent() {
        messages_sent--;
    }

    public void decrementMessagesDeleted() {
        messages_deleted--;
    }




}
