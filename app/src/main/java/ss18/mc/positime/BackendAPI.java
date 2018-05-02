package ss18.mc.positime;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackendAPI extends Activity {



    public class LoginCall extends AsyncTask<String, Void, Boolean> {
        protected Context mContext;

        public LoginCall(Context context) {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String data = "Basic " + Base64.encodeToString(params[1].getBytes(), Base64.NO_WRAP);

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", data);
                connection.setRequestProperty("Accept", "*/*");

                connection.setDoOutput(true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(params[1]);
                writer.close();

                Log.e("Post", data);
                Log.e("URL", url.toString());
                connection.connect();
                if (connection.getResponseCode() == 400) {
                    connection.disconnect();
                    return false;
                }

                Log.e("Response", connection.getResponseMessage() + "");
                InputStream stream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                String response = builder.toString();
                this.saveJWT(response);
            } catch (Exception e) {
                Log.e(e.toString(), "Something with request");
            }
            return true;
        }

        private void saveJWT(String JWT){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("JWT",JWT);
            editor.apply();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                //TODO: change Activity
                Toast.makeText(this.mContext, "Login successful", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this.mContext, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
