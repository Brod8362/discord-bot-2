package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.richcommands.CommandType;
import pw.byakuren.discord.commands.richcommands.RichCommand;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.objects.cache.datatypes.ServerParameter;
import pw.byakuren.discord.objects.cache.datatypes.ServerSettings;

import java.util.List;

public class SetModeratorRole extends RichCommand {

    private final @NotNull Cache c;

    public SetModeratorRole(@NotNull Cache c) {
        names=new String[]{"setmodrole", "modrole"};
        help="Sets the moderator role for the server. Role must be pingable.";
        minimum_permission=CommandPermission.SERVER_ADMIN;
        parameters=new OptionData[]{new OptionData(OptionType.ROLE, "modrole", "The role to set the mod role to.", false)};
        type=CommandType.INTEGRATED;

        this.c = c;
    }

    private void setModRole(@NotNull Guild g, @NotNull Role r) {
        ServerCache sc = c.getServerCache(g);
        Role modr = sc.getModeratorRole(g.getJDA());
        ServerSettings setting = new ServerSettings(g, ServerParameter.SERVER_MODERATOR_ROLE, r.getIdLong());
        setting.write_state= WriteState.PENDING_WRITE;
        if (modr==null) {
            sc.getSettings().getData().add(setting);
        } else {
            sc.removeModeratorRole();
            sc.getSettings().getData().add(setting);
        }
    }

    @Override
    public void run(@NotNull Message message, @NotNull List<String> args) {
        if (message.getMentionedRoles().size() == 0) {
            Role r = c.getServerCache(message.getGuild()).getModeratorRole(message.getJDA());
            message.reply(r != null ? "Moderator role is currently set to "+r.getAsMention() :
                    "Moderator role is not set.").mentionRepliedUser(false).queue();
        } else {
            setModRole(message.getGuild(), message.getMentionedRoles().get(0));
            message.reply("Mod role set to "+message.getMentionedRoles().get(0).getAsMention()).queue();
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        //no buttons supported
    }

    @Override
    public void runSlash(@NotNull SlashCommandEvent event) {
        if (event.getOption("modrole") != null) {
            Role r = event.getOption("modrole").getAsRole();
            setModRole(event.getGuild(), r);
            event.reply("Mod role set to "+r.getAsMention()).queue();
        } else {
            Role r = c.getServerCache(event.getGuild()).getModeratorRole(event.getJDA());
            event.reply(r != null ? "Moderator role is currently set to "+r.getAsMention() :
                    "Moderator role is not set.").mentionRepliedUser(false).queue();
        }
    }
}
