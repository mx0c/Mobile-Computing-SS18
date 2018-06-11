package ss18.mc.positime;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import ss18.mc.positime.model.Response;
import ss18.mc.positime.model.User;
import ss18.mc.positime.network.NetworkUtil;
import ss18.mc.positime.utils.Validation;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout emailField;
    TextInputLayout passwordField;
    TextInputLayout passwordFieldConf;
    TextInputLayout firstname;
    TextInputLayout lastname;
    ProgressBar progressBar;

    private CompositeSubscription mSubscriptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Return Button
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Dont open keyboard on activity start

        initViews();
        mSubscriptions = new CompositeSubscription();

    }

    void initViews() {
        emailField = (TextInputLayout) findViewById(R.id.emailInpt);
        passwordField = (TextInputLayout) findViewById(R.id.passInpt);
        passwordFieldConf = (TextInputLayout) findViewById(R.id.passInptConf);
        firstname = (TextInputLayout) findViewById(R.id.firstname);
        lastname = (TextInputLayout) findViewById(R.id.lastname);
        progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);
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

    void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                //Check if all fields are filled correctly before sending a request

                TextInputLayout[] allFields = {emailField, passwordField, passwordFieldConf, firstname, lastname};

                if (!Validation.checkForEmptyFields(allFields)) {
                    Toast.makeText(this, R.string.notAllFields, Toast.LENGTH_SHORT).show();
                } else if (!Validation.validateEmail(emailField)) {
                    Toast.makeText(this, R.string.wrongEmail, Toast.LENGTH_SHORT).show();
                } else if (!Validation.validateFields(passwordField.getEditText().getText().toString(), passwordFieldConf.getEditText().getText().toString())) {
                    Toast.makeText(this, R.string.noPasswordMatch, Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setEmail(emailField.getEditText().getText().toString());
                    user.setPassword(passwordField.getEditText().getText().toString());
                    user.setFirstName(firstname.getEditText().getText().toString());
                    user.setLastName(lastname.getEditText().getText().toString());

                    progressBar.setVisibility(View.VISIBLE); //Show Progressbar
                    registerProcess(user);
                }

        }
    }

    private void registerProcess(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        progressBar.setVisibility(View.INVISIBLE);
        showSnackBarMessage(response.getMessage());
    }

    private void handleError(Throwable error) {

        progressBar.setVisibility(View.INVISIBLE);

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

        if (findViewById(android.R.id.content) != null) {

            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }


}
