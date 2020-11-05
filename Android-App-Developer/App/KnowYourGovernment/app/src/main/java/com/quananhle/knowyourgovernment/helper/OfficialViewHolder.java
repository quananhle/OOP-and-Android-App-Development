package com.quananhle.knowyourgovernment.helper;

import android.view.View;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;

import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {
    private TextView office;
    private TextView officialNameParty;
    public OfficialViewHolder (View view){
        super(view);
        office = (TextView) view.findViewById(R.id.dialog_office);
        officialNameParty = (TextView) view.findViewById(R.id.dialog_name_party);
    }
}
