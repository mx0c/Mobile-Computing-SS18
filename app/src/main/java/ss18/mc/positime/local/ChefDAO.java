package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Chef;
@Dao
public interface ChefDAO {
    @Query("SELECT * FROM chef")
    List<Chef> getAll();

    @Insert
    void insertAll(Chef... chefs);

    @Delete
    void delete(Chef chef);
}
