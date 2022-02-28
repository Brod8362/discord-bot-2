package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

public class AssignRoleAction extends MessageAction {

    private long roleId;

    public AssignRoleAction(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        boolean success = false;
        Exception ex = null;
        try {
            Role r = obj.getJDA().getRoleById(roleId);
            obj.getGuild().addRoleToMember(obj.getMember(), r).complete();
            success = true;
        } catch (Exception e) {
            ex=e;
        }
        return new ActionResult(success, getDisplay(), ex);
    }

    @Override
    public @NotNull String getName() {
        return "assignRole";
    }

    @Override
    public @NotNull Argument @NotNull [] getExpectedArguments() {
        return new Argument[]{new Argument("roleId", ArgumentType.ROLE_ID, "ID of role to assign")};
    }

    @Override
    protected @NotNull String @NotNull [] getArguments() {
        return new String[]{roleId+""};
    }

    @Override
    protected @NotNull MessageAction parseFromString(@NotNull String s) {
        return new AssignRoleAction(Long.parseLong(s));
    }
}
