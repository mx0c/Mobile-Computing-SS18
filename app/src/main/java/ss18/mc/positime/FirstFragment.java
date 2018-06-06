package ss18.mc.positime;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FirstFragment extends Fragment {

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
        View view =  inflater.inflate(R.layout.fragment_first, container, false);
        TextView current_pos = (TextView) view.findViewById(R.id.current_position_input);
        current_pos.setText("Im Bett");

        TextView current_work_time = (TextView) view.findViewById(R.id.current_work_time);
        current_work_time.setText("0 Stunden");

        TextView current_pause = (TextView) view.findViewById(R.id.current_pause);
        current_pause.setText("2 Stunden");

        TextView current_money_earned = (TextView) view.findViewById(R.id.current_money_earned);
        current_money_earned.setText("5 euro");

        TextView current_breaks = (TextView) view.findViewById(R.id.current_breaks_counted);
        current_breaks.setText("5");

        return view;


    }




}
