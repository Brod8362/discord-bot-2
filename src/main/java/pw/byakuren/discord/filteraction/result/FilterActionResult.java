package pw.byakuren.discord.filteraction.result;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;
import pw.byakuren.discord.util.BotEmbed;
import pw.byakuren.discord.util.ScalaReplacements;

import java.time.OffsetDateTime;
import java.util.List;

public class FilterActionResult {

    public final MessageFilterAction mfa;
    public final OffsetDateTime time = OffsetDateTime.now();
    public final Message message;
    public final boolean activated;
    public final List<FilterResult> results;

    public FilterActionResult(MessageFilterAction messageFilterAction, Message message, boolean activated, List<FilterResult> filterResults) {
        this.mfa = messageFilterAction;
        this.message = message;
        this.activated = activated;
        this.results = filterResults;
    }

    public MessageEmbed embedReport() {
        EmbedBuilder eb = BotEmbed.information(mfa.getName());
        eb.addField("Message Contents", message.getContentRaw(), false);
        eb.addField("Filter Results", ScalaReplacements.mkString(results,"\n"), false);
        eb.addField("Actions Taken", "haha not implemented yet!!", false);
        return eb.build();
    }
}
