package pw.byakuren.discord.filteraction;

import pw.byakuren.discord.filteraction.result.FilterResult;

public interface Filter<T> {

    public String getName();

    public String[] getArguments();

    public FilterResult apply(T obj);

}
