package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static pw.byakuren.discord.commands.permissions.CommandPermission.REGULAR_USER;

public class Invite extends Command {

    public Invite() {
        names= new String[]{"invite","inv"};
        help="Get an invite for this bot.";
        minimum_permission=REGULAR_USER;
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        JDA jda = message.getJDA();
        String url = "https://discordapp.com/oauth2/authorize?&client_id="+jda.getSelfUser().getId()+"&scope=bot&permissions=268445702";
        message.reply(url).mentionRepliedUser(false).queue();
    }
}
