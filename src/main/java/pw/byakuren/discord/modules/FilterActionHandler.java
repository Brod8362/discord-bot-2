package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.CommandHelper;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.MessageFilterAction;

public class FilterActionHandler implements Module {

    private final @NotNull Cache c;

    public FilterActionHandler(@NotNull Cache c) {
        this.c = c;
    }

    @Override
    public void run(@NotNull Message message) {
        ServerCache sc = c.getServerCache(message.getGuild());
        for (MessageFilterAction mfa : sc.getAllFilterActions()) {
            if (!mfa.getFilters().isEmpty())
                mfa.check(message);
        }
    }

    @Override
    public void run(@NotNull CommandHelper cmdhelp) {

    }

    @Override
    public void run(@NotNull Event event) {

    }

    @Override
    public @NotNull ModuleInfo getInfo() {
        return new ModuleInfo("FAHandler", "Brod8362", "v0.1b", ModuleType.MESSAGE_MODULE);
    }
}
