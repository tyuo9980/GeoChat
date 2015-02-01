package jpdk.geochat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class MessagesActivity extends ActionBarActivity {


    ArrayList<String> messageDescriptions = new ArrayList<String>();
    ArrayList<String> messageIdentifiers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        populateList(MapsActivity.messages);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateList(ArrayList<Message> messages){

        for (int i = 0; i < messages.size(); i++) {
            messageIdentifiers.add(String.valueOf(i));
            messageDescriptions.add(messages.get(i).message + "\nSend at: " + messages.get(i).sentAt +
                    "\nBy: " + messages.get(i).user + "\n At: " + messages.get(i).lat + ", " +
                    messages.get(i).lng);
        }
        MyExpandableListAdapter ela = new MyExpandableListAdapter(this, messageDescriptions, messageIdentifiers);


        ((ExpandableListView) findViewById(R.id.expandableListView)).setAdapter(ela);
    }
}
