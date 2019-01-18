package pw.byakuren.discord.modules;

public class ModuleInfo {
    private String name;
    private String author;
    private String version;


    public ModuleInfo(String name, String author, String version) {
        this.name = name;
        this.author = author;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }
}
