package pw.byakuren.discord.filteraction.arguments;

import org.jetbrains.annotations.NotNull;

public class Argument {

    public final @NotNull String name;
    public final @NotNull ArgumentType type;
    public final @NotNull String description;

    public Argument(@NotNull String name, @NotNull ArgumentType type, @NotNull String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s (%s): %s", name, type.name, description);
    }
}
