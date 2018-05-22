package ss18.mc.positime.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Person;
@Dao
public interface PersonDAO {
    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Query("SELECT p.* FROM person p,benutzer b WHERE b.benutzer_id LIKE p.person_id AND b.user_name = :userName")
    Person getAllByBenutzerUserName(String userName);

    @Insert
    void insertAll(Person... persons);

    @Delete
    void delete(Person person);
}
