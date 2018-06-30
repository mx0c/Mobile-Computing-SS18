package ss18.mc.positime.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ss18.mc.positime.R;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;

public class Overview_Details_Month_Adapter extends BaseAdapter implements ListAdapter {

    private List<Arbeitszeit> workingtimes_months;
    DateFormat df;
    private Context context;
    private BenutzerDatabase db;
    private String workplace;

    int actualMonthNumber;
    List<Integer> savedMonthNumbers = new ArrayList<>();

    TextView pause_sum;
    TextView worktime_sum;
    TextView salary_sum;
    TextView month_text;

    int pauseTime_sum;
    double workingTime_sum;


    public Overview_Details_Month_Adapter(List<Arbeitszeit> list, Context context, String worklplace){
        this.workingtimes_months = list;
        this.context = context;
        this.workplace = worklplace;
    }
    @Override

    public int getCount() {
        return workingtimes_months.size();
    }

    @Override
    public Object getItem(int pos) {
        return workingtimes_months.get(pos).getBreaktime();
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
            view = inflater.inflate(R.layout.custom_list_layout_details_month, null);
        }
        db = BenutzerDatabase.getBenutzerDatabase(context);
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //Order list of working days
        Collections.sort(workingtimes_months, new Comparator<Arbeitszeit>() {
            @Override
            public int compare(Arbeitszeit o1, Arbeitszeit o2) {
                return o1.getStarttime().compareTo(o2.getStarttime());
            }
        });

        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String [] months =dfs.getMonths();

        Calendar now = Calendar.getInstance();
        Date firstDate = workingtimes_months.get(position).getStarttime();
        now.setTime(firstDate);

        actualMonthNumber = now.get(Calendar.MONTH);
        month_text = view.findViewById(R.id.month_text);

        if (savedMonthNumbers.contains(actualMonthNumber)) {
            View v_null= new View(context);
            return v_null;
        } else {
            savedMonthNumbers.add(actualMonthNumber);
            month_text.setText(months[actualMonthNumber]);

            pause_sum = view.findViewById(R.id.pause_sum);
            worktime_sum = view.findViewById(R.id.time_sum);
            salary_sum = view.findViewById(R.id.money_sum);

            for (Arbeitszeit time : workingtimes_months) {
                Integer monthNr = getMonthNumber(time.getStarttime());

                if (monthNr.intValue() == actualMonthNumber) {
                    pauseTime_sum += time.getBreaktime() * time.getAmountBreaks();
                    pause_sum.setText(" "+String.valueOf(pauseTime_sum) + " minutes");

                    workingTime_sum += getWorkingTimeInHours(time.getStarttime(), time.getEndtime(), time.getBreaktime()*time.getAmountBreaks());
                    int hours= (int) workingTime_sum;
                    double minutes= workingTime_sum - hours;
                    minutes *= 60;
                    int min = (int) minutes;
                    if ( min < 10){
                        worktime_sum.setText(" "+hours + ":0" + min+ " hours");

                    }
                    else{
                        worktime_sum.setText(" "+hours + ":" + min+ " hours");
                    }

                    Double moneyPerHour = db.arbeitsortDAO().getMoneyPerHour(time.getArbeitsort_name());
                    salary_sum.setText(String.format(" %.2f €", moneyPerHour * workingTime_sum));
                }
            }
            pauseTime_sum = 0;
            workingTime_sum = 0.0;
            return view;
        }
    }


    public Integer getMonthNumber(Date date){

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer monthNr = c.get(Calendar.MONTH);

        return monthNr;
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
