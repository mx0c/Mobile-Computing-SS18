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

public class Overview_Details_Custom_Adapter extends BaseAdapter implements ListAdapter {

    private List<Arbeitszeit> daysList;
    private Context context;
    private String workplace;

    public Overview_Details_Custom_Adapter(List<Arbeitszeit> list, Context context, String workplace){
        this.daysList = list;
        this.context = context;
        this.workplace = workplace;
    }

    @Override
    public int getCount() {
        return daysList.size();
    }

    @Override
    public Object getItem(int pos) {
        return daysList.get(pos).getStarttime();
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout_details_day, null);
        }

        BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(context);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeDF = new SimpleDateFormat("HH:mm:ss");

        TextView date = view.findViewById(R.id.date);
        TextView timeSum = view.findViewById(R.id.time_sum);
        TextView pauseTime = view.findViewById(R.id.pause_time);
        TextView startTime = view.findViewById(R.id.start_time);
        TextView stopTime = view.findViewById(R.id.stop_time);
        TextView salary = view.findViewById(R.id.salary);

        date.setText(df.format(daysList.get(position).getStarttime()));
        timeSum.setText(calculateWorkTimeString(daysList.get(position).getStarttime(),daysList.get(position).getEndtime()));
        pauseTime.setText(String.valueOf(daysList.get(position).getBreaktime() * daysList.get(position).getAmountBreaks())+ " minutes");
        startTime.setText(getTimeInAmOrPm(timeDF.format(daysList.get(position).getStarttime())));
        stopTime.setText(getTimeInAmOrPm(timeDF.format(daysList.get(position).getEndtime())));
        salary.setText(calculateSalary(Double.valueOf(calculateWorkTime(daysList.get(position).getStarttime(),daysList.get(position).getEndtime())))+" €");

        return view;
    }

    public String calculateSalary(double time){
        double moneyperhour = BenutzerDatabase.getBenutzerDatabase(context).arbeitsortDAO().getMoneyPerHour(workplace);
        String res =  new Double(time * moneyperhour).toString();
        String cents = res.split("\\.")[1]+"0";
        try {
            cents = cents.substring(0, 2);
        }catch(Exception e){}
        return res.split("\\.")[0] +"."+cents;
    }

    public double calculateWorkTime(Date start, Date end) {
        Long diff = end.getTime() - start.getTime();
        Long diffMinutes = diff / (60 * 1000) % 60;
        Long diffHours = diff / (60 * 60 * 1000) % 24;
        return Double.valueOf(diffHours.toString() +  "." + diffMinutes.toString());
    }

    public String calculateWorkTimeString(Date start, Date end){
        Long diff = end.getTime() - start.getTime();
        Long diffMinutes = diff / (60 * 1000) % 60;
        Long diffHours = diff / (60 * 60 * 1000) % 24;

        String minutes = diffMinutes.toString();
        if(minutes.length() == 1){
            minutes += "0";
        }

        return diffHours.toString()+":"+minutes+" hours";
    }

    public String getTimeInAmOrPm(String hh_mm_ss){
        String time = "";
        String [] splitted= hh_mm_ss.split(":");
        int h = Integer.parseInt(splitted[0]);

        if(h < 12 ){
            time= hh_mm_ss+" am";
        }
        else if(h > 12){
            h = h -12 ;
            if(h> 9){
                time= "0"+h+ ":" + splitted[1] +":" + splitted[2] +" pm";
            }
            else{
                time= h+":" + splitted[1]+":" + splitted[2]+ " pm";
            }
        }
        else{
            int min= Integer.parseInt(splitted[1]);
            int sec = Integer.parseInt(splitted[2]);
            if( min > 0 || sec > 0){
                time= hh_mm_ss+ " pm";
            }
            else{
                time= hh_mm_ss+ " noon";
            }
        }

        return time;
    }
}

