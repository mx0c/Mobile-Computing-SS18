package ss18.mc.positime.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.dbmodel.PausenSettings;
import ss18.mc.positime.dbmodel.addressT;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;


public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();


    public static void populateAsync(@NonNull final BenutzerDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final BenutzerDatabase db) {
        populateWithTestData(db);
    }

    private static Benutzer addBenutzer(final BenutzerDatabase db, Benutzer benutzer) {
        db.benutzerDAO().insertAll(benutzer);
        return benutzer;
    }

    private static void populateWithTestData(BenutzerDatabase db) {
        db.clearAllTables(); //clear all tables


        //Create a user NOTE: The user is acutally the user returned by the backend server
        Benutzer user = new Benutzer();
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setPassWord("1234"); //Password is usually hashed
        user.setEmail("ge2thez@gmail.com");

        /*Benutzer user_lena = new Benutzer();
        user.setFirstName("Lena");
        user.setLastName("Essig");
        user.setPassWord("xxxx"); //Password is usually hashed
        user.setEmail("test@posi.de");*/


        db.benutzerDAO().insertAll(user);

        //Create Address for Workplace
        addressT address = new addressT();
        address.setCityA("Musterstadt");
        address.setPlzA(12345);
        address.setStreetA("Musterstraße");
        address.setStreetnrA(7);

        Arbeitsort arbeitsort = new Arbeitsort();
        arbeitsort.setAddresst(address);
        arbeitsort.setPlaceName("Daheim");
        arbeitsort.setBenutzer_mail("ge2thez@gmail.com"); //Beziehung zu User
        arbeitsort.setChefFistName("Helene");
        arbeitsort.setChefLastName("Helene!");
        arbeitsort.setLatA(48.531415);
        arbeitsort.setLongA(9.340418);
        arbeitsort.setMoneyPerhour(17);
        arbeitsort.setCurrency("€");
        arbeitsort.setRadiusA(50);
        arbeitsort.setWeeklyHours(40);

        Arbeitsort arbeitsort2 = new Arbeitsort();
        arbeitsort2.setAddresst(address);
        arbeitsort2.setPlaceName("Hochschule Reutlingen");
        arbeitsort2.setBenutzer_mail("ge2thez@gmail.com");
        arbeitsort2.setChefFistName("Nati");
        arbeitsort2.setChefLastName("Martinez");
        arbeitsort2.setLatA(48.531415);
        arbeitsort2.setLongA(9.340418);
        arbeitsort2.setMoneyPerhour(17);
        arbeitsort2.setCurrency("$");
        arbeitsort2.setRadiusA(50);
        arbeitsort2.setWeeklyHours(40);



        addressT address_convexis = new addressT();
        address.setCityA("Reutlingen");
        address.setPlzA(72770);
        address.setStreetA("Gerhard-Kindler-Straße");
        address.setStreetnrA(3);

        Arbeitsort arbeitsort3 = new Arbeitsort();
        arbeitsort3.setAddresst(address_convexis);
        arbeitsort3.setPlaceName("Convexis");
        arbeitsort3.setBenutzer_mail("ge2thez@gmail.com");
        arbeitsort3.setChefFistName("Max");
        arbeitsort3.setChefLastName("Mustermann");
        arbeitsort3.setLatA(48.49593);
        arbeitsort3.setLongA(9.13558);
        arbeitsort3.setMoneyPerhour(14);
        arbeitsort3.setCurrency("Euro");
        arbeitsort3.setRadiusA(20);
        arbeitsort3.setWeeklyHours(35);


        /*Arbeitsort arbeitsort4 = new Arbeitsort();
        arbeitsort4.setAddresst(address);
        arbeitsort4.setPlaceName("Daheim");
        arbeitsort4.setBenutzer_mail("test@posi.de"); //Beziehung zu User
        arbeitsort4.setChefFistName("Vorname");
        arbeitsort4.setChefLastName("Nachname");
        arbeitsort4.setLatA(48.531415);
        arbeitsort4.setLongA(9.340418);
        arbeitsort4.setMoneyPerhour(17);
        arbeitsort4.setCurrency("Euro");
        arbeitsort4.setRadiusA(50);
        arbeitsort4.setWeeklyHours(40);*/


        db.arbeitsortDAO().insertAll(arbeitsort, arbeitsort2, arbeitsort3);

        TimestampConverter time = new TimestampConverter();

        Date date1_start= time.fromTimestamp("2018-06-07 07:35:00");
        Date date1_end= time.fromTimestamp("2018-06-07 17:30:00");

        //Create Arbeitszeit to an Arbeitsort
        Arbeitszeit arbeitszeit = new Arbeitszeit();
        arbeitszeit.setArbeitszeitId(0);
        arbeitszeit.setAmountBreaks(1);
        arbeitszeit.setStarttime(date1_start);
        arbeitszeit.setEndtime(date1_end);
        arbeitszeit.setBreaktime(45); //Minutes
        arbeitszeit.setArbeitsort_name("Daheim");
        arbeitszeit.setWorkday(date1_start );



        Date date2_start= time.fromTimestamp("2018-06-08 08:00:00");
        Date date2_end= time.fromTimestamp("2018-06-08 16:30:00");

        Arbeitszeit arbeitszeit2 = new Arbeitszeit();
        arbeitszeit2.setArbeitszeitId(1);
        arbeitszeit2.setAmountBreaks(1);
        arbeitszeit2.setStarttime(date2_start);
        arbeitszeit2.setEndtime(date2_end);
        arbeitszeit2.setBreaktime(30); //Minutes
        arbeitszeit2.setArbeitsort_name("Daheim");
        arbeitszeit2.setWorkday(date2_start);


        Date date3_start= time.fromTimestamp("2018-06-06 07:55:00");
        Date date3_end= time.fromTimestamp("2018-06-06 16:48:00");

        Arbeitszeit arbeitszeit3 = new Arbeitszeit();
        arbeitszeit3.setArbeitszeitId(2);
        arbeitszeit3.setAmountBreaks(1);
        arbeitszeit3.setStarttime(date3_start);
        arbeitszeit3.setEndtime(date3_end);
        arbeitszeit3.setBreaktime(35); //Minutes
        arbeitszeit3.setArbeitsort_name("Daheim");
        arbeitszeit3.setWorkday(date3_start);


        Date date4_start= time.fromTimestamp("2018-06-05 08:45:00");
        Date date4_end= time.fromTimestamp("2018-06-05 18:20:00");

        Arbeitszeit arbeitszeit4 = new Arbeitszeit();
        arbeitszeit4.setArbeitszeitId(3);
        arbeitszeit4.setAmountBreaks(1);
        arbeitszeit4.setStarttime(date4_start);
        arbeitszeit4.setEndtime(date4_end);
        arbeitszeit4.setBreaktime(32); //Minutes
        arbeitszeit4.setArbeitsort_name("Daheim");
        arbeitszeit4.setWorkday(date4_start);


        Date date5_start= time.fromTimestamp("2018-06-05 06:30:00");
        Date date5_end= time.fromTimestamp("2018-06-05 17:20:00");

        Arbeitszeit arbeitszeit5 = new Arbeitszeit();
        arbeitszeit5.setArbeitszeitId(4);
        arbeitszeit5.setAmountBreaks(1);
        arbeitszeit5.setStarttime(date5_start);
        arbeitszeit5.setEndtime(date5_end);
        arbeitszeit5.setBreaktime(15); //Minutes
        arbeitszeit5.setArbeitsort_name("Convexis");
        arbeitszeit5.setWorkday(date5_start);

        db.arbeitszeitDAO().insertAll(arbeitszeit4,arbeitszeit3, arbeitszeit2, arbeitszeit, arbeitszeit5);

        //Create PausenSetting
        PausenSettings ps1 = new PausenSettings();
        PausenSettings ps2 = new PausenSettings();
        ps1.setArbeitsort_name("Daheim");
        ps1.setLengthMin(30);
        ps1.setTiggerMin(300);
        ps1.setPausensettingsId(0);

        ps2.setArbeitsort_name("Daheim");
        ps2.setLengthMin(15);
        ps2.setTiggerMin(540);
        ps2.setPausensettingsId(0);

        db.pausenSettingsDAO().insertAll(ps1, ps2);
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BenutzerDatabase mDb;

        PopulateDbAsync(BenutzerDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);


            return null;
        }

    }


}
