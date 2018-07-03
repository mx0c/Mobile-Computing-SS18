package ss18.mc.positime.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ss18.mc.positime.Edit_details_day;
import ss18.mc.positime.R;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;

public class Overview_Details_Custom_Adapter extends BaseAdapter implements ListAdapter {

    private List<Arbeitszeit> daysList;
    private Context context;
    private String workplace;
    BenutzerDatabase db;
    TextView salary;
    TextView timeSum;
    RelativeLayout dayList;
    Arbeitszeit selected_day;

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

        db = BenutzerDatabase.getBenutzerDatabase(context);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeDF = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TextView date = view.findViewById(R.id.date);
        timeSum = view.findViewById(R.id.time_sum);
        TextView pauseTime = view.findViewById(R.id.pause_time);
        TextView startTime = view.findViewById(R.id.start_time);
        TextView stopTime = view.findViewById(R.id.stop_time);
        salary = view.findViewById(R.id.salary);
        dayList = view.findViewById(R.id.day_list);

        date.setText(df.format(daysList.get(position).getStarttime()));

        setTimeSumAndSalaryText(daysList.get(position).getWorktime());

        pauseTime.setText(String.valueOf(daysList.get(position).getBreaktime() * daysList.get(position).getAmountBreaks())+ " minutes");
        startTime.setText(getTimeInAmOrPm(timeDF.format(daysList.get(position).getStarttime())));
        stopTime.setText(getTimeInAmOrPm(timeDF.format(daysList.get(position).getEndtime())));

        FloatingActionButton floating_delete= view.findViewById(R.id.floating_delete);
        FloatingActionButton floating_edit = view.findViewById(R.id.floating_edit);
        dayList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(floating_delete.getVisibility() == View.VISIBLE){
                    floating_delete.setVisibility(View.INVISIBLE);
                }
                else{
                    floating_delete.setVisibility(View.VISIBLE);
                    floating_delete.setClickable(true);
                }

                if(floating_edit.getVisibility() == View.VISIBLE){
                    floating_edit.setVisibility(View.INVISIBLE);
                }
                else{
                    floating_edit.setVisibility(View.VISIBLE);
                    floating_edit.setClickable(true);
                }
            }


        });

        floating_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Delete Day");
                alertDialog.setMessage("Do you really want to delete the data of this day? If you select yes the data will be deleted");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                selected_day = db.arbeitszeitDAO().getArbeitszeitFromID(daysList.get(position).getArbeitszeitId());

                                //Delete Arbeitszeit
                                db.arbeitszeitDAO().delete(selected_day); //Remove from database

                                daysList.remove(position); //Remove from list
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(), "Day deleted", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                notifyDataSetChanged();

                floating_delete.setVisibility(View.INVISIBLE);
                floating_edit.setVisibility(View.INVISIBLE);

            }
        });

        Date start_Time = daysList.get(position).getStarttime();

        floating_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floating_delete.setVisibility(View.INVISIBLE);
                floating_edit.setVisibility(View.INVISIBLE);

                Intent i = new Intent(context, Edit_details_day.class);

                selected_day = db.arbeitszeitDAO().getArbeitszeitFromID(daysList.get(position).getArbeitszeitId());
                Integer breaktime= selected_day.getBreaktime() * selected_day.getAmountBreaks();
                Date start_time= selected_day.getStarttime();

                String [] starttime_splitted= df_2.format(start_Time).split(" ");
                //String startTime_hhMMss= starttime_splitted[1];
                //i.putExtra("startTime", starttime_splitted[1]);
                i.putExtra("startTime", getTimeInAmOrPm(starttime_splitted[1]));

                Date end_time = selected_day.getEndtime();
                String [] endtime_splitted= df_2.format(end_time).split(" ");
                i.putExtra("endTime", getTimeInAmOrPm(endtime_splitted[1]));
                //i.putExtra("endTime", endtime_splitted[1]);

                i.putExtra("date", starttime_splitted[0]);
                i.putExtra("pause", Integer.toString(breaktime));

                i.putExtra("id", selected_day.getArbeitszeitId());
                ((Activity)context).startActivityForResult(i, 1);
                notifyDataSetChanged();

                //context.startActivity(i);

            }

        });



        return view;
    }

    public void setTimeSumAndSalaryText(int workTimeInSeconds){
        Double workTimeInMinutes = workTimeInSeconds / 60.0;

        Integer worktimeHours = (int) (workTimeInMinutes / 60.0); //hh
        Integer workTimeMinutes = (int) (workTimeInMinutes -(worktimeHours * 60) );     //mm
        Double moneyPerHour = db.arbeitsortDAO().getMoneyPerHour(workplace);
        Double rest=  workTimeMinutes/ 60.0;
        salary.setText( String.format("%.2f €",moneyPerHour *worktimeHours+ rest* moneyPerHour ));
        int min= (int) workTimeMinutes;
        if(min < 10){
            timeSum.setText( worktimeHours + ":0" + min +" hours");

        }
        else{
            timeSum.setText( worktimeHours + ":" + min +" hours");
        }


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
            if(h< 10){
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

