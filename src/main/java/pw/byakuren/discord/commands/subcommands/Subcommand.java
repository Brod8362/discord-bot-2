package pw.byakuren.discord.commands.subcommands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.commands.Command;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Subcommand extends Command {
    private final @NotNull String @NotNull [] names;
    private final @Nullable String help;
    private final @Nullable String syntax;
    private final CommandPermission permission;

    public Subcommand(@NotNull String @NotNull [] names, @Nullable String help, @Nullable String syntax, @NotNull Command c) {
        this.names = names;
        this.help = help;
        this.syntax = syntax;
        this.permission = c.minimumPermission();
    }

    @Override
    public @NotNull String @NotNull [] getNames() {
        return names;
    }

    @Override
    public @NotNull String getSyntax() {
        return syntax == null ? super.getSyntax() : syntax;
    }

    @Override
    public @NotNull String getHelp() {
        return help == null ? super.getHelp() : help;
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return permission;
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
