package com.karru.landing.home.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.model.PickUpGates;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>CurrentZonePickupsAdapter</h1>
 * used to inflate the current zones sub category
 * @author 3Embed
 * @since on 07-03-2018.
 */
public class CurrentZonePickupsAdapter extends RecyclerView.Adapter<CurrentZonePickupsAdapter.ZonesViewHolder> {
    private static final String TAG = "CurrentZonePickupsAdapter";
    private ArrayList<PickUpGates> pickUpGatesArrayList = new ArrayList<>();
    private Context mContext;
    private AppTypeface appTypeface;
    private HomeFragmentContract.View homeView;

    public CurrentZonePickupsAdapter(AppTypeface appTypeface, Context mContext)
    {
        this.mContext = mContext;
        this.appTypeface = appTypeface;
    }

    /**
     * <h2>notifyOnGoingBookingDetails</h2>
     * This method is used to notify adapter with the list
     * @param pickUpGates on going booking details list
     */
    void notifyPickupPointsDetails(ArrayList<PickUpGates> pickUpGates,HomeFragmentContract.View view)
    {
        this.homeView = view;
        Utility.printLog(TAG+" pickUpGates size 1 "+pickUpGates.size());
        this.pickUpGatesArrayList = pickUpGates;
        notifyDataSetChanged();
    }

    @Override
    public ZonesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_zone_address, parent, false);
        return new ZonesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZonesViewHolder holder, int position)
    {
        holder.tv_pick_zone_title.setTypeface(appTypeface.getPro_News());

        holder.tv_pick_zone_title.setText(pickUpGatesArrayList.get(position).getTitle());

        if(pickUpGatesArrayList.get(position).isSelected())
            holder.iv_pick_zone_icon.setSelected(true);
        else
            holder.iv_pick_zone_icon.setSelected(false);

        holder.ll_pick_zone_layout.setOnClickListener(view -> homeView.nearestPickupGate(
                pickUpGatesArrayList.get(position).getId(),pickUpGatesArrayList.get(position).getTitle()));
    }

    /**
     * <h3>getItemCount</h3>
     * This method is overridden method used to return the count of the recyclerViews views
     *
     * @return returns the size
     */
    @Override
    public int getItemCount() {
        return pickUpGatesArrayList.size();
    }

    /**
     * <h1>ZonesViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class ZonesViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_pick_zone_title) TextView tv_pick_zone_title;
        @BindView(R.id.iv_pick_zone_icon) ImageView iv_pick_zone_icon;
        @BindView(R.id.ll_pick_zone_layout) LinearLayout ll_pick_zone_layout;

        ZonesViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
