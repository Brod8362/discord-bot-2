package pw.byakuren.discord.commands.subcommands;

import pw.byakuren.discord.commands.Command;
import pw.byakuren.discord.commands.permissions.CommandPermission;

public abstract class Subcommand extends Command {

    public Subcommand(String[] names, String help, String syntax, Command c) {
        this.names=names;
        this.help=help;
        this.syntax=syntax;
        this.minimum_permission=c.minimumPermission();
    }

}
