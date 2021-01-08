package pw.byakuren.discord.util;

public abstract class MiscUtil {

    public static String booleanToYN(boolean b) {
        return b ? "y" : "n";
    }

    public static String booleanToEmoji(boolean b) {
        return b ? "✅" : "❌";
    }

}
