package pw.byakuren.discord.commands;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.commands.subcommands.Subcommand;

import java.util.*;

public class CommandHelper {

    private Map<String, Command> commands = new HashMap<>();
    private Set<Command> cmd_set = new HashSet<>();
    private Map<String, Command> button_ids = new HashMap<>();
    //todo

    public void registerCommand(JDA jda, Command cmd) {
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

    private void registerSlashCommand(JDA jda, RichCommand cmd) {
        //register all button IDs
        for (String id : cmd.requestedButtonIDs) {
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

        if (cmd.isGlobal() && cmd.minimum_permission == CommandPermission.REGULAR_USER) {
            jda.upsertCommand(cmd_data).queue();
        } else {
            for (Guild g : jda.getGuilds()) {
                g.upsertCommand(cmd_data).queue();
            }
        }
    }

    public Command getCommand(String n) {
        return commands.get(n);
    }

    public String getCommandHelp(String string) {
        String help = commands.get(string).getHelp();
        if (help != null) {
            return help;
        } else {
            return "No help defined.";
        }
    }

    public String getCommandSyntax(String string) {
        String syntax = commands.get(string).getSyntax();
        if (syntax != null) {
            return syntax;
        } else {
            return "No syntax defined.";
        }
    }

    public Command resolveButtonID(String string) {
        return button_ids.get(string);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public Set<Command> getCommandSet() {
        return cmd_set;
    }
}
