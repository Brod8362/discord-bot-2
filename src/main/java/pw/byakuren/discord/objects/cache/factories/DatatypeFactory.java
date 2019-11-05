package pw.byakuren.discord.objects.cache.factories;

import java.util.List;

public interface DatatypeFactory<E> {

    public List<E> getAll();

    public E get(Object... qualifiers);

}
