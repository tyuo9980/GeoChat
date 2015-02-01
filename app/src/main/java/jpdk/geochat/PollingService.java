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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class PollingService extends Service implements LocationListener
{
    private static final String TAG = "Pollingservice";
    private String retrieveURL = "http://geochat-api.herokuapp.com/messages";

    LocationManager locationManager;

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

        UpdateMessages();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, this);
    }

    @Override
    public void onDestroy() {
        System.out.println("Congrats! MyService stopped");
        Log.d(TAG, "onDestroy");
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void UpdateMessages() {
        new RetrieveMessageTask().execute("");

    }

    public void PopNotifications() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notif_icon)
                        .setContentTitle("New Message Discovered!")
                        .setContentText("Click to locate!");
    }

    public void parseJSON(String json) {
        ArrayList<Message> newMessages = new ArrayList<Message>();

        try {
            JSONArray msgArray = new JSONArray(json);

            for (int i = 0; i < msgArray.length(); i++){
                JSONObject jsonMsg = msgArray.getJSONObject(i);

                String id = jsonMsg.getString("id");
                String user = jsonMsg.getString("user");
                String msg = jsonMsg.getString("message");
                JSONArray coordinate = jsonMsg.getJSONArray("loc");
                double lat = Double.parseDouble(coordinate.getJSONObject(0).toString());
                double lng = Double.parseDouble(coordinate.getJSONObject(0).toString());
                String sentAt = jsonMsg.getString("sentAt");
                String sendAt = jsonMsg.getString("sendAt");
                int dur = jsonMsg.getInt("duration");

                Message message = new Message (user, msg, id, lat, lng, sentAt, sendAt, dur);
                newMessages.add(message);
            }

            MapsActivity.messages.addAll(newMessages);
        }
        catch(Exception e){}
    }

    //Location Listener
    public void onLocationChanged(Location location) {
        UpdateMessages();

        System.out.println("Moved 5 Meters!");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

    private class RetrieveMessageTask extends AsyncTask<String, Void, String> {
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
            double lat =  location.getLatitude();
            double lng = location.getLongitude();
            String[] coordinate = {Double.toString(lat), Double.toString(lng)};

            String result = "";
            InputStream inputStream = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(retrieveURL);

                String json = "";


                JSONArray jsonArray = new JSONArray(Arrays.asList(coordinate));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("loc", jsonArray);

                JSONArray jsonArray2 = new JSONArray();
                jsonArray2.put(jsonObject);

                json = jsonArray2.toString();
                //json = jsonObject.toString();

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
                        PopNotifications();
                    }
                }
                else {
                    result = "Did not work!";
                }

                System.out.println(result + "t");
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {}

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}