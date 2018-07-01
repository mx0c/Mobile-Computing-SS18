package ss18.mc.positime;
import java.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import java.util.ArrayList;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;
public class ThirdFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public ThirdFragment() {
    // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp1);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return view;
    }
    /*
     * Preparing the list data
     * TODO: dynamisch erzeugen, je nach vorhandenen Datensätzen in der Datenbank
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        /*Databaseconnection*/
        BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getActivity());
        DatabaseInitializer.populateSync(db);

        //Getting Data from the Database --> currently only all places. Should be changed in current user. But for demonstration its
        //nicer to see more data
        List<Arbeitsort> allOrts = db.arbeitsortDAO().getAll();
        int counter = allOrts.size();
        for(int i =0; i< counter; i++) {
            listDataHeader.add(allOrts.get(i).getPlaceName().toString());
            List<Arbeitszeit> allTimes = db.arbeitszeitDAO().getArbeitszeitenForArbeitsort(allOrts.get(i).getPlaceName());
            List<String> test = new ArrayList<String>();
            //Check if data available
            if(allTimes.size() != 0){
                //One Day
                long[] workDays = getWorkHoursForOneDay(allTimes);
                test.add("One Day:    "+String.valueOf(workDays[0])+"Stunden "+String.valueOf(workDays[1])+"Minuten");
                //7 Days:
                int b = allTimes.size();
                //Check if saved data size is bigger than 7
                //If yes --> get 7 days and all
                //If no --> only get all
                if(b > 7){
                    long[] work7days = getWorkHours7Days(allTimes);
                    test.add("7 Days:    "+String.valueOf(work7days[0])+"Stunden "+String.valueOf(work7days[1])+"Minuten");
                    long[] workAll = getWorkHoursAll(allTimes);
                    test.add("All:  "+String.valueOf(workAll[0])+"Stunden "+String.valueOf(workAll[1])+"Minuten");
                }
                else{
                    long[] workAll = getWorkHoursAll(allTimes);
                    test.add("All:  "+String.valueOf(workAll[0])+"Stunden "+String.valueOf(workAll[1])+"Minuten");
                }
                listDataChild.put(listDataHeader.get(i), test);
            }
            else{
                test.add("No Data.");
                listDataChild.put(listDataHeader.get(i), test);
            }

        }
    }
    /*******************************************************************************************************************/
    /*HELPING FUNCTIONS FOR GETTING WORKED TIME*/
    private long[] getWorkHoursForOneDay(List<Arbeitszeit> allTimes){
        //work with the worked time in millisecounds. Easier to handle.
        long diff = allTimes.get(0).getEndtime().getTime()-allTimes.get(0).getStarttime().getTime();
        long[] hoursMins = new long[2];
        long t = TimeUnit.MILLISECONDS.toMinutes(diff);
        //hours
        long erg1 = t/60;
        //minutes
        long ergMins = t - erg1*60;
        hoursMins[0] = erg1;
        hoursMins[1] = ergMins;
        return hoursMins;

    }
    private long[] getWorkHours7Days(List<Arbeitszeit> allTimes){
        long[] hoursMins = new long[2];
        long diffGesamt = 0;
        //ntake 7 days which are filled with data (7 workdays)
        //order of saved data not clear. But for Demonstration okay
        for(int i = 0; i< 7; i++){
            diffGesamt += allTimes.get(i).getEndtime().getTime()-allTimes.get(i).getStarttime().getTime();
        }
        long t = TimeUnit.MILLISECONDS.toMinutes(diffGesamt);
        //Hours
        long erg1 = t/60;
        //Minutes
        long ergMins = t - erg1*60;
        hoursMins[0] = erg1;
        hoursMins[1] = ergMins;
        return hoursMins;
    }

    private long[] getWorkHoursAll(List<Arbeitszeit> allTimes){
        long[] hoursMins = new long[2];
        long diffGesamt = 0;
        //Take all days which are filled with data
        for(int i = 0; i< allTimes.size(); i++){
            diffGesamt += allTimes.get(i).getEndtime().getTime()-allTimes.get(i).getStarttime().getTime();
        }
        long t = TimeUnit.MILLISECONDS.toMinutes(diffGesamt);
        //hours
        long erg1 = t/60;
        //minutes
        long ergMins = t - erg1*60;
        hoursMins[0] = erg1;
        hoursMins[1] = ergMins;
        return hoursMins;
    }
    /*******************************************************************************************************************/
}
