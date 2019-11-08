package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
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
        help="Manage the server log channel.";
        minimum_permission=CommandPermission.SERVER_ADMIN;

        this.c = c;

        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"set", "here"}, "Set the log channel to the current channel.", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                set(message, args);
            }
        });
        subcommands.add(new Subcommand(new String[]{"view", "v"}, "See the existing log channel.", null, this) {
            @Override
            public void run(Message message, List<String> args) {
                view(message, args);
            }
        });
    }

    private void set(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        ServerSettings s = new ServerSettings(message.getGuild(), ServerParameter.SERVER_LOG_CHANNEL, message.getChannel().getIdLong());
        s.write_state=PENDING_WRITE;
        sc.getSettings().getData().add(s);
        message.getChannel().sendMessageFormat("Set log channel to current channel (%s)",
                message.getTextChannel().getAsMention()).queue();
    }

    private void view(Message message, List<String> args) {
        TextChannel chan = c.getServerCache(message.getGuild()).getLogChannel(message.getJDA());
        message.getChannel().sendMessage(chan != null ? "Log channel is currently set to "+chan.getAsMention() :
                "Log channel is not set.").queue();
    }
}
