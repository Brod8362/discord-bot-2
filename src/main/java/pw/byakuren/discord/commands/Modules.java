package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.modules.Module;
import pw.byakuren.discord.modules.ModuleHelper;

import java.util.List;

public class Modules implements Command {

    private ModuleHelper mdhelp;

    public Modules(ModuleHelper mdhelp) {
        this.mdhelp = mdhelp;
    }

    @Override
    public String getName() {
        return "modules";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getHelp() {
        return "View and manage active modules.";
    }

    @Override
    public void run(Message message, List<String> args) {
        if (args.size() == 0) return;
        switch (args.get(0)) {
            case "disable":
                mdhelp.disable(mdhelp.getModule(args.get(1)));
                message.addReaction("✅").queue();
                return;
            case "enable":
                mdhelp.enable(mdhelp.getModule(args.get(1)));
                message.addReaction("✅").queue();
                break;
            case "list":
                EmbedBuilder embed = new EmbedBuilder();
                for (Module md: mdhelp.getModules().keySet()) {
                    embed.addField(md.getInfo().getName(), String.valueOf(mdhelp.isEnabled(md)), false);
                }
                if (mdhelp.getModules().keySet().size() == 0) {
                    embed.addField("No modules found.", "", false);
                }
                message.getChannel().sendMessage(embed.build()).queue();
        }
    }
}
