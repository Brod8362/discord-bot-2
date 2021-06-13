package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;

public class ServerInfo extends RichCommand {

    public ServerInfo() {
        names = new String[]{"serverinfo", "sinfo", "si"};
        help = "Find info about the current server";
        minimum_permission = CommandPermission.REGULAR_USER;
        type= CommandType.INTEGRATED;
    }

    private MessageEmbed buildEmbed(Guild s) {
        EmbedBuilder eb = BotEmbed.neutral(s.getName())
                .setThumbnail(s.getIconUrl())
                .addField("Members", Integer.toString(s.getMembers().size()), true) //todo deal with loading members for accurate counts
                .addField("Channel Count", Integer.toString(s.getChannels().size()), true)
                .addField("Role Count", Integer.toString(s.getRoles().size()), true)
                .addField("Default Channel", s.getDefaultChannel().getAsMention(), true);

        if (s.getOwner() != null) {
            eb.addField("Owner", s.getOwner().getAsMention(), true);
        }

        return eb.build();
    }

    @Override
    public void run(Message message, List<String> args) {
        message.reply(buildEmbed(message.getGuild())).mentionRepliedUser(false).queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        //no buttons
    }

    @Override
    public void runSlash(SlashCommandEvent event) {
        InteractionHook ih = event.deferReply().complete();
        ih.editOriginalEmbeds(buildEmbed(event.getGuild())).queue();
    }
}
