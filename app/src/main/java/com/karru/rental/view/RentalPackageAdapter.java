package com.karru.rental.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.karru.rental.RentCarContract;
import com.karru.rental.model.DataModel;
import com.karru.util.AppTypeface;
import com.heride.rider.R;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class RentalPackageAdapter extends RecyclerView.Adapter<RentalPackageAdapter.MyViewHolder> {

    private static final String TAG = "RentalPackageAdapter";
    private ArrayList<DataModel> dataSet;
    private RentalActivity mContext;
    private ArrayList<AppCompatTextView> appCompatTextViews = new ArrayList<>();
    private RentCarContract.Presenter presenter;
    private AppTypeface appTypeface;

    public RentalPackageAdapter(List<DataModel> dataSet, RentalActivity rentalActivity,
                                RentCarContract.Presenter presenter, AppTypeface appTypeface) {
        this.dataSet = (ArrayList<DataModel>) dataSet;
        this.mContext = rentalActivity;
        this.appTypeface = appTypeface;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppCompatTextView tv_rental_package = holder.tv_rental_package;
        appCompatTextViews.add(tv_rental_package);
        tv_rental_package.setTypeface(appTypeface.getPro_narMedium());
        tv_rental_package.setText(dataSet.get(position).getTitle());
        tv_rental_package.setOnClickListener(v -> {
            clearSelection();
            tv_rental_package.setSelected(true);
            presenter.updateSelectedPackage(dataSet.get(position).getPackageId());
            mContext.packageSelected(dataSet.get(position).getTitle(),dataSet.get(position).getPackageId());
        });
    }

    /**
     * <h2>clearSelection</h2>
     * used tp clear the selection
     */
    private void clearSelection()
    {
        for(AppCompatTextView appCompatTextView: appCompatTextViews)
        {
            appCompatTextView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_rental_package;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rental_package = itemView.findViewById(R.id.tv_rental_package);
        }
    }
}

