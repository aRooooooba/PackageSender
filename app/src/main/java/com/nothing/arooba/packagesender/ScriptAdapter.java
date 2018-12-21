package com.nothing.arooba.packagesender;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ScriptAdapter extends ArrayAdapter<Script> {

    private int resourceId;

    public ScriptAdapter(Context context, int textViewResourceId, List<Script> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        Script script = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView noText = view.findViewById(R.id.noText);
        TextView scriptNameText = view.findViewById(R.id.scriptNameText);
        TextView scriptRemarkText = view.findViewById(R.id.scriptRemarkText);
        TextView executeNumberText = view.findViewById(R.id.executeNumberText);
        noText.setText(String.valueOf(position + 1));
        scriptNameText.setText(script.getName());
        scriptRemarkText.setText(script.getRemark());
        String tmp = "已经用了 " + script.getExecuteNum() + " 次";
        executeNumberText.setText(tmp);
        return view;
    }
}
