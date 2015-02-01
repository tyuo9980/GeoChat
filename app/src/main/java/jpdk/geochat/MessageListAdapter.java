package jpdk.geochat;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.ArrayList;

public class MessageListAdapter implements ExpandableListAdapter {
    ArrayList<String> messageDescriptions;
    ArrayList<String> messageIdentifiers;
    Context context;

    public MessageListAdapter(Context context,ArrayList<String> messageDescriptions, ArrayList<String> messageIdentifiers) {
        super();
        this.messageDescriptions = messageDescriptions;
        this.messageIdentifiers = messageIdentifiers;
        this.context = context;
    }
    public Object getChild(int groupPosition, int childPosition) {
        //return messageDescriptions.get(childPosition);
        return null;
    }
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(messageDescriptions.get(groupPosition));
        return tv;
    }
    public Group getGroup(int groupPosition) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    public int getGroupCount() {
        System.out.println(messageIdentifiers.size());
        return messageIdentifiers.size();
    }
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(messageIdentifiers.get(groupPosition));
        tv.setTextSize(25f);
        tv.setMinHeight(30);
        return tv;
    }
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return messageDescriptions.isEmpty();
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

    public boolean hasStableIds() {
        return true;
    }
}
