package pw.byakuren.discord.modules;

public class ModuleInfo {
    public final String name;
    public final String author;
    public  final String version;
    public final ModuleType type;


    public ModuleInfo(String name, String author, String version, ModuleType type) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.type = type;
    }
}
