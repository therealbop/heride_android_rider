package com.karru.landing.history.history_details.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karru.help_center.zendesk_ticket_details.view.HelpIndexTicketDetailsActivity;
import com.karru.landing.history.history_details.model.HelpDataModel;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
public class HistoryDetailsHelpAdapter extends RecyclerView.Adapter<HistoryDetailsHelpAdapter.ViewHolder>
{
    private static final String TAG = "HistoryDetailsHelpAdapter";
    private AppTypeface appTypeface;
    private ArrayList<HelpDataModel> helpDataModels;
    private int historyType;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private String bookingId="";

    /**
     * This is the constructor of our adapter.
     * //@param reasons contains an array list.
     * //@param rv_OnItemViewsClickNotifier reference of OnItemViewClickNotifier Interface.
     */
    public HistoryDetailsHelpAdapter(Context context, AppTypeface appTypeface, ArrayList<HelpDataModel> helpDataModels)
    {
        this.appTypeface = appTypeface;
        this.mContext = context;
        this.helpDataModels = helpDataModels;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getItemCount()
    {
        return helpDataModels.size();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_details, parent, false);
        return new ViewHolder(viewList);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.bookingId = helpDataModels.get(0).getBookingId();
        Utility.printLog("BookingId:-"+bookingId);
        HelpDataModel helpDataModel = helpDataModels.get(position);
        holder.tv_help_list.setText(helpDataModel.getName());


        if(helpDataModel.getSubcat() != null && helpDataModel.getSubcat().size() > 0)
        {
            // mainCatViewHolder.ivSupportMainCat.setRotation(180);
            holder.iv_dir_icon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp_selector);
            bindChildViewHolder(holder.ll_SupportSubCat, helpDataModel);
        }else
        {
            holder.iv_dir_icon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp_selector);
            holder.ll_SupportSubCat.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if( mContext.getResources().getConfiguration().getLayoutDirection() == LayoutDirection.RTL)
                    holder.iv_dir_icon.setRotation(-180);
            }
        }

        //to notify when on item is clicked
        holder.view.setOnClickListener(v -> {
            if(helpDataModel.getSubcat() != null && helpDataModel.getSubcat().size() > 0) {
                if (helpDataModel.isHasSubCatExpanded()) {
                    holder.iv_dir_icon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp_selector);
                    holder.ll_SupportSubCat.setVisibility(View.GONE);
                    helpDataModel.setHasSubCatExpanded(false);
                } else {
                    holder.iv_dir_icon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    holder.ll_SupportSubCat.setVisibility(View.VISIBLE);
                    helpDataModel.setHasSubCatExpanded(true);
                }
            }else {
                Intent intent = new Intent(mContext, HelpIndexTicketDetailsActivity.class);
                intent.putExtra("ISTOAddTICKET",true);
                intent.putExtra("SUBJECT","BookingId:-"+bookingId+":-"+helpDataModel.getName());
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * <h2>bindChildViewHolder</h2>
     * <p>
     *     method to add sub category of support list to the recycler view
     * </p>
     * @param llSupportSubCat : reference of root linear layout for support
     * @param helpDataModel : list of sub categories
     */
    private void bindChildViewHolder (LinearLayout llSupportSubCat, HelpDataModel helpDataModel)
    {
        for(int j =0; j < helpDataModel.getSubcat().size(); j++)
        {
            View subCatRootView = layoutInflater.inflate(R.layout.item_support_sub_cat, llSupportSubCat, false);
            String subCatItem = helpDataModel.getSubcat().get(j);
            TextView tvSupportSubCat = subCatRootView.findViewById( R.id.tvSupportSubCat);
            tvSupportSubCat.setTypeface(appTypeface.getPro_News());
            tvSupportSubCat.setText(subCatItem);

            RelativeLayout rlSupportSubCat = subCatRootView.findViewById( R.id.rlSupportSubCat);
            rlSupportSubCat.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, HelpIndexTicketDetailsActivity.class);
                intent.putExtra("ISTOAddTICKET",true);
                intent.putExtra("SUBJECT","BookingId:-"+bookingId+":-"+helpDataModel.getName()+":-"+subCatItem);
                mContext.startActivity(intent);
            });

            if(j == helpDataModel.getSubcat().size() -1)
            {
                View viewSubCatBottomLine = subCatRootView.findViewById(R.id.viewSubCatBottomLine);
                viewSubCatBottomLine.setVisibility(View.GONE);
            }
            llSupportSubCat.addView(subCatRootView);
        }
    }



    /**
     * <h1>ListViewHolder</h1>
     * <p>
     *   This method is used to initialize the views
     * </p>
     */
    class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_help_list) TextView tv_help_list;
        @BindView(R.id.iv_dir_icon) ImageView iv_dir_icon;
        @BindView(R.id.ll_SupportSubCat) LinearLayout ll_SupportSubCat;
        View view;

        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view=itemView;
            tv_help_list.setTypeface(appTypeface.getPro_News());

        }
    }
}
