package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class Subscribe implements Command {

    DatabaseManager dbmg;

    public Subscribe(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }
    @Override
    public String getName() {
        return "subscribe";
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
                    if (!dbmg.checkModeratorSubscription(message.getMember(), m))
                        dbmg.addModeratorSubscription(message.getMember(), m);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "remove":
                for (Member m: message.getMentionedMembers()) {
                    if (dbmg.checkModeratorSubscription(message.getMember(), m))
                        dbmg.removeModeratorSubscription(message.getMember(), m);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "list":
                List<Long> list = dbmg.getModeratorSubscriptions(message.getMember());
                StringBuilder s = new StringBuilder();
                s.append("Subscriptions for "+message.getMember().getAsMention()+":\n");
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
