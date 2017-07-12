package com.example.sid.marwadishaadi;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sid on 08-Jul-17.
 */

public class CityAdapter extends ArrayAdapter<String> {

    Context context;
    protected int resource, textviewresourceid;
    protected List<String> list;
    protected List<String> tempItems;
    protected List<String> suggestions;



    public CityAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.resource=resource;
        this.textviewresourceid=textViewResourceId;
        this.list=objects;
        this.tempItems = new ArrayList<String>(list);
        this.suggestions = new ArrayList<String>();
    }


    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.places_row, parent, false);
        }
        String str = list.get(position);
        if (str != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.place_name);
            if (textView != null) {
                textView.setText(str);
            }
        }
        return convertView;
    }

    @Override
    public android.widget.Filter getFilter() {
        return placeFilter;
    }

    android.widget.Filter placeFilter = new android.widget.Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String res = (String) resultValue;
            return res;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            List<String> filteredList = (List<String>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (String str : filteredList) {
                    add(str);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                suggestions.clear();
                for (String str : tempItems) {
                    if (str.toLowerCase().startsWith(constraint.toString())) {
                        suggestions.add(str);
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }
    };


}
