package pw.byakuren.discord.commands.richcommands;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.Command;

public abstract class RichCommand extends Command {

    protected boolean global = false;
    protected @NotNull CommandType type = CommandType.TRADITIONAL;
    public @NotNull String @NotNull [] requestedButtonIDs = new String[]{};

    public abstract void onButtonClick(ButtonClickEvent event);

    public abstract void runSlash(SlashCommandEvent event);

    public boolean isGlobal() {
        return global;
    }

    public final boolean isSlash() {
        return type == CommandType.SLASH || type == CommandType.INTEGRATED;
    }

    @Override
    public final @NotNull CommandType getType() {
        return type;
    }
}
