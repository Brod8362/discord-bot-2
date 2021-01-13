package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;
import pw.byakuren.discord.filteraction.arguments.Argument;
import pw.byakuren.discord.filteraction.result.FilterResult;

public class EveryoneFilter extends MessageFilter {
    @Override
    public String getName() {
        return "pingsEveryone";
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }

    @Override
    public FilterResult apply(Message obj) {
        return new FilterResult(obj.mentionsEveryone(), inverted, getDisplay(), obj.mentionsEveryone() ? null : "the message does not mention everyone");
    }

    @Override
    protected MessageFilter parseFromString(String s) {
        return new EveryoneFilter();
    }

    @Override
    public Argument[] getExpectedArguments() {
        return new Argument[0];
    }
}
