package com.example.sid.marwadishaadi;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ListView;

import com.example.sid.marwadishaadi.Search.User;
import com.example.sid.marwadishaadi.Search.UsersAdapter;

import java.util.ArrayList;

/**
 * Created by hp pc on 26-06-2017.
 */

public class EditProfileBottomSheets extends BottomSheetDialogFragment {


    public int view_array;

    public EditProfileBottomSheets(int i) {
        if (i==111){
            view_array = R.array.status_search_array;
        }else if(i == 112){
            view_array  = R.array.aincome_search_array;
        }else{
            view_array = R.array.physicalstatus_search_array;
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setContentView(viewGetter(view_array));
    }

    private View viewGetter(int array) {

        ArrayList<User> arrayOfUsers = new ArrayList<>();
        String[] str = getResources().getStringArray(array);
        for (int i = 0; i < str.length; i++) {
            User user = new User(str[i], "null");
            arrayOfUsers.add(user);
        }
        UsersAdapter adapter = new UsersAdapter(getContext(), arrayOfUsers);
        View view = View.inflate(getContext(), R.layout.custom_list_view, null);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        return view;
    }
}
