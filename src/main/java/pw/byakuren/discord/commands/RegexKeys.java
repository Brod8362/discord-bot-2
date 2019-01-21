package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class RegexKeys implements Command {

    DatabaseManager dbmg = null;

    public RegexKeys(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }

    @Override
    public String getName() {
        return "regex";
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
        if (args.size() == 0) return;
        StringBuilder s = new StringBuilder();
        switch (args.get(0)) {
            case "add":
                if (args.size() == 1) return;
                for (int i = 1; i < args.size(); i++) {
                    s.append(args.get(i)).append(" ");
                }
                dbmg.addRegexKey(message.getGuild(), s.toString());
                break;
            case "remove":
                if (args.size() == 1) return;
                for (int i = 1; i < args.size(); i++) {
                    s.append(args.get(i)).append(" ");
                }
                dbmg.removeRegexKey(message.getGuild(), s.toString());
                break;
            case "list":
                List<String> list = dbmg.getRegexKeys(message.getGuild());
                s.append("Keys:\n");
                for (int i = 0; i < list.size()-1; i++) {
                    s.append("`").append(list.get(i)).append("`, ");
                }
                s.append("`").append(list.get(list.size() - 1)).append("`");
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            default:
                message.getChannel().sendMessage("Available arguments: `add [key]`, `remove [key]`, `list`").queue();
        }
    }
}
