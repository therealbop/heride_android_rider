package com.karru.rental.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.karru.rental.RentCarContract;
import com.karru.rental.model.CarDataModel;
import com.karru.util.AppTypeface;
import com.karru.util.Utility;
import com.heride.rider.R;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RentalVehicleTypesAdapter extends RecyclerView.Adapter<RentalVehicleTypesAdapter.MyViewHolder> {
    private List<CarDataModel> dataSet;
    private Context context;
    private RentalActivity activity;
    private Utility utility;
    private AppTypeface appTypeface;
    private RentCarContract.Presenter presenter;
    private ArrayList<ImageView> imageViews = new ArrayList<>();

    public RentalVehicleTypesAdapter(List<CarDataModel> dataSet, RentalActivity myActivity, Utility utility,
                                     AppTypeface appTypeface, RentCarContract.Presenter presenter) {
        this.dataSet = dataSet;
        this.appTypeface = appTypeface;
        this.context = myActivity;
        this.activity = myActivity;
        this.utility = utility;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageView carImage = holder.carImage;
        TextView carType = holder.carType;
        TextView carName = holder.carName;
        TextView minsLeft = holder.minsLeft;
        TextView cost = holder.cost;
        RelativeLayout relativeLayout = holder.relativeLayout;
        ImageView iv_rental_check = holder.iv_rental_check;

        imageViews.add(iv_rental_check);
        double total = Double.parseDouble(dataSet.get(position).getRental().getBaseFare()) +
                Double.parseDouble(dataSet.get(position).getRental().getDistanceFare())+
                Double.parseDouble(dataSet.get(position).getRental().getDurationFare())+
                Double.parseDouble(dataSet.get(position).getRental().getAdvanceFare());

        carType.setText(dataSet.get(position).getTypeName());
        carName.setText(dataSet.get(position).getTypeDesc());
        cost.setText(utility.currencyAdjustment(dataSet.get(position).getCurrencyAbbr(),dataSet.get(position).getCurrencySymbol(),String.valueOf(total)));
        minsLeft.setText(dataSet.get(position).getEta());

        Glide.with(context)
                .load(Uri.parse(dataSet.get(position).getVehicleImgOff()))
                .into(carImage);

        relativeLayout.setOnClickListener(v -> {
            clearAllSelections();
            iv_rental_check.setSelected(true);
            activity.handleClickOfVehicleType(context, dataSet.get(holder.getAdapterPosition()).getTypeName(),
                    minsLeft.getText().toString(), total,dataSet.get(position).getVehicleImgOff(),
                    dataSet.get(position).getVehicleImgOn(),dataSet.get(holder.getAdapterPosition()).getTypeId(),
                    dataSet.get(position).getCurrencyAbbr(),dataSet.get(position).getCurrencySymbol());
            activity.storeRentalFares(dataSet.get(position).getRental().getBaseFare(),dataSet.get(position).getRental().getDistanceFare(),
                    dataSet.get(position).getRental().getDurationFare(),dataSet.get(position).getRental().getAdvanceFare(),dataSet.get(position).getTypeName(),
                    dataSet.get(position).getCurrencyAbbr(),dataSet.get(position).getCurrencySymbol());
        });
    }

    /**
     * <h2>iv_rental_check</h2>
     * used to clear all image views selections
     */
    private void clearAllSelections()
    {
        for(ImageView imageView:imageViews)
            imageView.setSelected(false);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        ImageView iv_rental_check;
        TextView carType;
        TextView minsLeft;
        TextView carName;
        TextView cost;
        RelativeLayout relativeLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.iv_car);
            iv_rental_check = itemView.findViewById(R.id.iv_rental_check);
            carType = itemView.findViewById(R.id.carType);
            minsLeft = itemView.findViewById(R.id.tv_minsLeft);
            carName = itemView.findViewById(R.id.tv_carName);
            cost = itemView.findViewById(R.id.tv_cost);
            relativeLayout = itemView.findViewById(R.id.rl_all);

            carName.setTypeface(appTypeface.getPro_News());
            cost.setTypeface(appTypeface.getPro_narMedium());
            carType.setTypeface(appTypeface.getPro_narMedium());
            minsLeft.setTypeface(appTypeface.getPro_News());
        }
    }
}
