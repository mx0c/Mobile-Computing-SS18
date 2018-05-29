package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Bereich;

@Dao
public interface BereichDAO {
    @Query("SELECT * FROM bereich")
    List<Bereich> getAll();

    @Insert
    void insertAll(Bereich... bereichs);

    @Delete
    void delete(Bereich bereich);
}
