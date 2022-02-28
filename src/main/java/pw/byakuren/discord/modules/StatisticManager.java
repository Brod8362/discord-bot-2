package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.Triple;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.LastMessage;

import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class StatisticManager implements Module {

    private Cache c;

    public StatisticManager(Cache c) {
        this.c=c;
    }

    @Override
    public void run(@NotNull Message message) {

    }

    @Override
    public void run(@NotNull CommandHelper cmdhelp) {}

    @Override
    public void run(@NotNull Event event) {
        if (event instanceof MessageReceivedEvent) {
            messageEvent(event);
        } else if (event instanceof MessageDeleteEvent) {
            messageDeleteEvent(event);
        } else if (event instanceof MessageReactionAddEvent) {
            messageReactionAddEvent(event);
        }
    }

    private void messageEvent(@NotNull Event e) {
        MessageReceivedEvent ev = (MessageReceivedEvent) e;
        Message m = ev.getMessage();
        if (m.getAuthor().isBot()) return;
        Guild g = m.getGuild();
        ServerCache sc = c.getServerCache(g);
        List<LastMessage> msgs = sc.getLastMessages().getData();

        c.addMessageReference(m);

        //set user's last message
        //todo move to separate location
        for (int i = 0; i < msgs.size(); i++) {
            if (msgs.get(i).userid==m.getAuthor().getIdLong()) {
                LastMessage a = new LastMessage(m);
                a.write_state = PENDING_WRITE;
                msgs.set(i, new LastMessage(m));
                break;
            }
        }
        //increment sent msg count
        sc.getStatsForUser(m.getMember()).incrementStatistic(Statistic.MESSAGES_SENT);
        int c = m.getAttachments().size();
        for (int i =0; i < c; i++) {
            sc.getStatsForUser(m.getMember()).incrementStatistic(Statistic.ATTACHMENTS_SENT);
        }

    }

    private void messageDeleteEvent(Event e) {
        MessageDeleteEvent ev = (MessageDeleteEvent) e;
        Triple t = c.seeDeletedMessageAuthor(ev.getMessageIdLong());
        if (t == null ) return;
        ServerCache sc = c.getServerCache((long)t.b);
        sc.getStatsForUser((long)t.b, (long)t.c).incrementStatistic(Statistic.MESSAGES_DELETED);
    }

    private void messageReactionAddEvent(Event e) {
        MessageReactionAddEvent ev = (MessageReactionAddEvent) e;
        Message m = ev.getTextChannel().retrieveMessageById(ev.getMessageId()).complete();
        if (ev.getMember().getUser().isBot() || m.getAuthor().isBot()) return;
        Guild g = m.getGuild();
        ServerCache sc = c.getServerCache(g);
        //increment reaction recv
        sc.getStatsForUser(m.getMember()).incrementStatistic(Statistic.REACTIONS_RECEIVED);
        sc.getStatsForUser(ev.getMember()).incrementStatistic(Statistic.REACTIONS_SENT);
    }

    @Override
    public @NotNull ModuleInfo getInfo() {
        return new ModuleInfo("StatisticManager", "Brod8362", "d", ModuleType.EVENT_MODULE);
    }

}
