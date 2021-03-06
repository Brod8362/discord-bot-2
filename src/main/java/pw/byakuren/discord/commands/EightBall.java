package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.util.BotEmbed;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class EightBall extends RichCommand {

    private static String[] PHRASES = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.",
            "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
            "Reply hazy, try again", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
            "Concentrate and ask again.", "Don't count on it.", "My reply is no.", "My sources say no",
            "Outlook not so good.", "Very doubtful."};
    private static Random r = new Random();

    public EightBall() {
        names = new String[]{"8ball", "8b"};
        help = "Ask the magic 8ball a question";
        minimum_permission = CommandPermission.REGULAR_USER;
        type = CommandType.INTEGRATED;
        parameters = new OptionData[]{new OptionData(OptionType.STRING, "question", "The question you want to ask the mighty 8ball", true)};
    }

    @Override
    public void run(Message message, List<String> args) {
        message.reply(buildEmbed(String.join(" ", args))).mentionRepliedUser(false).queue();
    }

    public MessageEmbed buildEmbed(String ballmsg) {
        return BotEmbed.information(ballmsg).setDescription(getRandomPhrase()).build();
    }

    private String getRandomPhrase() {
        return PHRASES[r.nextInt(PHRASES.length)];
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        //not supported
    }

    @Override
    public void runSlash(SlashCommandEvent event) {
        event.replyEmbeds(buildEmbed(event.getOption("question").getAsString())).queue();
    }
}
