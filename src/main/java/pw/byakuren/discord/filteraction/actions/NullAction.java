package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class NullAction extends MessageAction {
    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        return new ActionResult(getDisplay(), null);
    }

    @Override
    public @NotNull String getName() {
        return "nothing";
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
        return new NullAction();
    }
}
