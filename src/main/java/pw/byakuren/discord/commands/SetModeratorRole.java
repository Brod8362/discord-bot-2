package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
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
        this.c = c;
    }

    @Override
    public @NotNull String @NotNull [] getNames() {
        return new String[]{"setmodrole", "modrole"};
    }

    @Override
    public @NotNull String getHelp() {
        return "Sets the moderator role for the server. Role must be pingable.";
    }

    @Override
    public @NotNull CommandPermission minimumPermission() {
        return CommandPermission.SERVER_ADMIN;
    }

    @Override
    public @NotNull OptionData @NotNull [] getParameters() {
        return new OptionData[]{new OptionData(OptionType.ROLE, "modrole", "The role to set the mod role to.", false)};
    }

    @Override
    public @NotNull CommandType getType() {
        return CommandType.INTEGRATED;
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
        final Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("You must be in a server to use this command.").queue();
            return;
        }
        final OptionMapping modroleOption = event.getOption("modrole");
        if (modroleOption != null) {
            Role r = modroleOption.getAsRole();
            setModRole(guild, r);
            event.reply("Mod role set to "+r.getAsMention()).queue();
        } else {
            Role r = c.getServerCache(guild).getModeratorRole(event.getJDA());
            event.reply(r != null ? "Moderator role is currently set to "+r.getAsMention() :
                    "Moderator role is not set.").mentionRepliedUser(false).queue();
        }
    }
}
