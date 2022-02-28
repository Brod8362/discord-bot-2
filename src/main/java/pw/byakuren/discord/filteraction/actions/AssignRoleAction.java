package pw.byakuren.discord.filteraction.actions;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.filteraction.MessageAction;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.ActionResult;

import java.util.Objects;

public class AssignRoleAction extends MessageAction {

    private long roleId;

    public AssignRoleAction(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public @NotNull ActionResult execute(@NotNull Message obj) {
        Exception ex = null;
        try {
            Role r = obj.getJDA().getRoleById(roleId);
            final Member member = obj.getMember();
            if (r == null) throw new RuntimeException("Role not found");
            if (member == null) throw new RuntimeException("Member not found");
            obj.getGuild().addRoleToMember(member, r).complete();
        } catch (Exception e) {
            ex=e;
        }
        return new ActionResult(getDisplay(), ex);
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
