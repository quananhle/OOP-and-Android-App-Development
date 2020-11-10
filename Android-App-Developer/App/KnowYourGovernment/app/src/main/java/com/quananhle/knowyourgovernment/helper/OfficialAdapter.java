package com.quananhle.knowyourgovernment.helper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quananhle.knowyourgovernment.MainActivity;
import com.quananhle.knowyourgovernment.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {
    private static final String TAG = "OfficialAdapter";
    private List<Officials> officialsList;
    private MainActivity mainActivity;

    public OfficialAdapter(List<Officials> officialsList, MainActivity mainActivity) {
        this.officialsList = officialsList;
        this.mainActivity = mainActivity;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Making New");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_dialog, parent, false);
        itemView.setOnClickListener(mainActivity);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfficialViewHolder officialViewHolder, int position) {
        Officials officials = officialsList.get(position);
        officialViewHolder.office.setText(officials.getOffice());
        officialViewHolder.officialNameParty
                .setText(String.format("%s (%s)", officials.getName(), officials.getParty()));
    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }
}
