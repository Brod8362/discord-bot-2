package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.util.ScalaReplacements;

import java.util.Arrays;

public abstract class MessageAction extends CacheEntry implements Action<Message> {

    public abstract String getName();

    /**
     * This method should return a string that can be parsed back into another Action with identical behavior,
     * e.g. passing the output of getDisplay() into parseFromString() should work and behave the same.
     * @return string representation of this action
     */
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
    protected void write(DatabaseManager dbmg) {
        //TODO implement
    }

    @Override
    protected void delete(DatabaseManager dbmg) {
        //TODO implement
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
