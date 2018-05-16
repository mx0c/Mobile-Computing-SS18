package ss18.mc.positime;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ss18.mc.positime.local.BenutzerDAO;
import ss18.mc.positime.local.BenutzerDatabase;

public class Profile extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            BenutzerDatabase benutzerDatabase = Room.inMemoryDatabaseBuilder(context, BenutzerDatabase.class).build();
            BenutzerDAO benutzerDAO = benutzerDatabase.getBenutzerDAO();

        }
}
