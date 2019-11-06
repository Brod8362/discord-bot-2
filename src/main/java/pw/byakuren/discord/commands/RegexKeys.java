package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.datatypes.RegexKey;

import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_DELETE;
import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class RegexKeys implements Command {

    private Cache c;

    public RegexKeys(Cache c) {
        this.c = c;
    }

    @Override
    public String[] getNames() {
        return new String[]{"regex","keys"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Manage regex keys.";
    }

    @Override
    public boolean needsBotOwner() {
        return false;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() == 0) return;
        StringBuilder s = new StringBuilder();
        List<RegexKey> list = c.getServerCache(message.getGuild()).getAllValidRegexKeys();
        switch (args.get(0)) {
            case "add":
                if (args.size() == 1) return;
                for (int i = 1; i < args.size()-1; i++) {
                    s.append(args.get(i)).append(" ");
                }
                s.append(args.get(args.size()-1));
                RegexKey k = new RegexKey(message.getGuild(), s.toString());
                k.write_state=PENDING_WRITE;
                c.getServerCache(message.getGuild()).getRegexKeys().getData().add(k);
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "remove":
                if (args.size() == 1) return;
                for (int i = 1; i < args.size()-1; i++) {
                    s.append(args.get(i)).append(" ");
                }
                s.append(args.get(args.size()-1));
                int pos = 0;
                for (; pos < list.size(); pos ++) {
                    if (list.get(pos).getKey().equals(s.toString()))
                        break;
                }
                if (pos == list.size()) {
                    message.getChannel().sendMessage("Key not found.").queue();
                    return;
                }
                c.getServerCache(message.getGuild()).getRegexKeys().getData().get(pos).write_state=PENDING_DELETE;
                message.addReaction("\uD83D\uDC4D").queue();
                break;
            case "list":
                if (list.size() == 0) {
                    message.getChannel().sendMessage("You have no regex keys, use `add [key]` to add some!").queue();
                    return;
                }
                s.append("Keys:\n");
                for (int i = 0; i < list.size()-1; i++) {
                    s.append("`").append(list.get(i).getKey()).append("`, ");
                }
                s.append("`").append(list.get(list.size() - 1).getKey()).append("`");
                message.getChannel().sendMessage(s.toString()).queue();
                break;
            default:
                message.getChannel().sendMessage("Available arguments: `add [key]`, `remove [key]`, `list`").queue();
        }
    }
}
