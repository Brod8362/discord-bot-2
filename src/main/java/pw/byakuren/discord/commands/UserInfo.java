package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.datatypes.UserStats;

import java.util.List;

public class UserInfo implements Command {

    Cache c;

    public UserInfo(Cache c) {
        this.c = c;
    }

    @Override
    public String[] getNames() {
        return new String[]{"userinfo", "uinfo", "ui"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Find info about a user.";
    }

    @Override
    public boolean needsBotOwner() {
        return false;
    }

    @Override
    public void run(Message message, List<String> args) {
        Member u;
        if (message.getMentionedMembers().size() == 0) {
            u = message.getMember();
        } else {
            u = message.getMentionedMembers().get(0);
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(String.format("%s#%s", u.getUser().getName(), u.getUser().getDiscriminator()));
        embed.setThumbnail(u.getUser().getAvatarUrl());
        embed.addField("Nickname", u.getNickname(), true);
        embed.addField("ID", u.getUser().getId(), true);
        embed.addField("Joined At", u.getTimeJoined().toString(), true);
        embed.addField("Account Creation Date", u.getUser().getTimeCreated().toString(), true);
        UserStats stats = c.getServerCache(u.getGuild()).getStatsForUser(u);
        if (stats != null) {
            for (Statistic s: Statistic.values()) {
                embed.addField(s.nice_name, stats.getStatistic(s)+"", true);
            }
        } else {
            embed.setFooter("User Statistics not available.", null);
        }
        message.getChannel().sendMessage(embed.build()).queue();
    }
}
