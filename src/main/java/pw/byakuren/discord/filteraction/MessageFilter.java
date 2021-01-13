package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.util.ScalaReplacements;

import java.io.Serializable;
import java.util.Arrays;

public abstract class MessageFilter implements Filter<Message>, Serializable {

    public boolean inverted = false;

    public String getDisplay() {
        return String.format("%s%s(%s)", isInverted() ? "!":"", getName(), getArgumentsDisplay());
    }

    public String getArgumentsDisplay() {
        return ScalaReplacements.mkString(Arrays.asList(getArguments().clone()),",");
    }

    public String toString() {
        return getDisplay();
    }

    protected abstract MessageFilter parseFromString(String s);

    public final MessageFilter fromString(String s) {
        return parseFromString(s.substring(getName().length()+1, s.lastIndexOf(")")));
    }

    abstract public Argument[] getExpectedArguments();

    @Override
    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
