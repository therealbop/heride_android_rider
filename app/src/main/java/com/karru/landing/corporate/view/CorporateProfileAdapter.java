package com.karru.landing.corporate.view;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.dagger.ActivityScoped;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.corporate.CorporateProfileModel;
import com.karru.util.AppTypeface;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

@ActivityScoped
public class CorporateProfileAdapter extends  RecyclerView.Adapter<CorporateProfileAdapter.ViewHolder>
{
    private ArrayList<CorporateProfileData> corporateProfileList;
    private AppTypeface appTypeface;

    @Inject
    public CorporateProfileAdapter(AppTypeface appTypeface)
    {
        this.appTypeface=appTypeface;

    }

    void setArrayList(ArrayList<CorporateProfileData> corporateProfileList)
    {
        this.corporateProfileList = corporateProfileList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_corporate_profile, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
       holder.tv_corporate_mail.setText(corporateProfileList.get(position).getCorporateEmail());
       holder.tv_corporate_name.setText(corporateProfileList.get(position).getInstituteName());
       if(corporateProfileList.size()-1 == position)
           holder.vw_corporate_divider.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return corporateProfileList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_corporate_name) TextView tv_corporate_name;
        @BindView(R.id.vw_corporate_divider) View vw_corporate_divider;
        @BindView(R.id.tv_corporate_mail) TextView tv_corporate_mail;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            setTypeFace();
        }

        void setTypeFace()
        {
            tv_corporate_name.setTypeface(appTypeface.getPro_narMedium());
            tv_corporate_name.setTypeface(appTypeface.getPro_News());
        }
    }
}
