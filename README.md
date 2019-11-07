Info
====

This is a full rewrite of my other discord bot in JDA.
[Add to your server](https://discordapp.com/oauth2/authorize?&client_id=451526467596058625&scope=bot&permissions=0)

Or, if you want to run it yourself, read below.

Features
========
* Add problematic users to VoiceWatch and keep an eye on when they join/leave voice channels
* Add keywords and be alerted to when they're said in chat
* Associate a role for moderation
* Count messages, attachments, and reactions for each user
* Exclude channels from being flagged by the regex keys
* Flexible "modules" to ensure bot stability

### Planned Features
* Temporary Voice Ban
* More "fun" features
* Command enabling/disabling
* More coming soon!

Setup
====
Put your token in the file named "token.default" and rename it to "token", with no extension.

If you'd like a custom prefix, make a file named "prefix" and put your desired prefix in it. Ensure this file has no extension. Restart the bot for changes to take effect.

Ensure both files are in a folder, and get the most recent .jar file from [here](https://github.com/brod8362/discord-bot-2/releases).

Ensure this jar file is also in the same folder as the token and prefix files.

Run it from the command line with the command:

```bash
java -jar discordbot.jar
``` 

Where `discordbot.jar` is the name of the jar file you downloaded. 
I recommend you put this command into a script of some kind (`.sh`, `.bat`) to make it easier to run in the future.

### Troubleshooting


```
Exception in thread "main" java.lang.RuntimeException: Token file not found.
	at pw.byakuren.discord.Main.getToken(Main.java:69)
	at pw.byakuren.discord.Main.main(Main.java:38)
```
This exception occurs when the bot is unable to find the token file. Ensure the file is named `token` and has no extension. 

```
javax.security.auth.login.LoginException: The provided token is invalid!
	at net.dv8tion.jda.internal.JDAImpl.verifyToken(JDAImpl.java:333)
	at net.dv8tion.jda.internal.JDAImpl.verifyToken(JDAImpl.java:270)
	at net.dv8tion.jda.internal.JDAImpl.login(JDAImpl.java:210)
	at net.dv8tion.jda.api.JDABuilder.build(JDABuilder.java:807)
	at pw.byakuren.discord.Main.main(Main.java:40)
```
This exception occurs when the token file is invalid. Ensure the token is correctly written into the file, and is a valid token.

Bugs
====
Please report any bugs you find, and feel free to fix them with a pull request.


