package pw.byakuren.discord.commands;




import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandHelper {

    private Map<String, Command> commands = new HashMap<>();
    private Set<Command> cmd_set = new HashSet<>();

    public void registerCommand(Command cmd) {
        for (String s : cmd.getNames()) {
            Command c = commands.get(s);
            if (c == null) {
                commands.put(s, cmd);
            } else {
                throw new RuntimeException(String.format("Command alias %s conflicts with alias for command %s", s, c.getNames()[0]));
            }
        }
        cmd_set.add(cmd);
    }

    public Command getCommand(String n) {
        return commands.get(n);
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

    public Set<Command> getCommandSet() {
        return cmd_set;
    }
}
