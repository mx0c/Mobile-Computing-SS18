package ss18.mc.positime;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ss18.mc.positime.R;

public class Overview_Custom extends Fragment implements View.OnClickListener {

    View v;

    Button date1_button;
    Button date2_button;
    EditText dateStart;
    EditText dateEnd;
    Button searchButton;

    public Overview_Custom(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_workplace__details__custom, container, false);


        date1_button= v.findViewById(R.id.date1_button);
        if(date1_button != null){
            date1_button.setOnClickListener(this);
        }

        date2_button=v.findViewById(R.id.date2_button);
        if(date2_button != null) {

            date2_button.setOnClickListener(this);
        }
        searchButton= v.findViewById(R.id.searchButton);
        if(searchButton != null) {
            searchButton.setOnClickListener(this);
        }

        return v;

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.date1_button:
                Intent i= new Intent(getActivity(), Select_date_calendar.class);
                startActivityForResult(i, 100);
                break;
            case R.id.date2_button:
                Intent intent= new Intent(getActivity(), Select_date_calendar.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.searchButton:
                Toast.makeText(getActivity(), "SEARCH", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);
        String date= intent.getStringExtra("DATE");
        if(reqCode == 100 && resultCode == 100 ){
            //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
            changeStartDateText(date);
        }
        if(reqCode == 200 && resultCode == 100){
            //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();

            changeEndDateText(date);
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
    public void changeStartDateText(String date){
        dateStart= getView().findViewById(R.id.dateStart);
        dateStart.setText(date);
    }
    public void changeEndDateText(String date){
        dateEnd= getView().findViewById(R.id.dateEnd);
        dateEnd.setText(date);
    }



}
