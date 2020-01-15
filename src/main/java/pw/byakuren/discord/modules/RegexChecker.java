package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.RegexKey;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;

public class RegexChecker implements Module {

    Cache c;

    public RegexChecker(Cache c) {
        this.c = c;
    }

    @Override
    public void run(Message message) {
        if (message.getAuthor().isBot()) return;
        ServerCache sc = c.getServerCache(message.getGuild());
        if (sc.channelIsExcluded(message.getTextChannel())) return;
        List<RegexKey> keys = sc.getAllValidRegexKeys();
        if (keys.size() == 0) return;
        String highlighted = message.getContentRaw();
        Matcher m = keys.get(0).getPattern().matcher(message.getContentRaw());
        int matched_count = 0;
        for (RegexKey k : keys) {
            m.usePattern(k.getPattern());
            if (m.matches()) {
                matched_count++;
                highlighted = highlighted.replace(m.group(), String.format("__%s__", m.group()));
            }
        }

        if (matched_count == 0) return;

        TextChannel log = sc.getLogChannel(message.getJDA());
        if (log == null) {
            message.getGuild().getOwner().getUser().openPrivateChannel().complete()
                    .sendMessageFormat("Tried to log a naughty user but you didn't set your log channel in %s!",
                            message.getGuild().getName()).queue();
        }
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Message matches for keys");
        b.setDescription(String.format("[Jump](%s)\n%s", message.getJumpUrl(), message.getContentDisplay()));
        b.setColor(Color.RED);
        b.setAuthor(message.getAuthor().getName(), message.getJumpUrl(), message.getAuthor().getAvatarUrl());
        b.setTimestamp(LocalDateTime.now());
        b.setFooter("#" + message.getTextChannel().getName(), null);
        TextChannel lc = sc.getLogChannel(message.getJDA());
        lc.sendMessage(b.build()).queue();
    }


    @Override
    public void run(CommandHelper cmdhelp) {

    }

    @Override
    public void run(Event event) {

    }

    @Override
    public ModuleInfo getInfo() {
        return new ModuleInfo("RegexChecker", "Brod8362", "d", ModuleType.MESSAGE_MODULE);
    }
}
