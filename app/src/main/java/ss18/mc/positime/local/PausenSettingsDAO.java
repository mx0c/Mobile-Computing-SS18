package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.PausenSettings;
@Dao
public interface PausenSettingsDAO {
    @Query("SELECT * FROM pausensettings")
    List<PausenSettings> getAll();

    @Insert
    void insertAll(PausenSettings... pausenSettings);

    @Delete
    void delete(Arbeitsort pausensettings);
}
