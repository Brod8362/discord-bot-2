package pw.byakuren.discord.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class MiscUtil {

    public static String booleanToYN(boolean b) {
        return b ? "y" : "n";
    }

    public static String booleanToEmoji(boolean b) {
        return b ? "✅" : "❌";
    }

    public static <T> byte[] serializeList(List<T> list) throws IOException {
        ArrayList al = new ArrayList(list); //this is done to ensure the list is serializable
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(list);
        oos.flush();
        return bos.toByteArray();
    }

    public static <T> List<T> deserializeList(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayList<T> ar = (ArrayList<T>) ois.readObject();
        ois.close();
        bis.close();
        return ar;
    }

}
