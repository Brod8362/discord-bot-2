package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;
import java.util.stream.Collectors;

public class CompatabilityCommand extends Command {
    public CompatabilityCommand() {
        names=new String[]{"compatibility", "compat"};
        help="See how compatible you are with something!";
        minimum_permission= CommandPermission.REGULAR_USER;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.isEmpty())
            return;
        String obj = String.join(" ", args);
        int compat = (obj.toLowerCase().hashCode()+message.getAuthor().getId().hashCode())%101;
        message.getChannel().sendMessage(BotEmbed.information("Compatibility").setDescription(String.format(
                "%s and %s\n**%d%%** Compatible", message.getAuthor().getAsMention(), obj, compat)
        ).build()).queue();
    }
}
