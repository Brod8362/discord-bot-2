package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.commands.CommandHelper;

public interface Module {


    void run(Message message);

    void run(CommandHelper cmdhelp);


    ModuleInfo getInfo();

    boolean isExtension();

}
