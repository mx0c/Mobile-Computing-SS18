package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
@Dao
public interface ArbeitszeitDAO {
    @Query("SELECT * FROM arbeitszeit")
    List<Arbeitszeit> getAll();

    @Insert
    void
    insertAll(Arbeitszeit... arbeitszeits);

    @Delete
    void delete(Arbeitszeit arbeitszeit);
}
