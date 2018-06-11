package ss18.mc.positime.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ss18.mc.positime.R;
import ss18.mc.positime.Workplace;
import ss18.mc.positime.Workplace_Details;
import ss18.mc.positime.Workplace_add_edit;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.local.BenutzerDatabase;

public class Overview_Workplaces_Adapter extends BaseAdapter implements ListAdapter {
    private List<Arbeitsort> list;
    private Context context;
    private BenutzerDatabase db;



    public Overview_Workplaces_Adapter(List<Arbeitsort> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos).getPlaceName();
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.overview_workplaces_adapater_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getPlaceName());

        //Handle buttons and add onClickListeners
        ImageView showDetails = (ImageView) view.findViewById(R.id.show_details);



        //Click on 'show Details'
        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String placeName = list.get(position).getPlaceName();

                Intent i = new Intent(context, Workplace_Details.class);

                i.putExtra("workplace", placeName);

                context.startActivity(i);
            }
        });

        return view;
    }


}
