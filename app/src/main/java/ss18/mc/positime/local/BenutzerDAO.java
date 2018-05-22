package ss18.mc.positime.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.*;

import java.util.List;

import io.reactivex.Flowable;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.dbmodel.Bereich;
import ss18.mc.positime.dbmodel.Chef;
import ss18.mc.positime.dbmodel.PausenSettings;
import ss18.mc.positime.dbmodel.Person;
import ss18.mc.positime.model.Benutzer;

@Dao
public interface BenutzerDAO {
    @Query("SELECT * FROM benutzer")
    List<Benutzer> getAll();

    @Query("SELECT * FROM benutzer WHERE user_name = :userName")
    Benutzer getAllByUserName(String userName);

    @Insert
    void insertAll(Benutzer... benutzers);

    @Delete
    void delete(Benutzer benutzer);
}
