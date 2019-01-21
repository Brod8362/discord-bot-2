package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQL implements Command {

    DatabaseManager dbmg;

    public SQL(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }

    @Override
    public String getName() {
        return "sql";
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
        switch (args.get(0)) {
            case "create":
                dbmg.createNeededTables();
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "dropall":
                ArrayList<String> tables = dbmg.getSQL().getTables();
                for (String s: tables) {
                    try {
                        dbmg.getSQL().dropTable(s);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            default:
                message.addReaction("❔").queue();
        }
    }
}

