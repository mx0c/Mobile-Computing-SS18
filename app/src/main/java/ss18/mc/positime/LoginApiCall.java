package ss18.mc.positime;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginApiCall extends AsyncTask<String,Void,Boolean> {
    LoginApiCall(){

    }

    @Override
    protected Boolean doInBackground(String... params) {
        String urlString = params[0]; // URL to call
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
            connection.connect();
            if (connection.getResponseCode() == 400){
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
            Log.d("Response", response);
        } catch (Exception e) {
            Log.e(e.toString(), "Something with request");
        }
        return true;
    }

}
