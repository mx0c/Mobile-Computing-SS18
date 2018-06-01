package ss18.mc.positime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;
import ss18.mc.positime.utils.DatabaseInitializer;

public class Profile extends AppCompatActivity {

    TextView first,last,mail;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(this);
            DatabaseInitializer.populateSync(db);

            /*

            List<Arbeitsort> allOrts = db.arbeitsortDAO().getAll();
            List<Benutzer> allUsers = db.benutzerDAO().getAll();
            List<Arbeitszeit> zeiten = db.arbeitszeitDAO().getAll();


            first = (TextView) findViewById(R.id.textFirstnameDB);
            last = (TextView) findViewById(R.id.textLastnameDB);
            mail = (TextView) findViewById(R.id.textEmailDB);

            first.setText(allUsers.get(0).getFirstName().toString());
            last.setText(allUsers.get(0).getLastName().toString());
            mail.setText(allUsers.get(0).getEmail().toString());

            */





            /*
            String BenutzernameTest = "Richie";

            Benutzer testb = BenutzerDatabase.getBenutzerDatabase(this).benutzerDAO().getAllByUserName(BenutzernameTest);
            Person testp = BenutzerDatabase.getBenutzerDatabase(this).personDAO().getAllByBenutzerUserName(BenutzernameTest);

            */
        }
}
