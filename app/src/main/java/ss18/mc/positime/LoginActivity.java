package ss18.mc.positime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void onClick (View view){
        switch(view.getId()){
            case R.id.registerText:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.loginButton:
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);

                Toast.makeText(this, "login", Toast.LENGTH_LONG).show();
                LoginApiCall post = new LoginApiCall();

                post.execute("http://192.168.0.129:8080/api/v1/authenticate",email.getText()+":"+password.getText());


                break;
        }
    }
}
