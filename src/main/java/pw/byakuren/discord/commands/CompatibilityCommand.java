package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;
import java.util.stream.Collectors;

public class CompatibilityCommand extends RichCommand {
    public CompatibilityCommand() {
        names = new String[]{"compatibility", "compat"};
        help = "See how compatible you are with something!";
        minimum_permission = CommandPermission.REGULAR_USER;
        type = CommandType.INTEGRATED;
        parameters = new OptionData[]{new OptionData(OptionType.STRING, "thing", "What you want to check compatibility with", true)};
    }

    private MessageEmbed logic(User u, String other) {
        int compat = (other.toLowerCase().hashCode() + u.getId().hashCode()) % 101;
        return BotEmbed.information("Compatibility")
                .setDescription(String.format("%s and %s\n**%d%%** Compatible", u.getAsMention(), other, compat))
                .build();
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.isEmpty())
            return;
        String obj = String.join(" ", args);
        message.reply(logic(message.getAuthor(), obj)).mentionRepliedUser(false).queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        //as usual, its not used
    }

    @Override
    public void runSlash(SlashCommandEvent event) {
        event.replyEmbeds(logic(event.getUser(), event.getOption("thing").getAsString())).queue();
    }
}
