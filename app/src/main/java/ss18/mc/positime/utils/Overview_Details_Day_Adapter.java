package ss18.mc.positime.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ss18.mc.positime.Edit_details_day;
import ss18.mc.positime.R;
import ss18.mc.positime.Workplace_add_edit;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.ArbeitsortDAO;
import ss18.mc.positime.local.BenutzerDatabase;

public class Overview_Details_Day_Adapter extends BaseAdapter implements ListAdapter {

    private List<Arbeitszeit> list_breaktimes;
    private Context context;
    private BenutzerDatabase db;
    private String worklplace;

    ImageView delete_day;
    ImageView edit_day;
    ImageView add_day;

    Arbeitszeit selected_day;

    int clickedListPosition;

    public Overview_Details_Day_Adapter(List<Arbeitszeit> list, Context context, String workplace) {
        this.list_breaktimes = list;
        this.context = context;
        this.worklplace = workplace;
    }

    @Override
    public int getCount() {
        return list_breaktimes.size();
    }

    @Override
    public Object getItem(int pos) {
        return list_breaktimes.get(pos).getBreaktime();
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout_details_day, null);
        }

        /*delete_day = (ImageView) view.findViewById(R.id.delete_day);
        edit_day = (ImageView) view.findViewById(R.id.edit_day);
        add_day = (ImageView) view.findViewById(R.id.add_day);*/
        notifyDataSetChanged();
        Calendar now = Calendar.getInstance();
        Integer actual_weekNr = now.get(Calendar.WEEK_OF_YEAR);

        //getallDataOfCalendarWeek

        db = BenutzerDatabase.getBenutzerDatabase(context);
        DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TextView date = (TextView) view.findViewById(R.id.date);
        Date date_break= list_breaktimes.get(position).getWorkday();
        String dateS= df.format(date_break);
        String [] splitted_date = dateS.split(" ");
        date.setText(splitted_date[0]);

        TextView breakTime= (TextView) view.findViewById(R.id.pause_time);
        Integer time = list_breaktimes.get(position).getBreaktime();
        breakTime.setText(String.valueOf( list_breaktimes.get(position).getBreaktime()* list_breaktimes.get(position).getAmountBreaks())  + " minutes");

        TextView start_time= (TextView) view.findViewById(R.id.start_time);
        Date start_Time = list_breaktimes.get(position).getStarttime();


        // Splitten der Uhrzeit für Am, Pm
        String startTime = df.format(start_Time);
        String [] splitted_startTime= startTime.split(" ");
        String start_timeAmPm = getTimeInAmOrPm(splitted_startTime[1]);
        //start_time.setText(splitted_startTime[1]);
        start_time.setText(start_timeAmPm);

        TextView stop_time= (TextView) view.findViewById(R.id.stop_time);
        Date stop_Time = list_breaktimes.get(position).getEndtime();

        String stopTime = df.format(stop_Time);
        String [] splitted_stopTime= stopTime.split(" ");
        String stop_timeAmPm = getTimeInAmOrPm(splitted_stopTime[1]);
       // stop_time.setText(splitted_stopTime[1]);
        stop_time.setText(stop_timeAmPm);


        String [] startTime_splitted_calculation = splitted_startTime[1].split(":");
        Integer startH = Integer.parseInt(startTime_splitted_calculation[0]);
        Integer startMin = Integer.parseInt(startTime_splitted_calculation[1]);


        String [] endTime_splitted_calculation = splitted_stopTime[1].split(":");
        Integer endH = Integer.parseInt(endTime_splitted_calculation[0]) ;
        Integer endMin = Integer.parseInt(endTime_splitted_calculation[1]);

        Integer breakTime_calculation = list_breaktimes.get(position).getBreaktime() * list_breaktimes.get(position).getAmountBreaks();

        Double diff_calculation_hours = (endH - startH) * 60.0;
        Double diff_calulation_minutes = endMin -startMin - 0.0;

        diff_calulation_minutes -= breakTime_calculation;

        TextView salary_text = (TextView) view.findViewById(R.id.salary);

        Double result_time_calculation_minutes = diff_calculation_hours + diff_calulation_minutes + 0.0; //in minutes
        double result_time_calc_hours = result_time_calculation_minutes / 60; //hh,mm

        int hours = (int) result_time_calc_hours;
        double  minutes= result_time_calc_hours - hours;
        minutes= minutes * 60;


        Double moneyPerHour = db.arbeitsortDAO().getMoneyPerHour(worklplace);
        salary_text.setText( String.format("%.2f €",moneyPerHour *result_time_calc_hours ));


        int min= (int) minutes;
        TextView timeSum= view.findViewById(R.id.time_sum);
        if(min < 10){
            timeSum.setText( hours + ":0" + min +" hours");

        }
        else{
            timeSum.setText( hours + ":" + min +" hours");
        }



        FloatingActionButton floating_delete= view.findViewById(R.id.floating_delete);
        FloatingActionButton floating_edit = view.findViewById(R.id.floating_edit);


        RelativeLayout day_list = view.findViewById(R.id.day_list);

        day_list.setOnClickListener(new View.OnClickListener() {
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
                alertDialog.setTitle(R.string.workplace_delete_title);
                alertDialog.setMessage("Do you really want to delete the data of this day? If you select yes the data will be deleted");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                selected_day = db.arbeitszeitDAO().getArbeitszeitFromID(list_breaktimes.get(position).getArbeitszeitId());

                                //Delete Arbeitszeit
                                db.arbeitszeitDAO().delete(selected_day); //Remove from database

                                list_breaktimes.remove(position); //Remove from list
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

        floating_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floating_delete.setVisibility(View.INVISIBLE);
                floating_edit.setVisibility(View.INVISIBLE);

                Intent i = new Intent(context, Edit_details_day.class);

                selected_day = db.arbeitszeitDAO().getArbeitszeitFromID(list_breaktimes.get(position).getArbeitszeitId());
                Integer breaktime= selected_day.getBreaktime() * selected_day.getAmountBreaks();
                Date start_time= selected_day.getStarttime();

                String [] starttime_splitted= df.format(start_Time).split(" ");
                //String startTime_hhMMss= starttime_splitted[1];
                i.putExtra("startTime", starttime_splitted[1]);

                Date end_time = selected_day.getEndtime();
                String [] endtime_splitted= df.format(end_time).split(" ");
                i.putExtra("endTime", endtime_splitted[1]);

                i.putExtra("date", starttime_splitted[0]);
                i.putExtra("pause", Integer.toString(breaktime));

                i.putExtra("id", selected_day.getArbeitszeitId());
                ((Activity)context).startActivityForResult(i, 1);
                //context.startActivity(i);

            }

        });


        return view;
    }

    protected void onActivityResult( int reqCode, int resCode, Intent data){
            if(reqCode == 1){
                if(resCode == 2){
                    notifyDataSetChanged();
                }
            }
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
