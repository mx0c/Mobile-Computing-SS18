package ss18.mc.positime;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Overview_Details_Day_Adapter;

public class Overview_Custom extends Fragment implements View.OnClickListener {

    View v;
    Button date1_button;
    Button date2_button;
    EditText dateStart;
    EditText dateEnd;
    Button searchButton;

    private Date mStartDate;
    private Date mEndDate;
    private String mSelectedWorkplace;
    private ListView mListView;
    private List<Arbeitszeit> mWorkingTimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_workplace__details__custom, container, false);

        mSelectedWorkplace = getActivity().getIntent().getExtras().getString("workplace");
        mListView = v.findViewById(R.id.customList);

        dateStart = v.findViewById(R.id.dateStart);
        dateEnd = v.findViewById(R.id.dateEnd);

        date1_button = v.findViewById(R.id.date1_button);
        if(date1_button != null){
            date1_button.setOnClickListener(this);
        }

        date2_button = v.findViewById(R.id.date2_button);
        if(date2_button != null) {
            date2_button.setOnClickListener(this);
        }
        searchButton = v.findViewById(R.id.searchButton);
        //searchButton.setEnabled(false);
        if(searchButton != null) {
            searchButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()){
            case R.id.date1_button:
                i = new Intent(getActivity(), Select_date_calendar.class);
                startActivityForResult(i, 100);
                break;
            case R.id.date2_button:
                i = new Intent(getActivity(), Select_date_calendar.class);
                startActivityForResult(i, 200);
                break;
            case R.id.searchButton:
                try {
                    mStartDate = new SimpleDateFormat("dd-mm-yyyy").parse(dateStart.getText().toString());
                    mEndDate = new SimpleDateFormat("dd-mm-yyyy").parse(dateEnd.getText().toString());
                } catch (ParseException e){
                    e.printStackTrace();
                }
                BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getActivity());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mWorkingTimes = db.arbeitszeitDAO().getArbeitszeitenForArbeitsortBetween(mSelectedWorkplace, df.format(mStartDate), df.format(mEndDate));
                mListView.setAdapter(new Overview_Details_Day_Adapter(mWorkingTimes,getActivity(), mSelectedWorkplace));
                break;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);
        String date= intent.getStringExtra("DATE");
        if(reqCode == 100 && resultCode == 100 ){
            //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
            dateStart.setText(date);
        }
        if(reqCode == 200 && resultCode == 100){
            //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
            dateEnd.setText(date);
        }
        if (dateStart != null && dateEnd != null) {

            String [] start_dtae= dateStart.getText().toString().split("-");
            Integer start_day= Integer.parseInt(start_dtae[0]);
            Integer start_month= Integer.parseInt(start_dtae[1]);
            Integer start_year= Integer.parseInt(start_dtae[2]);


            String [] end_date= dateEnd.getText().toString().split("-");

            Integer end_day= Integer.parseInt(end_date[0]);
            Integer end_month= Integer.parseInt(end_date[1]);
            Integer end_year= Integer.parseInt(end_date[2]);

            if(start_year > end_year){
                Toast.makeText(getActivity(), "End date is before start date!", Toast.LENGTH_SHORT).show();
                searchButton.setEnabled(false);
            }
            else if(start_year.equals(end_year) ){
                if(start_month > end_month){

                    searchButton.setEnabled(false);
                    Toast.makeText(getActivity(), "End month is before start month!", Toast.LENGTH_SHORT).show();
                }
                else if(start_month == end_month){
                    if(start_day > end_day){

                        searchButton.setEnabled(false);
                        Toast.makeText(getActivity(), "End day is before start day!", Toast.LENGTH_SHORT).show();
                    }
                    else if(start_day== end_day){
                        searchButton.setEnabled(false);
                        Toast.makeText(getActivity(), "Select different days!", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        searchButton.setEnabled(true);
                    }
                }
            }
        }
    }
}
