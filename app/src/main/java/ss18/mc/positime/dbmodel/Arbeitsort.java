package ss18.mc.positime.dbmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import ss18.mc.positime.dbmodel.addressT;
import ss18.mc.positime.dbmodel.currencyT;

import ss18.mc.positime.model.Benutzer;

@Entity(tableName = "arbeitsort", foreignKeys = {
        @ForeignKey(entity = Chef.class,parentColumns = "chef_id",childColumns = "arbeitsort_id"),
        @ForeignKey(entity = Benutzer.class,parentColumns = "benutzer_id",childColumns = "arbeitsort_id")})
public class Arbeitsort {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "arbeitsort_id")
    private int arbeitsortId;

    @ColumnInfo(name = "place_name")
    private String placeName;
    @ColumnInfo(name = "lat")
    private String latA;
    @ColumnInfo(name = "long")
    private String longA;
    @ColumnInfo(name = "radius")
    private int radiusA;
    @ColumnInfo(name = "money_perhour") //Gehalt pro Stunde
    private double moneyPerhour;
    @ColumnInfo(name = "weekly_hours")  //Sollstundensatz pro Woche
    private double weeklyHours;

    @ColumnInfo(name = "chef_id")
    private int chefId;
    @ColumnInfo(name = "benutzer_id")
    private int benutzerId;

    @Embedded
    private addressT addresst;

    @Embedded
    private currencyT currencyt;

    public Arbeitsort(int arbeitsortId, String placeName, String latA, String longA, int radiusA, double moneyPerhour, double weeklyHours, int chefId, int benutzerId, addressT addresst, currencyT currencyt) {
        this.arbeitsortId = arbeitsortId;
        this.placeName = placeName;
        this.latA = latA;
        this.longA = longA;
        this.radiusA = radiusA;
        this.moneyPerhour = moneyPerhour;
        this.weeklyHours = weeklyHours;
        this.chefId = chefId;
        this.benutzerId = benutzerId;
        this.addresst = addresst;
        this.currencyt = currencyt;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int benutzerId) {
        this.benutzerId = benutzerId;
    }

    public int getArbeitsortId() {
        return arbeitsortId;
    }

    public void setArbeitsortId(int arbeitsortId) {
        this.arbeitsortId = arbeitsortId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLatA() {
        return latA;
    }

    public void setLatA(String latA) {
        this.latA = latA;
    }

    public String getLongA() {
        return longA;
    }

    public void setLongA(String longA) {
        this.longA = longA;
    }

    public int getRadiusA() {
        return radiusA;
    }

    public void setRadiusA(int radiusA) {
        this.radiusA = radiusA;
    }

    public double getMoneyPerhour() {
        return moneyPerhour;
    }

    public void setMoneyPerhour(double moneyPerhour) {
        this.moneyPerhour = moneyPerhour;
    }

    public double getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(double weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public addressT getAddresst() {
        return addresst;
    }

    public void setAddresst(addressT addresst) {
        this.addresst = addresst;
    }

    public currencyT getCurrencyt() {
        return currencyt;
    }

    public void setCurrencyt(currencyT currencyt) {
        this.currencyt = currencyt;
    }
}
