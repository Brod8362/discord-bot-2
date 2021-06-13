package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.datatypes.UserStats;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;

public class UserInfo extends RichCommand {

    Cache c;

    public UserInfo(Cache c) {
        names = new String[]{"userinfo", "uinfo", "ui"};
        help = "Find info about a user.";
        minimum_permission = CommandPermission.REGULAR_USER;
        parameters = new OptionData[]{new OptionData(OptionType.USER, "user", "The user to find information about", false)};
        type = CommandType.INTEGRATED;
        this.c = c;
    }

    private MessageEmbed buildUserEmbed(Member u) {
        EmbedBuilder embed = BotEmbed.neutral(String.format("%s#%s", u.getUser().getName(), u.getUser().getDiscriminator()))
                .setThumbnail(u.getUser().getAvatarUrl())
                .addField("Nickname", u.getEffectiveName(), true)
                .addField("ID", u.getUser().getId(), true)
                .addField("Joined At", u.getTimeJoined().toString(), true)
                .addField("Account Creation Date", u.getUser().getTimeCreated().toString(), true);
        UserStats stats = c.getServerCache(u.getGuild()).getStatsForUser(u);
        if (stats != null) {
            for (Statistic s : Statistic.values()) {
                embed.addField(s.nice_name, stats.getStatistic(s) + "", true);
            }
        } else {
            embed.setFooter("User Statistics not available.", null);
        }
        return embed.build();
    }

    @Override
    public void run(Message message, List<String> args) {
        Member u = message.getMember();
        if (message.getMentionedMembers().size() != 0) {
            u = message.getMentionedMembers().get(0);
        }

        message.reply(buildUserEmbed(u)).mentionRepliedUser(false).queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        //no buttons supported
    }

    @Override
    public void runSlash(SlashCommandEvent event) {
        Member m;
        try {
            m = event.getOption("user").getAsMember();
        } catch (Exception e) {
            m = null;
        }
        if (m == null) {
            m = event.getMember();
        }
        event.replyEmbeds(buildUserEmbed(m)).queue();
    }
}
