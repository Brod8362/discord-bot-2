package pw.byakuren.discord.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MiscUtil {

    public static @NotNull String booleanToYN(boolean b) {
        return b ? "y" : "n";
    }

    public static @NotNull String booleanToEmoji(boolean b) {
        return b ? "✅" : "❌";
    }

    public static <T> byte @NotNull [] serializeList(@NotNull List<T> list) throws IOException {
        ArrayList<T> al = new ArrayList<>(list); //this is done to ensure the list is serializable
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(al);
        oos.flush();
        return bos.toByteArray();
    }

    public static <T> @NotNull List<T> deserializeList(byte @NotNull [] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayList<T> ar = (ArrayList<T>) ois.readObject();
        ois.close();
        bis.close();
        return ar;
    }

    /**
     * A rather questionable function that returns a or b, depending on which exists.
     * If they both exist, a will be returned, and if either exists, null will be returned.
     * @param a Object #1
     * @param b Object #2
     * @param <T> Type of objects
     * @return which object exists. null if neither exists.
     */
    public static <T> @Nullable T which(@Nullable T a, T b) {
        if (a!=null) return a;
        return b; //this will return either null or b.
    }

    public static <T> @NotNull List<String> stringMap(@NotNull List<T> t) {
        return t.stream().map(Object::toString).collect(Collectors.toList());
    }

    public static boolean flip(boolean input, boolean should) {
        if (should) {
            return !input;
        }
        return input;
    }

}
