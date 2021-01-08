package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.FilterActionResult;

import java.util.ArrayList;
import java.util.List;

public class MessageFilterAction extends CacheEntry {

    private final long guild;
    private final String name;
    private List<Filter<Message>> filters;
    private List<Action<Message>> actions;

    public MessageFilterAction(long guild, String name) {
        this(guild, name, new ArrayList<>(), new ArrayList<>());
    }

    public MessageFilterAction(long guild, String name, List<Filter<Message>> filters, List<Action<Message>> actions) {
        this.guild = guild;
        this.name=name;
        this.filters = filters;
        this.actions = actions;
    }

    public void addFilter(Filter<Message> filter) {

    }

    public void addAction(Action<Message> action) {

    }

    public FilterActionResult check(Message msg) {
        List<Filter<Message>> triggered = new ArrayList<>();

        for (Filter<Message> f: filters) {
            if (f.apply(msg)) triggered.add(f);
        }

        boolean trigger = triggered.size() == filters.size();

        if (trigger) {
            for (Action<Message> a: actions) {
                a.execute(msg);
            }
        }

        return new FilterActionResult(this, msg, trigger, triggered);
    }

    @Override
    protected void write(DatabaseManager dbmg) {

    }

    @Override
    protected void delete(DatabaseManager dbmg) {

    }

    public long getGuildId() {
        return guild;
    }

    public String getName() {
        return name;
    }
}
