package com.nothing.arooba.packagesender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScriptAdapter extends ArrayAdapter<Script> {

    private int resourceId;

    public ScriptAdapter(Context context, int textViewResourceId, ArrayList<Script> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Script script = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView noText = (TextView) view.findViewById(R.id.noText);
        TextView scriptNameText = (TextView) view.findViewById(R.id.scriptNameText);
        TextView scriptRemarkText = (TextView) view.findViewById(R.id.scriptRemarkText);
        TextView executeNumberText = (TextView) view.findViewById(R.id.executeNumberText);
        noText.setText(script.getNo());
        scriptNameText.setText(script.getName());
        scriptRemarkText.setText(script.getRemark());
        executeNumberText.setText(script.getExecuteNum());
    }
}
