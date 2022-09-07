package com.karru.landing.home.view;

import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.heride.rider.R;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.karru.utility.Constants.FOOTER_ROW;
import static com.karru.utility.Constants.ITEM_ROW;

/**
 * <h2>SomeOneRideContactsAdapter</h2>
 * used to inflate the contact
 */
public class SomeOneRideContactsAdapter extends RecyclerView.Adapter<SomeOneRideContactsAdapter.ViewHolder>
{
    private static final String TAG = "SomeOneRideContactsAdapter";
    private SomeOneRideBottomSheet context;
    private ArrayList<ContactDetails> contactList;
    int lastCheckedPosition = 0;
    private AppTypeface appTypeface;

    /**
     * <h2>SomeOneRideContactsAdapter</h2>
     * This is a contsructor of this adapter class
     *
     * @param context context of the activity from which its called
     * @param imagesFileList list of image files
     */
    SomeOneRideContactsAdapter(SomeOneRideBottomSheet context, ArrayList<ContactDetails> imagesFileList,
                               AppTypeface appTypeface)
    {
        this.context = context;
        this.appTypeface = appTypeface;
        this.contactList = imagesFileList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_ROW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_profile, parent, false);
            return new ViewHolder(view);
        } else if (viewType == FOOTER_ROW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_profile, parent, false);
            return new ViewHolder(view);
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_someOne_name.setTypeface(appTypeface.getPro_narMedium());
        if (contactList.size() > 0)
        {
            if (contactList.get(position).getImgUrl() != null && !contactList.get(position).getImgUrl().equals(""))
            {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.placeholder(holder.book_profile_grey_default_image);
                requestOptions = requestOptions.optionalCircleCrop();

                Glide.with(context)
                        .load(contactList.get(position).getImgUrl())
                        .apply(requestOptions)
                        .into(holder.iv_someOne_profile_pic);
            }
            holder.tv_someOne_name.setText(contactList.get(position).getName());
        }

        if (position == lastCheckedPosition) {
            holder.iv_someOne_profile_select.setVisibility(View.VISIBLE);
        } else {
            holder.iv_someOne_profile_select.setVisibility(View.INVISIBLE);
        }

        holder.view.setOnClickListener(view ->
        {
            lastCheckedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            Utility.printLog(TAG+" posision for slectioin "+position+" la "+lastCheckedPosition);
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == contactList.size()) {
            return FOOTER_ROW;
        }
        return ITEM_ROW;
    }

    @Override
    public int getItemCount()
    {
        return contactList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_user_name) TextView tv_someOne_name;
        @BindView(R.id.iv_user_profile_pic) ImageView iv_someOne_profile_pic;
        @BindView(R.id.iv_user_profile_select) ImageView iv_someOne_profile_select;
        @BindDrawable(R.drawable.book_profile_grey_default_image) Drawable book_profile_grey_default_image;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view=view;
            ButterKnife.bind(this, view);
        }
    }
}
