package jpdk.geochat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'");

        for (int i = 0; i < messages.size(); i++) {
            messageIdentifiers.add(String.valueOf(i) + " : \"" + messages.get(i).message + "\"");
            try {
                messageDescriptions.add("Sent " + calculateTimeDiff(dateToCalendar(sdf.parse(messages.get(i).sentAt))) +
                        "\nBy: " + messages.get(i).user + "\nAt: " + messages.get(i).lat + ", " +
                        messages.get(i).lng);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        MessageListAdapter ela = new MessageListAdapter(this, messageDescriptions, messageIdentifiers);
        ((ExpandableListView) findViewById(R.id.expandableListView)).setAdapter(ela);
    }

    private String calculateTimeDiff(Calendar c) {
        Calendar current = Calendar.getInstance();
        long difference = current.getTimeInMillis() - c.getTimeInMillis();
        if (difference <= 60000) {
            if (difference < 2000)
                return "a second ago";
            return (int)(difference / 1000) + " seconds ago";
        } else if (difference <= 3600000) {
            if (difference < 120000)
                return "a minute ago";
            return (int)(difference / 60000) + " minutes ago";
        } else if (difference <= 86400000) {
            if (difference < 7200000)
                return "an hour ago";
            return (int)(difference / 3600000) + " hours ago";
        } else if (difference <= 604800000) {
            if (difference < 172800000)
                return "a day ago";
            return (int)(difference / 604800000) + " days ago";
        } else if (difference <= 2419200000l) {
            if (difference < 1209600000)
                return "a week ago";
            return (int)(difference / 604800000) + " weeks ago";
        } else if (difference <= 378682800000l) {
            if (difference < 63113800000l)
                return "a month ago";
            return (int)(difference / 31556900000l) + " months ago";
        } else if (difference < 63113800000l)
            return "a year ago";
        return (int) (difference / 31556900000l) + " years ago";
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}