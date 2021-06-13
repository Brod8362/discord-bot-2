package pw.byakuren.discord.commands.richcommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import pw.byakuren.discord.commands.Command;

public abstract class RichCommand extends Command {

    protected boolean global = false;
    public String[] requestedButtonIDs = new String[]{};

    public abstract void onButtonClick(ButtonClickEvent event);

    public abstract void runSlash(SlashCommandEvent event);

    public boolean isGlobal() {
        return global;
    }
}
