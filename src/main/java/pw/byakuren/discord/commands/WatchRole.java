package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class WatchRole implements Command {

    private DatabaseManager dbmg;

    public WatchRole(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }

    @Override
    public String getName() {
        return "role";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Manage watched pingable roles.";
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
                for (Role r: message.getMentionedRoles()) {
                    if (!dbmg.checkWatchedRole(r))
                        dbmg.addWatchedRole(r);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "remove":
                for (Role r: message.getMentionedRoles()) {
                    if (dbmg.checkWatchedRole(r))
                        dbmg.removeWatchedRole(r);
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "list":
                List<Long> list = dbmg.getWatchedRoles(message.getGuild());
                StringBuilder s = new StringBuilder();
                s.append("Watched roles:\n");

                for (Long aList : list) {
                    s.append("<@&").append(aList).append("> ");
                }
                if (list.size() == 0) {
                    s = new StringBuilder();
                    s.append("No watched roles. Use 'add' to add some.");
                }
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            default:
                message.getChannel().sendMessage("Available arguments: `add [mention role]`, `remove [mention roles]`, `list`").queue();
        }
    }
}
