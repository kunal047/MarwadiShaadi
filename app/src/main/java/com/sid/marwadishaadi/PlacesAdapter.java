package com.sid.marwadishaadi;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sid on 01-Jul-17.
 */

public class PlacesAdapter extends ArrayAdapter<Place> {

    protected Context context;
    protected List<Place> placeList;
    protected List<Place> placeListAll;
    protected List<Place> placeListSuggestions;
    protected int resource, textviewresourceid;

    public PlacesAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Place> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context = context;
        this.resource = resource;
        this.textviewresourceid = textViewResourceId;

        this.placeList = new ArrayList<>(objects);
        this.placeListAll = new ArrayList<>(objects);
        this.placeListSuggestions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Place getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.places_row, parent, false);
        }
        Place place = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.place_name);

        textView.setText(place.getPlace());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Place) resultValue).getPlace();
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                placeList.clear();

                if (results != null && results.count > 0) {

                    List<Place> filteredList = (List<Place>) results.values;
                    for (Place place : filteredList) {
                        placeList.add(place);
                        notifyDataSetChanged();
                    }
                } else if (constraint == null) {
                    placeList.addAll(placeListAll);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    placeListSuggestions.clear();
                    notifyDataSetChanged();
                    for (Place place : placeListAll) {
                        if (place.getCity().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            placeListSuggestions.add(place);
                        }
                    }
                    filterResults.values = placeListSuggestions;
                    filterResults.count = placeListSuggestions.size();
                }
                return filterResults;
            }
        };

    }
}