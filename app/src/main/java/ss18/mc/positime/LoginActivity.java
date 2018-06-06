package ss18.mc.positime;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.model.Benutzer;
import ss18.mc.positime.model.Response;
import ss18.mc.positime.network.NetworkUtil;
import ss18.mc.positime.services.LocationService;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.Validation;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";

    //View Initialization
    TextInputLayout email;
    TextInputLayout password;
    ProgressBar progressBar;

    BenutzerDatabase database; //Datebase declaration

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Initialization
        initView();
        initSharedPreferences(); //Init default SharedPreferences to safe token

        mSubscriptions = new CompositeSubscription();
        database = BenutzerDatabase.getBenutzerDatabase(this);

        get_runtime_permissions();
        checkTokenProcess();
    }

    /*
        ####################################################################
                                Initialization
        ####################################################################
     */
    private void initView() {
        email = (TextInputLayout) findViewById(R.id.emailLogin);
        password = (TextInputLayout) findViewById(R.id.passwordLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /*
     ####################################################################
                          Click Handler
     ####################################################################
    */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerText:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.loginButton:
                //Validation if all fields are correct

                if (!Validation.checkForEmptyFields(new TextInputLayout[]{email, password})) {
                    Toast.makeText(this, R.string.notAllFields, Toast.LENGTH_SHORT).show();
                } else if (!Validation.validateEmail(email)) {
                    Toast.makeText(this, R.string.wrongEmail, Toast.LENGTH_SHORT).show();
                } else {
                    //Login User
                    loginProcess(email.getEditText().getText().toString(), password.getEditText().getText().toString());
                    //Make progress bar visible to indicate login process
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    /*
     ####################################################################
                         Network Connectivity
     ####################################################################
    */

    //Token Process
    private void checkTokenProcess() {
        String token = this.mSharedPreferences.getString(Constants.TOKEN, null);
        String email = this.mSharedPreferences.getString(Constants.EMAIL, null);

        Log.d(TAG, "Checking if token is valid for " + email + " token: " + token);

        if (token != null && email != null) {
            mSubscriptions.add(NetworkUtil.getRetrofit(token).checkToken(email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleTokenResponse, this::handleError));
        } else {

            return; //Dont do request if there is no token anyways
        }
    }

    private void handleTokenResponse(Response response) {
        progressBar.setVisibility(View.GONE);

        //start dashboard if token is valid
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }



    //Login Process
    private void loginProcess(String email, String password) {
        mSubscriptions.add(NetworkUtil.getRetrofit(email, password).login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }


    private void handleResponse(Response response) {
        progressBar.setVisibility(View.GONE);
        JSONObject message = null;

        Log.d(TAG, "Received Message: " + response.getMessage() + "\n" + response.getUser() + "\n" + response.getToken());

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN, response.getToken());
        editor.putString(Constants.EMAIL, response.getMessage());

        //Convert UserJson from String into a JSONObject
        try {
            message = new JSONObject(response.getUser());
        } catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        }

        try {
            editor.putString(Constants.EMAIL, message.getString(Constants.EMAIL));
            editor.putString(Constants.FIRSTNAME, message.getString(Constants.FIRSTNAME));
            editor.putString(Constants.LASTNAME, message.getString(Constants.LASTNAME));
            editor.putString(Constants.PASSWORD, message.getString(Constants.PASSWORD));
        } catch (JSONException e) {
            Log.e(TAG, "Exception", e);
        } finally {
            editor.apply();
            email.getEditText().setText(null);
            password.getEditText().setText(null);
        }

        //Insert the user into the database if not existing
        try {
            Benutzer user = new Benutzer();
            user.setPassWord(mSharedPreferences.getString(Constants.PASSWORD, ""));
            user.setLastName(mSharedPreferences.getString(Constants.FIRSTNAME, ""));
            user.setFirstName(mSharedPreferences.getString(Constants.LASTNAME, ""));
            user.setEmail(mSharedPreferences.getString(Constants.EMAIL, ""));
            database.benutzerDAO().insertAll(user);
            Log.d(TAG, "Stored new user in database:" + user.getEmail());
        } catch (SQLiteConstraintException e) { //Constraint Exception is thrown if you try to insert an element whose primarykey is already present in the database -> duplicate object
            Log.d(TAG, "User already exists in the database. User wont be persisted locally.", e);
        }

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);

    }


    private void handleError(Throwable error) {

        progressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            error.printStackTrace();
            showSnackBarMessage("Network Error !");
        }
    }


    private void showSnackBarMessage(String message) {
        if (this != null) {
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
        }
    }


    /*
     ####################################################################
                            Permissions
     ####################################################################
    */
    private void check_gps() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("GPS is disabled");
            alertDialog.setMessage("Do you want to enable GPS?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
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
        }
    }

    private void get_runtime_permissions() {
        if (Build.VERSION.SDK_INT < 23) {
            //no need for runtimePermissions
            check_gps();
            Intent i = new Intent(this, LocationService.class);
            startService(i);
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check_gps();
                    Intent i = new Intent(this, LocationService.class);
                    startService(i);
                } else {
                    // permission denied
                    // maybe ask again?
                    //get_runtime_permissions();
                }
                return;
            }
        }
    }


    /*
     ####################################################################
                                    Other
     ####################################################################
    */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
