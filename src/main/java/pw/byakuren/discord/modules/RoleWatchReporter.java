package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.util.BotEmbed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoleWatchReporter implements Module {

    private Cache c;

    public RoleWatchReporter(Cache c) {
        this.c = c;
    }

    @Override
    public void run(Message message) {
        if (message.getAuthor().isBot()) return;
        ServerCache sc = c.getServerCache(message.getGuild());
        if (sc.channelIsExcluded(message.getTextChannel())) return;
        List<Role> roles = new ArrayList<>();
        for (Role r : message.getMentionedRoles()) {
            if (sc.roleIsWatched(r)) {
                roles.add(r);
            }
        }
        if (roles.size() > 0) {
            alert(message, roles);
        }
    }

    @Override
    public void run(CommandHelper cmdhelp) {

    }

    @Override
    public void run(Event event) {

    }

    private void alert(Message m, List<Role> rs) {
        TextChannel lc = c.getServerCache(m.getGuild()).getLogChannel(m.getJDA());
        if (lc == null) return;
        EmbedBuilder b = BotEmbed.headerAuthor("Watched role" + (rs.size() > 1 ? "s" : "") + " pinged",
                m.getMember().getUser())
                .setDescription(m.getContentRaw())
                .setTimestamp(LocalDateTime.now())
                .setFooter("#" + m.getChannel().getName(), null);
        lc.sendMessage(b.build()).queue();
    }

    @Override
    public ModuleInfo getInfo() {
        return new ModuleInfo("RoleWatchReporter", "Brod8362", "d", ModuleType.MESSAGE_MODULE);
    }
}
