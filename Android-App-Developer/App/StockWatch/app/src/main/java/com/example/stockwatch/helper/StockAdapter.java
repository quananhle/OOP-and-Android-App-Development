package com.example.stockwatch.helper;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stockwatch.MainActivity;
import com.example.stockwatch.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DecimalFormat formatter = new DecimalFormat("#,###,###.##");
    private static final String TAG = "StockAdapter";

    private List<Stock> stockList;
    private MainActivity mainActivity;

    public StockAdapter(List<Stock> stockList, MainActivity mainActivity) {
        this.stockList = stockList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_entry, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new StockViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position){
        Stock stock = stockList.get(position);
        holder.company.setText(stock.getCompany());
        holder.symbol.setText(stock.getSymbol());
        holder.price.setText(String.format(Locale.US, "$%.2f", stock.getCurrentPrice()));
        holder.todayPriceChange.setText
                (String.format(Locale.US, "$%.2f", stock.getTodayPriceChange()));
        if(stock.getTodayPriceChange() > 0){
            holder.price.setText("$" + formatter.format(stock.getCurrentPrice()));
            holder.price.setTextColor(Color.GREEN);
            holder.todayPriceChange.setText("▲ +" + String.valueOf
                    (decimalFormat.format(stock.getTodayPriceChange())));
            holder.todayPriceChange.setTextColor(Color.GREEN);
            holder.todayPercentChange.setText(" (+" + String.valueOf
                    (decimalFormat.format(stock.getTodayPercentChange() * 100))+"%)");
            holder.todayPercentChange.setTextColor(Color.GREEN);
        } else {
            holder.price.setText("$" + formatter.format(stock.getCurrentPrice()));
            holder.price.setTextColor(Color.RED);
            holder.todayPriceChange.setText("▼ -" + String.valueOf
                    (decimalFormat.format(stock.getTodayPriceChange())));
            holder.todayPriceChange.setTextColor(Color.RED);
            holder.todayPercentChange.setText(" (-" + String.valueOf
                    (decimalFormat.format(stock.getTodayPercentChange() * 100))+"%)");
            holder.todayPercentChange.setTextColor(Color.RED);
        }
    }
    @Override
    public int getItemCount(){
        return stockList.size();
    }
    //=========================HELPER•METHODS=================================//
    public static String toPercentage(double n, int digits){
        return String.format("%."+ digits +"f", n)+"%";
    }
}
