package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;

import java.util.regex.Pattern;

public class InviteFilter extends MessageFilter {

    private final String regex = "(https?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite)/[^\\s/]+?(?=\\b)";

    @Override
    public String getRepresentation() {
        return "hasInvite";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public boolean apply(Message obj) {
        return obj.getContentRaw().matches(regex);
    }
}
