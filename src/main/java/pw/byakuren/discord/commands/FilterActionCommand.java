package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.FilterActionResult;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;
import pw.byakuren.discord.util.BotEmbed;
import pw.byakuren.discord.util.MessageFilterParser;
import pw.byakuren.discord.util.ScalaReplacements;

import java.util.List;

public class FilterActionCommand extends Command {

    private Cache c;

    public FilterActionCommand(Cache c) {
        this.c=c;
        names=new String[]{"filteraction", "fa"};
        minimum_permission= CommandPermission.MOD_ROLE;
        subcommands.add(new Subcommand(new String[]{"test", "t"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_test(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"add", "a"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_add(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"list", "l"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_list(message, args);
            }
        });
    }

    private void cmd_test(Message msg, List<String> args) {
        //Note that this expects a message ID.
        ServerCache sc = c.getServerCache(msg.getGuild());
        MessageFilterAction mfa = sc.getFilterActionByName(args.get(0));
        //TODO blah blah finish this
        //FilterActionResult result = mfa.check(fs);
    }

    private void cmd_add(Message msg, List<String> args) {
        String mfaName = args.get(0);
        ServerCache sc = c.getServerCache(msg.getGuild());
        String filterString = ScalaReplacements.mkString(args.subList(1, args.size()));
        Filter<Message> filter = MessageFilterParser.fromString(filterString);

        if (filter == null) {
            msg.reply(
                    BotEmbed.bad("Looks like your filter couldn't be parsed. Check you spelled everything right and try again.").build()
            ).queue();
            return;
        }

        MessageFilterAction mfa = sc.getFilterActionByName(mfaName);
        if (mfa == null) {
            mfa = new MessageFilterAction(msg.getGuild().getIdLong(), mfaName);
            sc.getMessageFilterActions().getData().add(mfa);
        }
        mfa.addFilter(filter);
        mfa.write_state = WriteState.PENDING_WRITE;
        msg.reply("200").queue();
    }

    private void cmd_list(Message msg, List<String> args) {
        ServerCache sc = c.getServerCache(msg.getGuild());
        msg.reply(String.format("```%s```", ScalaReplacements.mkString(sc.getAllFilterActions(),"\n"))).queue();
    }
}
