package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.result.FilterActionResult;
import pw.byakuren.discord.filteraction.result.FilterResult;
import pw.byakuren.discord.util.ScalaReplacements;

import java.util.ArrayList;
import java.util.List;

public class MessageFilterAction extends CacheEntry {

    private final long guild;
    private final String name;
    private final List<Filter<Message>> filters;
    private final List<Action<Message>> actions;

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
        filters.add(filter);
    }

    public void addAction(Action<Message> action) {
        actions.add(action);
    }

    public FilterActionResult check(Message msg) {
        List<FilterResult> results = new ArrayList<>();
        int applied_count = 0;

        for (Filter<Message> f: filters) {
            FilterResult fr = f.apply(msg);
            if (fr.triggered) {
                applied_count++;
            }
            results.add(fr);
        }

        //todo just scan the list and make sure all are true
        boolean trigger = applied_count == filters.size();

        if (trigger) {
            for (Action<Message> a: actions) {
                a.execute(msg);
            }
        }

        return new FilterActionResult(this, msg, trigger, results);
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

    public String prettyPrint() {
        return String.format("%s: filters [%s], actions [%s]", name, ScalaReplacements.mkString(filters, ","),
                ScalaReplacements.mkString(actions, ","));
    }

    @Override
    public String toString() {
        return prettyPrint();
    }
}
