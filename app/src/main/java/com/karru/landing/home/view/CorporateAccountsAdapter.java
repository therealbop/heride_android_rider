package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.karru.rental.RentCarContract;
import com.heride.rider.R;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>CorporateAccountsAdapter</h1>
 * used to inflate the corporate accounts
 */
public class CorporateAccountsAdapter extends RecyclerView.Adapter<CorporateAccountsAdapter.CorporateAccountsViewHolder>
{
    private static final String TAG = "DriverPreferencesAdapter";
    private ArrayList<CorporateProfileData> corporateProfileData = new ArrayList<>();
    private Context mContext;
    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter homeFragmentPresenter;
    private  HomeFragmentContract.View view;
    private RentCarContract.View rentalView;
    private RentCarContract.Presenter rentalPresenter;

    /**
     * <h2>DriverPreferencesAdapter</h2>
     * constructor
     * @param appTypeface app type face
     * @param mContext context
     */
    public CorporateAccountsAdapter(AppTypeface appTypeface, Context mContext,
                                    HomeFragmentContract.Presenter homeFragmentPresenter,
                                    RentCarContract.Presenter rentalPresenter)
    {
        this.mContext = mContext;
        this.appTypeface = appTypeface;
        this.rentalPresenter = rentalPresenter;
        this.homeFragmentPresenter = homeFragmentPresenter;
        Utility.printLog(TAG+" driver preference size "+this.corporateProfileData.size());
    }

    @NonNull
    @Override
    public CorporateAccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_corporate_profile_choose, parent,
                false);
        return new CorporateAccountsViewHolder(view);
    }

    /**
     * <h2>populateCorporateAccounts</h2>
     * used to populate the profiles
     * @param corporateProfileData profile data
     */
    void populateCorporateAccounts(ArrayList<CorporateProfileData> corporateProfileData,
                                   HomeFragmentContract.View view, RentCarContract.View rentalView)
    {
        this.view = view;
        this.rentalView = rentalView;
        this.corporateProfileData = corporateProfileData;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CorporateAccountsViewHolder holder, int position)
    {
        Utility.printLog(TAG+" driver preference text "+ corporateProfileData.get(position).isSelected()
                +" "+ corporateProfileData.get(position).getInstituteName());
        holder.tv_corporate_title.setTypeface(appTypeface.getPro_News());

        if(corporateProfileData.get(position).isSelected())
            holder.tv_corporate_title.setSelected(true);

        if(position == 0)
        {
            holder.iv_corporate_icon.setImageDrawable(holder.confirmation_personal_icon);
            holder.tv_corporate_title.setText(corporateProfileData.get(position).getInstituteName());
        }
        else if(position == corporateProfileData.size()-1)
        {
            holder.tv_corporate_title.setText(corporateProfileData.get(position).getInstituteName());
            holder.tv_corporate_title.setTextColor(holder.order_status);
            holder.iv_corporate_icon.setImageResource(R.drawable.ic_payment_add_icon);
            holder.tv_corporate_title.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    null,null);
        }
        else
        {
            com.karru.util.Utility utility = new com.karru.util.Utility();
            holder.iv_corporate_icon.setImageDrawable(holder.briefcase_icon);
            holder.tv_corporate_title.setText(corporateProfileData.get(position).getInstituteName()
                    +" ("+utility.currencyAdjustment(corporateProfileData.get(position).getCurrencyAbbr(),
                    corporateProfileData.get(position).getCurrencySymbol(),
                    corporateProfileData.get(position).getUserWalletBalance()+")"));
        }

        holder.rl_corporate_layout.setOnClickListener(view ->
        {
            if(position == corporateProfileData.size()-1)
            {
                if(this.view != null)
                    this.view.openSetupCorporateScreen();
                else
                    this.rentalView.openSetupCorporateScreen();
            }
            else
            {
                if(homeFragmentPresenter!=null)
                    homeFragmentPresenter.setSelectedProfile(corporateProfileData.get(position));
                else
                    rentalPresenter.setSelectedProfile(corporateProfileData.get(position));
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
        return corporateProfileData.size();
    }

    /**
     * <h1>ZonesViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class CorporateAccountsViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_corporate_title) AppCompatTextView tv_corporate_title;
        @BindView(R.id.iv_corporate_icon) ImageView iv_corporate_icon;
        @BindView(R.id.rl_corporate_layout) RelativeLayout rl_corporate_layout;
        @BindDrawable(R.drawable.confirmation_personal_icon) Drawable confirmation_personal_icon;
        //@BindDrawable(R.drawable.ic_payment_add_icon_selector) Drawable ic_payment_add_icon;
        @BindDrawable(R.drawable.briefcase_icon) Drawable briefcase_icon;
        @BindColor(R.color.order_status) int order_status;

        CorporateAccountsViewHolder(View itemView)

        {
            super(itemView);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            ButterKnife.bind(this, itemView);
        }
    }
}