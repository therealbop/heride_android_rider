package com.karru.landing.favorite.view;

import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.landing.favorite.FavoriteDriversContract;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.CustomGridLayoutManager;
import com.heride.rider.R;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.FAVORITE_OFFLINE_DRIVER;
import static com.karru.utility.Constants.FAVORITE_ONLINE_DRIVER;

public class FavoriteDriversActivity extends DaggerAppCompatActivity implements FavoriteDriversContract.View
{
    @Inject AppTypeface appTypeface;
    @Inject Alerts alerts;
    @Inject @Named(FAVORITE_ONLINE_DRIVER) FavoriteDriversAdapter favoriteOnlineDriversAdapter;
    @Inject @Named(FAVORITE_OFFLINE_DRIVER) FavoriteDriversAdapter favoriteOfflineDriversAdapter;
    @Inject FavoriteDriversContract.Presenter presenter;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_favorite_online) TextView tv_favorite_online;
    @BindView(R.id.tv_favorite_offline) TextView tv_favorite_offline;
    @BindView(R.id.ll_favorite_drivers_list) LinearLayout ll_favorite_drivers_list;
    @BindView(R.id.rl_back_button) RelativeLayout rl_back_button;
    @BindView(R.id.iv_back_button) ImageView iv_back_button;
    @BindView(R.id.pb_favorite_progress) ProgressBar pb_favorite_progress;
    @BindView(R.id.ll_favorite_empty_background) LinearLayout ll_favorite_empty_background;
    @BindView(R.id.ll_favorite_offline_layout) LinearLayout ll_favorite_offline_layout;
    @BindView(R.id.ll_favorite_online_layout) LinearLayout ll_favorite_online_layout;
    @BindView(R.id.rv_favorite_online_drivers_list) RecyclerView rv_favorite_online_drivers_list;
    @BindView(R.id.rv_favorite_offline_drivers_list) RecyclerView rv_favorite_offline_drivers_list;
    @BindString(R.string.alert) String alert;
    @BindString(R.string.favorites) String favorites;
    @BindString(R.string.fav_alert) String fav_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_drivers);
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    /**
     * <h2>initializeViews</h2>
     * used to initialize the views
     */
    private void initializeViews()
    {
        ButterKnife.bind(this);
        tv_all_tool_bar_title.setText(favorites);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_favorite_online.setTypeface(appTypeface.getPro_narMedium());
        tv_favorite_offline.setTypeface(appTypeface.getPro_narMedium());

        RecyclerView.LayoutManager mLayoutManager = new CustomGridLayoutManager(getApplicationContext(),2,false);
        mLayoutManager.canScrollVertically();
        rv_favorite_online_drivers_list.setLayoutManager(mLayoutManager);
        rv_favorite_online_drivers_list.setItemAnimator(new DefaultItemAnimator());
        rv_favorite_online_drivers_list.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
        rv_favorite_online_drivers_list.setAdapter(favoriteOnlineDriversAdapter);

        RecyclerView.LayoutManager mLayoutManagerOffline = new CustomGridLayoutManager(getApplicationContext(),2,false);
        rv_favorite_offline_drivers_list.setLayoutManager(mLayoutManagerOffline);
        rv_favorite_offline_drivers_list.setItemAnimator(new DefaultItemAnimator());
        rv_favorite_offline_drivers_list.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
        rv_favorite_offline_drivers_list.setAdapter(favoriteOfflineDriversAdapter);

        presenter.getFavoriteDrivers();
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_back_button:
            case R.id.iv_back_button:
                finish();
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.disposeObservable();
    }

    @Override
    public void showProgressDialog()
    {
        pb_favorite_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressDialog()
    {
        pb_favorite_progress.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyScreen()
    {
        ll_favorite_empty_background.setVisibility(View.GONE);
        ll_favorite_drivers_list.setVisibility(View.VISIBLE);
        favoriteOnlineDriversAdapter.notifyDataSetChanged();
        favoriteOfflineDriversAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideOnlineLayout()
    {
        ll_favorite_online_layout.setVisibility(View.GONE);
    }

    @Override
    public void hideOfflineLayout()
    {
        ll_favorite_offline_layout.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyScreen()
    {
        ll_favorite_empty_background.setVisibility(View.VISIBLE);
        ll_favorite_drivers_list.setVisibility(View.GONE);
        favoriteOnlineDriversAdapter.notifyDataSetChanged();
        favoriteOfflineDriversAdapter.notifyDataSetChanged();
    }

    @Override
    public void showConfirmationAlert(String driverId)
    {
        Dialog dialog = alerts.userPromptWithTwoButtons(this);
        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById(R.id.tv_alert_body);
        TextView tv_alert_yes =  dialog.findViewById(R.id.tv_alert_yes);

        tv_alert_title.setText(alert);
        tv_alert_body.setText(fav_alert);
        tv_alert_yes.setOnClickListener(v ->
        {
            dialog.dismiss();
            presenter.deleteDriverFromFav(driverId);
        });
        dialog.show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
