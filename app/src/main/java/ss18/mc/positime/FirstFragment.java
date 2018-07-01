package ss18.mc.positime;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ss18.mc.positime.utils.Constants;

public class FirstFragment extends Fragment {
    View view;
    BroadcastReceiver broadcastReceiver;
    public FirstFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**/


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle   savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_first, container, false);


        return view;


    }

    public void initBackgroundServiceInfo(){
        TextView current_workplacename = (TextView) view.findViewById(R.id.current_workplace_fragment);
        TextView current_pos = (TextView) view.findViewById(R.id.current_position_input);
        TextView current_work_time = (TextView) view.findViewById(R.id.current_work_time);
        TextView current_pause = (TextView) view.findViewById(R.id.current_pause);
        TextView current_money_earned = (TextView) view.findViewById(R.id.current_money_earned);
        TextView current_breaks = (TextView) view.findViewById(R.id.current_breaks_counted);


        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){

                Bundle test = intent.getExtras();
                if(test != null){
                    String current_workplace_name = (String) test.get("current_workplace_name");
                    String current_time_hours = test.get("current_workplace_time_hours").toString();
                    String current_time_mins = test.get("current_workplace_time_").toString();
                    String current_time_pause = test.get("current_workplace_pause_minutes").toString();
                    String current_money = test.get("current_workplace_money_earned").toString();
                    String current_breaks_count = test.get("current_workplace_pause_count").toString();
                    current_workplacename.setText(current_workplace_name);
                    current_pos.setText(current_workplace_name);
                    current_work_time.setText(current_time_hours+"h"+current_time_mins+"min");
                    current_pause.setText(current_time_pause+" min");
                    current_money_earned.setText(current_money+" Euro");
                    current_breaks.setText(current_breaks_count);
                }


            }
        };
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));

    }


}
