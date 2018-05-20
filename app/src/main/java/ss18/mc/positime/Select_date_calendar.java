package ss18.mc.positime;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


// You need to add this to gradle.build dependencies:
//compile 'com.github.sundeepk:compact-calendar-view:2.0.2.3'


public class Select_date_calendar extends AppCompatActivity {

    TextView month_text;
    CompactCalendarView calendar;
    SimpleDateFormat dtf;
    String[] month_list;

    static Long clickedDate_Epochtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_calendar);
        calendar= (CompactCalendarView)findViewById(R.id.calendar);
        calendar.setUseThreeLetterAbbreviation(true);
        calendar.displayOtherMonthDays(true);

        dtf= new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        month_text= findViewById(R.id.month);

    }
    @Override
    protected  void onResume(){
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

                Intent i= new Intent(Select_date_calendar.this, FragmentTab.class);
                String day_number= date_split[2];
                Integer month_number= Arrays.asList(month_list).indexOf(date_split[1])+1;
                String year_number= date_split[5];

                //Date format: dd-mm-yyyy
                String result= day_number+"-"+month_number.toString()+"-"+year_number;
                i.putExtra("DATE", result);
                setResult(100, i);
                finish();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month_text.setText(dtf.format(firstDayOfNewMonth));
            }
        });

    }
}
