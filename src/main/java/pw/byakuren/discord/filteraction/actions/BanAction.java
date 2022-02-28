package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

import java.util.Objects;

public class BanAction extends MessageAction {

    private final int delDays;

    public BanAction(int delDays) {
        this.delDays = delDays;
    }

    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        Exception ex = null;
        try {
            final Member member = obj.getMember();
            if (member == null) throw new RuntimeException("Member not found");
            member.ban(delDays).complete();
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(getName(), ex);
    }

    @Override
    public @NotNull String getName() {
        return "ban";
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("days", ArgumentType.NUMBER, "number of days of messages to delete")};
    }

    @Override
    protected @NotNull String @NotNull [] getArguments() {
        return new String[]{delDays+""};
    }

    @Override
    protected @NotNull MessageAction parseFromString(@NotNull String s) {
        return new BanAction(Integer.parseInt(s));
    }
}
