package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class AdminFilter extends MessageFilter {
    @Override
    public String getRepresentation() {
        return "isAdmin";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean trigger = false;
        String reason = "the user does not have the Administrator permission";
        if (obj.getMember() != null) {
            trigger = obj.getMember().hasPermission(Permission.ADMINISTRATOR);
            if (trigger) reason = null;
        }
        return new FilterResult(trigger, getDisplay(), reason);
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new AdminFilter();
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[0];
    }
}
