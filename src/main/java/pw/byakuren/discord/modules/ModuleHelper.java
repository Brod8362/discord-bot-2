package pw.byakuren.discord.modules;

import java.util.HashMap;
import java.util.Map;

public class ModuleHelper {

    private Map<Module, Boolean> modules = new HashMap<>();

    public void registerModule(Module md) {
        if (md.getInfo().name.contains(" ")) {
            throw new IllegalArgumentException("Module name cannot have spaces");
        }
        modules.put( md, true);
    }

    public void registerModule(Module md, boolean enabled) {
        modules.put( md, enabled);
    }

    public boolean isEnabled(Module md) {
        return modules.get(md); //returns enabled state of the module
    }

    public Map<Module, Boolean> getModules() {
        return modules;
    }

    public Module getModule(String name) {
        for (Module md : modules.keySet() ) {
           if (md.getInfo().name.toLowerCase().equals(name.toLowerCase()))
               return md;
        }
        return null;
    }

    public void enable(Module md) {
        modules.replace(md, isEnabled(md), true);
    }

    public void disable(Module md) {
        modules.replace(md, isEnabled(md), false);
    }


}
