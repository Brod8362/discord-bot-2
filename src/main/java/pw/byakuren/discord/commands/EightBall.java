package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EightBall extends RichCommand {

    private static final @NotNull String @NotNull [] PHRASES = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.",
            "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
            "Reply hazy, try again", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
            "Concentrate and ask again.", "Don't count on it.", "My reply is no.", "My sources say no",
            "Outlook not so good.", "Very doubtful."};
    private static final @NotNull Random r = new Random();

    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"8ball", "8b"};
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return CommandPermission.REGULAR_USER;
    }

    @Override
    public @NotNull OptionData @NotNull [] getParameters() {
        return new OptionData[]{new OptionData(OptionType.STRING, "question", "The question you want to ask the mighty 8ball", true)};
    }

    @Override
    public @NotNull String getHelp() {
        return "Ask the magic 8ball a question";
    }

    @Override
    public @NotNull CommandType getType() {
        return CommandType.INTEGRATED;
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        message.reply(buildEmbed(String.join(" ", args))).mentionRepliedUser(false).queue();
    }

    public @NotNull MessageEmbed buildEmbed(@NotNull String ballmsg) {
        return BotEmbed.information(ballmsg).setDescription(getRandomPhrase()).build();
    }

    private @NotNull String getRandomPhrase() {
        return PHRASES[r.nextInt(PHRASES.length)];
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        //not supported
    }

    @Override
    public void runSlash(@NotNull SlashCommandEvent event) {
        final OptionMapping question = Objects.requireNonNull(event.getOption("question"));
        event.replyEmbeds(buildEmbed(question.getAsString())).queue();
    }
}
