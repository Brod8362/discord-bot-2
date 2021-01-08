package pw.byakuren.discord.filteraction;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;

import java.time.OffsetDateTime;
import java.util.List;

public class FilterActionResult {

    public final MessageFilterAction messageFilterAction;
    public final OffsetDateTime time = OffsetDateTime.now();
    public final Message message;
    public final boolean activated;
    public final List<Filter<Message>> filtersMet;

    public FilterActionResult(MessageFilterAction messageFilterAction, Message message, boolean activated, List<Filter<Message>> filtersMet) {
        this.messageFilterAction = messageFilterAction;
        this.message = message;
        this.activated = activated;
        this.filtersMet = filtersMet;
    }
}
