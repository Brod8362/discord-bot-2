package pw.byakuren.discord.modules;

import net.dv8tion.jda.api.entities.Message;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.commands.CommandHelper;

public class StatisticManager implements Module {

    private DatabaseManager dbmg;

    public StatisticManager(DatabaseManager d) {
        dbmg=d;
    }

    @Override
    public void run(Message message) {

    }

    @Override
    public void run(CommandHelper cmdhelp) {}

    @Override
    public ModuleInfo getInfo() {
        return new ModuleInfo("StatisticManager", "Brod8362", "d");
    }

    @Override
    public boolean isExtension() {
        return true;
    }
}
