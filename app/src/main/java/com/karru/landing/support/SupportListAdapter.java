package com.karru.landing.support;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.authentication.privacy.WebViewActivity;
import com.karru.landing.support.model.SupportDataModel;
import com.karru.landing.support.model.SupportSubCatModel;
import com.karru.util.AppTypeface;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.ButterKnife;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.WEB_LINK;

/**
 * <h1>SupportListAdapter</h1>
 * <p>
 *     adapter class to show support categories along with its sub categories on expand
 * </p>
 * @since 30/10/17.
 */

public class SupportListAdapter extends RecyclerView.Adapter<SupportListAdapter.SupportMainCatViewHolder>
{
    private Context mContext;
    private ArrayList<SupportDataModel> supportDataPojosAL;
    private LayoutInflater layoutInflater;
    private SupportActivity fragment;

    @Inject AppTypeface appTypeface;

    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     */
    @Inject
    SupportListAdapter(Context mContext)
    {
        this.mContext = mContext;
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    void setFragmentRef(SupportActivity fragment) {
        this.fragment = fragment;
    }

    /**
     * <h>Data List setter</h>
     * <p>this method is using to set ArrayList for the adpter</p>
     * @param supportDataPojos is the Arraylist contains options
     */
    void setAdapterList(ArrayList<SupportDataModel> supportDataPojos)
    {
        this.supportDataPojosAL = supportDataPojos;
    }

    @Override
    public int getItemCount()
    {
        return supportDataPojosAL.size();
    }

    @Override
    public SupportMainCatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_support, parent, false);
        return new SupportMainCatViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(final SupportMainCatViewHolder mainCatViewHolder, final int position)
    {
        final SupportDataModel mainCatItem = supportDataPojosAL.get(position);
        mainCatViewHolder.tvSupportMainCatName.setText(mainCatItem.getName());
        mainCatViewHolder.tvSupportMainCatName.setTypeface(appTypeface.getPro_News());

        if(mainCatItem.getSubcat() != null && mainCatItem.getSubcat().size() > 0)
        {
            // mainCatViewHolder.ivSupportMainCat.setRotation(180);
            mainCatViewHolder.ivSupportMainCat.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp_selector);
            bindChildViewHolder(mainCatViewHolder.llSupportSubCat, mainCatItem.getSubcat());
        }else
        {
            mainCatViewHolder.ivSupportMainCat.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp_selector);
            mainCatViewHolder.llSupportSubCat.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if( mContext.getResources().getConfiguration().getLayoutDirection() == LayoutDirection.RTL)
                    mainCatViewHolder.ivSupportMainCat.setRotation(-180);
            }
        }

        //to notify when on item is clicked
        mainCatViewHolder.rlSupportMainCat.setOnClickListener(v -> {
            if(mainCatItem.getSubcat() != null && mainCatItem.getSubcat().size() > 0) {
                if (mainCatItem.getHasSubCatExpanded()) {
                    mainCatViewHolder.ivSupportMainCat.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp_selector);
                    mainCatViewHolder.llSupportSubCat.setVisibility(View.GONE);
                    mainCatItem.setHasSubCatExpanded(false);
                } else {
                    mainCatViewHolder.ivSupportMainCat.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    mainCatViewHolder.llSupportSubCat.setVisibility(View.VISIBLE);
                    mainCatItem.setHasSubCatExpanded(true);
                }
            }else {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(WEB_LINK, mainCatItem.getLink());
                intent.putExtra(SCREEN_TITLE, mContext.getString(R.string.title_support));
                intent.putExtra(COMING_FROM, "web");
                fragment.startActivity(intent);
            }
        });
    }

    /**
     * <h2>bindChildViewHolder</h2>
     * <p>
     *     method to add sub category of support list to the recycler view
     * </p>
     * @param llSupportSubCat: reference of root linear layout for support
     * @param subCatAL: list of sub categories
     */
    private void bindChildViewHolder (LinearLayout llSupportSubCat, final ArrayList<SupportSubCatModel> subCatAL)
    {
        for(int j =0; j < subCatAL.size(); j++)
        {
            View subCatRootView = layoutInflater.inflate(R.layout.item_support_sub_cat, llSupportSubCat, false);
            final SupportSubCatModel subCatItem = subCatAL.get(j);
            TextView tvSupportSubCat = subCatRootView.findViewById( R.id.tvSupportSubCat);
            tvSupportSubCat.setTypeface(appTypeface.getPro_News());
            tvSupportSubCat.setText(subCatItem.getName());

            RelativeLayout rlSupportSubCat = subCatRootView.findViewById( R.id.rlSupportSubCat);
            rlSupportSubCat.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(WEB_LINK, subCatItem.getLink());
                intent.putExtra(SCREEN_TITLE, mContext.getString(R.string.title_support));
                intent.putExtra(COMING_FROM, "web");
                fragment.startActivity(intent);
            });

            if(j == subCatAL.size() -1)
            {
                View viewSubCatBottomLine = subCatRootView.findViewById( R.id.viewSubCatBottomLine);
                viewSubCatBottomLine.setVisibility(View.GONE);
            }
            llSupportSubCat.addView(subCatRootView);
        }
    }

    /**
     * <h2>SupportMainCatViewHolder</h2>
     * This method is used where all the views are defined.
     */
    static class SupportMainCatViewHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout rlSupportMainCat;
        private TextView tvSupportMainCatName;
        private ImageView ivSupportMainCat;
        private LinearLayout llSupportSubCat;

        SupportMainCatViewHolder(View rootView)
        {
            super(rootView);
            rlSupportMainCat = rootView.findViewById( R.id.rlSupportMainCat);
            tvSupportMainCatName = rootView.findViewById( R.id.tvSupportMainCatName);
            ivSupportMainCat = rootView.findViewById( R.id.ivSupportMainCat);
            llSupportSubCat = rootView.findViewById( R.id.llSupportSubCat);
        }
    }
}
