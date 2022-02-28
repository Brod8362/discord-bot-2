package pw.byakuren.discord.objects.cache.datatypes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ServerParameter {

    SERVER_LOG_CHANNEL("log_channel", "Server Log Channel", ServerSettingType.CHANNEL),
    SERVER_MODERATOR_ROLE("mod_role", "Server Moderator ROle", ServerSettingType.ROLE);

    enum ServerSettingType {
        CHANNEL, ROLE, BOOLEAN, INTEGER
    }

    public final @NotNull String string;
    public final @NotNull String name;
    public final @NotNull ServerSettingType type;

    ServerParameter(@NotNull String str, @NotNull String n, @NotNull ServerSettingType type) {
        string=str;
        name=n;
        this.type=type;
    }

    public static @Nullable ServerParameter stringToSetting(String str) {
        for (ServerParameter s: ServerParameter.values()) {
            if (s.string.equals(str)) return s;
        }
        return null;
    }
}
