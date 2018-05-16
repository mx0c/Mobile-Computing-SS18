package ss18.mc.positime.local;

import ss18.mc.positime.model.Benutzer;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import static ss18.mc.positime.local.BenutzerDatabase.*;

@Database(entities={Benutzer.class}, version = 1)
public abstract class BenutzerDatabase extends RoomDatabase{
    public abstract BenutzerDAO benutzerDAO();
}
