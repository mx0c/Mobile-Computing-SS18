package ss18.mc.positime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Workplace_add_edit extends AppCompatActivity {
    private static String TAG = "Workplace_add_edit";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplace_add_edit);

        //Initialization
        initView();



        checkIfCalledByIntent();
    }

    //Put view initializations in here
    private void initView() {
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.workplace_add_activity); //Set title of actionbar
        //TODO init your views here
    }

    private void checkIfCalledByIntent(){
        String source = getIntent().getExtras().getString("source");
        switch(source){
            case "edit":
                Toast.makeText(this,source, Toast.LENGTH_SHORT).show();
                break;
            case "add":
                Toast.makeText(this,source, Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d(TAG, "Intent was not started by any activity");
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

}


