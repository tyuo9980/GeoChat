package jpdk.geochat;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends ActionBarActivity {

    public static boolean isForeground;

    public static ArrayList<Message> messages = new ArrayList<Message>();

    public static GoogleMap map;
    public Location currentLocation = null;
    public String messageBody = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        isForeground = true;

        setUpMapIfNeeded();

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lat =  location.getLatitude();
        double lng = location.getLongitude();
        LatLng coordinate = new LatLng(lat, lng);

        CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);

        //start polling service
        startService(new Intent(this, PollingService.class));
    }

    @Override
    protected void onPause(){
        super.onPause();
        isForeground = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.map_friends:
                openFriends();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openFriends() {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            map.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (map != null) {


                map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location loc) {
                        // TODO Auto-generated method stub
                        currentLocation = loc;

                        map.clear();
                    }
                });
            }
        }
    }

    public void compose(View view) {
        Intent intent = new Intent(this, ComposeActivity.class);
        intent.putExtra("lat", currentLocation.getLatitude());
        intent.putExtra("lng", currentLocation.getLongitude());
        startActivity(intent);
    }

    public static void updateMarkers(){
        map.clear();

        for (int i = 0; i < messages.size(); i++){
            map.addMarker(new MarkerOptions().position(new LatLng(messages.get(i).lat, messages.get(i).lng)));
        }
    }

    public void messages(View view) {
        Intent intent = new Intent(this, MessagesActivity.class);
        startActivity(intent);
    }

    public void logoff(View view) {

    }
}