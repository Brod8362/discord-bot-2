package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class AdminFilter extends MessageFilter {
    @Override
    public @NotNull String getName() {
        return "isAdmin";
    }

    @Override
    public @NotNull String @NotNull [] getArguments() {
        return new String[0];
    }

    @Override
    public @NotNull FilterResult apply(@NotNull Message obj) {
        boolean trigger = false;
        String reason = "the user does not have the Administrator permission";
        if (obj.getMember() != null) {
            trigger = obj.getMember().hasPermission(Permission.ADMINISTRATOR);
            if (trigger) reason = null;
        }
        return new FilterResult(trigger, inverted, getDisplay(), reason);
    }

    @Override
    protected @NotNull MessageFilter parseFromString(String s) {
        return new AdminFilter();
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[0];
    }
}
