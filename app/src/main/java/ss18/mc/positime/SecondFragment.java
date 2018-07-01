package ss18.mc.positime;

import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.HashMap;

import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;

public class SecondFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    String current_workplace;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    BroadcastReceiver broadcastReceiver;
    public SecondFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secound, container, false);
// get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return view;
    }
    /*
     * Creating list with current working dates
     *
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        /*Databaseconnection*/
        BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(getActivity());
        if(Constants.USE_TESTDATA) {
            DatabaseInitializer.populateSync(db);
        }
        //TEMPORAY FOR TESTS ONLY
        String userMail = "ge2thez@gmail.com";
        current_workplace = "Hochschule Reutlingen";
        //Getting Data from the Database
        List<Arbeitsort> allOrts = db.arbeitsortDAO().getArbeitsorteForUser(userMail);
        int counter = allOrts.size();
        // Adding child data

        for(int i =0; i< counter; i++){
            listDataHeader.add(allOrts.get(i).getPlaceName().toString());
            List<String> test = new ArrayList<String>();
            if(allOrts.get(i).getPlaceName().toString().equals(current_workplace)){
                //nehme Current Workplace Daten von Marius
                String[] current_inf = gettingBrodcastInformation();
                test.add("Duration:     "+current_inf[0]+"h "+current_inf[1]+"min");
                test.add("Breaks:   "+current_inf[2]);
                test.add("Money Earned:     "+current_inf[3]);
                test.add("Breaks Count:     "+current_inf[4]);
            }
            else{
                //Check if data available for current date.
                DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curr = new Date();
                String now = df.format(curr);
                List<Arbeitszeit> currDay = db.arbeitszeitDAO().getArbeitszeitenForArbeitsortOneDay(current_workplace,now);
                if(currDay.size() != 0){
                    //Shows only the worked time if any time was worked. We dont save Breaks in our database
                    long diff = currDay.get(0).getEndtime().getTime()-currDay.get(0).getStarttime().getTime();
                    long[] hoursMins = new long[2];
                    long t = TimeUnit.MILLISECONDS.toMinutes(diff);
                    //hours
                    long erg1 = t/60;
                    //minutes
                    long ergMins = t - erg1*60;
                    hoursMins[0] = erg1;
                    hoursMins[1] = ergMins;
                    test.add("Worked time:  "+hoursMins[0]+" h  "+hoursMins[1]+" min");
                    double money = allOrts.get(i).getMoneyPerhour();
                    double[] moneyEarned = {(hoursMins[0]*money),(hoursMins[1]*money)};
                    double moneyEarned_sum = moneyEarned[0]+moneyEarned[1];
                    test.add("Money Earned:     "+moneyEarned_sum);
                }
                test.add("no information for today.");


        }
        listDataChild.put(listDataHeader.get(i), test);
        }



    }
    public String[] gettingBrodcastInformation(){
        String[] current_informations = new String[5];
        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                Bundle test = intent.getExtras();
                if(test != null){
                    current_informations[0] = test.get("current_workplace_time_hours").toString();
                    current_informations[1] = test.get("current_workplace_time_").toString();
                    current_informations[2] = test.get("current_workplace_pause_minutes").toString();
                    current_informations[3] = test.get("current_workplace_money_earned").toString();
                    current_informations[4] = test.get("current_workplace_pause_count").toString();
                }


            }
        };
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));

        return current_informations;
    }
}
