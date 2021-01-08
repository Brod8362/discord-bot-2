package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;

public class HasPings extends MessageFilter {

    private final int count;

    public HasPings(int count) {
        this.count = count;
    }

    @Override
    public String getRepresentation() {
        return "hasPings";
    }

    @Override
    public String[] getArguments() {
        return new String[]{count+""};
    }

    @Override
    public boolean apply(Message obj) {
        return obj.getMentionedRoles().size()>=count;
    }
}
