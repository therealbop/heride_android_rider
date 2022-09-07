package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.karru.landing.home.model.DriverPreferenceDataModel;
import com.karru.landing.home.model.DriverPreferenceModel;
import com.heride.rider.R;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>DriverPreferencesAdapter</h1>
 * used to populate the driver preference list
 * @author 3Embed
 * @since on 03-04-2018.
 */
public class DriverPreferencesAdapter extends RecyclerView.Adapter<DriverPreferencesAdapter.DriverPrefViewHolder>
{
    private static final String TAG = "DriverPreferencesAdapter";
    private Context mContext;
    private AppTypeface appTypeface;
    private com.karru.util.Utility utility;
    private DriverPreferenceModel driverPreferenceModel;
    private ArrayList<DriverPreferenceDataModel> tempPreferenceDataModels;

    /**
     * <h2>DriverPreferencesAdapter</h2>
     * constructor
     * @param appTypeface app type face
     * @param mContext context
     */
    public DriverPreferencesAdapter(AppTypeface appTypeface, Context mContext,
                                    DriverPreferenceModel driverPreferenceDataModels,
                                    com.karru.util.Utility utility,ArrayList<DriverPreferenceDataModel> tempPreferenceDataModels)
    {
        this.mContext = mContext;
        this.appTypeface = appTypeface;
        this.utility = utility;
        this.tempPreferenceDataModels = tempPreferenceDataModels;
        this.driverPreferenceModel = driverPreferenceDataModels;
    }

    @NonNull
    @Override
    public DriverPrefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_driver_preference, parent,
                false);
        return new DriverPrefViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverPrefViewHolder holder, int position)
    {
        Utility.printLog(TAG+" driver preference text "+ driverPreferenceModel.getData().get(position).isSelected()
                +" "+ driverPreferenceModel.getData().get(position).getName());

        holder.tv_driver_preference_title.setTypeface(appTypeface.getPro_News());
        if(driverPreferenceModel.getData().get(position).getFees().equals(""))
            holder.tv_driver_preference_title.setText(driverPreferenceModel.getData().get(position).getName());
        else
            holder.tv_driver_preference_title.setText(driverPreferenceModel.getData().get(position).getName()+
                    " ("+utility.currencyAdjustment(driverPreferenceModel.getCurrencyAbbr(),driverPreferenceModel.getCurrencySymbol(),
                    driverPreferenceModel.getData().get(position).getFees()+")"));

        if(!driverPreferenceModel.getData().get(position).getIcon().equals(""))
        {
            Glide.with(mContext)
                    .load(driverPreferenceModel.getData().get(position).getIcon())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.iv_driver_preference_icon);
        }
        if(driverPreferenceModel.getSelectedDriverPref().contains(driverPreferenceModel.getData().get(position).getId()))
            holder.tv_driver_preference_title.setSelected(true);
        else
            holder.tv_driver_preference_title.setSelected(false);

        if(driverPreferenceModel.getSelectedDriverPref().isEmpty())
            tempPreferenceDataModels.get(position).setSelected(false);

        holder.rl_driver_pref_layout.setOnClickListener(view ->
        {
            if(tempPreferenceDataModels.get(position).isSelected())
            {
                holder.tv_driver_preference_title.setSelected(false);
                tempPreferenceDataModels.get(position).setSelected(false);
            }
            else
            {
                holder.tv_driver_preference_title.setSelected(true);
                tempPreferenceDataModels.get(position).setSelected(true);
            }
        });
    }

    /**
     * <h3>getItemCount</h3>
     * This method is overridden method used to return the count of the recyclerViews views
     *
     * @return returns the size
     */
    @Override
    public int getItemCount() {
        return driverPreferenceModel.getData().size();
    }

    /**
     * <h1>ZonesViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class DriverPrefViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_driver_preference_title) AppCompatTextView tv_driver_preference_title;
        @BindView(R.id.iv_driver_preference_icon) ImageView iv_driver_preference_icon;
        @BindView(R.id.rl_driver_pref_layout) RelativeLayout rl_driver_pref_layout;

        DriverPrefViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
