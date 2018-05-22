package ss18.mc.positime.dbmodel;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;




@Entity(tableName = "arbeitszeit", foreignKeys = @ForeignKey(entity = Arbeitsort.class,parentColumns = "arbeitsort_id",
        childColumns = "arbeitszeit_id"))
public class Arbeitszeit {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "arbeitszeit_id")
    private int arbeitszeitId;

    @ColumnInfo(name = "amount_breaks")     //Anzahl Pausen
    private int amountBreaks;

    @ColumnInfo(name = "arbeitsort_id")
    public int arbeitsortId;

    @Embedded
    private dateT workday;

    @Embedded(prefix = "start_")
    private timeT starttime;

    @Embedded(prefix = "end_")
    private timeT endtime;

    @Embedded(prefix = "break_")
    private timeT breaktime;

    public int getArbeitsortId() {
        return arbeitsortId;
    }

    public void setArbeitsortId(int arbeitsortId) {
        this.arbeitsortId = arbeitsortId;
    }

    public Arbeitszeit(int arbeitszeitId, int amountBreaks, int arbeitsortId, dateT workday, timeT starttime, timeT endtime, timeT breaktime) {
        this.arbeitszeitId = arbeitszeitId;
        this.amountBreaks = amountBreaks;
        this.arbeitsortId = arbeitsortId;
        this.workday = workday;
        this.starttime = starttime;
        this.endtime = endtime;
        this.breaktime = breaktime;
    }

    public int getArbeitszeitId() {
        return arbeitszeitId;
    }

    public void setArbeitszeitId(int arbeitszeitId) {
        this.arbeitszeitId = arbeitszeitId;
    }

    public int getAmountBreaks() {
        return amountBreaks;
    }

    public void setAmountBreaks(int amountBreaks) {
        this.amountBreaks = amountBreaks;
    }

    public dateT getWorkday() {
        return workday;
    }

    public void setWorkday(dateT workday) {
        this.workday = workday;
    }

    public timeT getStarttime() {
        return starttime;
    }

    public void setStarttime(timeT starttime) {
        this.starttime = starttime;
    }

    public timeT getEndtime() {
        return endtime;
    }

    public void setEndtime(timeT endtime) {
        this.endtime = endtime;
    }

    public timeT getBreaktime() {
        return breaktime;
    }

    public void setBreaktime(timeT breaktime) {
        this.breaktime = breaktime;
    }
}
