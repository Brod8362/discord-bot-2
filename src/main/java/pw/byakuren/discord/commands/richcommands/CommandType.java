package pw.byakuren.discord.commands.richcommands;

public enum CommandType {

    /**
     * Traditional is your normal command, one you type in chat with a prefix and the bot replies.
     */
    TRADITIONAL,

    /**
     * Slash is a slash-only command - it CANNOT be used in chat
     */
    SLASH,

    /**
     * /* Integrated supports both slash and traditional, and should have similar behavior in both instances.
     */
    INTEGRATED,

    /**
     * Subcommand is only for subcommands.
     */
    SUBCOMMAND
}
