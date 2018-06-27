package ss18.mc.positime;
import android.content.Intent;
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
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;
import android.widget.Toast;
public class FourthFragment extends Fragment {
    TextView month_text;
    CompactCalendarView calendar;
    SimpleDateFormat dtf;
    String[] month_list;
    View view;
    private Context mContext;

    SharedPreferences mSharedPreferences;
    static Long clickedDate_Epochtime;
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

        Date date= Calendar.getInstance().getTime();
        Calendar c= Calendar.getInstance(TimeZone.getDefault());
        Integer numberOfMonth= c.get(Calendar.MONTH);
        Integer year= c.get(Calendar.YEAR);
        DateFormatSymbols dfs= new DateFormatSymbols(Locale.ENGLISH);
        month_list= dfs.getShortMonths();


        String month= month_list[numberOfMonth];
        month_text.setText(month+ " "+ year);



        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //clickedDate_Epochtime= dateClicked.getTime();
                String date= dateClicked.toString();
                String[] date_split= date.split(" ");
                Toast.makeText(getActivity(), date,
                 Toast.LENGTH_LONG).show();
                Intent i= new Intent(getActivity(), FragmentTab.class);
                String day_number= date_split[2];
                Integer month_number= Arrays.asList(month_list).indexOf(date_split[1])+1;
                String year_number= date_split[5];

                //Date format: dd-mm-yyyy
                String result= day_number+"-"+month_number.toString()+"-"+year_number;
                //Toast.makeText(getActivity(), result,
                       // Toast.LENGTH_LONG).show();
                showData(result);
                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_calender, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, 700, 900);

                // define your view here that found in popup_layout
                // for example let consider you have a button

                Button btn = (Button) popupView.findViewById(R.id.button_test);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
//Ueberpruefen ob Datum in Datenbank
                BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getActivity());
                //Temporary
                DatabaseInitializer.populateSync(db);

                String userMail = mSharedPreferences.getString(Constants.EMAIL, null);
                //List<Arbeitszeit> test_date = db.arbeitszeitDAO().getWorkdate();
                popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                TextView t = (TextView) popupView.findViewById(R.id.test);
                //t.setText((CharSequence) test_date);

                //i.putExtra("DATE", result);
                //getActivity().setResult(100, i);
                //getActivity().finish();

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

                String userMail = mSharedPreferences.getString(Constants.EMAIL, null);
                //Aktueller Arbeitsort aus Shared Preferences
                String arbeitsort;
                //Arbeitszeit ausgeben fuer Email

            }
        });

    }



}
