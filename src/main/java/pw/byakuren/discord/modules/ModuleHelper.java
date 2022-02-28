package pw.byakuren.discord.modules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModuleHelper {

    private final @NotNull Map<Module, Boolean> modules = new HashMap<>();

    public void registerModule(@NotNull Module md) {
        if (md.getInfo().name.contains(" ")) {
            throw new IllegalArgumentException("Module name cannot have spaces");
        }
        modules.put( md, true);
    }

    public void registerModule(@NotNull Module md, boolean enabled) {
        modules.put( md, enabled);
    }

    public boolean isEnabled(@NotNull Module md) {
        return modules.get(md); //returns enabled state of the module
    }

    public @NotNull Map<Module, Boolean> getModules() {
        return modules;
    }

    public @Nullable Module getModule(@NotNull String name) {
        for (Module md : modules.keySet() ) {
           if (md.getInfo().name.equalsIgnoreCase(name))
               return md;
        }
        return null;
    }

    public void enable(@NotNull Module md) {
        modules.replace(md, isEnabled(md), true);
    }

    public void disable(@NotNull Module md) {
        modules.replace(md, isEnabled(md), false);
    }


}
