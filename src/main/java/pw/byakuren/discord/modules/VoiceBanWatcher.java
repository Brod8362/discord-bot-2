package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.datatypes.VoiceBan;

public class VoiceBanWatcher implements Module {

    private Cache c;

    public VoiceBanWatcher(Cache c) {
        this.c = c;
    }

    @Override
    public void run(Message message) {}

    @Override
    public void run(CommandHelper cmdhelp) {}

    @Override
    public void run(Event event) {
        if (event instanceof GuildVoiceJoinEvent) {
            join_event((GuildVoiceJoinEvent) event);
        }
    }

    @Override
    public ModuleInfo getInfo() {
        return new ModuleInfo("VoiceBanWatcher", "Brod8362", "d", ModuleType.EVENT_MODULE);
    }

    private void join_event(GuildVoiceJoinEvent ev) {
        Member m = ev.getMember();
        if (m.getUser().isBot() || !check_banned(m)) return;
        try {
            ev.getGuild().kickVoiceMember(m).queue();
        } catch (InsufficientPermissionException e) {
            c.getServerCache(ev.getGuild()).getLogChannel(ev.getJDA()).sendMessage(
                    "Tried to disconnect banned voice user "+m.getAsMention()+", but missing permission "+e.getPermission())
                    .queue();
            return;
        }

        PrivateChannel ch = m.getUser().openPrivateChannel().complete();
        VoiceBan vb = c.getServerCache(ev.getGuild()).getValidVoiceBan(m);
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Voice banned");
        b.setDescription(String.format("You were removed from #%s because you were voice banned by <@%d>.\nReason: `%s`",
                ev.getChannelJoined().getName(), vb.getModId(),
                (vb.getReason() != null ? vb.getReason() : "<None provided>"))
                );
        b.setFooter("Voice ban will expire at");
        b.setTimestamp(vb.getExpireTime());
        ch.sendMessage(b.build()).queue();
    }

    private boolean check_banned(Member m) {
        return c.getServerCache(m.getGuild()).userIsVoiceBanned(m);
    }
}
