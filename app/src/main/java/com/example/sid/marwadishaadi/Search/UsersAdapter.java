package com.example.sid.marwadishaadi.Search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sid.marwadishaadi.R;

import java.util.ArrayList;


public class UsersAdapter extends ArrayAdapter<User> {
    ArrayList<User> arraylist;
    //    int count=0;
    private int selectedPosition = -1;
    private static final String TAG = "UsersAdapter";
    private  CheckBox checkbox;
    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.spinner_multiple_select, users);
        arraylist=users;
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_multiple_select, parent, false);
        }

        checkbox=(CheckBox)convertView.findViewById(R.id.checkBox);
        checkbox.setText(user.getName());

        checkbox.setOnCheckedChangeListener(myCheckChangList);
        checkbox.setTag(position);
        checkbox.setChecked(user.box);
        if (position == 0)
        {
            checkbox.setChecked(true);
        }
//        tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }

    ArrayList<User> getBox() {
        ArrayList<User> box = new ArrayList<User>();
        for (User p : arraylist) {
            if (p.box)
                box.add(p);
        }
        return box;
    }
    User getUser(int position) {
        return ((User) getItem(position));
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getUser((Integer) buttonView.getTag()).box = isChecked;
        }
    };
}


