package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

import java.util.regex.Pattern;

public class RegexFilter extends MessageFilter {

    private final Pattern pattern;

    public RegexFilter(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String getName() {
        return "matchesRegex";
    }

    @Override
    public String[] getArguments() {
        return new String[]{pattern.pattern()};
    }

    @Override
    public String getArgumentsDisplay() {
        return pattern.pattern();
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        Pattern.compile(s); //this will throw an exception that will be caught if the regex is invalid
        return new RegexFilter(s);
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("regex",  ArgumentType.STRING, "regex to match against")};
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean trigger = pattern.matcher(obj.getContentRaw()).find();
        String reason = trigger ? null : "the message does not match the regex `"+pattern.pattern()+"`";
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }
}
