package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class MentionsRoleFilter extends MessageFilter {

    private long roleId;

    public MentionsRoleFilter(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String getName() {
        return "mentionsRole";
    }

    @Override
    public String[] getArguments() {
        return new String[]{roleId+""};
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean apply = obj.getMentionedRoles().contains(obj.getJDA().getRoleById(roleId));
        String reason = apply ? null : "the message does not contain a mention to the role <@&"+roleId+">";
        return new FilterResult(apply, inverted, getDisplay(), reason);
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new MentionsRoleFilter(Long.parseLong(s));
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("roleId", ArgumentType.ROLE_ID, "The role ID this should check for")};
    }
}
