package pw.byakuren.discord.util;

import org.jetbrains.annotations.NotNull;

public enum BotEmoji {

    OK("\uD83C\uDD97"), X("❌"), TRASH("\uD83D\uDDD1️"), CHECK("✅");

    public final @NotNull String unicode;

    BotEmoji(@NotNull String s) {
        unicode = s;
    }

    @Override
    public @NotNull String toString() {
        return unicode;
    }
}
