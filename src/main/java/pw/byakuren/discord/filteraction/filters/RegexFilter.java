package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class RegexFilter extends MessageFilter {

    private final String pattern;

    public RegexFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getName() {
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
    protected MessageFilter parseFromString(String s) {
        return new RegexFilter(s);
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("regex",  ArgumentType.STRING, "regex to match against")};
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean trigger = obj.getContentRaw().matches(pattern);
        String reason = trigger ? null : "the message does not match the regex `"+pattern+"`";
        return new FilterResult(trigger, getDisplay(), reason);
    }
}
