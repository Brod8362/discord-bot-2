package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.util.ScalaReplacements;

import java.util.Arrays;

public abstract class MessageAction implements Action<Message> {

    public abstract String getName();

    public String getDisplay() {
        return String.format("%s<%s>", getName(), getArgumentsDisplay());
    }

    private String getArgumentsDisplay() {
        return ScalaReplacements.mkString(Arrays.asList(getArguments().clone()), ",");
    }

    public abstract Argument[] getExpectedArguments();

    protected abstract String[] getArguments();

    protected abstract MessageAction parseFromString(String s);

    public final MessageAction fromString(String s) {
        return parseFromString(s.substring(getName().length() + 1, s.lastIndexOf(">")));
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
