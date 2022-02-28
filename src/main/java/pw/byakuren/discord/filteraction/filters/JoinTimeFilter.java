package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

import java.time.OffsetDateTime;

public class JoinTimeFilter extends MessageFilter {

    private final int minutes;

    public JoinTimeFilter(int hours) {
        this.minutes = hours;
    }

    @Override
    public @NotNull String getName() {
        return "hereFor";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[]{minutes+""};
    }

    @Override
    public @NotNull String getArgumentsDisplay() {
        return minutes+"";
    }

    @Override
    protected @NotNull MessageFilter parseFromString(@NotNull String s) {
        return new JoinTimeFilter(Integer.parseInt(s));
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("minutes", ArgumentType.NUMBER, "minutes since the user joined")};
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean trigger = false;
        Member m = obj.getMember();
        if (m != null) {
            trigger = (OffsetDateTime.now().toEpochSecond()-m.getTimeJoined().toEpochSecond())/(60) <= minutes;
        }
        String reason = trigger ? null : String.format("the user has been here for more than %d minutes", minutes);
        return new FilterResult(trigger, inverted,  getDisplay(), reason);
    }
}
