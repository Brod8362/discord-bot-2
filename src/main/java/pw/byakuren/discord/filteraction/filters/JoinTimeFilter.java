package pw.byakuren.discord.filteraction.filters;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.filteraction.MessageFilter;

import java.time.OffsetDateTime;

public class JoinTimeFilter extends MessageFilter {

    private final int minutes;

    public JoinTimeFilter(int hours) {
        this.minutes = hours;
    }

    @Override
    public String getRepresentation() {
        return "hereFor";
    }

    @Override
    public String[] getArguments() {
        return new String[]{minutes+""};
    }

    @Override
    public String getArgumentsDisplay() {
        return minutes+" minutes";
    }

    @Override
    public boolean apply(Message obj) {
        Member m = obj.getMember();
        if (m != null) {
            return (OffsetDateTime.now().toEpochSecond()-m.getTimeJoined().toEpochSecond())/(60) <= minutes;
        }
        return false;
    }
}
