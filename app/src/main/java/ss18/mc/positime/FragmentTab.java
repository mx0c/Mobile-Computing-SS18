package ss18.mc.positime;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTab extends Fragment implements View.OnClickListener {

    Button button;
    View v;

    //Custom-View
    Button date1_button;
    Button date2_button;
    EditText dateStart;
    EditText dateEnd;
    Button searchButton;

    //Day-View
    LinearLayout data1;
    FloatingActionButton add_button;
    FloatingActionButton edit_button;
    FloatingActionButton delete_button;
    TextView add_text;
    TextView edit_text;
    TextView delete_text;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fragment_tab, container, false);
        button = (Button) v.findViewById(R.id.button);

        // ToDO:
        //Information of Day/ Month ...
        button.setText("Angaben fehlen nocht");


        if(this.getTag().toString() =="day") {
            v = inflater.inflate(R.layout.workplace_details_day, container, false);
        }
        if(this.getTag().toString() =="custom") {
            v = inflater.inflate(R.layout.activity_workplace__details__custom, container, false);
        }

        //Day View:

        data1= v.findViewById(R.id.day_1_details);
        if(data1 != null){
            data1.setOnClickListener(this);
        }

        edit_button= v.findViewById(R.id.editButton);
        if(edit_button != null){
            edit_button.setOnClickListener(this);
        }
        add_button= v.findViewById(R.id.addButton);
        if(add_button != null){
            add_button.setOnClickListener(this);
        }

        delete_button= v.findViewById(R.id.deleteButton);
        if(delete_button != null){
            delete_button.setOnClickListener(this);
        }

        add_text= v.findViewById(R.id.addText);
        if(add_text != null){
            add_text.setOnClickListener(this);
        }
        edit_text= v.findViewById(R.id.editText);
        if(edit_text != null){
            edit_text.setOnClickListener(this);
        }

        delete_text= v.findViewById(R.id.deleteText);
        if(delete_text != null){
            delete_text.setOnClickListener(this);
        }
        //Custom View:
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
        switch (v.getId()){
            case R.id.day_1_details:

                data1.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.layout_border_clicked));
                add_button.setVisibility(View.VISIBLE);
                edit_button.setVisibility(View.VISIBLE);
                delete_button.setVisibility(View.VISIBLE);
                add_text.setVisibility(View.VISIBLE);
                edit_text.setVisibility(View.VISIBLE);
                delete_text.setVisibility(View.VISIBLE);

            break;
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
            case R.id.addButton:
                Toast.makeText(getActivity(), "add sth", Toast.LENGTH_LONG).show();
                break;
            case R.id.editButton:
                Toast.makeText(getActivity(), "edit sth", Toast.LENGTH_LONG).show();
                break;
            case R.id.deleteButton:
                Toast.makeText(getActivity(), "deleted", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);
        String date= intent.getStringExtra("DATE");
        if(reqCode == 100 && resultCode == 100 ){
            showIntent(date);
            //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
            changeStartDateText(date);
        }
        if(reqCode == 200 && resultCode == 100){
            showIntent(date);
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

    public void showIntent(String date){
        /*String[] date_split= date.split(" ");
        int day= Integer.parseInt(date_split[2]);
        if(day == 1 || day == 21 || day == 31){
            Toast.makeText(getActivity(), date_split[1]+ " "+ date_split[2]+"st", Toast.LENGTH_SHORT).show();

        }
        else if(day == 2 || day == 22){
            Toast.makeText(getActivity(), date_split[1]+ " "+ date_split[2]+"nd", Toast.LENGTH_SHORT).show();

        }
        else if(day == 3 || day == 23){
            Toast.makeText(getActivity(), date_split[1]+ " "+ date_split[2]+"rd", Toast.LENGTH_SHORT).show();

        }
        else if(day > 3 && day <= 20){
            Toast.makeText(getActivity(), date_split[1]+ " "+ date_split[2]+"th", Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getActivity(), date_split[1]+ " "+ date_split[2]+"th", Toast.LENGTH_SHORT).show();
        }*/
    }

}
