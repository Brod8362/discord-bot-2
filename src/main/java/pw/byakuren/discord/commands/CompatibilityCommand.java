package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.util.BotEmbed;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompatibilityCommand extends RichCommand {
    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"compatibility", "compat"};
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return CommandPermission.REGULAR_USER;
    }

    @Override
    public @NotNull OptionData @NotNull [] getParameters() {
        return new OptionData[]{new OptionData(OptionType.STRING, "thing", "What you want to check compatibility with", true)};
    }

    @Override
    public @NotNull String getHelp() {
        return "See how compatible you are with something!";
    }

    @Override
    public @NotNull CommandType getType() {
        return CommandType.INTEGRATED;
    }

    private @NotNull MessageEmbed logic(@NotNull User u, @NotNull String other) {
        int compat = (other.toLowerCase().hashCode() + u.getId().hashCode()) % 101;
        return BotEmbed.information("Compatibility")
                .setDescription(String.format("%s and %s\n**%d%%** Compatible", u.getAsMention(), other, compat))
                .build();
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        if (args.isEmpty())
            return;
        String obj = String.join(" ", args);
        message.reply(logic(message.getAuthor(), obj)).mentionRepliedUser(false).queue();
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        //as usual, its not used
    }

    @Override
    public void runSlash(@NotNull SlashCommandEvent event) {
        final OptionMapping thing = Objects.requireNonNull(event.getOption("thing"));
        event.replyEmbeds(logic(event.getUser(), thing.getAsString())).queue();
    }
}
