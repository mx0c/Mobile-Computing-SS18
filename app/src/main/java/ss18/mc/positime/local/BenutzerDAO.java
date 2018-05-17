package ss18.mc.positime.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.*;

import java.util.List;

import io.reactivex.Flowable;
import ss18.mc.positime.model.Benutzer;

@Dao
public interface BenutzerDAO {
    @Query("SELECT * FROM benutzer")
    List<Benutzer> getAll();

    @Query("SELECT * FROM benutzer WHERE first_name LIKE :firstname")
    Benutzer getAllBYFirstname(String firstname);


    @Insert
    void insertAll(Benutzer... benutzer);

    @Delete
    void delete(Benutzer benutzer);
}
