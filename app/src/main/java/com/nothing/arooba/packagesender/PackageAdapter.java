package com.nothing.arooba.packagesender;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PackageAdapter extends ArrayAdapter<Package> {

    private static final String TAG = "PackageAdapter";
    private int resourceId;

    public PackageAdapter(Context context, int textViewResourceId, List<Package> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        Package pck = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView pckText = view.findViewById(R.id.pckText);
        Log.d(TAG, "type: " + pck.getType());
        if (pck.getType().equals("GET")) {
            pckText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.get_bg, 0, 0, 0);
        } else if (pck.getType().equals("POST")) {
            pckText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.post_bg, 0, 0, 0);
        } else {
            Log.d(TAG, "WRONG!!!");
        }
        pckText.setText(pck.getUrl());
        return view;
    }
}
