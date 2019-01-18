package pw.byakuren.discord.commands;




import java.util.HashMap;
import java.util.Map;

public class CommandHelper {

    private Map<String, Command> commands = new HashMap<>();

    public void registerCommand(Command cmd) {
        commands.put(cmd.getName(), cmd);
    }


    public String getCommandHelp(String string) {
        String help = commands.get(string).getHelp();
        if (help != null) {
            return help;
        } else {
            return "No help defined.";
        }
    }

    public String getCommandSyntax(String string) {
        String syntax = commands.get(string).getSyntax();
        if (syntax != null) {
            return syntax;
        } else {
            return "No syntax defined.";
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
