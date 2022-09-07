package com.karru.landing.my_vehicles;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.karru.util.AppTypeface;
import com.heride.rider.R;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>CorporateAccountsAdapter</h1>
 * used to inflate the corporate accounts
 */
public class MyVehiclesAdapter extends RecyclerView.Adapter<MyVehiclesAdapter.CorporateAccountsViewHolder>
{
    private static final String TAG = "MyVehiclesAdapter";
    private Context mContext;
    private AppTypeface appTypeface;
    private MyVehiclesContract.Presenter presenter;
    private MyVehiclesContract.View myVehiclesView;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<MyVehiclesDataModel> myVehiclesDataModels;
    private int comingFrom;

    /**
     * <h2>MyVehiclesAdapter</h2>
     * constructor
     * @param appTypeface app type face
     * @param mContext context
     */
    MyVehiclesAdapter(AppTypeface appTypeface, Context mContext, MyVehiclesContract.Presenter presenter,
                      MyVehiclesContract.View myVehiclesView,ArrayList<MyVehiclesDataModel> myVehiclesDataModels)
    {
        this.mContext = mContext;
        this.appTypeface = appTypeface;
        this.presenter = presenter;
        this.myVehiclesView = myVehiclesView;
        this.myVehiclesDataModels = myVehiclesDataModels;
    }

    @NonNull
    @Override
    public CorporateAccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vehicle_choose, parent,
                false);
        return new CorporateAccountsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CorporateAccountsViewHolder holder, int position)
    {
        holder.tv_vehicle_name.setTypeface(appTypeface.getPro_News());
        holder.tv_vehicle_color.setTypeface(appTypeface.getPro_News());
        imageViews.add(holder.iv_vehicle_tick);

        holder.tv_vehicle_name.setText(myVehiclesDataModels.get(position).getYear()+" "+
                myVehiclesDataModels.get(position).getMake()+" "+myVehiclesDataModels.get(position).getModel());
        holder.tv_vehicle_color.setText(myVehiclesDataModels.get(position).getColor());

        if(myVehiclesDataModels.get(position).isDefault())
            holder.iv_vehicle_tick.setVisibility(View.VISIBLE);
        else
            holder.iv_vehicle_tick.setVisibility(View.GONE);

        holder.iv_vehicle_delete.setOnClickListener(v -> myVehiclesView.showDeleteVehicleAlert(position));

        holder.cv_vehicle_layout.setOnClickListener(v ->
        {
            hideTicks();
            holder.iv_vehicle_tick.setVisibility(View.VISIBLE);
            presenter.handleDefaultVehicle(myVehiclesDataModels.get(position),comingFrom);
        });
    }

    /**
     * <h2>setParent</h2>
     * used to set the parent
     * @param parent 1 for home , 0 from menu
     */
    void setParent(int parent)
    {
        comingFrom = parent;
    }

    /**
     * <h2>hideTicks</h2>
     * used to hide ticks
     */
    private void hideTicks()
    {
        for(ImageView imageView:imageViews)
            imageView.setVisibility(View.GONE);
    }

    /**
     * <h3>getItemCount</h3>
     * This method is overridden method used to return the count of the recyclerViews views
     *
     * @return returns the size
     */
    @Override
    public int getItemCount() {
        return myVehiclesDataModels.size();
    }

    /**
     * <h1>ZonesViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class CorporateAccountsViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_vehicle_name) TextView tv_vehicle_name;
        @BindView(R.id.tv_vehicle_color) TextView tv_vehicle_color;
        @BindView(R.id.iv_vehicle_tick) AppCompatImageView iv_vehicle_tick;
        @BindView(R.id.iv_vehicle_delete) AppCompatImageView iv_vehicle_delete;
        @BindView(R.id.cv_vehicle_layout) CardView cv_vehicle_layout;

        CorporateAccountsViewHolder(View itemView)
        {
            super(itemView);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            ButterKnife.bind(this, itemView);
        }
    }
}