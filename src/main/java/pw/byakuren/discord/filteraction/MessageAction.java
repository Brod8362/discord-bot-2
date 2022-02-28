package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.util.ScalaReplacements;

import java.io.Serializable;
import java.util.Arrays;

public abstract class MessageAction implements Action<Message>, Serializable {

    public @NotNull String getDisplay() {
        return String.format("%s<%s>", getName(), getArgumentsDisplay());
    }

    private @NotNull String getArgumentsDisplay() {
        return ScalaReplacements.mkString(Arrays.asList(getArguments().clone()), ",");
    }

    public abstract @NotNull Argument @NotNull [] getExpectedArguments();

    protected abstract @NotNull String @NotNull [] getArguments();

    protected abstract @NotNull MessageAction parseFromString(@NotNull String s);

    public final @NotNull MessageAction fromString(@NotNull String s) {
        return parseFromString(s.substring(getName().length() + 1, s.lastIndexOf(">")));
    }

    @Override
    public @NotNull String toString() {
        return getDisplay();
    }
}
