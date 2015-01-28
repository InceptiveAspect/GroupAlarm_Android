package tushar.alarmapp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;


public class SearchActivity extends ActionBarActivity {
    String response;
    String groupId;
    JSONArray jsonArr;
    JSONObject child;
    //String child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        response = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);
        groupId = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE2);
        try {
            jsonArr = new JSONArray(response);
        }
        catch (JSONException e) {
        }


        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        Log.i("Fuck", jsonArr.length()+"");
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
            child = jsonArr.getJSONObject(i);
            TextView tv = new TextView(this);
            tv.setText(child.getString("username"));
            Button button = new Button(this);
            button.setText("Add to Group");
            button.setId(Integer.parseInt(child.getString("userId")));
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String userId = "" + v.getId();
                    Map<String, String> value = new HashMap<String, String>();
                    value.put("userId", userId);
                    value.put("groupId", groupId);
                    String json = new GsonBuilder().create().toJson(value, Map.class);
                    Request requestTask = new Request();
                    requestTask.execute("http://158.130.109.100:3000/addToGroup", json);
                }
            });
            ll.addView(tv);
            ll.addView(button);

            }
            catch(JSONException e) {
            }
        }
        this.setContentView(sv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
                //String response = EntityUtils.toString(httpEntity);

                //Intent searchIntent = new Intent(SearchActivity.this, HomeActivity.class);
                //searchIntent.putExtra(EXTRA_MESSAGE, response);
                //startActivity(searchIntent);

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
