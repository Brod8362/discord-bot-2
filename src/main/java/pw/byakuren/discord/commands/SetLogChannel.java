package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.ServerParameter;
import pw.byakuren.discord.objects.cache.datatypes.ServerSettings;

import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class SetLogChannel extends Command {

    Cache c;

    public SetLogChannel(Cache c) {
        names=new String[]{"logchannel","setlogchannel","slc"};
        syntax="<none required>";
        help="Set current channel to the server log channel";
        minimum_permission=CommandPermission.SERVER_ADMIN;

        this.c = c;
    }

    @Override
    public void run(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        ServerSettings s = new ServerSettings(message.getGuild(), ServerParameter.SERVER_LOG_CHANNEL, message.getChannel().getIdLong());
        s.write_state=PENDING_WRITE;
        sc.getSettings().getData().add(s);
        message.getChannel().sendMessageFormat("Set log channel to current channel (%s)",
                message.getTextChannel().getAsMention()).queue();
    }
}
