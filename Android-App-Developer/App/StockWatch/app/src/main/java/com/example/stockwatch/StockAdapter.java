package com.example.stockwatch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "StockAdapter";

    private List<Stock> stockList;
    private MainActivity mainActivity;

    public StockAdapter(List<Stock> stockList, MainActivity mainActivity) {
        this.stockList = stockList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent, getContext())
                .inflate(R.layout.stock_entry, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Stock stock = stockList.get(position);
        holder.company.setText(stock.getCompany());
        holder.symbol.setText(stock.getSymbol());
        holder.price.setText(String.format(Locale.US, "$%.2f", stock.getCurrentPrice()));
        holder.todayPriceChange.setText(String.format(Locale.getDefault(), ));
        holder.todayPercentChange.setText(stock.getTodayPercentChange());
    }

}
