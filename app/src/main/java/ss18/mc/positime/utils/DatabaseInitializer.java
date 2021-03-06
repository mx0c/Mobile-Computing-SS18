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
        if(Constants.USE_TESTDATA) {
            PopulateDbAsync task = new PopulateDbAsync(db);
            task.execute();
        }
    }

    public static void populateSync(@NonNull final BenutzerDatabase db) {
        if(Constants.USE_TESTDATA) {
            populateWithTestData(db);
        }
    }

    private static Benutzer addBenutzer(final BenutzerDatabase db, Benutzer benutzer) {
        db.benutzerDAO().insertAll(benutzer);
        return benutzer;
    }

    private static void populateWithTestData(BenutzerDatabase db) {
        db.clearAllTables(); //clear all tables


        //Create a user NOTE: The user is acutally the user returned by the backend server
        /*Benutzer user = new Benutzer();
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setPassWord("1234"); //Password is usually hashed
        user.setEmail("ge2thez@gmail.com");*/

        Benutzer user_lena = new Benutzer();
        user_lena.setFirstName("Lena");
        user_lena.setLastName("Essig");
        user_lena.setPassWord("1234"); //Password is usually hashed
        user_lena.setEmail("julia@web.de");


        db.benutzerDAO().insertAll(user_lena);

        //Create Address for Workplace
        addressT address = new addressT();
        address.setCityA("Musterstadt");
        address.setPlzA(12345);
        address.setStreetA("Musterstraße");
        address.setStreetnrA(7);

        Arbeitsort arbeitsort = new Arbeitsort();
        arbeitsort.setAddresst(address);
        arbeitsort.setPlaceName("Daheim");
        arbeitsort.setBenutzer_mail("julia@web.de"); //Beziehung zu User
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
        arbeitsort2.setBenutzer_mail("julia@web.de");
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

        addressT address_julia = new addressT();
        address.setCityA("Dörnach");
        address.setPlzA(72124);
        address.setStreetA("Mühlbachstraße");
        address.setStreetnrA(35);


        Arbeitsort arbeitsort3 = new Arbeitsort();
        arbeitsort3.setAddresst(address_convexis);
        arbeitsort3.setPlaceName("Convexis");
        arbeitsort3.setBenutzer_mail("test@posi.de");
        arbeitsort3.setChefFistName("Max");
        arbeitsort3.setChefLastName("Mustermann");
        arbeitsort3.setLatA(48.49593);
        arbeitsort3.setLongA(9.13558);
        arbeitsort3.setMoneyPerhour(14);
        arbeitsort3.setCurrency("Euro");
        arbeitsort3.setRadiusA(20);
        arbeitsort3.setWeeklyHours(35);

        /*Arbeitsort arbeitsort4 = new Arbeitsort();
        arbeitsort4.setAddresst(address_julia);
        arbeitsort4.setPlaceName("Julias Schreibtisch");
        arbeitsort4.setBenutzer_mail("julia@web.de");
        arbeitsort4.setChefFistName("Max");
        arbeitsort4.setChefLastName("Mustermann");
        arbeitsort4.setLatA(48.59088241974331);
        arbeitsort4.setLongA(9.174538112787104);
        arbeitsort4.setMoneyPerhour(140);
        arbeitsort4.setCurrency("Euro");
        arbeitsort4.setRadiusA(50);
        arbeitsort4.setWeeklyHours(55);*/



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

        Date date1_start= time.fromTimestamp("2018-06-04 01:35:00");
        Date date1_end= time.fromTimestamp("2018-06-04 17:30:00");

        Arbeitszeit arbeitszeit23 = new Arbeitszeit();
        arbeitszeit23.setArbeitszeitId(0);
        arbeitszeit23.setAmountBreaks(3);
        arbeitszeit23.setStarttime(date1_start);
        arbeitszeit23.setEndtime(date1_end);
        arbeitszeit23.setBreaktime(45); //Minutes
        arbeitszeit23.setArbeitsort_name("Julias Schreibtisch");
        arbeitszeit23.setWorkday(date1_start );


        //Create Arbeitszeit to an Arbeitsort
        Arbeitszeit arbeitszeit = new Arbeitszeit();
        arbeitszeit.setArbeitszeitId(0);
        arbeitszeit.setAmountBreaks(3);
        arbeitszeit.setStarttime(date1_start);
        arbeitszeit.setEndtime(date1_end);
        arbeitszeit.setWorktime(54612);
        arbeitszeit.setBreaktime(45); //Minutes
        arbeitszeit.setArbeitsort_name("Daheim");
        arbeitszeit.setWorkday(date1_start );



        Date date2_start= time.fromTimestamp("2018-06-05 09:00:00");
        Date date2_end= time.fromTimestamp("2018-06-05 16:30:00");

        Arbeitszeit arbeitszeit2 = new Arbeitszeit();
        arbeitszeit2.setArbeitszeitId(0);
        arbeitszeit2.setAmountBreaks(1);
        arbeitszeit2.setStarttime(date2_start);
        arbeitszeit2.setEndtime(date2_end);
        arbeitszeit2.setWorktime(28800);
        arbeitszeit2.setBreaktime(30); //Minutes
        arbeitszeit2.setArbeitsort_name("Daheim");
        arbeitszeit2.setWorkday(date2_start);


        Date date3_start= time.fromTimestamp("2018-06-06 07:55:00");
        Date date3_end= time.fromTimestamp("2018-06-06 16:48:00");

        Arbeitszeit arbeitszeit3 = new Arbeitszeit();
        arbeitszeit3.setArbeitszeitId(0);
        arbeitszeit3.setAmountBreaks(1);
        arbeitszeit3.setStarttime(date3_start);
        arbeitszeit3.setEndtime(date3_end);
        arbeitszeit3.setWorktime(29880);
        arbeitszeit3.setBreaktime(35); //Minutes
        arbeitszeit3.setArbeitsort_name("Daheim");
        arbeitszeit3.setWorkday(date3_start);


        Date date4_start= time.fromTimestamp("2018-06-18 03:45:00");
        Date date4_end= time.fromTimestamp("2018-06-18 18:20:00");

        Arbeitszeit arbeitszeit4 = new Arbeitszeit();
        arbeitszeit4.setArbeitszeitId(0);
        arbeitszeit4.setAmountBreaks(1);
        arbeitszeit4.setStarttime(date4_start);
        arbeitszeit4.setEndtime(date4_end);
        arbeitszeit4.setWorktime(39780);
        arbeitszeit4.setBreaktime(32); //Minutes
        arbeitszeit4.setArbeitsort_name("Daheim");
        arbeitszeit4.setWorkday(date4_start);


        Date date5_start= time.fromTimestamp("2018-06-19 06:30:00");
        Date date5_end= time.fromTimestamp("2018-06-19 17:20:00");

        Arbeitszeit arbeitszeit5 = new Arbeitszeit();
        arbeitszeit5.setArbeitszeitId(4);
        arbeitszeit5.setAmountBreaks(0);
        arbeitszeit5.setStarttime(date5_start);
        arbeitszeit5.setEndtime(date5_end);
        arbeitszeit5.setWorktime(38088);
        arbeitszeit5.setBreaktime(15); //Minutes
        arbeitszeit5.setArbeitsort_name("Convexis");
        arbeitszeit5.setWorkday(date5_start);

        Date date6_start= time.fromTimestamp("2018-06-25 08:00:00");
        Date date6_end= time.fromTimestamp("2018-06-25 16:45:00");

        Arbeitszeit arbeitszeit6 = new Arbeitszeit();
        arbeitszeit6.setArbeitszeitId(0);
        arbeitszeit6.setAmountBreaks(1);
        arbeitszeit6.setStarttime(date6_start);
        arbeitszeit6.setEndtime(date6_end);
        arbeitszeit6.setWorktime(29700);
        arbeitszeit6.setBreaktime(30); //Minutes
        arbeitszeit6.setArbeitsort_name("Daheim");
        arbeitszeit6.setWorkday(date6_start);

        Date date7_start= time.fromTimestamp("2018-06-12 04:00:00");
        Date date7_end= time.fromTimestamp("2018-06-12 18:05:00");

        Arbeitszeit arbeitszeit7 = new Arbeitszeit();
        arbeitszeit7.setArbeitszeitId(0);
        arbeitszeit7.setAmountBreaks(4);
        arbeitszeit7.setStarttime(date7_start);
        arbeitszeit7.setEndtime(date7_end);
        arbeitszeit7.setWorktime(45288);
        arbeitszeit7.setBreaktime(90); //Minutes
        arbeitszeit7.setArbeitsort_name("Daheim");
        arbeitszeit7.setWorkday(date7_start);

        Date date8_start= time.fromTimestamp("2018-06-11 08:00:00");
        Date date8_end= time.fromTimestamp("2018-06-11 17:05:00");

        Arbeitszeit arbeitszeit8 = new Arbeitszeit();
        arbeitszeit8.setArbeitszeitId(0);
        arbeitszeit8.setAmountBreaks(1);
        arbeitszeit8.setStarttime(date8_start);
        arbeitszeit8.setEndtime(date8_end);
        arbeitszeit8.setWorktime(30888);
        arbeitszeit8.setBreaktime(30); //Minutes
        arbeitszeit8.setArbeitsort_name("Daheim");
        arbeitszeit8.setWorkday(date8_start);


        Date date9_start= time.fromTimestamp("2018-07-02 08:00:00");
        Date date9_end= time.fromTimestamp("2018-07-02 17:05:00");

        Arbeitszeit arbeitszeit9 = new Arbeitszeit();
        arbeitszeit9.setArbeitszeitId(0);
        arbeitszeit9.setAmountBreaks(1);
        arbeitszeit9.setStarttime(date9_start);
        arbeitszeit9.setEndtime(date9_end);
        arbeitszeit9.setWorktime(30888);
        arbeitszeit9.setBreaktime(30); //Minutes
        arbeitszeit9.setArbeitsort_name("Daheim");
        arbeitszeit9.setWorkday(date9_start);

        Date start_january= time.fromTimestamp("2018-01-01 08:00:00");
        Date end_january= time.fromTimestamp("2018-01-01 17:05:00");

        Arbeitszeit january1 = new Arbeitszeit();
        january1.setArbeitszeitId(0);
        january1.setAmountBreaks(1);
        january1.setStarttime(start_january);
        january1.setEndtime(end_january);
        january1.setWorktime(32652);
        january1.setBreaktime(1); //Minutes
        january1.setArbeitsort_name("Daheim");
        january1.setWorkday(start_january);

        Date start_january2= time.fromTimestamp("2018-01-01 16:00:00");
        Date end_january2= time.fromTimestamp("2018-01-01 17:05:00");

        Arbeitszeit january2 = new Arbeitszeit();
        january2.setArbeitszeitId(0);
        january2.setAmountBreaks(1);
        january2.setStarttime(start_january2);
        january2.setEndtime(end_january2);
        january2.setWorktime(31500);
        january2.setBreaktime(20); //Minutes
        january2.setArbeitsort_name("Daheim");
        january2.setWorkday(start_january2);


        Date start_february= time.fromTimestamp("2018-02-10 8:00:00");
        Date end_february= time.fromTimestamp("2018-02-10 17:05:00");

        Arbeitszeit february = new Arbeitszeit();
        february.setArbeitszeitId(0);
        february.setAmountBreaks(1);
        february.setStarttime(start_february);
        february.setEndtime(end_february);
        february.setWorktime(29412);
        february.setBreaktime(55); //Minutes
        february.setArbeitsort_name("Daheim");
        february.setWorkday(start_february);

        Date start_february2= time.fromTimestamp("2018-02-10 8:00:00");
        Date end_february2= time.fromTimestamp("2018-02-10 16:25:00");

        Arbeitszeit february2 = new Arbeitszeit();
        february2.setArbeitszeitId(0);
        february2.setAmountBreaks(1);
        february2.setStarttime(start_february2);
        february2.setEndtime(end_february2);
        february2.setWorktime(29088);
        february2.setBreaktime(20); //Minutes
        february2.setArbeitsort_name("Daheim");
        february2.setWorkday(start_february2);


        db.arbeitszeitDAO().insertAll(arbeitszeit5,arbeitszeit, arbeitszeit2, arbeitszeit3, arbeitszeit4, arbeitszeit6, arbeitszeit7, arbeitszeit8, arbeitszeit9, january1, january2, february, february2);

        //Create PausenSetting
        PausenSettings ps1 = new PausenSettings();
        PausenSettings ps2 = new PausenSettings();
        ps1.setArbeitsort_name("Daheim");
        ps1.setLengthMin(30);
        ps1.setTiggerMin(300);
        ps1.setPausensettingsId(0);

        ps2.setArbeitsort_name("Convexis");
        ps2.setLengthMin(15);
        ps2.setTiggerMin(540);
        ps2.setPausensettingsId(0);
        PausenSettings ps3 = new PausenSettings();
        ps1.setArbeitsort_name("Julias Schreibtisch");
        ps1.setLengthMin(30);
        ps1.setTiggerMin(300);
        ps1.setPausensettingsId(0);
        db.pausenSettingsDAO().insertAll(ps1, ps2,ps3);
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
