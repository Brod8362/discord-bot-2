package pw.byakuren.discord.filteraction;

import pw.byakuren.discord.filteraction.result.ActionResult;

/**
 * Represents a single action that can be performed on object T.
 * @param <T> The type of object the action can be performed on
 */
public interface Action<T> {

    public ActionResult execute(T obj);

    public String getName();

}
