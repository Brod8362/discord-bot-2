package pw.byakuren.discord.commands.richcommands;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pw.byakuren.discord.commands.Command;

public abstract class RichCommand extends Command {

    protected boolean global = false;
    protected CommandType type = CommandType.TRADITIONAL;
    public String[] requestedButtonIDs = new String[]{};

    public abstract void onButtonClick(ButtonClickEvent event);

    public abstract void runSlash(SlashCommandEvent event);

    public boolean isGlobal() {
        return global;
    }

    public final boolean isSlash() {
        return type == CommandType.SLASH || type == CommandType.INTEGRATED;
    }

    @Override
    public final CommandType getType() {
        return type;
    }
}
