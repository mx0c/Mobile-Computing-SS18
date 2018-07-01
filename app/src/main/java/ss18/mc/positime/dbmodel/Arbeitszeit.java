package ss18.mc.positime.dbmodel;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import ss18.mc.positime.utils.TimestampConverter;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "arbeitszeit",
        indices = {@Index("arbeitszeit_id")},
        foreignKeys = @ForeignKey(entity = Arbeitsort.class,parentColumns = "place_name",
        childColumns = "arbeitsort_name", onDelete = CASCADE))
public class Arbeitszeit {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "arbeitszeit_id")
    private int arbeitszeitId = 0;

    @ColumnInfo(name = "amount_breaks")     //Anzahl Pausen
    private int amountBreaks;

    @ColumnInfo(name = "arbeitsort_name")
    public String arbeitsort_name;

    @ColumnInfo(name = "workday")
    @TypeConverters({TimestampConverter.class})
    private Date workday;

    @ColumnInfo(name = "starttime")
    @TypeConverters(TimestampConverter.class)
    private Date starttime;

    @ColumnInfo(name = "endtime")
    @TypeConverters(TimestampConverter.class)
    private Date endtime;

    @ColumnInfo(name = "breaktime")
    private int breaktime;

    @ColumnInfo(name = "worktime")
    private int worktime;

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

    public String getArbeitsort_name() {
        return arbeitsort_name;
    }

    public void setArbeitsort_name(String arbeitsort_name) { this.arbeitsort_name = arbeitsort_name; }

    public Date getWorkday() {
        return workday;
    }

    public void setWorkday(Date workday) {
        this.workday = workday;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public int getBreaktime() {
        return breaktime;
    }

    public void setBreaktime(int breaktime) {
        this.breaktime = breaktime;
    }

    public int getWorktime() { return worktime; }

    public void setWorktime(int worktime) { this.worktime = worktime; }
}
