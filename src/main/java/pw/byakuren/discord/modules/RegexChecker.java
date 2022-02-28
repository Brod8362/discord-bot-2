package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.RegexKey;
import pw.byakuren.discord.util.BotEmbed;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class RegexChecker implements Module {

    private @NotNull Cache c;

    public RegexChecker(@NotNull Cache c) {
        this.c = c;
    }

    @Override
    public void run(@NotNull Message message) {
        if (message.getAuthor().isBot()) return;
        ServerCache sc = c.getServerCache(message.getGuild());
        if (sc.channelIsExcluded(message.getTextChannel())) return;
        List<RegexKey> keys = sc.getAllValidRegexKeys();
        if (keys.size() == 0) return;
        String highlighted = message.getContentRaw();
        Matcher m = keys.get(0).getPattern().matcher(message.getContentRaw());
        int matched_count = 0;
        for (RegexKey k : keys) {
            m.reset();
            m.usePattern(k.getPattern());
            if (m.find()) {
                matched_count++;
                highlighted = highlighted.replace(m.group(), String.format("__%s__", m.group()));
            }
        }

        if (matched_count == 0) return;

        TextChannel log = sc.getLogChannel(message.getJDA());
        if (log == null) {
            final Member owner = Objects.requireNonNull(message.getGuild().getOwner());
            owner.getUser().openPrivateChannel().complete()
                    .sendMessageFormat("Tried to log a naughty user but you didn't set your log channel in %s!",
                            message.getGuild().getName()).queue();
            return;
        }
        EmbedBuilder b = BotEmbed.headerAuthor("Message matched regex keys", message.getJumpUrl(),
                message.getAuthor(), BotEmbed.BAD_COLOR)
                .setDescription(String.format("[Jump](%s)\n%s", message.getJumpUrl(), highlighted))
                .setTimestamp(LocalDateTime.now())
                .setFooter("#" + message.getTextChannel().getName(), null);
        log.sendMessage(b.build()).queue();
    }


    @Override
    public void run(@NotNull CommandHelper cmdhelp) {

    }

    @Override
    public void run(@NotNull Event event) {

    }

    @Override
    public @NotNull ModuleInfo getInfo() {
        return new ModuleInfo("RegexChecker", "Brod8362", "d", ModuleType.MESSAGE_MODULE);
    }
}
