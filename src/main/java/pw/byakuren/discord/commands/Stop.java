package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.Main;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.objects.cache.Cache;

import java.util.List;

public class Stop extends RichCommand {

    private DatabaseManager dbmg;
    private Cache c;


    public Stop(DatabaseManager dbmg, Cache c) {
        names=new String[]{"stop"};
        help="Stop the bot.";
        minimum_permission=CommandPermission.BOT_OWNER;
        requestedButtonIDs = new String[]{"stop:stop", "stop:cancel"};

        this.dbmg = dbmg;
        this.c = c;
    }

    @Override
    public void run(Message message, List<String> args) {
        JDA jda = message.getJDA();
        Message m = message.reply("Waiting for write threads...").mentionRepliedUser(false).complete();
        try {
            c.writeAllAndQuit();
        } catch (Exception e) {
            Main.reportError(message, e);
        }
        m.editMessage("Shutting down. \uD83D\uDE34").complete();
        dbmg.getSQL().disconnect();
        jda.shutdown();
        System.exit(0);
    }

    public void onButtonClick(ButtonClickEvent ev) {
        switch (ev.getComponentId()) {
            case "stop:stop":
                ev.editMessage("Shutting down. You will get a message when shutdown is complete.")
                        .setActionRows().complete();
                try {
                    c.writeAllAndQuit();
                } catch (Exception e) {
                    //todo allow support to report errors with slash commands
                    //Main.reportError(e.get, e);
                }
                dbmg.getSQL().disconnect();
                ev.getUser().openPrivateChannel().complete().sendMessage("Shutdown complete").complete();
                ev.getJDA().shutdown();
                System.exit(0);
            case "stop:cancel":
                ev.editMessage("Shutdown canceled").setActionRows().queue();
            default:
        }
    }

    @Override
    public void runSlash(SlashCommandEvent event) {
        event.reply("Are you sure you want to shut down?")
                .addActionRow(Button.danger("stop:stop", "Shut Down"), Button.secondary("stop:cancel", "Cancel"))
                .setEphemeral(true).queue();
    }

    public boolean global() {
        return false;
    }

}
