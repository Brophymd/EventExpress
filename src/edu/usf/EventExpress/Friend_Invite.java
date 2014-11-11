package edu.usf.EventExpress;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by Varik on 11/11/2014.
 */

public class Friend_Invite extends Activity {

    MyCustomAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_invite);
        showList();
        InviteButton();

    }

    private void showList(){
        //This ArrayList being built is dummy test code, should be able to fill a list with their name/email and thier
        //googleIDs. Friend is a class I made, should be in there with the the source code.
        // Link to where I got this code: http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
        //used most, but not all of it was needed.
        //Rest of the code up till other comments works on its own.
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        Friend friend = new Friend("1", "Mark@gmail.com",false);
        friendList.add(friend);
        friend = new Friend("2","Vi@someemail.com",false);
        friendList.add(friend);
        friend = new Friend("3","Micah@someemail.com",false);
        friendList.add(friend);

        dataAdapter = new MyCustomAdapter(this, R.layout.check_box, friendList);
        ListView listView = (ListView)findViewById(R.id.listView_friend_invites);
        listView.setAdapter(dataAdapter);
    }

    private class MyCustomAdapter extends ArrayAdapter<Friend>{

        private ArrayList<Friend> friendList;

        public MyCustomAdapter(Context context, int textViewResourceID, ArrayList<Friend> friendList){
            super(context, textViewResourceID, friendList);
            this.friendList = new ArrayList<Friend>();
            this.friendList.addAll(friendList);
        }

        private class ViewHolder{
            CheckBox name;
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(pos));

            if(convertView == null){
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.check_box, null);

                holder = new ViewHolder();
                holder.name = (CheckBox)convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener(){
                   public void onClick(View v){
                       CheckBox cb = (CheckBox)v;
                       Friend friend = (Friend)cb.getTag();
                       friend.setSelected(cb.isChecked());
                   }
                });
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            Friend friend = friendList.get(pos);
            holder.name.setText(friend.getName());
            holder.name.setChecked(friend.isSelected());
            holder.name.setTag(friend);

            return convertView;
        }
    }

    private void InviteButton(){
        Button inviteButton = (Button)findViewById(R.id.button_invite);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Friend> friendList = dataAdapter.friendList;
                //the code inside For-Loop is dummy test code Here could be where you send out the invites.

                for(int i=0;i<friendList.size();i++){
                    Friend friend = friendList.get(i);
                    if(friend.isSelected())
                        Toast.makeText(getApplicationContext(),friend.getName() +"Checked",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}