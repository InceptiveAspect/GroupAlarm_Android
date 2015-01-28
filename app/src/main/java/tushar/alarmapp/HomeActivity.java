package tushar.alarmapp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends ActionBarActivity {

    String username;
    String groupId = "";
    String[] message;
    public final static String EXTRA_MESSAGE = "tushar.alarmapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "tushar.alarmapp.MESSAGE2";
    EditText searchField;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        if (!intent.getStringExtra(LoginActivity.EXTRA_MESSAGE).equals(null)) {
            message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE).split("#");
            username = message[0];
            if (message.length == 2) {
                groupId = message[1];
            }
        }
        Log.i("Homefuck", username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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

    public void setUpGroup(View view) {
        Intent setUpGroupIntent = new Intent(HomeActivity.this, CreateGroupActivity.class);
        setUpGroupIntent.putExtra(EXTRA_MESSAGE, username);
        startActivity(setUpGroupIntent);
    }

    public void search(View view) {
        searchField = (EditText) findViewById(R.id.search);
        query = searchField.getText().toString();
        Map<String, String> value = new HashMap<String, String>();
        value.put("query" , query);

        String json = new GsonBuilder().create().toJson(value, Map.class);
        Request requestTask = new Request();
        requestTask.execute("http://158.130.109.100:3000/search", json);
    }

    private class Request extends AsyncTask<String, String, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);
                httpPost.setEntity(new StringEntity(params[1]));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);

                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                searchIntent.putExtra(EXTRA_MESSAGE, response);
                searchIntent.putExtra(EXTRA_MESSAGE2, groupId);
                startActivity(searchIntent);

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
