package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.model.Benutzer;
@Dao
public interface ArbeitsortDAO {
    @Query("SELECT * FROM arbeitsort")
    List<Arbeitsort> getAll();

    @Insert
    void insertAll(Arbeitsort... arbeitsorts);

    @Delete
    void delete(Arbeitsort arbeitsort);
}
