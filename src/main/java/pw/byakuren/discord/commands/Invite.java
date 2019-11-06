package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class Invite implements Command {

    @Override
    public String[] getNames() {
        return new String[]{"invite","inv"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Get an invite for this bot.";
    }

    @Override
    public boolean needsBotOwner() {
        return false;
    }

    @Override
    public void run(Message message, List<String> args) {
        JDA jda = message.getJDA();
        String url = "https://discordapp.com/oauth2/authorize?&client_id="+jda.getSelfUser().getId()+"&scope=bot&permissions=0";
        message.getChannel().sendMessage(url).queue();
    }
}
