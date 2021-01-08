package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;

public class RegexFilter extends MessageFilter {

    private final String pattern;

    public RegexFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getRepresentation() {
        return "matchesRegex";
    }

    @Override
    public String[] getArguments() {
        return new String[]{pattern};
    }

    @Override
    public String getArgumentsDisplay() {
        return pattern;
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("regex",  ArgumentType.STRING, "regex to match against")};
    }

    @Override
    public boolean apply(Message obj) {
        return obj.getContentRaw().matches(pattern);
    }
}