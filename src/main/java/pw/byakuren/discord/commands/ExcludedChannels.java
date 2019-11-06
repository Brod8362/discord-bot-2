package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;

import java.util.List;

import static pw.byakuren.discord.commands.permissions.CommandPermission.MOD_ROLE;

public class ExcludedChannels implements Command {

    private DatabaseManager dbmg;

    public ExcludedChannels(DatabaseManager dbmg) {
        this.dbmg = dbmg;
    }

    @Override
    public String[] getNames() {
        return new String[]{"exclude"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Exclude a channel from being flagged for regex keywords.";
    }

    @Override
    public CommandPermission minimumPermission() {
        return MOD_ROLE;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() == 0) return;
        switch (args.get(0)) {
            case "list":
                List<TextChannel> list = dbmg.getExcludedChannels(message.getGuild());
                StringBuilder s = new StringBuilder();
                s.append("Channels:");
                for (TextChannel c : list) {
                    s.append(String.format("<#%s>", c.getId()));
                }
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            case "here":
                if (dbmg.checkExcludedChannel(message.getTextChannel())) {
                    dbmg.removeExcludedChannel(message.getTextChannel());
                } else {
                    dbmg.addExcludedChannel(message.getTextChannel());
                }
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            default:
                message.getChannel().sendMessage("Available options: `here`, `list`").queue();
        }
    }
}
