package ss18.mc.positime.dbmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "pausensettings", foreignKeys = @ForeignKey(entity = Arbeitsort.class,parentColumns = "arbeitsort_id",
        childColumns = "pausensettings_id"))
public class PausenSettings {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pausensettings_id")
    private int pausensettingsId;

    @ColumnInfo(name = "trigger_min")
    private int tiggerMin;
    @ColumnInfo(name = "length_min")
    private int lengthMin;

    @ColumnInfo(name = "arbeitsort_id")
    private int arbeitsortId;

    public int getArbeitsortId() {
        return arbeitsortId;
    }

    public void setArbeitsortId(int arbeitsortId) {
        this.arbeitsortId = arbeitsortId;
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
