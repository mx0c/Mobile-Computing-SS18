package ss18.mc.positime;

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
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;
import ss18.mc.positime.local.BenutzerDatabase;
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

        //Getting Data from the Database
        List<Arbeitsort> allOrts = db.arbeitsortDAO().getAll();
        // Adding child data
        //dynamisch erzeugen aus DB
        listDataHeader.add(allOrts.get(0).getPlaceName().toString());
        List<String> test = new ArrayList<String>();
        test.add(allOrts.get(0).getCurrency());
        listDataChild.put(listDataHeader.get(0), test);
    }
}
