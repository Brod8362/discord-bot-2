package pw.byakuren.discord.commands.richcommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pw.byakuren.discord.commands.Command;

public abstract class RichCommand extends Command {

    protected boolean global = false;
    public String[] requestedButtonIDs;

    public abstract void onButtonClick(ButtonClickEvent event);

    public abstract void runSlash(SlashCommandEvent event);

    public boolean isGlobal() {
        return global;
    }

}
