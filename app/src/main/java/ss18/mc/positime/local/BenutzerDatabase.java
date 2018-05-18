package ss18.mc.positime.local;

import ss18.mc.positime.model.Benutzer;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;

import static ss18.mc.positime.local.BenutzerDatabase.*;

@Database(entities={Benutzer.class}, version = 1)
public abstract class BenutzerDatabase extends RoomDatabase{
    private static BenutzerDatabase INSTANCE;

    public abstract BenutzerDAO benutzerDAO();

    public static BenutzerDatabase getBenutzerDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    BenutzerDatabase.class,"benutzer-database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
