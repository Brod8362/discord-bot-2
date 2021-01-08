package pw.byakuren.discord.filteraction.arguments;

public enum ArgumentType {

    NUMBER("number"), STRING("string"), ROLE_ID("role id"), USER_ID("user id"), CHANNEL_ID("channel id");

    public final String name;

    ArgumentType(String s) {
        name = s;
    }


}
