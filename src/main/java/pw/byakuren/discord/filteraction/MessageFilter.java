package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.CacheEntry;
import pw.byakuren.discord.util.ScalaReplacements;

import java.util.Arrays;

public abstract class MessageFilter extends CacheEntry implements Filter<Message> {


    @Override
    protected void write(DatabaseManager dbmg) {

    }

    @Override
    protected void delete(DatabaseManager dbmg) {

    }

    public String getDisplay() {
        return String.format("%s(%s)", getRepresentation(), getArgumentsDisplay());
    }

    public String getArgumentsDisplay() {
        return ScalaReplacements.mkString(Arrays.asList(getArguments().clone()),",");
    }

    public String toString() {
        return getDisplay();
    }
}
