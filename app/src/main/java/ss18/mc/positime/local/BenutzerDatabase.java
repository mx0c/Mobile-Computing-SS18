package ss18.mc.positime.local;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.dbmodel.Bereich;
import ss18.mc.positime.dbmodel.PausenSettings;
import ss18.mc.positime.model.Benutzer;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;

@Database(entities={Benutzer.class,Arbeitszeit.class, Arbeitsort.class, Bereich.class, PausenSettings.class}, version = 5, exportSchema = false)
public abstract class BenutzerDatabase extends RoomDatabase{
    private static BenutzerDatabase INSTANCE;

    public abstract BenutzerDAO benutzerDAO();
    public abstract ArbeitszeitDAO arbeitszeitDAO();
    public abstract ArbeitsortDAO arbeitsortDAO();
    public abstract BereichDAO bereichDAO();
    public abstract PausenSettingsDAO pausenSettingsDAO();

    public static BenutzerDatabase getBenutzerDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    BenutzerDatabase.class,"benutzer-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
