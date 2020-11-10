package com.quananhle.knowyourgovernment.helper;

import android.view.View;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;

import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {
    public TextView office;
    public TextView officialNameParty;
    public OfficialViewHolder (View view){
        super(view);
        office = view.findViewById(R.id.dialog_office);
        officialNameParty = view.findViewById(R.id.dialog_name_party);
    }
}
