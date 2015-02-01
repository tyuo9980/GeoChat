package jpdk.geochat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ComposeActivity extends Activity {

    String messageBody = "";
    double lat = 0.0;
    double lng = 0.0;
    int duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);

        System.out.println(lat + " " + lng);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_compose);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Message Posted!", Toast.LENGTH_SHORT);
                System.out.println("Message Posted!");
            }
        });

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

    /*
    public submitMessage(Message message){
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lat =  location.getLatitude();
        double lng = location.getLongitude();
        ArrayList<Double> coordinate = new ArrayList<Double>();
        coordinate.add(lat);
        coordinate.add(lng);

        String result = "";
        InputStream inputStream = null;

        try {
            String get = retrieveURL;

            get = get ;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(get);

            String json = "";


            JSONArray jsonArray = new JSONArray(coordinate);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loc", jsonArray);

            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(jsonObject);

            //json = jsonArray2.toString();
            json = jsonObject.toString();

            System.out.println(json);

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);

                parseJSON(result);

                if (MapsActivity.isForeground) {
                    System.out.println("Notifying!");
                    PopNotifications();
                }
            }
            else {
                result = "Did not work!";
            }

            System.out.println(result);
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }*/
}
