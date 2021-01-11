package pw.byakuren.discord.util;

public enum BotEmoji {

    OK("\uD83C\uDD97"), X("❌"), TRASH("\uD83D\uDDD1️"), CHECK("✅");

    public final String unicode;

    BotEmoji(String s) {
        unicode = s;
    }

    @Override
    public String toString() {
        return unicode;
    }
}
