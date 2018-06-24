package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.model.Benutzer;
@Dao
public interface ArbeitsortDAO {
    @Query("SELECT * FROM arbeitsort")
    List<Arbeitsort> getAll();

    @Query("SELECT * FROM arbeitsort WHERE benutzer_mail = :usrEmail")
    List<Arbeitsort> getArbeitsorteForUser(String usrEmail);

    @Query("SELECT * FROM arbeitsort WHERE benutzer_mail = :benutzer_mail AND place_name = :place_name ")
    Arbeitsort getOneArbeitsortForBenutzer(String place_name, String benutzer_mail);


    @Query("SELECT arbeitsort.money_perhour FROM arbeitsort WHERE place_name = :place_name ")
    Double getMoneyPerHour( String place_name);

    @Update
    void updateArbeitsort(Arbeitsort arbeitsort);

    @Insert
    void insertAll(Arbeitsort... arbeitsorts);

    @Delete
    void delete(Arbeitsort arbeitsort);
}
