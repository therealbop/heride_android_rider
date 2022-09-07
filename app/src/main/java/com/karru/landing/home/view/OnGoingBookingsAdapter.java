package com.karru.landing.home.view;

import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.model.OnGoingBookingsModel;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnGoingBookingsAdapter extends RecyclerView.Adapter<OnGoingBookingsAdapter.OnGoingViewHolder> {
    private static final String TAG = "OnGoingBookingsAdapter";
    private ArrayList<OnGoingBookingsModel> onGoingBookingsModels = new ArrayList<>();
    private Context mContext;
    private AppTypeface appTypeface;
    private HomeFragmentContract.View view;

    public OnGoingBookingsAdapter(AppTypeface appTypeface, Context mContext)
    {
        this.mContext = mContext;
        this.appTypeface = appTypeface;
    }

    /**
     * <h2>notifyOnGoingBookingDetails</h2>
     * This method is used to notify adapter with the list
     * @param onGoingBookingsModels on going booking details list
     */
    void notifyOnGoingBookingDetails(ArrayList<OnGoingBookingsModel> onGoingBookingsModels,
                                     HomeFragmentContract.View view)
    {
        this.view = view;
        Utility.printLog(TAG+" onGoingBookingsModels size 1 "+onGoingBookingsModels.size());
        this.onGoingBookingsModels.clear();
        this.onGoingBookingsModels = onGoingBookingsModels;
        notifyDataSetChanged();
    }

    /**
     * <h2>notifyOnGoingItemDetails</h2>
     * This method is used to notify adapter with the row
     * @param onGoingBookingsModels on going booking detail row
     */
    void notifyOnGoingItemDetails(int pos, OnGoingBookingsModel onGoingBookingsModels,
                                  HomeFragmentContract.View view)
    {
        this.view = view;
        Utility.printLog(TAG+" onGoingBookingsModels size pos "+pos);
        this.onGoingBookingsModels.set(pos,onGoingBookingsModels);
        notifyItemChanged(pos,onGoingBookingsModels);
    }

    @Override
    public OnGoingViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ongoing_bookings, parent, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        return new OnGoingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OnGoingViewHolder holder, int position)
    {
        holder.tv_home_bottom_live_book.setTypeface(appTypeface.getPro_narMedium());
        String title ;
        switch (onGoingBookingsModels.get(position).getBookingStatus())
        {
            case 6:
                title = onGoingBookingsModels.get(position).getDriverName()
                        +" "+mContext.getString(R.string.is)+" "+mContext.getString(R.string.on_the_way_pick);
                holder.tv_home_bottom_live_book.setText(title);
                break;

            case 7:
                title = onGoingBookingsModels.get(position).getDriverName() +" "+mContext.getString(R.string.has);
                holder.tv_home_bottom_live_book.setText(title);
                break;

            case 9:
                title = onGoingBookingsModels.get(position).getDriverName()
                        +" "+mContext.getString(R.string.is)+" "+mContext.getString(R.string.on_the_way_drop);
                holder.tv_home_bottom_live_book.setText(title);
                break;

        }

        holder.rl_home_bottom_live_book.setOnClickListener(v -> view.openLiveTrackingScreen(onGoingBookingsModels.get(position)
                .getBookingId()));
    }

    /**
     * <h3>getItemCount</h3>
     * This method is overridden method used to return the count of the recyclerViews views
     *
     * @return returns the size
     */
    @Override
    public int getItemCount() {
        return onGoingBookingsModels.size();
    }

    /**
     * <h1>ZonesViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class OnGoingViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_home_bottom_live_book) AppCompatTextView tv_home_bottom_live_book;
        @BindView(R.id.rl_home_bottom_live_book) RelativeLayout rl_home_bottom_live_book;

        OnGoingViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}