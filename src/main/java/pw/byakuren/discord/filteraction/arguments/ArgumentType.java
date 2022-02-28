package pw.byakuren.discord.filteraction.arguments;

import org.jetbrains.annotations.NotNull;

public enum ArgumentType {

    NUMBER("number"), STRING("string"), ROLE_ID("role id"), USER_ID("user id"), CHANNEL_ID("channel id");

    public final @NotNull String name;

    ArgumentType(@NotNull String s) {
        name = s;
    }


}
