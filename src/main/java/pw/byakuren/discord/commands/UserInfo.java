package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.objects.Statistic;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.datatypes.UserStats;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;

public class UserInfo extends RichCommand {

    private final @NotNull Cache c;

    public UserInfo(@NotNull Cache c) {
        this.c = c;
    }

    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"userinfo", "uinfo", "ui"};
    }

    @Override
    public @NotNull String getHelp() {
        return "Find info about a user.";
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return CommandPermission.REGULAR_USER;
    }

    @Override
    public @NotNull OptionData @NotNull [] getParameters() {
        return new OptionData[]{new OptionData(OptionType.USER, "user", "The user to find information about", false)};
    }

    @Override
    public @NotNull CommandType getType() {
        return CommandType.INTEGRATED;
    }

    private @NotNull MessageEmbed buildUserEmbed(@NotNull Member u) {
        EmbedBuilder embed = BotEmbed.neutral(String.format("%s#%s", u.getUser().getName(), u.getUser().getDiscriminator()))
                .setThumbnail(u.getUser().getAvatarUrl())
                .addField("Nickname", u.getEffectiveName(), true)
                .addField("ID", u.getUser().getId(), true)
                .addField("Joined At", u.getTimeJoined().toString(), true)
                .addField("Account Creation Date", u.getUser().getTimeCreated().toString(), true);
        UserStats stats = c.getServerCache(u.getGuild()).getStatsForUser(u);
        for (Statistic s : Statistic.values()) {
            embed.addField(s.nice_name, stats.getStatistic(s) + "", true);
        }
        return embed.build();
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        Member u = message.getMember();
        if (message.getMentionedMembers().size() != 0) {
            u = message.getMentionedMembers().get(0);
        }

        if (u == null) {
            message.reply(BotEmbed.bad("Could not find the requested user.").build()).mentionRepliedUser(false).queue();
        } else {
            message.reply(buildUserEmbed(u)).mentionRepliedUser(false).queue();
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        //no buttons supported
    }

    @Override
    public void runSlash(@NotNull SlashCommandEvent event) {
        final OptionMapping userOpt = event.getOption("user");
        Member m = userOpt == null ? null : userOpt.getAsMember();
        if (m == null) {
            m = event.getMember();
        }
        if (m == null) {
            event.replyEmbeds(BotEmbed.bad("This command must be run from within a server.").build()).queue();
        } else {
            event.replyEmbeds(buildUserEmbed(m)).queue();
        }
    }
}
