package pw.byakuren.discord.objects.cache;

import pw.byakuren.discord.DatabaseManager;

import java.util.List;

public class CacheObject<CacheDatatype> {

    DatabaseManager dbmg;

    List<CacheDatatype> data;


    public CacheObject() {
        data = getAllFromDatabase();
    }

    public <T> CacheDatatype get(T... qualifiers) {
        for (CacheDatatype o: data) {
            if (o.matches(qualifiers)) return o;
        }
    }

    public List<CacheDatatype> getAll() {
        return data;
    }

    private <E> CacheDatatype getFromDatabase() {
        //todo database getting here
        return null;
    }

    public List<CacheDatatype> getAllFromDatabase() {
        return null; //todo have it run the static method of the class
    }
}
