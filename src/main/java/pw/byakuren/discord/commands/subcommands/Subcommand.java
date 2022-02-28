package pw.byakuren.discord.commands.subcommands;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.Command;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Subcommand extends Command {

    public Subcommand(@NotNull String @NotNull [] names, @NotNull String help, @NotNull String syntax, @NotNull Command c) {
        this.names=names;
        this.help=help;
        this.syntax=syntax;
        this.minimum_permission=c.minimumPermission();
    }


    //done so subcommands cannot have more subcommands, to better conform to the slash command system
    @Override
    public final @NotNull List<Subcommand> getSubcommands() {
        return Collections.emptyList();
    }

    @Override
    public final @NotNull CommandType getType() {
        return CommandType.SUBCOMMAND;
    }
}
