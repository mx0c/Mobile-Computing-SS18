package ss18.mc.positime.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.*;

import java.util.List;

import ss18.mc.positime.dbmodel.*;
import io.reactivex.Flowable;
import ss18.mc.positime.model.Benutzer;

@Dao
public abstract class BenutzerDAO {
    @Query("SELECT * FROM benutzer")
    abstract List<Benutzer> getAll();

    @Query("SELECT * FROM benutzer WHERE eMail =:BenutzerEmail")
    abstract Benutzer getAllBYeMail(String BenutzerEmail);


    @Insert
    abstract void insertAll(List<Benutzer> benutzer);

    @Update
    abstract void update(Benutzer benutzer);

    @Delete
    abstract void delete(Benutzer benutzer);
}
