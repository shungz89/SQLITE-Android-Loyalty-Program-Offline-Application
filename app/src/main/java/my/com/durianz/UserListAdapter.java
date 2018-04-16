package my.com.durianz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 007 on 08-Mar-18.
 */

public class UserListAdapter extends BaseAdapter {

    //==========setup adapter==========
    //To use this adapter, we need to bind it to an Activity (refer to below)
    private Activity activity;

    //We will need a layout inflater to inflate our view (refer to below)
    private LayoutInflater inflater = null;

    //We need to create a List to STORE every barriers we created
    private List<Users> UserList = new ArrayList<>();

    //To create this adapter we need 2 input, one is which activity to bind it, two is the UserList that contains list of barriers
    public UserListAdapter(Activity activity, List<Users> UserList) {
        this.activity = activity;
        this.UserList = UserList;

        //with this we then can inflate our view later
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //==========setup adapter==========

    //==========setup getters==========
    @Override
    public int getCount() {

        //default is zero, change this to the size of the list
        return this.UserList.size();
    }

    //ivan: although not using the Generic Adapter library, this should not return a generic <code>Object</code>
    //ivan: the adapter library already become a specific type as BarrierAdapter, the getItem should always return <code>Barrier</code>
    @Override
    public Object getItem(int i) {

        //default is null, change this to such so that we get the item from the list
        return this.UserList.get(i);
    }

    @Override
    public long getItemId(int i) {

        //default is 0, change to this so we can get the itemId based on the number order given
        return i;
    }
    //==========setup getters==========

    //==========adapter creation==========
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            //Notice the "inflater" here, we can straight away inflate our custom xml to be used
            view = inflater.inflate(R.layout.user_list_layout, viewGroup, false);

            //setup the UI components as usual
            TextView IC_TextView = (TextView) view.findViewById(R.id.userlist_ic_textview);
            TextView Name_TextView = (TextView) view.findViewById(R.id.userlist_name_textview);

            IC_TextView.setText(UserList.get(i).getIC());
            Name_TextView.setText(UserList.get(i).getName());

        }
        return view;
    }
    //==========adapter creation==========

    public void setMyList(List userList) {
        this.UserList = userList;
        this.notifyDataSetChanged();
    }

}



