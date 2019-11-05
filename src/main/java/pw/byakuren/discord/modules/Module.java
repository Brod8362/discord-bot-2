package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import pw.byakuren.discord.commands.CommandHelper;

public interface Module {


    void run(Message message);

    void run(CommandHelper cmdhelp);

    void run(Event event);

    ModuleInfo getInfo();

}
