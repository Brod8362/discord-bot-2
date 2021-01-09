package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.result.FilterActionResult;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;
import pw.byakuren.discord.util.*;

import java.util.Arrays;
import java.util.List;

public class FilterActionCommand extends Command {

    private Cache c;

    public FilterActionCommand(Cache c) {
        this.c = c;
        names = new String[]{"filteraction", "fa"};
        minimum_permission = CommandPermission.MOD_ROLE;
        subcommands.add(new Subcommand(new String[]{"test", "t"}, "Test a given FA on your message, with verbose results.",
                "<faName> [more message content to test]", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_test(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"add", "a"}, "Add a new filter or action", "<faName> <filterOrAction>", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_add(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"list", "l"}, "List all FAs on the server, or see the config of a specific one.",
                "<faName>", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_list(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"trash", "t"}, "Delete an entire FA.", "<faName>", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_trash(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"availableFilters", "af"}, "See available fitlers.", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_af(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"availableActions", "aa"}, "See available actions", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_aa(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"remove", "r"}, "Remove a filter or action", "<faName> <filterOrAction>", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_remove(message, args);
            }
        });
    }

    private void cmd_trash(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        if (args.size() == 1) {
            if (sc.getFilterActionByName(args.get(0)) == null) {
                message.reply(
                        BotEmbed.bad("Cannot find FA with name " + args.get(0)).build()
                ).queue();
            } else {
                for (MessageFilterAction mfa : sc.getAllFilterActions()) {
                    if (mfa.getName().equals(args.get(0))) {
                        mfa.write_state = WriteState.PENDING_DELETE;
                        message.addReaction(BotEmoji.TRASH.unicode).queue();
                    }
                }
            }
        } else {
            message.reply(
                    BotEmbed.bad("Please provide the name of one FA to delete.").build()
            ).queue();
        }
    }

    private void cmd_af(Message message, List<String> args) {
        message.reply(String.format("```%s```", ScalaReplacements.mkString(Arrays.asList(MessageFilterParser.getExamples().clone()), "\n"))).queue();
    }

    private void cmd_aa(Message message, List<String> args) {
        message.reply(String.format("```%s```", ScalaReplacements.mkString(Arrays.asList(MessageActionParser.getExamples().clone()), "\n"))).queue();
    }

    private void cmd_test(Message msg, List<String> args) {
        ServerCache sc = c.getServerCache(msg.getGuild());
        MessageFilterAction mfa = sc.getFilterActionByName(args.get(0));
        FilterActionResult far = mfa.check(msg);
        msg.reply(far.embedReport()).queue();
    }

    private void cmd_add(Message msg, List<String> args) {
        String mfaName = args.get(0);
        ServerCache sc = c.getServerCache(msg.getGuild());
        String qString = ScalaReplacements.mkString(args.subList(1, args.size()));
        Filter<Message> filter = MessageFilterParser.fromString(qString);
        Action<Message> action = MessageActionParser.fromString(qString);

        if (!syntaxCheck(qString)) {
            msg.reply(
                    BotEmbed.bad("Looks like your query couldn't be parsed. Check you spelled everything right and try again. " +
                            "Note that filters use () and actions use <>.").build()
            ).queue();
            return;
        }

        MessageFilterAction mfa = sc.getFilterActionByName(mfaName);
        if (mfa == null) {
            mfa = new MessageFilterAction(msg.getGuild().getIdLong(), mfaName);
            sc.getMessageFilterActions().getData().add(mfa);
        }
        if (filter != null) {
            mfa.addFilter(filter);
        } else if (action != null) {
            mfa.addAction(action);
        } else {
            msg.reply(
                    BotEmbed.bad("Your filter/action doesn't exist. Check your spelling and the examples list, and try again.").build()
            ).queue();
            return;
        }
        mfa.write_state = WriteState.PENDING_WRITE;
        msg.addReaction(BotEmoji.OK.toString()).queue();
    }

    private void cmd_list(Message msg, List<String> args) {
        ServerCache sc = c.getServerCache(msg.getGuild());
        if (sc.getAllFilterActions().size() == 0) {
            msg.reply(
                    BotEmbed.information("You haven't configured any filteractions yet.").build()
            ).queue();
            return;
        }
        if (args.size() == 0) {
            //generic list of all
            msg.reply(String.format("```%s```", ScalaReplacements.mkString(sc.getAllFilterActions(), "\n"))).queue();
        } else if (args.size() == 1) {
            //specific instance of a filter action
            MessageFilterAction mfa = sc.getFilterActionByName(args.get(0));
            if (mfa == null) {
                msg.reply(
                        BotEmbed.bad(String.format("The FA \"%s\" doesn't exist. Check your spelling and try again.", args.get(0))).build()
                ).queue();
            } else {
                //todo make this the "info" embed
                msg.reply("`" + mfa.prettyPrint() + "`").queue();
            }
        } else {
            //too many arguments
            msg.reply(
                    BotEmbed.bad("Too many arguments, expected 0-1 but got " + args.size()).build()
            ).queue();
        }
    }

    private void cmd_remove(Message msg, List<String> args) {
        String mfaName = args.get(0);
        ServerCache sc = c.getServerCache(msg.getGuild());
        String qString = ScalaReplacements.mkString(args.subList(1, args.size()));
        MessageFilterAction mfa = sc.getFilterActionByName(mfaName);
        String removeName = qString.split("[(<]")[0];

        if (mfa == null) {
            msg.reply(
                    BotEmbed.bad("No FA by the name " + mfaName + " exists.").build()
            ).queue();
            return;
        }

        if (mfa.removeAction(removeName) || mfa.removeFilter(removeName)) {
            msg.addReaction(BotEmoji.TRASH.unicode).queue();
        }
    }

    private boolean syntaxCheck(String s) {
        String pattern = "[A-z]+((\\()|(<)).*((\\))|(>))";
        return s.matches(pattern);
    }
}
