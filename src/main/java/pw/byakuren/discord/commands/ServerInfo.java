package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;

import java.util.List;

public class ServerInfo implements Command {

    @Override
    public String[] getNames() {
        return new String[]{"serverinfo", "sinfo", "si"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Find info about the current server.";
    }

    @Override
    public CommandPermission minimumPermission() {
        return CommandPermission.REGULAR_USER;
    }

    @Override
    public void run(Message message, List<String> args) {
        Guild s = message.getGuild();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(s.getName());
        embed.setThumbnail(s.getIconUrl());
        embed.addField("Members", Integer.toString(s.getMembers().size()), true);
        embed.addField("Owner", s.getOwner().getAsMention(), true);
        embed.addField("Region", s.getRegion().toString(), true);
        embed.addField("Channel Count", Integer.toString(s.getChannels().size()), true);
        embed.addField("Role Count", Integer.toString(s.getRoles().size()), true);
        embed.addField("Default Channel", s.getDefaultChannel().getAsMention(), true);
        message.getChannel().sendMessage(embed.build()).queue();
    }
}
