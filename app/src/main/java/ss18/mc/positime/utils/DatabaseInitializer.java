package ss18.mc.positime.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;

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
        arbeitsort.setCurrency("Euro");
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
        arbeitsort2.setCurrency("Euro");
        arbeitsort2.setRadiusA(50);
        arbeitsort2.setWeeklyHours(40);


        db.arbeitsortDAO().insertAll(arbeitsort, arbeitsort2);

        //Create Arbeitszeit to an Arbeitsort
        Arbeitszeit arbeitszeit = new Arbeitszeit();
        arbeitszeit.setArbeitszeitId(0);
        arbeitszeit.setAmountBreaks(1);
        arbeitszeit.setStarttime(new Date());
        arbeitszeit.setEndtime(new Date());
        arbeitszeit.setBreaktime(10); //Minutes
        arbeitszeit.setArbeitsort_name("Daheim");
        arbeitszeit.setWorkday(new Date());

        db.arbeitszeitDAO().insertAll(arbeitszeit);

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
