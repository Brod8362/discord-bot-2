package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.RegexKey;

import java.util.ArrayList;
import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_DELETE;
import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class RegexKeys extends Command {

    private Cache c;

    public RegexKeys(Cache c) {
        names = new String[]{"regex", "keys"};
        help = "Manage regex keys.";
        minimum_permission = CommandPermission.MOD_ROLE;

        this.c = c;
        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"add", "a"}, "Add a regex key.", "[regex key]", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_add(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"del", "d"}, "Delete a regex key.", "[regex key]", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_del(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"list", "l"}, "View all existing regex keys.", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_list(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"update"}, "Update existing regex keys to the new format." +
                " (Removes (?i) and adds \\b to word boundaries) This cannot be undone.", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_update(message, args);
            }
        });
    }

    private void cmd_add(Message message, List<String> args) {
        if (args.size() == 0) return;
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.size() - 1; i++) {
            s.append(args.get(i)).append(" ");
        }
        s.append(args.get(args.size() - 1));
        RegexKey k = new RegexKey(message.getGuild(), s.toString());
        k.write_state = PENDING_WRITE;
        c.getServerCache(message.getGuild()).getRegexKeys().getData().add(k);
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_del(Message message, List<String> args) {
        List<RegexKey> list = c.getServerCache(message.getGuild()).getRegexKeys().getData();
        StringBuilder s = new StringBuilder();
        if (args.size() == 0) return;
        for (int i = 1; i < args.size() - 1; i++) {
            s.append(args.get(i)).append(" ");
        }
        s.append(args.get(args.size() - 1));
        int pos = 0;
        for (; pos < list.size(); pos++) {
            if (list.get(pos).getKey().equals(s.toString()))
                break;
        }
        if (pos == list.size()) {
            message.reply("Key not found.").mentionRepliedUser(false).queue();
            return;
        }
        c.getServerCache(message.getGuild()).getRegexKeys().getData().get(pos).write_state = PENDING_DELETE;
        message.addReaction("\uD83D\uDC4D").queue();
    }

    private void cmd_list(Message message, List<String> args) {
        List<RegexKey> list = c.getServerCache(message.getGuild()).getAllValidRegexKeys();
        StringBuilder s = new StringBuilder();
        if (list.size() == 0) {
            message.reply("You have no regex keys, use `add [key]` to add some!").mentionRepliedUser(false).queue();
            return;
        }
        s.append("Keys:\n");
        for (int i = 0; i < list.size() - 1; i++) {
            s.append("`").append(list.get(i).getKey()).append("`, ");
        }
        s.append("`").append(list.get(list.size() - 1).getKey()).append("`");
        message.reply(s.toString()).mentionRepliedUser(false).queue();
    }

    private void cmd_update(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        List<RegexKey> keys = sc.getAllValidRegexKeys();
        List<RegexKey> new_keys = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            RegexKey k = keys.get(i);
            if (needsUpdating(k)) {
                RegexKey updated = update(k);
                k.write_state = PENDING_DELETE;
                updated.write_state = PENDING_WRITE;
                new_keys.add(updated);
            }
        }
        sc.getRegexKeys().getData().addAll(new_keys);
        message.reply("Updated " + new_keys.size() + " keys.").mentionRepliedUser(false).queue();
    }

    private boolean needsUpdating(RegexKey p) {
        return p.getKey().contains("(?i)") ||
                !p.getKey().startsWith("\\b") ||
                !p.getKey().endsWith("\\b") ||
                p.getKey().contains(".");
    }

    private RegexKey update(RegexKey p) {
        String source = p.getKey();
        source = source.replaceAll("\\(\\?i\\)", "");
        source = source.replaceAll("\\.", "\\S");
        if (!source.startsWith("\\b"))
            source = "\\b" + source;
        if (!source.endsWith("\\b"))
            source += "\\b";
        return new RegexKey(p.getGuild(), source);
    }
}
