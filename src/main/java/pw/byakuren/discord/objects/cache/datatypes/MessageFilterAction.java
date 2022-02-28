package pw.byakuren.discord.objects.cache.datatypes;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.filteraction.Action;
import pw.byakuren.discord.filteraction.Filter;
import pw.byakuren.discord.filteraction.result.ActionResult;
import pw.byakuren.discord.filteraction.result.FilterActionResult;
import pw.byakuren.discord.filteraction.result.FilterResult;
import pw.byakuren.discord.objects.cache.WriteState;
import pw.byakuren.discord.util.ScalaReplacements;

import java.util.ArrayList;
import java.util.List;

public class MessageFilterAction extends CacheEntry {

    private final long guild;
    private final @NotNull String name;
    private final @NotNull List<Filter<Message>> filters;
    private final @NotNull List<Action<Message>> actions;

    public MessageFilterAction(long guild, @NotNull String name) {
        this(guild, name, new ArrayList<>(), new ArrayList<>());
    }

    public MessageFilterAction(long guild, @NotNull String name, @NotNull List<Filter<Message>> filters, @NotNull List<Action<Message>> actions) {
        this.guild = guild;
        this.name = name;
        this.filters = filters;
        this.actions = actions;
    }

    public void addFilter(Filter<Message> filter) {
        filters.add(filter);
    }

    public void addAction(Action<Message> action) {
        actions.add(action);
    }

    public @NotNull FilterActionResult check(@NotNull Message msg) {
        List<FilterResult> filterResults = new ArrayList<>();
        List<ActionResult> actionResults = new ArrayList<>();
        int applied_count = 0;

        for (Filter<Message> f : filters) {
            FilterResult fr = f.apply(msg);
            if (fr.functionName()) {
                applied_count++;
            }
            filterResults.add(fr);
        }

        //todo just scan the list and make sure all are true
        boolean trigger = applied_count == filters.size();

        if (trigger) {
            for (Action<Message> a : actions) {
                actionResults.add(a.execute(msg));
            }
        }

        return new FilterActionResult(this, msg, trigger, filterResults, actionResults);
    }

    @Override
    protected void write(@NotNull DatabaseManager dbmg) {
        dbmg.addFilterAction(guild, this);
    }

    @Override
    protected void delete(@NotNull DatabaseManager dbmg) {
        dbmg.removeFilterAction(guild, this);
    }

    public long getGuildId() {
        return guild;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String prettyPrint() {
        return String.format("%s: filters [%s], actions [%s]", name, ScalaReplacements.mkString(filters, ","),
                ScalaReplacements.mkString(actions, ","));
    }

    @Override
    public @NotNull String toString() {
        return prettyPrint();
    }

    public @NotNull List<Action<Message>> getActions() {
        return actions;
    }

    public @NotNull List<Filter<Message>> getFilters() {
        return filters;
    }

    public boolean removeFilter(@NotNull String name) {
        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i).getName().equals(name)) {
                filters.remove(i);
                this.write_state= WriteState.PENDING_WRITE;
                return true;
            }
        }
        return false;
    }

    public boolean removeAction(@NotNull String name) {
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getName().equals(name)) {
                actions.remove(i);
                this.write_state= WriteState.PENDING_WRITE;
                return true;
            }
        }
        return false;
    }
}
