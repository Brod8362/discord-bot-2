package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.modules.Module;
import pw.byakuren.discord.modules.ModuleHelper;

import java.util.List;
import java.util.Map;

public class Modules extends Command {

    private ModuleHelper mdhelp;

    public Modules(ModuleHelper mdhelp) {
        names=new String[]{"modules"};
        minimum_permission=CommandPermission.BOT_OWNER;
        syntax="View and manage active modules.";

        this.mdhelp = mdhelp;
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
                StringBuilder s = new StringBuilder();
                for (Map.Entry<Module, Boolean> e: mdhelp.getModules().entrySet()) {
                    s.append(String.format("**%s** - %s\n", e.getKey().getInfo().name,
                            (e.getValue() ? "enabled":"disabled")));
                }
                embed.setDescription(s);
                embed.setTitle("Loaded modules");
                if (mdhelp.getModules().keySet().size() == 0) {
                    embed.addField("No modules found.", "", false);
                }
                message.getChannel().sendMessage(embed.build()).queue();
        }
    }
}
