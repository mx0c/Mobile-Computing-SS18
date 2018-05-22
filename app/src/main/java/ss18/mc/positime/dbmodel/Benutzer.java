package ss18.mc.positime.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ss18.mc.positime.dbmodel.Person;

@Entity(tableName = "benutzer", foreignKeys =
@ForeignKey(entity = Person.class, parentColumns = "person_id", childColumns = "benutzer_id"))
public class Benutzer {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "benutzer_id")
    private int benutzerId;

    @ColumnInfo(name = "user_name")
    private String userName;
    @ColumnInfo(name = "password")
    private String passWord;
    @ColumnInfo(name = "mail") //das ist die Email
    private String Mail;

    @ColumnInfo(name = "person_id")
    private int personId;


    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int benutzerId) {
        this.benutzerId = benutzerId;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String eMail) {
        this.Mail = Mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }



    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}