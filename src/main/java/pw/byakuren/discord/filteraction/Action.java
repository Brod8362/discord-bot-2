package pw.byakuren.discord.filteraction;

/**
 * Represents a single action that can be performed on object T.
 * @param <T> The type of object the action can be performed on
 */
public interface Action<T> {

    public void execute(T obj);

}
