package jpdk.geochat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;


public class ComposeActivity extends Activity {

    String messageBody = "";
    double lat = 0.0;
    double lng = 0.0;
    int duration = 0;

    String submitURL = "http://geochat-api.herokuapp.com/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);

        System.out.println(lat + " " + lng);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_compose);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expiry, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final TextView charLeft = (TextView) findViewById(R.id.charLeftText);
        final EditText messageArea = (EditText) findViewById(R.id.messageArea);

        messageArea.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charLeft.setHint((140 - messageArea.getText().length()) + " characters left");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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

    public void submit(View view) {
        /*
        Calendar c = Calendar.getInstance();
        long ms = c.getTimeInMillis();
        ms = Integer.valueOf(((EditText) findViewById(R.id.duration)).toString());
        Message message = new Message("-1", ((EditText) findViewById(R.id.messageArea)).toString(), lat, lng, "", ms, duration);
        */
        String message = findViewById(R.id.messageArea).toString();
        System.out.println(message);

        new SubmitMessageTask().execute(message);

        finish();
    }

    private class SubmitMessageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];

            ArrayList<Double> coordinate = new ArrayList<Double>();
            coordinate.add(lat);
            coordinate.add(lng);

            String result = "";
            InputStream inputStream = null;
            String json = "";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(submitURL);

                JSONArray jsonArray = new JSONArray(coordinate);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("loc", jsonArray);
                jsonObject.put("message", message);

                json = jsonObject.toString();

                System.out.println(json);

                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null) {
                    System.out.println("msg submit success");
                }
                else {
                    System.out.println("msg submit failed");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return "message submitted";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}