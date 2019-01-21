package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class WatchRole implements Command {

    private DatabaseManager dbmg;

    public WatchRole(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }

    @Override
    public String getName() {
        return "watch";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public boolean needsBotOwner() {
        return false;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() < 1) return;
        switch (args.get(0)) {
            case "add":
                for (Member m: message.getMentionedMembers()) {
                    if (!dbmg.checkWatchedUser(m))
                        dbmg.addWatchedUser(m);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "remove":
                for (Member m: message.getMentionedMembers()) {
                    if (dbmg.checkWatchedUser(m))
                        dbmg.removeWatchedUser(m);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "list":
                List<Long> list = dbmg.getWatchedUsers(message.getGuild());
                StringBuilder s = new StringBuilder();
                s.append("Watched users:\n");
                for (Long aList : list) {
                    s.append("<@").append(aList).append("> ");
                }
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            default:
                message.getChannel().sendMessage("Available arguments: `add [mention users]`, `remove [mention users]`, `list`").queue();
        }
    }
}
