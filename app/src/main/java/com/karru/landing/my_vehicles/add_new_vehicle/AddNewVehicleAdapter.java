package com.karru.landing.my_vehicles.add_new_vehicle;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karru.authentication.privacy.WebViewActivity;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.model.promo_code_model.PromoCodeDataModel;
import com.karru.landing.home.promo_code.PromoCodeContract;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;
import com.karru.util.TimezoneMapper;
import com.heride.rider.R;

import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.WEB_LINK;

/**
 * <h1>PromoCodeAdapter</h1>
 * used to inflate the promo code in promo code screen
 * @author 3Embed
 * @since on 15-06-2018.
 */
public class AddNewVehicleAdapter extends RecyclerView.Adapter<AddNewVehicleAdapter.ViewHolder> {
    private static final String TAG = "PromoCodeAdapter";
    private ArrayList<PromoCodeDataModel> promoCodeDataModel;
    private Context mContext;
    private AppTypeface appTypeface;
    private DateFormatter dateFormatter;
    private PromoCodeContract.View promocodeActivityView;

    public AddNewVehicleAdapter(AppTypeface appTypeface, Context mContext, ArrayList<PromoCodeDataModel> promoCodeDataModels, DateFormatter dateFormatter, PromoCodeContract.View promocodeActivityView)
    {
        this.mContext = mContext;
        this.appTypeface = appTypeface;
        this.promoCodeDataModel = promoCodeDataModels;
        this.dateFormatter = dateFormatter;
        this.promocodeActivityView = promocodeActivityView;
        this.appTypeface = appTypeface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_promo_code_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        PromoCodeDataModel promoCode = promoCodeDataModel.get(position);
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(Double.parseDouble(GMT_CURRENT_LAT),
                Double.parseDouble(GMT_CURRENT_LNG));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        holder.tv_promocode_value.setText(promoCode.getCode());
        holder.tv_promo_code_brif_dis.setText(promoCode.getTitle());
        if(promoCode.getDescription() != null && !promoCode.getDescription().isEmpty())
        {
            holder.tv_promo_code_full_des.setVisibility(View.VISIBLE);
            holder.tv_promo_code_full_des.setText(Html.fromHtml(promoCode.getDescription().trim()));
        }else {
            holder.tv_promo_code_full_des.setVisibility(View.GONE);
        }

        String dateEnd = dateFormatter.getDateInSpecificFormatWithTime(promoCode.getEndTime(),timeZone);
        holder.tv_promo_expire_date.setText(dateEnd);
        holder.tv_promo_code_apply.setOnClickListener(v -> promocodeActivityView.setPromoCodeValue(promoCode.getCode()));
        holder.tv_promo_code_full_des.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = holder.tv_promo_code_full_des.getLineCount();
                String description = holder.tv_promo_code_full_des.getText().toString().trim();
                Log.d(TAG, "run: "+description);
                if(lineCount > 2 && !promoCode.getDescription().equals(""))
                {
                    holder.tv_view_more.setVisibility(View.VISIBLE);
                    holder.tv_view_more.setMaxLines(2);
                    holder.tv_view_more.setText(mContext.getText(R.string.readMore));
                }
                else
                {
                    holder.tv_view_more.setVisibility(View.GONE);
                }
            }
        });
        holder.tv_view_more.setOnClickListener(v -> {

                    if (holder.tv_view_more.getText().toString().equals(mContext.getString(R.string.readMore))) {
                        holder.tv_view_more.setText(mContext.getResources().getString(R.string.readLess));
                        holder.tv_promo_code_full_des.setMaxLines(100);
                    } else {
                        holder.tv_view_more.setText(mContext.getString(R.string.readMore));
                        holder.tv_promo_code_full_des.setMaxLines(2);
                    }
                }
        );

        if(!promoCode.getTermsAndConditions().isEmpty()) {

            holder.tv_term_and_condition.setVisibility(View.VISIBLE);
            holder.tv_term_and_condition.setOnClickListener(v ->
            {
                Intent intentTerms = new Intent(mContext, WebViewActivity.class);
                intentTerms.putExtra(WEB_LINK, promoCode.getTermsAndConditions());
                intentTerms.putExtra(COMING_FROM, mContext.getString(R.string.comingFrom));
                intentTerms.putExtra(SCREEN_TITLE, mContext.getResources().getString(R.string.terms_and_conditions));
                mContext.startActivity(intentTerms);
            });
        }
        else {
            holder.tv_term_and_condition.setVisibility(View.GONE);
        }

    }

    /**
     * <h3>getItemCount</h3>
     * This method is overridden method used to return the count of the recyclerViews views
     *
     * @return returns the size
     */
    @Override
    public int getItemCount() {
        return promoCodeDataModel.size();
    }

    /**
     * <h1>ZonesViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_promocode_value) TextView tv_promocode_value;
        @BindView(R.id.tv_promo_code_brif_dis) TextView tv_promo_code_brif_dis;
        @BindView(R.id.tv_promo_code_full_des) TextView tv_promo_code_full_des;
        @BindView(R.id.tv_promo_expire_date) TextView tv_promo_expire_date;
        @BindView(R.id.tv_promo_code_apply) TextView tv_promo_code_apply;
        @BindView(R.id.tv_separator) TextView tv_separator;
        @BindView(R.id.tv_view_more) TextView tv_view_more;
        @BindView(R.id.tv_term_and_condition) TextView tv_term_and_condition;


        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_promocode_value.setTypeface(appTypeface.getPro_News());
            tv_promo_code_brif_dis.setTypeface(appTypeface.getPro_narMedium());
            tv_promo_code_full_des.setTypeface(appTypeface.getPro_News());
            tv_promo_expire_date.setTypeface(appTypeface.getPro_News());
            tv_promo_code_apply.setTypeface(appTypeface.getPro_News());
            tv_separator.setTypeface(appTypeface.getPro_News());
            tv_view_more.setTypeface(appTypeface.getPro_News());
            tv_term_and_condition.setTypeface(appTypeface.getPro_News());

        }
    }
}
