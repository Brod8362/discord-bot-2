package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;

public class ServerInfo extends Command {

    public ServerInfo() {
        names = new String[]{"serverinfo", "sinfo", "si"};
        help = "Find info about the current server";
        minimum_permission = CommandPermission.REGULAR_USER;
    }

    @Override
    public void run(Message message, List<String> args) {
        Guild s = message.getGuild();
        EmbedBuilder embed = BotEmbed.neutral(s.getName())
                .setThumbnail(s.getIconUrl())
                .addField("Members", Integer.toString(s.getMembers().size()), true)
                .addField("Owner", s.getOwner().getAsMention(), true)
                .addField("Region", s.getRegion().toString(), true)
                .addField("Channel Count", Integer.toString(s.getChannels().size()), true)
                .addField("Role Count", Integer.toString(s.getRoles().size()), true)
                .addField("Default Channel", s.getDefaultChannel().getAsMention(), true);
        message.getChannel().sendMessage(embed.build()).queue();
    }
}
