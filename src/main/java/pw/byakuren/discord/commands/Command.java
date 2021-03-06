package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    protected String[] names;
    protected CommandPermission minimum_permission;
    protected String syntax;
    protected String help;
    protected List<Subcommand> subcommands = new ArrayList<>();
    protected OptionData[] parameters = new OptionData[]{};

    public Command() { }

    public final String[] getNames() {
        return names;
    }

    public final String getPrimaryName() {
        assert names != null;
        return names[0];
    }

    public final String getSyntax() {
        return syntax;
    }

    public final String getHelp() {
        return help;
    }

    public final String getTypeAbbreviation() {
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

    public final boolean canRun(Member m, Cache c) {
         return CommandPermission.getPermission(m, c).ordinal() >= minimumPermission().ordinal();
    }

    private Subcommand getDefaultCommand() {
        assert subcommands != null;
        if (subcommands.size() == 0) {
            throw new UnsupportedOperationException("Command has no subcommands.");
        }
        return subcommands.get(0);
    }

    public final Subcommand getSubcommand(String s) {
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

    public List<Subcommand> getSubcommands() {
        return subcommands;
    }

    public final OptionData[] getParameters() {
        return parameters;
    }

    public void run(Message message, List<String> args) {
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

    public CommandType getType() {
        return CommandType.TRADITIONAL;
    }

}
