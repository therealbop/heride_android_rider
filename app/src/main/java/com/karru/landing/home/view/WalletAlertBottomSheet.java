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

@SuppressLint("ValidFragment")
public class WalletAlertBottomSheet extends BottomSheetDialogFragment
{
    private static final String TAG = "WalletAlertBottomSheet";

    @BindView(R.id.tv_wallet_alert_title) TextView tv_wallet_alert_title;
    @BindView(R.id.tv_wallet_alert_card) TextView tv_wallet_alert_card;
    @BindView(R.id.tv_wallet_alert_cash) TextView tv_wallet_alert_cash;
    @BindView(R.id.tv_wallet_alert_recharge) TextView tv_wallet_alert_recharge;
    @BindView(R.id.ll_wallet_alert_bottom) LinearLayout ll_wallet_alert_bottom;
    @BindString(R.string.wallet_excess) String wallet_excess;
    @BindString(R.string.recharge_wallet_alert) String recharge_wallet_alert;
    @BindString(R.string.card) String card;
    @BindString(R.string.cash) String cash;
    @BindString(R.string.you_have) String you_have;
    @BindString(R.string.in_wallet) String in_wallet;
    @BindString(R.string.use_for_booking) String use_for_booking;
    @BindString(R.string.yes) String yes;
    @BindString(R.string.no) String no;

    private Unbinder unbinder;
    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter presenter;
    private RentCarContract.Presenter rentalPresenter;
    private boolean isWalletExtraMoney;
    private String walletBalance;
    private int paymentType;
    private HomeFragmentContract.View homeView;
    private RentCarContract.View rentalView;

    public WalletAlertBottomSheet(AppTypeface appTypeface, HomeFragmentContract.Presenter presenter,
                                  RentCarContract.Presenter rentalPresenter)
    {
        this.appTypeface = appTypeface;
        this.presenter = presenter;
        this.rentalPresenter = rentalPresenter;
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
        setTypeface();
    }

    @OnClick({R.id.tv_wallet_alert_card,R.id.tv_wallet_alert_cash,R.id.tv_wallet_alert_recharge})
    public void clickEvent(View view)
    {
        getDialog().dismiss();
        switch (view.getId())
        {
            case R.id.tv_wallet_alert_card:
                if(presenter!=null)
                {
                    if(isWalletExtraMoney)
                        presenter.chooseCardWithWallet(false);
                    else
                        presenter.openRequest();
                }
                else
                {
                    if(isWalletExtraMoney)
                        rentalPresenter.chooseCardWithWallet(false);
                    else
                        rentalPresenter.openRequest();
                }
                break;

            case R.id.tv_wallet_alert_cash:
                if(presenter!=null)
                {
                    if(isWalletExtraMoney)
                        presenter.chooseCashWithWallet(false);
                    else
                    {
                        switch (paymentType)
                        {
                            case 1:
                                presenter.chooseCardWithWallet(true);
                                break;

                            case 2:
                                presenter.chooseCashWithWallet(true);
                                break;
                        }
                    }
                }
                else
                {
                    if(isWalletExtraMoney)
                        rentalPresenter.chooseCashWithWallet(false);
                    else
                    {
                        switch (paymentType)
                        {
                            case 1:
                                rentalPresenter.chooseCardWithWallet(true);
                                break;

                            case 2:
                                rentalPresenter.chooseCashWithWallet(true);
                                break;
                        }
                    }
                }
                break;

            case R.id.tv_wallet_alert_recharge:
                if(homeView != null)
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
        ll_wallet_alert_bottom.setVisibility(View.VISIBLE);
        tv_wallet_alert_recharge.setVisibility(View.GONE);
        if(isWalletExtraMoney)
        {
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setCancelable(false);
            tv_wallet_alert_title.setText(wallet_excess);
            tv_wallet_alert_card.setText(card);
            tv_wallet_alert_cash.setText(cash);
        }
        else
        {
            tv_wallet_alert_title.setText(you_have+" "+walletBalance+" "+in_wallet+" "+use_for_booking);
            tv_wallet_alert_card.setText(no);
            tv_wallet_alert_cash.setText(yes);
        }
    }

    /**
     * <h2>setTypeface</h2>
     * Used to set the typeface
     */
    private void setTypeface()
    {
        tv_wallet_alert_title.setTypeface(appTypeface.getPro_News());
        tv_wallet_alert_card.setTypeface(appTypeface.getPro_narMedium());
        tv_wallet_alert_cash.setTypeface(appTypeface.getPro_narMedium());
        tv_wallet_alert_recharge.setTypeface(appTypeface.getPro_narMedium());
    }

    /**
     * <h2>populateWalletAlert</h2>
     * used to populate the wallet money
     * @param isWalletExtraMoney wallet money is enabled
     */
    public void populateWalletAlert(boolean isWalletExtraMoney, String walletBalance,int paymentType,
                                    HomeFragmentContract.View homeView,RentCarContract.View rentalView)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
        this.isWalletExtraMoney = isWalletExtraMoney;
        this.walletBalance = walletBalance;
        this.paymentType = paymentType;
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
        unbinder.unbind();
    }
}