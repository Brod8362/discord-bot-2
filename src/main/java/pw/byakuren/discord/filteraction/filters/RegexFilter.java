package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

import java.util.regex.Pattern;

public class RegexFilter extends MessageFilter {

    private final @NotNull Pattern pattern;

    public RegexFilter(@NotNull String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public @NotNull String getName() {
        return "matchesRegex";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[]{pattern.pattern()};
    }

    @Override
    public @NotNull String getArgumentsDisplay() {
        return pattern.pattern();
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new RegexFilter(s);
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("regex",  ArgumentType.STRING, "regex to match against")};
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean trigger = pattern.matcher(obj.getContentRaw()).find();
        String reason = trigger ? null : "the message does not match the regex `"+pattern.pattern()+"`";
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }
}
