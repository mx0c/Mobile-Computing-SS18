package ss18.mc.positime.dbmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import ss18.mc.positime.dbmodel.*;
@Entity(tableName = "bereich", foreignKeys =
@ForeignKey(entity = Arbeitsort.class,parentColumns = "place_name", childColumns = "bereich_id"))
public class Bereich {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bereich_id")
    private int bereichId;

    @ColumnInfo(name = "area_name")
    private String areaName;
    @ColumnInfo(name = "area_radius")
    private int areaRadius;
    @ColumnInfo(name = "area_lat")
    private double areaLat;
    @ColumnInfo(name = "area_long")
    private double areaLong;

    @ColumnInfo(name = "arbeitsort_id")
    private int arbeitsortId;

    public int getArbeitsortId() {
        return arbeitsortId;
    }

    public void setArbeitsortId(int arbeitsortId) {
        this.arbeitsortId = arbeitsortId;
    }

    public int getBereichId() {
        return bereichId;
    }

    public void setBereichId(int bereichId) {
        this.bereichId = bereichId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAreaRadius() {
        return areaRadius;
    }

    public void setAreaRadius(int areaRadius) {
        this.areaRadius = areaRadius;
    }

    public double getAreaLat() {
        return areaLat;
    }

    public void setAreaLat(double areaLat) {
        this.areaLat = areaLat;
    }

    public double getAreaLong() {
        return areaLong;
    }

    public void setAreaLong(double areaLong) {
        this.areaLong = areaLong;
    }
}
