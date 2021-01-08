package pw.byakuren.discord.filteraction;

public interface Filter<T> {

    public String getRepresentation();

    public String[] getArguments();

    public boolean apply(T obj);

}
