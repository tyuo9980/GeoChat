package jpdk.geochat;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MessagesActivity extends ActionBarActivity {

    ExpandableListAdapter ela;
    ArrayList<String> messageDescriptions;
    ArrayList<String> messageIdentifiers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //populateList(MapsActivity.messages);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
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

    public void populateList(ArrayList<Message> messages){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < messages.size(); i++) {
            messageIdentifiers.add(String.valueOf(i));
            c.setTimeInMillis(Long.valueOf(messages.get(i).sentAt));
            messageDescriptions.add(messages.get(i).message + "\nSend at: " + sdf.format(c) +
                    "\nBy: " + messages.get(i).user + "\n At: " + messages.get(i).lat + ", " +
                    messages.get(i).lng);
        }
        ela = new ExpandableListAdapter() {

            Context con;
            public void ExpandableListAdapter(Context context) {
                con = context;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getGroupCount() {
                return messageDescriptions.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 0;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return null;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return messageDescriptions.get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return 0;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                TextView tv = new TextView(con);
                tv.setText(messageIdentifiers.get(groupPosition));
                return tv;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView tv = new TextView(con);
                tv.setText(messageDescriptions.get(groupPosition));
                return tv;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {

            }

            @Override
            public void onGroupCollapsed(int groupPosition) {

            }

            @Override
            public long getCombinedChildId(long groupId, long childId) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId) {
                return 0;
            }
        };
        ((ExpandableListView) findViewById(R.id.expandableListView)).setAdapter(ela);
    }
}
