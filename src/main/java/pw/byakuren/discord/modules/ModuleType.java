package pw.byakuren.discord.modules;

public enum ModuleType {

    EVENT_MODULE("Event Module"), MESSAGE_MODULE("Message Module"), COMMAND_MODULE("Command Module");

    public final String name;

    ModuleType(String s) {
        name=s;
    }

}
