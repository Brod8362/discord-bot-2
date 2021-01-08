package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;

public class AdminFilter extends MessageFilter  {
    @Override
    public String getRepresentation() {
        return "isAdmin";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public boolean apply(Message obj) {
        if (obj.getMember() != null) {
            return obj.getMember().hasPermission(Permission.ADMINISTRATOR);
        }
        return false;
    }
}
