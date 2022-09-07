package com.karru.booking_flow.address.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.booking_flow.address.AddressSelectContract;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.util.AppTypeface;

import java.util.ArrayList;
import static com.karru.utility.Constants.FAV_TYPE_LIST;

/**
 * <h1>FavAddressAdapter</h1>
 * This class is used for inflating the fav address lists
 * @version v1.0
 * @since 31/07/17.
 */
public class FavAddressAdapter extends RecyclerView.Adapter<FavAddressAdapter.AddressViewHolder>
{
    private Context mContext;
    private ArrayList<FavAddressDataModel> favAddressDataModels;
    private AddressSelectContract.View addressSelectView;
    private AppTypeface appTypeface;
    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     * @param favAddressDataModels contains an array list.
     * @param addressSelectView reference of  AddressSelectContract.View Interface.
     */
    FavAddressAdapter(Context mContext, ArrayList<FavAddressDataModel> favAddressDataModels,
                      AddressSelectContract.View addressSelectView,AppTypeface appTypeface)
    {
        this.mContext = mContext;
        this.favAddressDataModels = favAddressDataModels;
        this.addressSelectView = addressSelectView;
        this.appTypeface = appTypeface;
    }

    /**
     * getting the actual size of list.
     * @return size.
     */
    @Override
    public int getItemCount()
    {
        return favAddressDataModels.size();
    }

    /**
     * Override method is used to inflate the layout on the screen.
     * @param parent contains parent view.
     * @param viewType which kind of view.
     * @return view holder instance.
     */
    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_drop_address, parent, false);
        return new AddressViewHolder(rowItem);
    }

    /**
     * Overrided method is used to set text and other data on views.
     * @param addressViewHolder instance of view holder class.
     * @param position current position.
     */
    @Override
    public void onBindViewHolder(final AddressViewHolder addressViewHolder, final int position)
    {
        addressViewHolder.tv_select_address.setSelected(true);
        addressViewHolder.tv_select_address.setText(favAddressDataModels.get(position).getAddress());
        addressViewHolder.tv_select_address_fav_title.setText(favAddressDataModels.get(position).getName());
        addressViewHolder.tv_select_address_fav_title.setTypeface(appTypeface.getPro_News());
        addressViewHolder.tv_select_address.setTypeface(appTypeface.getPro_News());
        addressViewHolder.iv_select_address_left_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_book_a_truck_heart_icon_on_selector));
        addressViewHolder.iv_select_address_right_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_delete_icon_off));
        addressViewHolder.rl_select_address_layout.setOnClickListener(v -> addressSelectView.onAddressItemViewClicked(addressViewHolder.rl_select_address_layout, position, FAV_TYPE_LIST));
        //to notify when on item delete is clicked
        addressViewHolder.iv_select_address_right_icon.setOnClickListener(v -> addressSelectView.onAddressItemViewClicked(addressViewHolder.iv_select_address_right_icon, position, FAV_TYPE_LIST));
    }

    /**
     * <h2>AddressViewHolder</h2>
     * This method is used where all the views are defined.
     */
    static class AddressViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_select_address_left_icon,iv_select_address_right_icon;
        TextView tv_select_address,tv_select_address_fav_title;
        RelativeLayout rl_select_address_layout;
        AddressViewHolder(View rootView)
        {
            super(rootView);
            rl_select_address_layout = rootView.findViewById(R.id.rl_select_address_layout);
            iv_select_address_right_icon = rootView.findViewById(R.id.iv_select_address_right_icon);
            iv_select_address_right_icon.setVisibility(View.VISIBLE);
            tv_select_address = rootView.findViewById(R.id.tv_select_address);
            tv_select_address_fav_title = rootView.findViewById(R.id.tv_select_address_fav_title);
            iv_select_address_left_icon = rootView.findViewById(R.id.iv_select_address_left_icon);
        }
    }
}
