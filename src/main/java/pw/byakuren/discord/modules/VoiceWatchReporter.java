package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.util.BotEmbed;

import java.time.LocalDateTime;

public class VoiceWatchReporter implements Module {

    private Cache c;

    public VoiceWatchReporter(Cache c) {
        this.c = c;
    }

    @Override
    public void run(Message message) {

    }

    @Override
    public void run(CommandHelper cmdhelp) {

    }

    @Override
    public void run(Event event) {
        if (event instanceof GuildVoiceJoinEvent)
            userJoinVoice((GuildVoiceJoinEvent) event);
        else if (event instanceof GuildVoiceLeaveEvent)
            userLeaveVoice((GuildVoiceLeaveEvent) event);
        else if (event instanceof GuildVoiceMoveEvent)
            userMove((GuildVoiceMoveEvent) event);
    }

    private void userJoinVoice(GuildVoiceJoinEvent e) {
        Member m = e.getMember();
        if (c.getServerCache(m.getGuild()).userIsWatched(m))
            reportUser(m, e.getChannelJoined(), true);
    }

    private void userLeaveVoice(GuildVoiceLeaveEvent e) {
        Member m = e.getMember();
        if (c.getServerCache(m.getGuild()).userIsWatched(m))
            reportUser(m, e.getChannelLeft(), false);
    }

    private void userMove(GuildVoiceMoveEvent e) {
        Member m = e.getMember();
        if (c.getServerCache(m.getGuild()).userIsWatched(m))
            reportUserMoved(m, e.getChannelLeft(), e.getChannelJoined());
    }

    private void reportUser(Member m, VoiceChannel chan, boolean joined) {
        TextChannel lc = c.getServerCache(m.getGuild()).getLogChannel(m.getJDA());
        EmbedBuilder b = BotEmbed.headerAuthor("Watched user voice activity", m.getUser())
                .setColor(joined ? BotEmbed.BAD_COLOR : BotEmbed.OK_COLOR)
                .setDescription("User " + (joined ? "joined" : "left") + " voice channel #" + chan.getName())
                .setTimestamp(LocalDateTime.now());
        lc.sendMessage(b.build()).queue();
    }

    private void reportUserMoved(Member m, VoiceChannel f, VoiceChannel t) {
        TextChannel lc = c.getServerCache(m.getGuild()).getLogChannel(m.getJDA());
        EmbedBuilder b = BotEmbed.headerAuthor("Watched user voice activity", m.getUser())
                .setColor(BotEmbed.BAD_COLOR)
                .setDescription("User moved from #" + f.getName() + " to #" + t.getName())
                .setTimestamp(LocalDateTime.now());
        lc.sendMessage(b.build()).queue();
    }

    @Override
    public ModuleInfo getInfo() {
        return new ModuleInfo("VoiceWatchReporter", "Brod8362", "d", ModuleType.EVENT_MODULE);
    }
}
