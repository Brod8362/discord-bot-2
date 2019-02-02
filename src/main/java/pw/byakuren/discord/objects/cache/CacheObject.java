package pw.byakuren.discord.objects.cache;

import pw.byakuren.discord.DatabaseManager;

import java.sql.PreparedStatement;

public class CacheObject {

    DatabaseManager dbmg;

    public <T> CacheObject(T datatype, PreparedStatement get_statement, PreparedStatement set_statement) {

    }

}
