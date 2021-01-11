package pw.byakuren.discord.filteraction.arguments;

public class Argument {

    public final String name;
    public final ArgumentType type;
    public final String description;

    public Argument(String name, ArgumentType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", name, type.name, description);
    }
}
