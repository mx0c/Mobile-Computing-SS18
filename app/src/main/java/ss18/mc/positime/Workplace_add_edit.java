package ss18.mc.positime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.w3c.dom.Text;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.Validation;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Workplace_add_edit extends AppCompatActivity {
    private static String TAG = "Workplace_add_edit";
    private final static int PLACE_PICKER_REQUEST = 999;
    SharedPreferences mSharedPreferences;
    BenutzerDatabase db;
    Toolbar toolbar;
    TextInputLayout workplace_name;
    TextInputLayout workplace_chef_ffname;
    TextInputLayout workplace_chef_llname;
    TextInputLayout workplace_hr_per_week;
    TextInputLayout workplace_money_per_hr;
    RadioGroup workplace_currency_radioGroup;
    RadioButton workplace_currency_radio1;
    RadioButton workplace_currency_radio2;
    Button workplace_btn_save;
    Button workplace_select_posi;
    TextView workplace_radius_seek;
    SeekBar workplace_radius_seekbar;
    Arbeitsort ao;

    Double lat = 0.0;
    Double lon = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplace_add_edit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Dont open keyboard on activity start

        //Initialization
        initView();
        initSharedPreferences();


        checkIfCalledByIntent();


        workplace_radius_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                workplace_radius_seek.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /*
        ####################################################################
                                Initialization
        ####################################################################
     */
    //Put view initializations in here
    private void initView() {
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Database
        db = BenutzerDatabase.getBenutzerDatabase(this);
        ao = new Arbeitsort();


        //Edit / Add Workplace activity
        workplace_name = (TextInputLayout) findViewById(R.id.workplace_name);
        workplace_chef_ffname = (TextInputLayout) findViewById(R.id.workplace_chef_ffname);
        workplace_chef_llname = (TextInputLayout) findViewById(R.id.workplace_chef_lname);
        workplace_hr_per_week = (TextInputLayout) findViewById(R.id.workplace_hr_per_week);
        workplace_money_per_hr = (TextInputLayout) findViewById(R.id.workplace_money_per_hr);
        workplace_currency_radioGroup = (RadioGroup) findViewById(R.id.currency_radio);
        workplace_currency_radio1 = (RadioButton) findViewById(R.id.currency_radio1);
        workplace_currency_radio2 = (RadioButton) findViewById(R.id.currency_radio2);
        workplace_btn_save = (Button) findViewById(R.id.workplace_btn_save);
        workplace_select_posi = (Button) findViewById(R.id.workplace_select_posi);
        workplace_radius_seek = (TextView) findViewById(R.id.workplace_radius_seek);
        workplace_radius_seekbar = (SeekBar) findViewById(R.id.workplace_radius_seekbar);

        workplace_radius_seek.setText(String.valueOf(this.workplace_radius_seekbar.getProgress()));
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void checkIfCalledByIntent() {
        String source = getIntent().getExtras().getString("source");
        switch (source) {
            case "edit":
                loadWorkplaceData();
                this.workplace_name.setEnabled(false);
                getSupportActionBar().setTitle(R.string.workplace_edit_activity); //Set title of actionbar
                break;
            case "add":
                getSupportActionBar().setTitle(R.string.workplace_add_activity); //Set title of actionbar
                break;
            default:
                Log.d(TAG, "Intent was not started by any activity");
        }
    }

    public void loadWorkplaceData() {
        String arbeitsort;
        String user = mSharedPreferences.getString(Constants.EMAIL, null);
        arbeitsort = getIntent().getExtras().getString("workplace"); //Get the name of the workplace, passed by the caller intent

        //Fetch data from Database
        ao = db.arbeitsortDAO().getOneArbeitsortForBenutzer(arbeitsort, user);

        //Put text in all the views
        this.workplace_name.getEditText().setText(ao.getPlaceName());
        this.workplace_chef_ffname.getEditText().setText(ao.getChefFistName());
        this.workplace_chef_llname.getEditText().setText(ao.getChefLastName());
        this.workplace_hr_per_week.getEditText().setText(String.valueOf(ao.getWeeklyHours()));
        this.workplace_money_per_hr.getEditText().setText(String.valueOf(ao.getMoneyPerhour()));

        this.workplace_radius_seekbar.setProgress(ao.getRadiusA());
        this.workplace_radius_seek.setText(String.valueOf(ao.getRadiusA()));

        //Currency Handling
        try {
            String currency = ao.getCurrency();
            if (ao.getCurrency().equals("€")) {
                this.workplace_currency_radio1.setText(currency);
                this.workplace_currency_radio1.setChecked(true);
            } else {
                this.workplace_currency_radio2.setText(currency);
                this.workplace_currency_radio2.setChecked(true);
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "Currency was not set in database");
        }

        //Temporary
        this.workplace_select_posi.setText(String.valueOf(ao.getLatA()) + " , " + String.valueOf(ao.getLongA()));
    }


    public void saveWorkplaceData(Boolean update) {
        String arbeitsort;
        double weeklyHrs;
        double moneyPerHr;

        db = BenutzerDatabase.getBenutzerDatabase(this);
        ao = new Arbeitsort();

        //Check if values have been entered before converting
        if (this.workplace_hr_per_week.getEditText().getText().toString().equals("") || this.workplace_hr_per_week.getEditText().getText().equals(null)) {
            weeklyHrs = 0;
        } else {
            weeklyHrs = Double.valueOf(this.workplace_hr_per_week.getEditText().getText().toString());
        }

        if (this.workplace_money_per_hr.getEditText().getText().toString().equals("")) {
            moneyPerHr = 0;
        } else {
            moneyPerHr = Double.valueOf(this.workplace_money_per_hr.getEditText().getText().toString());
        }


        //Put text in all the views
        ao.setPlaceName(this.workplace_name.getEditText().getText().toString());
        ao.setChefFistName(this.workplace_chef_ffname.getEditText().getText().toString());
        ao.setChefLastName(this.workplace_chef_llname.getEditText().getText().toString());
        ao.setBenutzer_mail(mSharedPreferences.getString(Constants.EMAIL, ""));
        ao.setRadiusA(this.workplace_radius_seekbar.getProgress());
        ao.setWeeklyHours(weeklyHrs);
        ao.setMoneyPerhour(moneyPerHr);
        ao.setLongA(this.lon);
        ao.setLatA(this.lat);

        //Currency Handling
        if (this.workplace_currency_radio1.isChecked()) {
            ao.setCurrency(this.workplace_currency_radio1.getText().toString());
        } else if (this.workplace_currency_radio2.isChecked()) {
            ao.setCurrency(this.workplace_currency_radio2.getText().toString());
        }

        try {
            if (update.equals(true)) {
                db.arbeitsortDAO().updateArbeitsort(ao);
                Toast.makeText(this, R.string.workplace_updated, Toast.LENGTH_SHORT).show();
            } else {
                db.arbeitsortDAO().insertAll(ao);
                Toast.makeText(this, R.string.workplace_created, Toast.LENGTH_SHORT).show();
            }

        } catch (SQLiteConstraintException e) {
            Toast.makeText(this, "Workplace with this name already exists, select another name!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }
    }

    /*
     ####################################################################
                          Click Handler
     ####################################################################
    */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workplace_btn_save:
                if (getIntent().getExtras().getString("source").equals("edit")) { //if the activity is in edit mode and wants to save then an update needs to be executed
                    saveWorkplaceData(true);
                    Intent workplace = new Intent(this, Workplace.class);
                    startActivity(workplace);
                } else {
                    saveWorkplaceData(false); //exectue a normal insert
                    Intent workplace = new Intent(this, Workplace.class);
                    startActivity(workplace);
                }
                break;
            case R.id.workplace_select_posi:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    // for activty
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                    // for fragment
                    //startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //Return Button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Log.d(TAG, place.getLatLng().toString());

                Log.v(TAG, String.valueOf(place.getLatLng().latitude));

                //TODO This solution is just temporary
                this.lat = place.getLatLng().latitude;
                this.lon = place.getLatLng().longitude;
                this.workplace_select_posi.setText(this.lat.toString() + " , " + this.lon.toString()); //Set Place Name
            }
        }
    }
}


