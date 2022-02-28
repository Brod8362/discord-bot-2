package pw.byakuren.discord.modules;

import org.jetbrains.annotations.NotNull;

public class ModuleInfo {

    public final @NotNull String name;
    public final @NotNull String author;
    public final @NotNull String version;
    public final @NotNull ModuleType type;

    public ModuleInfo(@NotNull String name, @NotNull String author, @NotNull String version, @NotNull ModuleType type) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.type = type;
    }
}
