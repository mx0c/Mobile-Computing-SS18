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
import java.time.Year;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ss18.mc.positime.R;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;

public class Overview_Details_Month_Adapter extends BaseAdapter implements ListAdapter {

    private List<Arbeitszeit> workingtimes_months;
    DateFormat df;
    private Context context;
    private BenutzerDatabase db;
    private String workplace;


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

        df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Collections.sort(workingtimes_months, new Comparator<Arbeitszeit>() {
            @Override
            public int compare(Arbeitszeit o1, Arbeitszeit o2) {
                return o1.getStarttime().compareTo(o2.getStarttime());
            }
        });

        Date actualYearD= new Date();
        actualYearD= workingtimes_months.get(0).getStarttime();
        String[] year_splitted = df.format(actualYearD).split(" ");
        String dateS=year_splitted[0];
        String yearS= dateS.split("-")[0];


        Calendar now = Calendar.getInstance();
        Integer actual_weekNr = now.get(Calendar.WEEK_OF_YEAR);

        //getallDataOfCalendarWeek

        db = BenutzerDatabase.getBenutzerDatabase(context);


        /*TextView date = (TextView) view.findViewById(R.id.DATE);
        Date date_break= workingtimes_months.get(position).getWorkday();
        String dateS= df.format(date_break);
        String [] splitted_date = dateS.split(" ");
        date.setText(splitted_date[0]);*/


        return view;
    }

}
