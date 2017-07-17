package com.example.sid.marwadishaadi;

import android.content.Context;
import android.provider.Contacts;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sid on 01-Jul-17.
 */

public class PlacesAdapter extends ArrayAdapter<Place> {

    protected Context context;
    protected List<Place> placeList;
    protected List<Place> tempItems;
    protected List<Place> suggestions;
    protected int resource, textviewresourceid;

    public PlacesAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Place> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context = context;
        this.placeList = objects;
        this.resource = resource;
        this.textviewresourceid = textViewResourceId;
        this.tempItems = new ArrayList<Place>(placeList);
        this.suggestions = new ArrayList<Place>();
    }



    @Override
    public Place getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.places_row, parent, false);
        }
        Place place = placeList.get(position);
        if (place != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.place_name);
            if (textView != null) {
                textView.setText(place.getPlace());
            }
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return placeFilter;
    }

    Filter placeFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String res = ((Place) resultValue).getPlace();
            return res;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            List<Place> filteredList = (List<Place>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (Place place : filteredList) {
                    add(place);
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
                for (Place place : tempItems) {
                    if (place.getCity().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(place);
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }
    };


}