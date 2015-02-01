package jpdk.geochat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PollingService extends Service implements LocationListener
{
    private static final String TAG = "Pollingservice";
    private String retrieveURL = "http://geochat-api.herokuapp.com/messages";

    public double lat = 0, lng = 0;

    LocationManager locationManager;
    boolean initialLoad = true;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("Congrats! MyService Created");
        Log.d(TAG, "onCreate");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("Congrats! MyService started");
        Log.d(TAG, "onStart");

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        //RetrieveAllMessages();
    }

    @Override
    public void onDestroy() {
        System.out.println("Congrats! MyService stopped");
        Log.d(TAG, "onDestroy");
    }

    public void UpdateMessages() {
        new UpdateMessageTask().execute("");
    }

    /*
    public void RetrieveAllMessages(){
        new RetrieveAllMessageTask().execute("");
    }
    */

    public void PopNotifications() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notif_icon)
                        .setContentTitle("New Message Discovered!")
                        .setContentText("Click to locate!");
    }

    public void parseJSON(String json) {
        System.out.println("parsing!");
        //System.out.println(json);

        ArrayList<Message> newMessages = new ArrayList<Message>();

        try {
            JSONArray msgArray = new JSONArray(json);


            for (int i = 0; i < msgArray.length(); i++){
                JSONObject jsonMsg = msgArray.getJSONObject(i);

                String id = jsonMsg.getString("_id");
                //String user = jsonMsg.getString("user");
                String user = "";

                String msg = jsonMsg.getString("message");
                JSONArray coordinate = jsonMsg.getJSONArray("loc");
                double lat = coordinate.getDouble(0);
                double lng = coordinate.getDouble(1);
                String sentAt = jsonMsg.getString("sentAt");
                String sendAt = jsonMsg.getString("sendAt");
                int dur = jsonMsg.getInt("duration");

                Message message = new Message (user, msg, id, lat, lng, sentAt, sendAt, dur);
                newMessages.add(message);

                //message.speak();
            }

            //MapsActivity.messages.addAll(newMessages);
            MapsActivity.messages = newMessages;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Location Listener
    public void onLocationChanged(Location location) {
        if (initialLoad) {
            initialLoad = false;
            //RetrieveAllMessages();
        }

        UpdateMessages();
        //MapsActivity.updateMarkers();

        lat =  location.getLatitude();
        lng = location.getLongitude();

        System.out.println(lat + " " + lng);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

    private class UpdateMessageTask extends AsyncTask<String, Void, String> {
        private String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }

        @Override
        protected String doInBackground(String... params) {
            Criteria criteria = new Criteria();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);

            System.out.println("bg " + lat + " " + lng);

            String result = "";
            InputStream inputStream = null;

            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(retrieveURL + "?lat=" + lat + "&lng=" + lng + "&since=1");

            try{
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if(statusCode == 200){
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                } else {
                    System.out.println("failed");
                }
            }catch(ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            System.out.println(builder.toString());

            parseJSON(builder.toString());

            return "Done";
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