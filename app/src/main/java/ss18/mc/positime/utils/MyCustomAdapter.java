package ss18.mc.positime.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ss18.mc.positime.LoginActivity;
import ss18.mc.positime.R;
import ss18.mc.positime.Workplace;
import ss18.mc.positime.Workplace_add_edit;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.local.BenutzerDatabase;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private List<Arbeitsort> list;
    private Context context;
    private BenutzerDatabase db;


    public MyCustomAdapter(List<Arbeitsort> list, Context context) {
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
            view = inflater.inflate(R.layout.my_custom_list_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getPlaceName());

        //Handle buttons and add onClickListeners
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.delete_Btn);
        ImageView editBtn = (ImageView) view.findViewById(R.id.edit_Btn);


        //Click on delete button
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                //list.remove(position); //or some other task
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(R.string.workplace_delete_title);
                alertDialog.setMessage("Do you really want to delete the workplace? If you select yes all data to the workplace will be deleted"); //TODO Make it work with strings.xml
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Database
                                db = BenutzerDatabase.getBenutzerDatabase(context);
                                Arbeitsort ab = db.arbeitsortDAO().getOneArbeitsortForBenutzer(list.get(position).getPlaceName(), list.get(position).getBenutzer_mail());

                                //Delete Arbeitsort
                                db.arbeitsortDAO().delete(ab); //Remove from database

                                list.remove(position); //Remove from list
                                notifyDataSetChanged();
                                Toast.makeText(v.getContext(), R.string.workplace_deleted, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                notifyDataSetChanged();
            }
        });

        //Click on edit button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_workplace = new Intent(context, Workplace_add_edit.class);
                edit_workplace.putExtra("source", "edit"); //TODO Add to constants
                edit_workplace.putExtra("workplace", list.get(position).getPlaceName()); //TODO Add to constants
                context.startActivity(edit_workplace);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}