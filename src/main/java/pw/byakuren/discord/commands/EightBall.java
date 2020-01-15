package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class EightBall extends Command {

    private static String[] PHRASES = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.",
            "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
            "Reply hazy, try again", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
            "Concentrate and ask again.", "Don't count on it.", "My reply is no.", "My sources say no",
            "Outlook not so good.", "Very doubtful."};
    private static Random r = new Random();

    public EightBall() {
        names=new String[]{"8ball", "8b"};
        help="Ask the magic 8ball a question";
        minimum_permission= CommandPermission.REGULAR_USER;
    }

    @Override
    public void run(Message message, List<String> args) {
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(Color.CYAN);
        b.setDescription(getRandomPhrase());
        message.getChannel().sendMessage(b.build()).queue();
    }

    private String getRandomPhrase() {
        return PHRASES[r.nextInt(PHRASES.length)];
    }
}
