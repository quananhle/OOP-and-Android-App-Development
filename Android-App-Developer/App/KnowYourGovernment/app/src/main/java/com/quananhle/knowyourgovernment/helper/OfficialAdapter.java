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
    private List<Official> officialList;
    private MainActivity mainActivity;

    public OfficialAdapter(List<Official> officialList, MainActivity mainActivity) {
        this.officialList = officialList;
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
        Official official = officialList.get(position);
        officialViewHolder.office.setText(official.getOffice());
        officialViewHolder.officialNameParty
                .setText(String.format("%s (%s)", official.getName(), official.getParty()));
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
