package ss18.mc.positime;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.WindowManager;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import android.content.Context;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.widget.PopupWindow;
import android.widget.Button;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;
import ss18.mc.positime.utils.TimestampConverter;

import android.widget.Toast;
public class FourthFragment extends Fragment {
    Date dateClick;
    TextView month_text;
    CompactCalendarView calendar;
    SimpleDateFormat dtf;
    String[] month_list;
    View view;
    Date current_Date;
    private Context mContext;
    BroadcastReceiver broadcastReceiver;
    SharedPreferences mSharedPreferences;
    static Long clickedDate_Epochtime;
    String workplace;
    TextView currDate;
    TextView startTime;
    TextView endTime;
    TextView sumTime;
    public FourthFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    initSharedPreferences();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_fourth, container, false);
        
        calendar= (CompactCalendarView)view.findViewById(R.id.calendar);
        calendar.setUseThreeLetterAbbreviation(true);
        calendar.displayOtherMonthDays(true);

        dtf= new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        month_text= view.findViewById(R.id.month);
        return view;
    }
    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
    @Override
    public  void onResume(){
        super.onResume();
        //dateClick = Calendar.getInstance().getTime();
        Date date= Calendar.getInstance().getTime();
        Calendar c= Calendar.getInstance(TimeZone.getDefault());
        Integer numberOfMonth= c.get(Calendar.MONTH);
        Integer year= c.get(Calendar.YEAR);
        DateFormatSymbols dfs= new DateFormatSymbols(Locale.ENGLISH);
        month_list= dfs.getShortMonths();
        String month= month_list[numberOfMonth];
        month_text.setText(month+ " "+ year);
        TimestampConverter time = new TimestampConverter();
        current_Date = time.fromTimestamp("2018-06-04 01:35:00");


        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //clickedDate_Epochtime= dateClicked.getTime();
                dateClick = dateClicked;
                String date= dateClicked.toString();
                String[] date_split= date.split(" ");
                Intent i= new Intent(getActivity(), FragmentTab.class);
                String day_number= date_split[2];
                Integer month_number= Arrays.asList(month_list).indexOf(date_split[1])+1;
                String year_number= date_split[5];
                //Date format: dd-mm-yyyy
                String result= day_number+"-"+month_number.toString()+"-"+year_number;



                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_calender, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, 700, 900);
                // define your view here that found in popup_layout
                // for example let consider you have a button
                currDate = (TextView) popupView.findViewById(R.id.currDate);
                startTime = (TextView) popupView.findViewById(R.id.startTime);
                endTime = (TextView) popupView.findViewById(R.id.endTime);
                sumTime = (TextView) popupView.findViewById(R.id.sumTime);
                Button btn = (Button) popupView.findViewById(R.id.button_test);
                showData(result);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                //i.putExtra("DATE", result);
                //getActivity().setResult(100, i);
                //getActivity().finish();

            }
            public void getBackgroundInfo(){
                broadcastReceiver = new BroadcastReceiver(){
                    public void onReceive(Context context, Intent intent){

                        Bundle test = intent.getExtras();
                        workplace = (String) test.get("current_workplace_name");


                    }
                };
                getActivity().registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month_text.setText(dtf.format(firstDayOfNewMonth));
            }
            public void showData(String result){
                //Ueberpruefen ob Datum in Datenbank
                BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getActivity());
                //Temporary
                DatabaseInitializer.populateSync(db);
                getBackgroundInfo();
                String userMail = mSharedPreferences.getString(Constants.EMAIL, null);

                DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fridayS = df.format(dateClick);
                String[] ergS = fridayS.split(" ");
                String workDayMorning = ergS[0] + " 00:00:00";
                String workDayEvening = ergS[0] + " 59:59:59";
                List<Arbeitszeit> test1 = db.arbeitszeitDAO().getArbeitszeitenForArbeitsortBetween(workplace,workDayMorning,workDayEvening);
                //Check if data is available

                if(test1.size() != 0){
                    currDate.setText(ergS[0]);
                    String[] ergDayStart = test1.get(0).getStarttime().toString().split(" ");
                    startTime.setText("Start time:  "+ergDayStart[3] + " Uhr");
                    String[] ergDayEnd = test1.get(0).getEndtime().toString().split(" ");
                    endTime.setText("End time:  "+ergDayEnd[3]+ " Uhr");
                    long diff = test1.get(0).getWorktime();
                    long[] hoursMins = new long[2];
                    long t = TimeUnit.SECONDS.toMinutes(diff);
                    //hours
                    long erg1 = t/60;
                    //minutes
                    long ergMins = t - erg1*60;
                    hoursMins[0] = erg1;
                    hoursMins[1] = ergMins;
                    sumTime.setText("Worked time:   "+hoursMins[0]+" h  "+hoursMins[1]+" min");
                }
                else{
                    currDate.setText("No data available.");
                }

            }
        });

    }



}
