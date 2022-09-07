package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karru.rental.RentCarContract;
import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <h1>WalletHardLimitAlert</h1>
 * used to show hard limit alert
 * @author 3Embed
 * @since  on 11-04-2018.
 */
@SuppressLint("ValidFragment")
public class WalletHardLimitAlert extends BottomSheetDialogFragment
{
    private static final String TAG = "WalletAlertBottomSheet";

    @BindView(R.id.tv_wallet_alert_title) TextView tv_wallet_alert_title;
    @BindView(R.id.tv_wallet_alert_recharge) TextView tv_wallet_alert_recharge;
    @BindView(R.id.ll_wallet_alert_bottom) LinearLayout ll_wallet_alert_bottom;
    @BindString(R.string.recharge_wallet_alert) String recharge_wallet_alert;

    private Unbinder unbinder;
    private AppTypeface appTypeface;
    private HomeFragmentContract.View homeView;
    private RentCarContract.View rentalView;

    public WalletHardLimitAlert(AppTypeface appTypeface)
    {
        this.appTypeface = appTypeface;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        Utility.printLog(TAG+" setupDialog ");
        View contentView = View.inflate(getContext(), R.layout.dialog_wallet_balance_alert, null);
        dialog.setContentView(contentView);
        initialize(contentView);
    }

    @OnClick({R.id.tv_wallet_alert_card,R.id.tv_wallet_alert_cash,R.id.tv_wallet_alert_recharge})
    public void clickEvent(View view)
    {
        getDialog().dismiss();
        switch (view.getId())
        {
            case R.id.tv_wallet_alert_recharge:
                if(homeView!=null)
                    homeView.addWalletMoneyLayout();
                else
                    rentalView.addWalletMoneyLayout();
                break;
        }
    }

    /**
     * <h2>initialize</h2>
     * used to initialize the views
     * @param contentView view
     */
    @SuppressLint("SetTextI18n")
    private void initialize(View contentView)
    {
        unbinder= ButterKnife.bind(this,contentView);
        tv_wallet_alert_recharge.setTypeface(appTypeface.getPro_narMedium());
        tv_wallet_alert_title.setTypeface(appTypeface.getPro_News());
        ll_wallet_alert_bottom.setVisibility(View.GONE);
        tv_wallet_alert_recharge.setVisibility(View.VISIBLE);
        tv_wallet_alert_title.setText(recharge_wallet_alert);
    }

    /**
     * <h2>addView</h2>
     * used to give the view
     * @param homeView view
     */
    public void addView(HomeFragmentContract.View homeView, RentCarContract.View rentalView)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
        unbinder.unbind();
    }
}
