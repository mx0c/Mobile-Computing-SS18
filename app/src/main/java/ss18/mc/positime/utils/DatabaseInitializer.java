package ss18.mc.positime.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;
import ss18.mc.positime.local.BenutzerDAO;


public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final BenutzerDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final BenutzerDatabase db){
        populateWithTestData(db);
    }

    private static Benutzer addBenutzer(final BenutzerDatabase db, Benutzer benutzer){
        db.benutzerDAO().insertAll(benutzer);
        return benutzer;
    }

    private static void populateWithTestData(BenutzerDatabase db) {
        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("Richard");
        benutzer.setLastName("Spitz");
        benutzer.setTest("richie@richie.de");
        addBenutzer(db, benutzer);

        List<Benutzer> benutzerList = db.benutzerDAO().getAll();
        Log.d(DatabaseInitializer.TAG, "Row Count: " + benutzerList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {

        private final BenutzerDatabase mDb;
        PopulateDbAsync(BenutzerDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params){
            populateWithTestData(mDb);
            return null;
        }
    }

}
