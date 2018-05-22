package ss18.mc.positime;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import ss18.mc.positime.dbmodel.Person;
import ss18.mc.positime.local.BenutzerDAO;

import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;
import ss18.mc.positime.utils.DatabaseInitializer;

public class Profile extends AppCompatActivity {

    TextView first,last,mail;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            DatabaseInitializer.populateAsync(BenutzerDatabase.getBenutzerDatabase(this));

            first = (TextView) findViewById(R.id.textFirstnameDB);
            last = (TextView) findViewById(R.id.textLastnameDB);
            mail = (TextView) findViewById(R.id.textEmailDB);

            String BenutzernameTest = "Richie";

            Benutzer testb = BenutzerDatabase.getBenutzerDatabase(this).benutzerDAO().getAllByUserName(BenutzernameTest);
            Person testp = BenutzerDatabase.getBenutzerDatabase(this).personDAO().getAllByBenutzerUserName(BenutzernameTest);
            first.setText(testp.getFirstName().toString());
            last.setText(testp.getLastName().toString());
            mail.setText(testb.getMail().toString());
        }
}
