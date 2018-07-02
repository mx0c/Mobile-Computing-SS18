package ss18.mc.positime;

import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
    private SharedPreferences mSharedPreferences;
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
        DatabaseInitializer.populateSync(db);
        //TEMPORAY FOR TESTS ONLY
        //String userMail = mSharedPreferences.getString(Constants.EMAIL, "Your Email");
        String userMail = "julia@web.de";
        //current_workplace = (String)gettingWorkplace();
        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                Bundle test = intent.getExtras();
                String current_workplace_name = (String) test.get("current_workplace_name");


                if(current_workplace_name.equals("0")){
                    current_workplace = "0";
                }else{
                    current_workplace = current_workplace_name;
                }


            }
        };
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));
        //Getting Data from the Database
        List<Arbeitsort> allOrts = db.arbeitsortDAO().getArbeitsorteForUser(userMail);
        int counter = allOrts.size();
        // Adding child data

        for(int i =0; i< counter; i++){
            listDataHeader.add(allOrts.get(i).getPlaceName().toString());
            List<String> test = new ArrayList<String>();
            if(allOrts.get(i).getPlaceName().toString().equals("toe")){

                //nehme Current Workplace Daten von Marius
                String[] current_inf = new String[5];
                List<String> test1 = new ArrayList<String>();
                broadcastReceiver = new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context context, Intent intent){
                        Bundle test = intent.getExtras();
                        //String current_workplace_name = (String) test.get("current_workplace_name");
                        String[] current_time = test.getString("current_workplace_time").split("\\.");
                        current_inf[0] = current_time[0];
                        current_inf[1] = current_time[1];
                        current_inf[3] = test.get("current_workplace_money_earned").toString();
                        current_inf[4] = test.get("current_workplace_pause_count").toString();

                        test1.add("Duration:     "+current_inf[0]+"h "+current_inf[1]+"min");
                        if(current_inf[2] == null ){
                            test1.add("Breaks:   no breaks");
                        }
                        test1.add("Breaks:   "+current_inf[2]);
                        test1.add("Money Earned:     "+current_inf[3]);
                        test1.add("Breaks Count:     "+current_inf[4]);

                    }
                };
                getActivity().registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));

                listDataChild.put(listDataHeader.get(i), test1);
            }
            else{
                //Check if data available for current date.
                DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curr = new Date();
                String now = df.format(curr);
                List<Arbeitszeit> currDay = db.arbeitszeitDAO().getArbeitszeitenForArbeitsortOneDay(current_workplace,now);
                try{
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
                catch(Exception e){
                    test.add("no information for today.");

                }
                    listDataChild.put(listDataHeader.get(i), test);


        }

        }



    }


    public String gettingWorkplace(){
        final String[] current_information = {new String()};
        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                Bundle test = intent.getExtras();
                String current_workplace_name = (String) test.get("current_workplace_name");


                if(current_workplace_name.equals("0")){
                    current_information[0] = "0";
                }else{
                    current_information[0] = current_workplace_name;
                }


            }
        };
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));

        return current_information[0];
    }
}
