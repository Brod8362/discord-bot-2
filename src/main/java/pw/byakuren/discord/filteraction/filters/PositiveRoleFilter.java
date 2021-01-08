package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;

import java.util.List;

public class PositiveRoleFilter extends MessageFilter {

    public final long roleId;

    public PositiveRoleFilter(long role) {
        this.roleId = role;
    }

    @Override
    public String getRepresentation() {
        return "hasRole";
    }

    @Override
    public String[] getArguments() {
        return new String[]{""+ roleId};
    }

    @Override
    public String getArgumentsDisplay() {
        return String.format("<@&%d>", roleId);
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("role", ArgumentType.ROLE_ID, "role needed to trigger this filter")};
    }

    @Override
    public boolean apply(Message msg) {
        if (!msg.isFromGuild()) return false;
        Member m = msg.getMember();
        if (m != null) {
            List<Role> roles = m.getRoles();
            for (Role r: roles) {
                if (r.getIdLong()==roleId) {
                    return true;
                }
            }
        }
        return false;
    }
}
