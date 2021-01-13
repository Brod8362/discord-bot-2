package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class RevokeRoleAction extends MessageAction {

    private long roleId;

    public RevokeRoleAction(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public ActionResult execute(Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            Role r = obj.getJDA().getRoleById(roleId);
            obj.getGuild().removeRoleFromMember(obj.getMember(), r).complete();
            success = true;
        } catch (Exception e) {
            ex=e;
        }
        return new ActionResult(success, getDisplay(), ex);
    }

    @Override
    public String getName() {
        return "removeRole";
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("roleId", ArgumentType.ROLE_ID, "ID of role to remove")};
    }

    @Override
    protected String[] getArguments() {
        return new String[]{roleId+""};
    }

    @Override
    protected MessageAction parseFromString(String s) {
        return new RevokeRoleAction(Long.parseLong(s));
    }
}
