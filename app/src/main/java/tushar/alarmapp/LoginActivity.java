package tushar.alarmapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import java.net.Socket;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import com.google.gson.*;
import java.util.*;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class LoginActivity extends ActionBarActivity {

    private Socket client;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Button button;
    private PrintWriter printwriter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onLoginClicked(View view) {
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        usernameField.setText("");
        passwordField.setText("");

        Map<String, String> value = new HashMap<String, String>();
        value.put("username" , username);
        value.put("password" , password);
        Log.i("Fuck", "Added username to map: " + username);
        Log.i("Fuck", "Added password to map: " + password);
        String json = new GsonBuilder().create().toJson(value, Map.class);
        Request requestTask = new Request();
        requestTask.execute("http://158.130.109.100:3000/login", json);
    }

    private class Request extends AsyncTask<String, String, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... params) {
            try {
                HttpPost httpPost = new HttpPost(params[0]);
                httpPost.setEntity(new StringEntity(params[1]));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                return new DefaultHttpClient().execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.i("Fuck", e.toString());
            }
            return null;
        }
    }
}


