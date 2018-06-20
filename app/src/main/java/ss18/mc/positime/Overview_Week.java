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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;
import ss18.mc.positime.utils.Overview_Details_Week_Adapter;

public class Overview_Week extends Fragment {


    View view;
    private static String TAG = "Overview Weeks";
    BenutzerDatabase db;
    Overview_Details_Week_Adapter adapter;
    SharedPreferences mSharedPreferences;
    TextView month_year;

    String month_yearS;

    public String workplace;

        public Overview_Week(){

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
            view= inflater.inflate(R.layout.activity_overview__week, container, false);

            initDaysList();
            return view;
        }

    private void initDaysList() {


        BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getContext());
        //Temporary
        //DatabaseInitializer.populateSync(db);

        String userMail = mSharedPreferences.getString(Constants.EMAIL, null);

        List<Arbeitsort> workplaces = db.arbeitsortDAO().getArbeitsorteForUser(userMail);

        DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Calendar now = Calendar.getInstance(Locale.ENGLISH);
        Integer weekNr = now.get(Calendar.WEEK_OF_YEAR);

        now.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        Date monday= now.getTime();
        String mondayS= df.format(monday);

        now.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Date friday= now.getTime();
        String fridayS = df.format(friday);

        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String [] months =dfs.getMonths();
        Integer actualMonthNumber= now.get(Calendar.MONTH);
        String year = " "+String.valueOf(now.get(Calendar.YEAR));
        String actualMonth= months[now.get(Calendar.MONTH)];
        month_yearS = actualMonth + year;

        now.set(Calendar.WEEK_OF_MONTH,1);
        now.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY) ;
        now.set(Calendar.HOUR_OF_DAY, 0);
        String monthStart= df.format(now.getTime());

        if( !months[now.get(Calendar.MONTH)].equals(actualMonth)){

            now.set(Calendar.MONTH, actualMonthNumber);
            now.set(Calendar.WEEK_OF_MONTH, 2);
        }
        Integer maxWeekCount = now.getActualMaximum(Calendar.WEEK_OF_MONTH);

        now.set(Calendar.WEEK_OF_MONTH, maxWeekCount);
        now.set(Calendar.DAY_OF_WEEK ,Calendar.FRIDAY);
        now.set(Calendar.HOUR_OF_DAY, 23);
        String monthEnd = df.format( now.getTime());



        //getArbeitszeitenOfMonth
            //Startdate, Enddate
        //List<Arbeitszeit> workingTimesOfWeek = db.arbeitszeitDAO().getArbeitszeitenForArbeitsOrt(workplace, monday, friday);

        List<Arbeitszeit> workingTimes = db.arbeitszeitDAO().getArbeitszeitenForArbeitsortBetween(workplace, monthStart, monthEnd);

        Log.d(TAG, "Workplaces found for user with email " + userMail + ": " + workplaces.size());
        Log.d(TAG, "Working Times found for workplace" + workplace + ": " + workplaces.size());

        adapter = new Overview_Details_Week_Adapter(workingTimes, getContext(), workplace);


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
            month_year = view.findViewById(R.id.actual_month);
            month_year.setText(month_yearS);
            ListView lView = (ListView) view.findViewById(R.id.workplace_list_view);
            lView.setAdapter(adapter);
        }

    }
}
