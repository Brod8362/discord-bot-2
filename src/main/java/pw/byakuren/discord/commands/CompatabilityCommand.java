package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;

public class CompatabilityCommand extends Command {
    public CompatabilityCommand() {
        names=new String[]{"compatibility", "compat"};
        help="See how compatible you are with something!";
        minimum_permission= CommandPermission.REGULAR_USER;
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() != 1) {
            message.getChannel().sendMessage("You need to give a thing!").queue();
            return;
        }
        int compat = (args.get(0).toLowerCase().hashCode()+message.getAuthor().getId().hashCode())%101;
        message.getChannel().sendMessage(BotEmbed.information("Compatability").setDescription(String.format(
                "%s and %s\n**%d%%** Compatible", message.getAuthor().getAsMention(), args.get(0), compat)
        ).build()).queue();
    }
}
