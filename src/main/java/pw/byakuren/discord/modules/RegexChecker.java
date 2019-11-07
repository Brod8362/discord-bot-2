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
import java.util.*;
import java.util.List;

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
        Set<String> matches = new HashSet<>();
        Set<String> str_matches = new HashSet<>();
        for (RegexKey k: keys) //match single words
            for (String s: message.getContentDisplay().split(" "))
                if (s.matches(k.getKey())) {
                    matches.add(k.getKey());
                    str_matches.add(s);
                }
        StringBuilder highlighted = new StringBuilder();
        for (String s: message.getContentDisplay().split(" ")) {
            if (str_matches.contains(s)) {
                highlighted.append("__").append(s).append("__ ");
            } else {
                highlighted.append(s).append(" ");
            }
        }
        if (matches.size() > 0) {
            TextChannel log = sc.getLogChannel(message.getJDA());
            if (log==null) {
                message.getGuild().getOwner().getUser().openPrivateChannel().complete()
                        .sendMessageFormat("Tried to log a naughty user but you didn't set your log channel in %s!", message.getGuild().getName())
                        .queue();
            }
            EmbedBuilder b = new EmbedBuilder();
            b.setTitle("Message matches for keys");
            b.setDescription(String.format("[Jump](%s)\n%s\n**Matched**: `%s`",
                    message.getJumpUrl(), highlighted,
                    String.join("`, `", matches)));
            b.setColor(Color.RED);
            b.setAuthor(message.getAuthor().getName(), message.getJumpUrl(), message.getAuthor().getAvatarUrl());
            b.setFooter(new Date().toString(), null);
            TextChannel lc = sc.getLogChannel(message.getJDA());
            lc.sendMessage(b.build()).queue();
        }
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
