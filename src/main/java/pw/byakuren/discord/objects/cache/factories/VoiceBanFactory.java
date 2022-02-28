package pw.byakuren.discord.objects.cache.factories;

import org.jetbrains.annotations.NotNull;
import pw.byakuren.discord.DatabaseManager;
import pw.byakuren.discord.objects.cache.datatypes.VoiceBan;

import java.util.List;

public class VoiceBanFactory extends DatatypeFactory<VoiceBan> {

    public VoiceBanFactory(long serverid, @NotNull DatabaseManager dbmg) {
        super(serverid, dbmg);
    }

    @Override
    public @NotNull List<VoiceBan> getAll() {
        return dbmg.getAllVoiceBans(serverid);
    }

}
