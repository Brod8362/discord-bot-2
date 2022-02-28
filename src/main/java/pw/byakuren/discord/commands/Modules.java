package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.modules.Module;
import pw.byakuren.discord.modules.ModuleHelper;

import java.util.List;
import java.util.Map;

public class Modules extends Command {

    private @NotNull ModuleHelper mdhelp;

    public Modules(@NotNull ModuleHelper mdhelp) {
        names=new String[]{"modules"};
        minimum_permission=CommandPermission.BOT_OWNER;
        help="View and manage active modules.";

        this.mdhelp = mdhelp;
        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"enable", "on", "e"}, "Enable a module.", "[module_name]", this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                cmd_enable(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"disable", "off", "d"}, "Disable a module.", "[module_name]", this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                cmd_disable(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"list", "l", "d"}, "View all modules.", "[module_name]", this) {
            @Override
            public void run(@NotNull Message message, @NotNull List<String> args) {
                cmd_list(message, args);
            }
        });
    }

    private void cmd_enable(@NotNull Message message, @NotNull List<String> args) {
        mdhelp.enable(mdhelp.getModule(args.get(0)));
        message.addReaction("✅").queue();
    }

    private void cmd_disable(@NotNull Message message, @NotNull List<String> args) {
        mdhelp.disable(mdhelp.getModule(args.get(0)));
        message.addReaction("✅").queue();
    }

    private void cmd_list(@NotNull Message message, @NotNull List<String> args) {
        EmbedBuilder embed = new EmbedBuilder();
        StringBuilder s = new StringBuilder();
        for (Map.Entry<Module, Boolean> e: mdhelp.getModules().entrySet()) {
            s.append(String.format("**%s** - %s\n", e.getKey().getInfo().name,
                    (e.getValue() ? "enabled":"disabled")));
        }
        embed.setDescription(s);
        embed.setTitle("Loaded modules");
        if (mdhelp.getModules().keySet().size() == 0) {
            embed.addField("No modules found.", "", false);
        }
        message.reply(embed.build()).mentionRepliedUser(false).queue();
    }
}
