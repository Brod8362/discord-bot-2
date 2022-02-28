package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    protected @NotNull String @NotNull [] names;
    protected @NotNull CommandPermission minimum_permission;
    protected @NotNull String syntax;
    protected @NotNull String help;
    protected @NotNull List<Subcommand> subcommands = new ArrayList<>();
    protected @NotNull OptionData @NotNull [] parameters = new OptionData[]{};

    public Command() { }

    public final String[] getNames() {
        return names;
    }

    public final String getPrimaryName() {
        assert names != null;
        return names[0];
    }

    public final @NotNull String getSyntax() {
        return syntax;
    }

    public final @NotNull String getHelp() {
        return help;
    }

    public final @NotNull String getTypeAbbreviation() {
        String abbrev = "?";
        switch (getType()) {
            case INTEGRATED:
                abbrev = "T|S";
                break;
            case SLASH:
                abbrev = "S";
                break;
            case TRADITIONAL:
                abbrev = "T";
                break;
            case SUBCOMMAND:
                abbrev = "-";
                break;
        }
        return abbrev;
    }

    public final CommandPermission minimumPermission() {
        return minimum_permission;
    }

    public final boolean canRun(@NotNull Member m, @NotNull Cache c) {
         return CommandPermission.getPermission(m, c).ordinal() >= minimumPermission().ordinal();
    }

    private @NotNull Subcommand getDefaultCommand() {
        assert subcommands != null;
        if (subcommands.size() == 0) {
            throw new UnsupportedOperationException("Command has no subcommands.");
        }
        return subcommands.get(0);
    }

    public final @Nullable Subcommand getSubcommand(@NotNull String s) {
        assert subcommands != null;
        for (Subcommand c : subcommands) {
            for (String n : c.getNames()) {
                if (s.equals(n)) {
                    return c;
                }
            }
        }
        return null;
    }

    public @NotNull List<Subcommand> getSubcommands() {
        return subcommands;
    }

    public final @NotNull OptionData @NotNull [] getParameters() {
        return parameters;
    }

    public void run(@NotNull Message message, @NotNull List<String> args) {
        if (subcommands.size() == 0) {
            message.reply("big ouchie: this command cannot be run, it's not implemented yet.").mentionRepliedUser(false).queue();
            return;
        }
        if (args.size() == 0){
            getDefaultCommand().run(message, args);
            return;
        }
        List<String> nargs = args.subList(1, args.size());
        Subcommand sc = getSubcommand(args.get(0));
        if ( sc == null ) {
            getDefaultCommand().run(message,args);
            return;
        }
        sc.run(message, nargs);
    }

    public @NotNull CommandType getType() {
        return CommandType.TRADITIONAL;
    }

}
