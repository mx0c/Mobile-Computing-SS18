package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
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

    @Update
    int updateArbeitszeit(Arbeitszeit az);

    @Delete
    void delete(Arbeitszeit arbeitszeit);

    @Query("SELECT * FROM arbeitszeit WHERE arbeitsort_name = :ArbeitsortName")
    List<Arbeitszeit> getArbeitszeitenForArbeitsort(String ArbeitsortName);

    @Query("SELECT * FROM arbeitszeit WHERE arbeitszeit_id = :id")
    Arbeitszeit getArbeitszeitFromID ( Integer id);

    @Query("SELECT * FROM arbeitszeit WHERE arbeitsort_name = :workplace AND starttime BETWEEN :startDate AND :endDate")
    List<Arbeitszeit> getArbeitszeitenForArbeitsortBetween(String workplace, String startDate, String endDate);

     /*@Query("SELECT * FROM arbeitszeit WHERE arbeitsort_name = :ArbeitsortName and benutzer.email =: eMail ")
    List<Arbeitszeit> getArbeitszeitenForArbeitsort(String ArbeitsortName, String eMail);*/

}
