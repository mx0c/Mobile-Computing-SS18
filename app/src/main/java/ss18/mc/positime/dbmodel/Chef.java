package ss18.mc.positime.dbmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "chef", foreignKeys =
@ForeignKey(entity = Person.class, parentColumns = "person_id", childColumns = "chef_id"))
public class Chef {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chef_id")
    private int chefId;

    @ColumnInfo(name = "person_id")
    private int personId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }


}

