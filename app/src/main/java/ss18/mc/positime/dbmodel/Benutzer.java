package ss18.mc.positime.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "benutzer")
public class Benutzer {
    @PrimaryKey (autoGenerate = true)
    private int benutzerId;

    @ColumnInfo(name = "tt")
    private String test;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;

    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int benutzerId) {
        this.benutzerId = benutzerId;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String Test) {
        this.test = Test;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

