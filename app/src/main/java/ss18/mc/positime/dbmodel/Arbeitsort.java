package ss18.mc.positime.dbmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ss18.mc.positime.model.Benutzer;

@Entity(tableName = "arbeitsort",
        indices = {@Index("place_name")},
        foreignKeys = {@ForeignKey(entity = Benutzer.class, parentColumns = "email", childColumns = "benutzer_mail")})
public class Arbeitsort {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "place_name")
    private String placeName;
    @ColumnInfo(name = "lat")
    private double latA;
    @ColumnInfo(name = "long")
    private double longA;
    @ColumnInfo(name = "radius")
    private int radiusA;
    @ColumnInfo(name = "money_perhour") //Gehalt pro Stunde
    private double moneyPerhour;
    @ColumnInfo(name = "weekly_hours")  //Sollstundensatz pro Woche
    private double weeklyHours;
    @ColumnInfo(name = "benutzer_mail")
    private String benutzer_mail;
    @ColumnInfo(name = "Chef_firstname")
    private String chefFistName;
    @ColumnInfo(name = "Chef_lastname")
    private String chefLastName;
    @ColumnInfo(name = "Currency")
    private String currency;

    @Embedded
    private addressT addresst;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLatA() {
        return latA;
    }

    public void setLatA(double latA) {
        this.latA = latA;
    }

    public double getLongA() {
        return longA;
    }

    public void setLongA(double longA) {
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

    public String getChefFistName() {
        return chefFistName;
    }

    public void setChefFistName(String chefFistName) {
        this.chefFistName = chefFistName;
    }

    public String getChefLastName() {
        return chefLastName;
    }

    public void setChefLastName(String chefLastName) {
        this.chefLastName = chefLastName;
    }

    public String getBenutzer_mail() {
        return benutzer_mail;
    }

    public void setBenutzer_mail(String benutzer_mail) {
        this.benutzer_mail = benutzer_mail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
