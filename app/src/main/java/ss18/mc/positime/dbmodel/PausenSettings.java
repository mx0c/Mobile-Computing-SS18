package ss18.mc.positime.dbmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "pausensettings", foreignKeys = @ForeignKey(entity = Arbeitsort.class,parentColumns = "place_name",
        childColumns = "arbeitsort_name"))
public class PausenSettings {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pausensettings_id")
    private int pausensettingsId;

    @ColumnInfo(name = "trigger_min")
    private int tiggerMin;
    @ColumnInfo(name = "length_min")
    private int lengthMin;

    @ColumnInfo(name = "arbeitsort_name")
    private String arbeitsort_name;


    public String getArbeitsort_name() {
        return arbeitsort_name;
    }

    public void setArbeitsort_name(String arbeitsort_name) {
        this.arbeitsort_name = arbeitsort_name;
    }

    public int getPausensettingsId() {
        return pausensettingsId;
    }

    public void setPausensettingsId(int pausensettingsId) {
        this.pausensettingsId = pausensettingsId;
    }

    public int getTiggerMin() {
        return tiggerMin;
    }

    public void setTiggerMin(int tiggerMin) {
        this.tiggerMin = tiggerMin;
    }

    public int getLengthMin() {
        return lengthMin;
    }

    public void setLengthMin(int lengthMin) {
        this.lengthMin = lengthMin;
    }
}
