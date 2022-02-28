package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;

import java.util.Objects;

public class KickAction extends MessageAction {
    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        Exception ex = null;
        try {
            final Member member = obj.getMember();
            if (member == null) throw new RuntimeException("Member not found");
            member.kick().complete();
        } catch (Exception e) {
            ex = e;
        }
        return new ActionResult(getName(), ex);
    }

    @Override
    public @NotNull String getName() {
        return "kick";
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[0];
    }

    @Override
    protected @NotNull String @NotNull [] getArguments() {
        return new String[0];
    }

    @Override
    protected @NotNull MessageAction parseFromString(@NotNull String s) {
        return new KickAction();
    }
}
