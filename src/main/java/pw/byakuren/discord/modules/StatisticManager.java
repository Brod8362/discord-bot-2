package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.LastMessage;
import pw.byakuren.discord.objects.cache.datatypes.UserStats;

import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class StatisticManager implements Module {

    private Cache c;

    public StatisticManager(Cache c) {
        this.c=c;
    }

    @Override
    public void run(Message message) {

    }

    @Override
    public void run(CommandHelper cmdhelp) {}

    @Override
    public void run(Event event) {
        if (event instanceof MessageReceivedEvent) {
            messageEvent(event);
        } else if (event instanceof MessageDeleteEvent) {
            messageDeleteEvent(event);
        } else if (event instanceof MessageReactionAddEvent) {
            messageReactionAddEvent(event);
        }
    }

    private void messageEvent(Event e) {
        MessageReceivedEvent ev = (MessageReceivedEvent) e;
        Message m = ev.getMessage();
        Guild g = m.getGuild();
        ServerCache sc = c.getServerCache(g);
        List<LastMessage> msgs = sc.getLastMessages().getData();
        List<UserStats> stats = sc.getUserStats().getData();
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
        for (UserStats stat : stats) {
            if (stat.getUser() == m.getAuthor().getIdLong()) {
                stat.incrementStatistic(Statistic.MESSAGES_SENT);
            }
        }
    }

    private void messageDeleteEvent(Event e) {
        MessageDeleteEvent ev = (MessageDeleteEvent) e;
        Message m = ev.getTextChannel().getMessageById(ev.getMessageId()).complete();
        if (m==null) return;
        Guild g = m.getGuild();
        ServerCache sc = c.getServerCache(g);
        List<UserStats> stats = sc.getUserStats().getData();
        for (UserStats stat : stats) {
            if (stat.getUser() == m.getAuthor().getIdLong()) {
                stat.incrementStatistic(Statistic.MESSAGES_DELETED);
            }
        }
    }

    private void messageReactionAddEvent(Event e) {
        MessageReactionAddEvent ev = (MessageReactionAddEvent) e;
        Message m = ev.getTextChannel().getMessageById(ev.getMessageId()).complete();
        Guild g = m.getGuild();
        ServerCache sc = c.getServerCache(g);
        List<LastMessage> msgs = sc.getLastMessages().getData();
        List<UserStats> stats = sc.getUserStats().getData();
        //increment reaction recv
        for (UserStats stat : stats) {
            if (stat.getUser() == m.getAuthor().getIdLong()) {
                stat.incrementStatistic(Statistic.REACTIONS_RECEIVED);
            } else if (stat.getUser() == ev.getMember().getUser().getIdLong()) {
                stat.incrementStatistic(Statistic.REACTIONS_SENT);
            }
        }
    }

    @Override
    public ModuleInfo getInfo() {
        return new ModuleInfo("StatisticManager", "Brod8362", "d", ModuleType.EVENT_MODULE);
    }

}
