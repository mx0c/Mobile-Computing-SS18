package ss18.mc.positime;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.DatabaseInitializer;
import ss18.mc.positime.utils.Overview_Details_Day_Adapter;
import ss18.mc.positime.utils.Overview_Details_Month_Adapter;

public class Overview_Month extends Fragment {

    View view;
    private static String TAG = "Overview Days";
    BenutzerDatabase db;
    Overview_Details_Month_Adapter adapter;
    SharedPreferences mSharedPreferences;
    public String workplace;

    DateFormat df;
    int year;

    public Overview_Month(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedPreferences();
        initDaysList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_overview__month, container, false);

        initDaysList();
        return view;

    }

    private void initDaysList() {

        BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getContext());
        //Temporary
        DatabaseInitializer.populateSync(db);

        //String userMail = mSharedPreferences.getString(Constants.EMAIL, null);
        String userMail = "ge2thez@gmail.com";
        List<Arbeitsort> workplaces = db.arbeitsortDAO().getArbeitsorteForUser(userMail);

        DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar now = Calendar.getInstance();
        Integer weekNr = now.get(Calendar.WEEK_OF_YEAR);

        now.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        Date monday= now.getTime();
        String mondayS= df.format(monday);

        now.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Date friday= now.getTime();
        String fridayS = df.format(friday);

        //arbeitszeiten für jeden Monat
            //arbeitszeiten für jeden Woche des Monats


        df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Calendar calendar= Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        String startDate= String.valueOf(year)+ "-01-01 00:00:01";
        String endDate= String.valueOf(year) +"-12-31 23:59:59";


        List<Arbeitszeit> workingTimesYear = db.arbeitszeitDAO().getArbeitszeitenForArbeitsortBetween(workplace, startDate, endDate);

        //List<Arbeitszeit> workingTimes = db.arbeitszeitDAO().getArbeitszeitenForArbeitsort(workplace);

        Log.d(TAG, "Workplaces found for user with email " + userMail + ": " + workplaces.size());
        Log.d(TAG, "Working Times found for workplace" + workplace + ": " + workplaces.size());

        adapter = new Overview_Details_Month_Adapter(workingTimesYear, getContext(), workplace);


        updateUI();

    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }


    private void updateUI() {
        if (view == null) { // Check if view is already inflated
            return;
        }
        else{
            TextView yearView= view.findViewById(R.id.actual_year);
            yearView.setText("Year " + String.valueOf(year));
            ListView lView = (ListView) view.findViewById(R.id.workplace_list_view);
            lView.setAdapter(adapter);
        }

    }

}
