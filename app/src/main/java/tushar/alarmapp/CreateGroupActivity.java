package tushar.alarmapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.EditText;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CreateGroupActivity extends ActionBarActivity {
    String username;
    String groupname;
    String alarminterval = "2";
    String groupIdValue;
    String hour;
    String minute;
    String value;
    private TimePicker alarmTimePicker;
    private EditText groupNameField;
    public final static String EXTRA_MESSAGE = "tushar.alarmapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Intent intent = getIntent();
        username = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);
        Log.i("CreateAccountfuck", username);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        groupNameField = (EditText) findViewById(R.id.groupname);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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

    public void createGroup(View view) {
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, );
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());*/

        hour = alarmTimePicker.getCurrentHour().toString();
        minute = alarmTimePicker.getCurrentMinute().toString();
        groupname = groupNameField.getText().toString();
        groupNameField.setText("");
        String alarmTime = hour+":"+minute;
        /*Map<String, String> value = new HashMap<String, String>();
        value.put("owner" , username);
        value.put("groupname" , groupname);
        value.put("alarm_time" , hour+":"+minute);
        value.put("alarm_interval", alarminterval);
        String json = new GsonBuilder().create().toJson(value, Map.class);*/
        value = "/"+groupname+"/"+alarmTime+"/"+alarminterval;
        String url = "http://158.130.109.100:3000/createGroup"+value;
        Request requestTask = new Request();
        requestTask.execute(url);
    }

    private class Request extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);

                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);
                JSONObject jsonObj = new JSONObject(response);
                groupIdValue = jsonObj.getString("groupId");
                Intent createGroupIntent = new Intent(CreateGroupActivity.this, HomeActivity.class);
                createGroupIntent.putExtra(EXTRA_MESSAGE, username+"#"+groupIdValue);
                startActivity(createGroupIntent);
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
