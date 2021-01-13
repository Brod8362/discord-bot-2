package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.arguments.ArgumentType;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class ContainsFilter extends MessageFilter {

    private String containText;

    public ContainsFilter(String containText) {
        this.containText = containText;
    }

    @Override
    public String getName() {
        return "contains";
    }

    @Override
    public String[] getArguments() {
        return new String[]{containText};
    }

    @Override
    public FilterResult apply(Message obj) {
        boolean apply = obj.getContentRaw().contains(containText);
        String reason = apply ? null : "the message does not contain the sequence specified";
        return new FilterResult(apply, inverted, getDisplay(), reason);
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new ContainsFilter(s);
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[]{new Argument("containText", ArgumentType.STRING, "Text the message should contain to trigger this filter")};
    }
}
