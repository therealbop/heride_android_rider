package com.karru.landing.home.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.heride.rider.R;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.util.AppTypeface;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h2>SomeOneRideContactsAdapter</h2>
 * used to inflate the contact
 */
public class FavDriversListAdapter extends RecyclerView.Adapter<FavDriversListAdapter.ViewHolder>
{
    private Context context;
    ArrayList<ContactDetails> favDriversList;
    private AppTypeface appTypeface;
    int lastCheckedPosition = 1;

    /**
     * <h2>SomeOneRideContactsAdapter</h2>
     * This is a contsructor of this adapter class
     *
     * @param context context of the activity from which its called
     * @param favDriversList list of image files
     */
    public FavDriversListAdapter(Context context, ArrayList<ContactDetails> favDriversList,
                                 AppTypeface appTypeface)
    {
        this.context = context;
        this.appTypeface = appTypeface;
        this.favDriversList = favDriversList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        //if you need three fix image view in width
        int deviceWidth = 0;
        if(favDriversList.size()>3)
            deviceWidth = (int) (displaymetrics.widthPixels / 3.5);
        else if(favDriversList.size() == 2)
            deviceWidth = displaymetrics.widthPixels / 2;
        else if(favDriversList.size() == 3)
            deviceWidth = displaymetrics.widthPixels / 3;

        holder.ll_user_profile_layout.setMinimumWidth(deviceWidth);

        holder.tv_user_name.setTypeface(appTypeface.getPro_narMedium());
        holder.tv_user_name.setText(favDriversList.get(position).getName());
        if (favDriversList.get(position).getImgUrl() != null && !favDriversList.get(position).getImgUrl().equals(""))
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(holder.book_profile_grey_default_image);
            requestOptions = requestOptions.optionalCircleCrop();

            Glide.with(context).load(favDriversList.get(position).getImgUrl())
                    .apply(requestOptions)
                    .into(holder.iv_user_profile_pic);
        }

        if (position == lastCheckedPosition)
            holder.iv_user_profile_select.setVisibility(View.VISIBLE);
        else
            holder.iv_user_profile_select.setVisibility(View.INVISIBLE);

        holder.ll_user_profile_layout.setOnClickListener(view ->
        {
            lastCheckedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount()
    {
        return favDriversList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_user_name) TextView tv_user_name;
        @BindView(R.id.iv_user_profile_pic) ImageView iv_user_profile_pic;
        @BindView(R.id.iv_user_profile_select) ImageView iv_user_profile_select;
        @BindView(R.id.ll_user_profile_layout) LinearLayout ll_user_profile_layout;
        @BindDrawable(R.drawable.book_profile_grey_default_image) Drawable book_profile_grey_default_image;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view=view;
            ButterKnife.bind(this, view);
        }
    }
}
