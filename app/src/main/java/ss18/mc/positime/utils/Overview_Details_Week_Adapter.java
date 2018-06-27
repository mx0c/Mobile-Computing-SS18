package ss18.mc.positime.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ss18.mc.positime.R;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;

public class Overview_Details_Week_Adapter extends BaseAdapter implements ListAdapter {

    private List<Arbeitszeit> workingTimes_week;
    private Context context;
    private BenutzerDatabase db;
    private String workplace;

    int UsedWeekNumber;
    int pauseTime_sum;
    double workingTime_sum;
    List<Integer> savedWeekNumbers = new ArrayList<>();

    DateFormat df;

    TextView actualKw;
    TextView pause_sum;
    TextView worktime_sum;
    TextView salary_sum;

    public Overview_Details_Week_Adapter(List<Arbeitszeit> list, Context context, String workplace){
        this.workingTimes_week = list;
        this.context = context;
        this.workplace = workplace;
    }

    @Override

    public int getCount() {
        return workingTimes_week.size();
    }

    @Override
    public Object getItem(int pos) {
        return workingTimes_week.get(pos).getStarttime();
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout_details_week, null);
        }

        db = BenutzerDatabase.getBenutzerDatabase(context);
        df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Collections.sort(workingTimes_week, new Comparator<Arbeitszeit>() {
            @Override
            public int compare(Arbeitszeit o1, Arbeitszeit o2) {
                return o1.getStarttime().compareTo(o2.getStarttime());
            }
        });

        Date selected_date= workingTimes_week.get(position).getWorkday();

        actualKw = view.findViewById(R.id.actual_cw);
        UsedWeekNumber = getWeekNumber(selected_date);

        if(savedWeekNumbers.contains(Integer.valueOf(UsedWeekNumber))){
            View v_null= new View(context);
                return v_null;
        }
        else {

            savedWeekNumbers.add(UsedWeekNumber);
            actualKw.setText("Week number " + String.valueOf(UsedWeekNumber));

            pause_sum = view.findViewById(R.id.pause_sum);
            worktime_sum = view.findViewById(R.id.time_sum);
            salary_sum = view.findViewById(R.id.money_sum);

            for (Arbeitszeit time : workingTimes_week) {
                Integer weekNr = getWeekNumber(time.getStarttime());

                if (weekNr.intValue() == UsedWeekNumber) {
                    pauseTime_sum += time.getBreaktime();
                    pause_sum.setText(" "+String.valueOf(pauseTime_sum) +" minutes");

                    workingTime_sum += getWorkingTimeInHours(time.getStarttime(), time.getEndtime(), time.getBreaktime()*time.getAmountBreaks());
                    int hours= (int) workingTime_sum;
                    double minutes= workingTime_sum - hours;
                    minutes *= 60;
                    int min = (int) minutes;
                    if ( min < 10){
                        worktime_sum.setText(" "+hours + ":0" + min+ " hours");
                    }
                    else{
                        worktime_sum.setText(" "+hours + ":" + min + " hours");
                    }
                    Double moneyPerHour = db.arbeitsortDAO().getMoneyPerHour(time.getArbeitsort_name());
                    salary_sum.setText(String.format(" %.2f €", moneyPerHour*workingTime_sum));
                }
            }

            pauseTime_sum = 0;
            workingTime_sum= 0.0;
            return view;
            }
    }

    public Integer getWeekNumber(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer WN= cal.get(Calendar.WEEK_OF_YEAR);

        return WN;
    }

    public Double getWorkingTimeInHours(Date start, Date end, int breakTime){
        String startTime = df.format(start);
        String [] splitted_startTime= startTime.split(" ");
        String [] startTime_splitted_calculation = splitted_startTime[1].split(":");
        Integer startH = Integer.parseInt(startTime_splitted_calculation[0]);
        Integer startMin = Integer.parseInt(startTime_splitted_calculation[1]);

        String stopTime = df.format(end);
        String [] splitted_stopTime= stopTime.split(" ");
        String [] endTime_splitted_calculation = splitted_stopTime[1].split(":");
        Integer endH = Integer.parseInt(endTime_splitted_calculation[0]) ;
        Integer endMin = Integer.parseInt(endTime_splitted_calculation[1]);

        Double diff_calculation_hours = (endH - startH) * 60.0;
        Double diff_calulation_minutes = endMin -startMin - 0.0;

        diff_calulation_minutes -= breakTime;

        Double result_time_calculation_minutes = diff_calculation_hours + diff_calulation_minutes + 0.0; //in minutes
        Double result_time_calc_hours = result_time_calculation_minutes / 60;

        return result_time_calc_hours;
    }
}
