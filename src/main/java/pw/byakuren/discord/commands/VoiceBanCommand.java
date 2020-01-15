package pw.byakuren.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import pw.byakuren.discord.commands.permissions.CommandPermission;
import pw.byakuren.discord.commands.subcommands.Subcommand;
import pw.byakuren.discord.commands.subcommands.SubcommandList;
import pw.byakuren.discord.objects.cache.Cache;
import pw.byakuren.discord.objects.cache.ServerCache;
import pw.byakuren.discord.objects.cache.datatypes.VoiceBan;

import java.time.LocalDateTime;
import java.util.List;

import static pw.byakuren.discord.objects.cache.WriteState.PENDING_WRITE;

public class VoiceBanCommand extends Command {

    private Cache c;

    public VoiceBanCommand(Cache c) {
        this.c=c;
        names=new String[]{"voiceban", "vb"};
        help="Ban a user from voice for a specified time.";
        minimum_permission= CommandPermission.MOD_ROLE;

        subcommands.add(new SubcommandList(this));
        subcommands.add(new Subcommand(new String[]{"view", "v"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_view(message, args);
            }
        });

        subcommands.add(new Subcommand(new String[]{"add", "a", "ban"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_add(message, args);
            }
        });

        subcommands.add(new Subcommand(new String[]{"current", "c"}, null,
                "@User [duration] {reason}", this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_current(message, args);
            }
        });

        subcommands.add(new Subcommand(new String[]{"all"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_all(message, args);
            }
        });

        subcommands.add(new Subcommand(new String[]{"cancel", "c"}, null, null, this) {
            @Override
            public void run(Message message, List<String> args) {
                cmd_cancel(message, args);
            }
        });
    }

    private void cmd_all(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        StringBuilder s = new StringBuilder();
        for (VoiceBan vb: sc.getPrevVoiceBans(10)) {
            s.append(vb).append("\n\n");
        }
        EmbedBuilder b = new EmbedBuilder();
        b.setDescription(s.toString());
        b.setTitle("Past 10 voice bans");
        message.getChannel().sendMessage(b.build()).queue();
    }

    private void cmd_view(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        if (message.getMentionedMembers().isEmpty()) {
            message.getChannel().sendMessage("You must mention a user.").queue();
            return;
        }
        VoiceBan vb = sc.getValidVoiceBan(message.getMentionedMembers().get(0));
        if (vb == null) {
            message.getChannel().sendMessage("User is not banned from voice.").queue();
            return;
        }
        sendVoiceBanInfo(message.getTextChannel(), vb);
    }

    private void cmd_add(Message message, List<String> args) {
        if (args.size() < 2) return; //when user & time not provided
        Member banned = message.getMentionedMembers().get(0);
        long gid = message.getGuild().getIdLong();
        long uid = banned.getIdLong();
        long mid = message.getAuthor().getIdLong();
        String reason = "";
        if (args.size() > 2) {
            reason = String.join(" ", args.subList(banned.getEffectiveName().split(" ").length+1, args.size()));
        }
        if (reason.isEmpty()) reason=null;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = parseTime(args.get(banned.getEffectiveName().split(" ").length));
        VoiceBan vb = new VoiceBan(gid,uid,mid,start,end, reason);
        vb.write_state=PENDING_WRITE;
        ServerCache sc = c.getServerCache(message.getGuild());
        sc.getVoiceBans().getData().add(vb);
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Voice Banned "+banned.getUser().getName());
        b.setDescription("Reason: `"+(reason == null ? "<None given>" : reason)+"`");
        b.setAuthor(banned.getUser().getName(), null, banned.getUser().getEffectiveAvatarUrl());
        b.setFooter("Banned by "+message.getAuthor().getName()+" | Expires");
        b.setTimestamp(vb.getExpireTime());
        message.getChannel().sendMessage(b.build()).queue();
        if (!message.getGuild().getSelfMember().hasPermission(Permission.VOICE_MOVE_OTHERS)) {
            message.getChannel().sendMessage(
                    "Note:Bot lacks VOICE_MOVE_OTHERS permission, removing banned users from voice will NOT work.")
                    .queue();
        }
    }

    private void cmd_current(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < sc.getValidVoiceBans().size() && i < 10; i++) {
            s.append(sc.getValidVoiceBans().get(i)).append("\n\n");
        }
        EmbedBuilder b = new EmbedBuilder();
        b.setDescription(s.toString());
        b.setTitle("Current voice bans");
        message.getChannel().sendMessage(b.build()).queue();
    }

    private void cmd_cancel(Message message, List<String> args) {
        ServerCache sc = c.getServerCache(message.getGuild());
        if (message.getMentionedMembers().isEmpty()) {
            message.getChannel().sendMessage("You must mention a user.").queue();
            return;
        }
        VoiceBan vb = sc.getValidVoiceBan(message.getMentionedMembers().get(0));
        if (vb == null) {
            message.getChannel().sendMessage("User is not banned from voice.").queue();
            return;
        }
        vb.cancel();
        message.getChannel().sendMessage("Canceled voice ban for user <@"+vb.getMemberId()+">").queue();
    }

    private LocalDateTime parseTime(String t) {
        LocalDateTime n = LocalDateTime.now();
        while (!t.isEmpty()) {
            String f = null;
            for (int i =2; i <= t.length(); i++) {
                if (t.substring(0, i).matches("\\d*[A-z]")) {
                    f = t.substring(0, i);
                    t=t.substring(i);
                    break;
                }
            }
            if (f==null) break;
            int l = Integer.parseInt(f.substring(0, f.length()-1));
            char c = f.charAt(f.length()-1);
            switch (c) {
                case 'd':
                    n = n.plusDays(l);
                    break;
                case 'h':
                    n = n.plusHours(l);
                    break;
                case 'm':
                    n = n.plusMinutes(l);
                    break;
                default:
                    //unrecognized unit
            }
        }
        return n;
    }

    private void sendVoiceBanInfo(TextChannel c, VoiceBan vb) {
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Voice ban");
        b.setDescription(String.format(
                "Banned member: <@%d>\n" +
                "Banned by:<@%d>\n" +
                "Banned on:%s\n" +
                "Expires on:%s\n" +
                "Reason:%s",
        vb.getMemberId(), vb.getModId(), VoiceBan.formatDateTime(vb.getStartTime()),
                VoiceBan.formatDateTime(vb.getExpireTime()), vb.getReason()));
        c.sendMessage(b.build()).queue();
    }

}

