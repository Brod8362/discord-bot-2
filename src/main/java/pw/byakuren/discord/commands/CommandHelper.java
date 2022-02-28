package pw.byakuren.discord.commands;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.commands.subcommands.Subcommand;

import java.util.*;

public class CommandHelper {

    private final @NotNull Map<String, Command> commands = new HashMap<>();
    private final @NotNull Set<Command> cmd_set = new HashSet<>();
    private final @NotNull Map<String, Command> button_ids = new HashMap<>();

    public void registerCommand(@NotNull JDA jda, @NotNull Command cmd) {
        for (String s : cmd.getNames()) {
            Command c = commands.get(s);
            if (c == null) {
                commands.put(s, cmd);
                if (cmd instanceof RichCommand && ((RichCommand) cmd).isSlash()) {
                    registerSlashCommand(jda, (RichCommand) cmd);
                }
            } else {
                throw new RuntimeException(String.format("Command alias %s conflicts with alias for command %s", s, c.getNames()[0]));
            }
        }
        cmd_set.add(cmd);
    }

    private void registerSlashCommand(@NotNull JDA jda, @NotNull RichCommand cmd) {
        //register all button IDs
        for (String id : cmd.getRequestedButtonIDs()) {
            if (button_ids.containsKey(id)) {
                throw new RuntimeException(
                        String.format("Duplicate button id %s attempted to be registered by %s, already in use by %s",
                                id, cmd.getPrimaryName(), button_ids.get(id).getPrimaryName()));
            } else {
                button_ids.put(id, cmd);
            }
        }

        CommandData cmd_data = new CommandData(cmd.getPrimaryName(), cmd.getHelp());
        if (cmd.getParameters().length > 0 && cmd.getSubcommands().size() > 0) {
            System.out.println("CONFLICT: command '" + cmd.getPrimaryName() +
                    "'cannot have both subcommands and parameters to be compatible with the slash command system." +
                    "This command will not be registered as a slash command.");
            return;
        } else if (cmd.getParameters().length > 0) {
            cmd_data.addOptions(cmd.getParameters());
        } else if (cmd.getSubcommands().size() > 0) {
            List<SubcommandData> subcmd_data = new ArrayList<>();
            for (Subcommand sc : cmd.getSubcommands()) {
                subcmd_data.add(new SubcommandData(sc.getPrimaryName(), sc.getHelp()).addOptions(sc.getParameters()));
            }
            cmd_data.addSubcommands(subcmd_data);
        }

        if (cmd.isGlobal() && cmd.minimumPermission() == CommandPermission.REGULAR_USER) {
            jda.upsertCommand(cmd_data).queue();
        } else {
            for (Guild g : jda.getGuilds()) {
                g.upsertCommand(cmd_data).queue();
            }
        }
    }

    public @Nullable Command getCommand(@NotNull String n) {
        return commands.get(n);
    }

    public @NotNull String getCommandHelp(@NotNull String string) {
        return commands.get(string).getHelp();
    }

    public @NotNull String getCommandSyntax(@NotNull String string) {
        return commands.get(string).getSyntax();
    }

    public @Nullable Command resolveButtonID(@NotNull String string) {
        return button_ids.get(string);
    }

    public @NotNull Map<String, Command> getCommands() {
        return commands;
    }

    public @NotNull Set<Command> getCommandSet() {
        return cmd_set;
    }

    public @NotNull Map<String, Command> getButtonIDs() { return button_ids; }
}
