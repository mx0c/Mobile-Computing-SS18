package ss18.mc.positime;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;
import ss18.mc.positime.utils.Overview_Details_Day_Adapter;
import ss18.mc.positime.utils.TimestampConverter;

public class Overview_Day extends Fragment{

    View view;
    private static String TAG = "Overview Days";
    BenutzerDatabase db;
    Overview_Details_Day_Adapter adapter;
    SharedPreferences mSharedPreferences;
    public String workplace;

    List<Arbeitszeit> workingTimes;
    TextView actual_week;
    Integer weekNr;

    public Overview_Day(  ){
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
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_overview__day, container, false);
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
        weekNr = new Integer(0);
        weekNr = now.get(Calendar.WEEK_OF_YEAR);

        now.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        now.set(Calendar.HOUR_OF_DAY, 0);
        Date monday= now.getTime();
        String mondayS= df.format(monday);

        now.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        now.set(Calendar.HOUR_OF_DAY, 23);
        Date friday= now.getTime();
        String fridayS = df.format(friday);

        //List<Arbeitszeit> workingTimesOfWeek = db.arbeitszeitDAO().getArbeitszeitenForArbeitsOrt(workplace, monday, friday);
       // workingTimes = db.arbeitszeitDAO().getArbeitszeitenForArbeitsort(workplace);

        workingTimes= db.arbeitszeitDAO().getArbeitszeitenForArbeitsortBetween(workplace, mondayS, fridayS);

        Log.d(TAG, "Workplaces found for user with email " + userMail + ": " + workplaces.size());
        Log.d(TAG, "Working Times found for workplace" + workplace + ": " + workplaces.size());

        adapter = new Overview_Details_Day_Adapter(workingTimes, getContext(), workplace);

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
            actual_week = view.findViewById(R.id.actual_week_textview);
            String number= String.valueOf(weekNr.intValue());
            actual_week.setText("Week number: " + number);

            ListView lView = (ListView) view.findViewById(R.id.workplace_list_view);
            lView.setAdapter(adapter);
        }

    }
}

