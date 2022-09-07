package com.karru.rental.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.util.AppTypeface;
import com.heride.rider.R;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RentalRulesAdapter extends RecyclerView.Adapter<RentalRulesAdapter.MyViewHolder> {

    private List<String> myRules;
    private TextView rulesTextView;
    private AppTypeface appTypeface;
    private Context context;
    private String baseFare;
    private String distanceFare;
    private String durationFare;
    private String advanceFare;
    private String name;
    private String packageName;
    private int abbr;
    private String symbol;

    RentalRulesAdapter(List<String> myRules, Context context, AppTypeface appTypeface, String baseFare, String distanceFare, String durationFare, String advanceFare,
                       String name,int abbr,String symbol,String packageName) {
        this.myRules = myRules;
        this.context = context;
        this.appTypeface = appTypeface;
        this.baseFare = baseFare;
        this.distanceFare = distanceFare;
        this.durationFare = durationFare;
        this.advanceFare = advanceFare;
        this.name = name;
        this.abbr = abbr;
        this.symbol = symbol;
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rules_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        rulesTextView.setText(myRules.get(position));
        rulesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RentalFareActivity.class);
                intent.putExtra("baseFare",baseFare);
                intent.putExtra("distanceFare",distanceFare);
                intent.putExtra("durationFare",durationFare);
                intent.putExtra("advanceFare",advanceFare);
                intent.putExtra("Name",name);
                intent.putExtra("abbr",abbr);
                intent.putExtra("symbol",symbol);
                intent.putExtra("package",packageName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myRules.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rulesTextView = itemView.findViewById(R.id.tv_rules);
            rulesTextView.setTypeface(appTypeface.getPro_News());
        }
    }
}
