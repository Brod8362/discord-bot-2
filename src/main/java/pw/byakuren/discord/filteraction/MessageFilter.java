package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.util.ScalaReplacements;

import java.io.Serializable;
import java.util.Arrays;

public abstract class MessageFilter implements Filter<Message>, Serializable {

    public boolean inverted = false;

    public @NotNull String getDisplay() {
        return String.format("%s%s(%s)", isInverted() ? "!":"", getName(), getArgumentsDisplay());
    }

    public @NotNull String getArgumentsDisplay() {
        return ScalaReplacements.mkString(Arrays.asList(getArguments().clone()),",");
    }

    public @NotNull String toString() {
        return getDisplay();
    }

    protected abstract @NotNull MessageFilter parseFromString(String s);

    public final @NotNull MessageFilter fromString(@NotNull String s) {
        return parseFromString(s.substring(getName().length()+1, s.lastIndexOf(")")));
    }

    abstract public @NotNull Argument @NotNull [] getExpectedArguments();

    @Override
    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
