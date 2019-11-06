package pw.byakuren.discord.objects.cache.datatypes;

public enum ServerParameter {

    SERVER_LOG_CHANNEL("log_channel", "Server Log Channel", ServerSettingType.CHANNEL);

    enum ServerSettingType {
        CHANNEL, ROLE, BOOLEAN, INTEGER;
    }

    public final String string;
    public final String name;
    public final ServerSettingType type;

    ServerParameter(String str, String n, ServerSettingType type) {
        string=str;
        name=n;
        this.type=type;
    }


    public static ServerParameter stringToSetting(String str) {
        for (ServerParameter s: ServerParameter.values()) {
            if (s.string.equals(str)) return s;
        }
        return null;
    }
}
