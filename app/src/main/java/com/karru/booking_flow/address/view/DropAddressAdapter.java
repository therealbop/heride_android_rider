package com.karru.booking_flow.address.view;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karru.utility.Utility;
import com.heride.rider.R;
import com.karru.booking_flow.address.AddressSelectContract;
import com.karru.booking_flow.address.model.AddressDataModel;
import com.karru.util.AppTypeface;

import java.util.ArrayList;

import static com.karru.utility.Constants.RECENT_TYPE_LIST;
import static com.karru.utility.Constants.SPECIAL_TYPE;

/**
 * <h1>DropAddressAdapter</h1>
 * This class is used to provide the DropAddress, where we can show our Drop Off Address.
 * @author 3embed
 * @since 17 Mar 2017.
 */
public class DropAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context mContext;
    private ArrayList<AddressDataModel> dropAddressPojoArrayList;
    private AddressSelectContract.View addressSelectView;
    private AppTypeface appTypeface;

    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     * @param dropAddressPojoArrayList contains an array list.
     * @param addressSelectView instance of address select notifier
     */
    DropAddressAdapter(Context mContext, ArrayList<AddressDataModel> dropAddressPojoArrayList,
                       AddressSelectContract.View addressSelectView, AppTypeface appTypeface)
    {
        this.mContext = mContext;
        this.dropAddressPojoArrayList = dropAddressPojoArrayList;
        Utility.printLog("special address list "+dropAddressPojoArrayList.size());
        this.addressSelectView = addressSelectView;
        this.appTypeface = appTypeface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == RECENT_TYPE_LIST)
        {
            View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_drop_address, parent, false);
            viewHolder = new RecentViewHolder(rowItem);
        }
        else
        {
            View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_drop_address, parent, false);
            viewHolder = new SpecialViewHolder(rowItem);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()== RECENT_TYPE_LIST)
        {
            // Typecast Viewholder
            // Set Viewholder properties
            // Add any click listener if any
            RecentViewHolder recentViewHolder = (RecentViewHolder) holder;
            recentViewHolder.tv_select_address_fav_title.setVisibility(View.GONE);
            recentViewHolder.tv_select_address.setSelected(true);
            recentViewHolder.tv_select_address.setText(dropAddressPojoArrayList.get(position).getAddress());
            recentViewHolder.tv_select_address.setTypeface(appTypeface.getPro_News());
            recentViewHolder.rl_drop_address_layout.setBackgroundResource(R.drawable.selector_white_layout);
            recentViewHolder.rl_drop_address_layout.setOnClickListener(v -> addressSelectView.onAddressItemViewClicked(recentViewHolder.rl_drop_address_layout, position, RECENT_TYPE_LIST));
            Utility.printLog("special address size "+getItemCount());
        }
        else
        {
            SpecialViewHolder recentViewHolder = (SpecialViewHolder) holder;
            recentViewHolder.tv_select_address_fav_title.setVisibility(View.GONE);
            recentViewHolder.tv_select_address.setSelected(true);
            recentViewHolder.tv_select_address.setText(dropAddressPojoArrayList.get(position).getAddress());
            recentViewHolder.tv_select_address.setTypeface(appTypeface.getPro_News());
            recentViewHolder.rl_drop_address_layout.setBackgroundResource(R.drawable.selector_white_layout);
            recentViewHolder.rl_drop_address_layout.setOnClickListener(v -> addressSelectView.onAddressItemViewClicked(recentViewHolder.rl_drop_address_layout, position, SPECIAL_TYPE));
            Utility.printLog("special address size "+getItemCount());
        }
    }

    @Override
    public int getItemCount()
    {
        return dropAddressPojoArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dropAddressPojoArrayList.get(position).getType();
    }

    /**
     * Inner class, where all the views are defined.
     */
    static class RecentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_drop_fav_icon;
        TextView tv_select_address,tv_select_address_fav_title;
        RelativeLayout rl_drop_address_layout;
        RecentViewHolder(View itemView)
        {
            super(itemView);
            rl_drop_address_layout = itemView.findViewById(R.id.rl_select_address_layout);
            iv_drop_fav_icon = itemView.findViewById(R.id.iv_select_address_right_icon);
            tv_select_address = itemView.findViewById(R.id.tv_select_address);
            tv_select_address_fav_title = itemView.findViewById(R.id.tv_select_address_fav_title);
        }
    }
    /**
     * Inner class, where all the views are defined.
     */
    static class SpecialViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_drop_fav_icon;
        TextView tv_select_address,tv_select_address_fav_title;
        RelativeLayout rl_drop_address_layout;
        SpecialViewHolder(View itemView)
        {
            super(itemView);
            rl_drop_address_layout = itemView.findViewById(R.id.rl_select_address_layout);
            iv_drop_fav_icon = itemView.findViewById(R.id.iv_select_address_right_icon);
            tv_select_address = itemView.findViewById(R.id.tv_select_address);
            tv_select_address_fav_title = itemView.findViewById(R.id.tv_select_address_fav_title);
        }
    }
}
