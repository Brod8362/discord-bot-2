package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.CommandHelper;

public interface Module {

    void run(@NotNull Message message);

    void run(@NotNull CommandHelper cmdhelp);

    void run(@NotNull Event event);

    @NotNull ModuleInfo getInfo();

}
