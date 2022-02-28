package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.datatypes.VoiceBan;
import pw.byakuren.discord.util.BotEmbed;

public class VoiceBanWatcher implements Module {

    private Cache c;

    public VoiceBanWatcher(Cache c) {
        this.c = c;
    }

    @Override
    public void run(@NotNull Message message) {
    }

    @Override
    public void run(@NotNull CommandHelper cmdhelp) {
    }

    @Override
    public void run(@NotNull Event event) {
        if (event instanceof GuildVoiceJoinEvent) {
            join_event((GuildVoiceJoinEvent) event);
        }
    }

    @Override
    public @NotNull ModuleInfo getInfo() {
        return new ModuleInfo("VoiceBanWatcher", "Brod8362", "d", ModuleType.EVENT_MODULE);
    }

    private void join_event(@NotNull GuildVoiceJoinEvent ev) {
        Member m = ev.getMember();
        VoiceBan vb = c.getServerCache(ev.getGuild()).getValidVoiceBan(m);
        if (m.getUser().isBot() || vb == null) return;
        try {
            ev.getGuild().kickVoiceMember(m).queue();
        } catch (InsufficientPermissionException e) {
            final TextChannel logChannel = c.getServerCache(ev.getGuild()).getLogChannel(ev.getJDA());
            if (logChannel == null) return;

            logChannel.sendMessage(
                    "Tried to disconnect banned voice user " + m.getAsMention() + ", but missing permission " + e.getPermission())
                    .queue();
            return;
        }

        PrivateChannel ch = m.getUser().openPrivateChannel().complete();
        EmbedBuilder b = BotEmbed.neutral("Voice banned")
                .setDescription(String.format("You were removed from #%s because you were voice banned by <@%d>.\nReason: `%s`",
                        ev.getChannelJoined().getName(), vb.getModId(),
                        (vb.getReason() != null ? vb.getReason() : "<None provided>"))
                )
                .setFooter("Voice ban will expire at")
                .setTimestamp(vb.getExpireTime());
        ch.sendMessage(b.build()).queue();
    }
}
