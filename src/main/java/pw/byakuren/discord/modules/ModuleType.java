package pw.byakuren.discord.modules;

import org.jetbrains.annotations.NotNull;

public enum ModuleType {

    EVENT_MODULE("Event Module"), MESSAGE_MODULE("Message Module"), COMMAND_MODULE("Command Module");

    public final @NotNull String name;

    ModuleType(@NotNull String s) {
        name=s;
    }

}
