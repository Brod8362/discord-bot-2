package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;

public class ServerInfo extends RichCommand {
    private @NotNull MessageEmbed buildEmbed(@NotNull Guild s) {
        final TextChannel defaultChannel = s.getDefaultChannel();
        EmbedBuilder eb = BotEmbed.neutral(s.getName())
                .setThumbnail(s.getIconUrl())
                .addField("Members", Integer.toString(s.getMembers().size()), true) //todo deal with loading members for accurate counts
                .addField("Channel Count", Integer.toString(s.getChannels().size()), true)
                .addField("Role Count", Integer.toString(s.getRoles().size()), true)
                .addField("Default Channel", defaultChannel == null ? "<none>" : defaultChannel.getAsMention(), true);

        final Member owner = s.getOwner();
        if (owner != null) {
            eb.addField("Owner", owner.getAsMention(), true);
        }

        return eb.build();
    }

    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"serverinfo", "sinfo", "si"};
    }

    @Override
    public @NotNull String getHelp() {
        return "Find info about the current server";
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return CommandPermission.REGULAR_USER;
    }

    @Override
    public @NotNull CommandType getType() {
        return CommandType.INTEGRATED;
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        message.reply(buildEmbed(message.getGuild())).mentionRepliedUser(false).queue();
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        //no buttons
    }

    @Override
    public void runSlash(@NotNull SlashCommandEvent event) {
        InteractionHook ih = event.deferReply().complete();
        final Guild guild = event.getGuild();
        if (guild == null) {
            ih.editOriginalEmbeds(BotEmbed.bad("This command must be run from within a server.").build()).queue();
        } else {
            ih.editOriginalEmbeds(buildEmbed(guild)).queue();
        }
    }
}
