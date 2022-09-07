package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karru.rental.RentCarContract;
import com.heride.rider.R;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

@SuppressLint("ValidFragment")
public class PaymentOptionsBottomSheet extends BottomSheetDialogFragment
{
    private static final String TAG = "PaymentOptionsBottomSheet";

    @BindView(R.id.tv_payment_options_title) TextView tv_payment_options_title;
    @BindView(R.id.tv_payment_options_wallet) AppCompatTextView tv_payment_options_wallet;
    @BindView(R.id.tv_payment_options_card) AppCompatTextView tv_payment_options_card;
    @BindView(R.id.tv_payment_options_cash) AppCompatTextView tv_payment_options_cash;
    @BindView(R.id.tv_payment_options_add_card) AppCompatTextView tv_payment_options_add_card;
    @BindView(R.id.tv_payment_options_add_wallet) AppCompatTextView tv_payment_options_add_wallet;
    @BindView(R.id.tv_payment_options_wallet_money) AppCompatTextView tv_payment_options_wallet_money;
    @BindView(R.id.rl_homepage_wallet_option) RelativeLayout rl_homepage_wallet_option;
    @BindString(R.string.card_ending_with) String card_ending_with;
    @BindString(R.string.wallet) String wallet;
    @BindDrawable(R.drawable.ic_payment_card_icon_selector) Drawable ic_payment_card_icon;
    @BindDrawable(R.drawable.ic_invoice_one_tick_mark_on) Drawable ic_invoice_one_tick_mark_on;
    @BindDrawable(R.drawable.ic_payment_tick_unselector_icon) Drawable ic_payment_tick_unselector_icon;
    @BindDrawable(R.drawable.ic_payment_cash_icon_selector) Drawable ic_payment_cash_icon;
    @BindDrawable(R.drawable.ic_payment_wallet_icon_selector) Drawable ic_payment_wallet_icon;

    private AppTypeface appTypeface;
    private HomeFragmentContract.Presenter presenter;
    private RentCarContract.Presenter rentalPresenter;
    private HomeFragmentContract.View homeView;
    private RentCarContract.View rentalView;

    public PaymentOptionsBottomSheet(AppTypeface appTypeface,
                                     HomeFragmentContract.Presenter presenter,
                                     RentCarContract.Presenter rentalPresenter) {
        this.appTypeface = appTypeface;
        this.presenter = presenter;
        this.rentalPresenter = rentalPresenter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Utility.printLog(TAG+" setupDialog ");
        View contentView = View.inflate(getContext(), R.layout.dialog_fragment_payment_options, null);
        dialog.setContentView(contentView);
        initialize(contentView);
        setTypeface();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @OnClick({R.id.tv_payment_options_add_card, R.id.tv_payment_options_card,
            R.id.tv_payment_options_cash,R.id.tv_payment_options_add_wallet,R.id.rl_homepage_wallet_option})
    public void clickEvent(View view)
    {
        getDialog().dismiss();
        switch (view.getId())
        {
            case R.id.tv_payment_options_add_card:
                if(homeView != null)
                    homeView.openPaymentScreen();
                else
                    rentalView.openPaymentScreen();
                break;

            case R.id.tv_payment_options_card:
                if(presenter!=null)
                    presenter.setCardPaymentOption();
                else
                    rentalPresenter.setCardPaymentOption();
                break;

            case R.id.tv_payment_options_cash:
                if(homeView != null)
                    homeView.setCashPaymentOption();
                else
                    rentalView.setCashPaymentOption();
                break;

            case R.id.tv_payment_options_add_wallet:
                if(homeView != null)
                    homeView.addWalletMoneyLayout();
                else
                    rentalView.addWalletMoneyLayout();
                break;

            case R.id.rl_homepage_wallet_option:
                if(homeView != null)
                    homeView.setWalletPaymentOption(tv_payment_options_wallet.getText().toString());
                else
                    rentalView.setWalletPaymentOption(tv_payment_options_wallet.getText().toString());
                break;
        }
    }

    /**
     * <h2>setTypeface</h2>
     * Used to set the typeface
     */
    private void setTypeface()
    {
        tv_payment_options_title.setTypeface(appTypeface.getPro_narMedium());
        tv_payment_options_wallet_money.setTypeface(appTypeface.getPro_narMedium());
        tv_payment_options_wallet.setTypeface(appTypeface.getPro_News());
        tv_payment_options_card.setTypeface(appTypeface.getPro_News());
        tv_payment_options_cash.setTypeface(appTypeface.getPro_News());
        tv_payment_options_add_card.setTypeface(appTypeface.getPro_News());
        tv_payment_options_add_wallet.setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize the variables
     */
    private void initialize(View contentView)
    {
        Utility.printLog(TAG+" initialize bottomsheet ");
        ButterKnife.bind(this,contentView);
        if(presenter!=null)
            presenter.checkForPaymentOptions();
        else
            rentalPresenter.checkForPaymentOptions();
    }

    /**
     * <h2>disableCashOption</h2>
     * used to disable cash option
     */
    public void disableCashOption(HomeFragmentContract.View homeView, RentCarContract.View view)
    {
        this.homeView = homeView;
        this.rentalView = view;
        tv_payment_options_cash.setVisibility(View.GONE);
    }

    /**
     * <h2>disableCardOption</h2>
     * used to disable card option
     */
    public void disableCardOption(HomeFragmentContract.View homeView, RentCarContract.View rentalView,
                                  boolean disable)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
        tv_payment_options_card.setVisibility(View.GONE);
        if(disable)
            tv_payment_options_add_card.setVisibility(View.GONE);
    }

    /**
     * <h2>disableWalletOption</h2>
     * used to disable waller option
     */
    public void disableWalletOption(HomeFragmentContract.View homeView, RentCarContract.View rentalView)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
        rl_homepage_wallet_option.setVisibility(View.GONE);
        tv_payment_options_add_wallet.setVisibility(View.GONE);
    }

    /**
     * <h2>hideWalletOption</h2>
     * used to hide wallet option
     */
    public void hideWalletOption(HomeFragmentContract.View homeView, RentCarContract.View rentalView)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
        rl_homepage_wallet_option.setVisibility(View.GONE);
    }

    /**
     * <h2>enableWalletOption</h2>
     * used to enable waller option
     */
    @SuppressLint("SetTextI18n")
    public void enableWalletOption(HomeFragmentContract.View homeView, RentCarContract.View rentalView, String amount)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
        tv_payment_options_wallet.setText(wallet+" ("+amount+")");
        rl_homepage_wallet_option.setVisibility(View.VISIBLE);
        tv_payment_options_add_wallet.setVisibility(View.VISIBLE);
    }

    /**
     * <h2>showPaymentOptions</h2>
     * used to set the default card
     * @param lastDigits last digits of card
     * @param cardType card type
     */
    @SuppressLint("SetTextI18n")
    public void showPaymentOptions(String lastDigits, String cardType, int paymentType,
                                   HomeFragmentContract.View homeView, RentCarContract.View rentalView,
                                   boolean isWalletSelected)
    {
        this.homeView = homeView;
        this.rentalView = rentalView;
        Utility.printLog(TAG+" payment type "+paymentType);
        Drawable cardBrand = ic_payment_card_icon;
        if(cardType!=null)
        {
            tv_payment_options_card.setText(card_ending_with+" "+lastDigits);
            cardBrand = getResources().getDrawable(BRAND_RESOURCE_MAP.get(cardType));
        }

        if(isWalletSelected)
        {
            tv_payment_options_card.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,ic_payment_tick_unselector_icon,null);
            tv_payment_options_cash.setCompoundDrawablesWithIntrinsicBounds(ic_payment_cash_icon,null,ic_payment_tick_unselector_icon,null);
            tv_payment_options_wallet_money.setCompoundDrawablesWithIntrinsicBounds(null,null,ic_invoice_one_tick_mark_on,null);
        }
        else
        {
            switch (paymentType)
            {
                case 1:
                    tv_payment_options_card.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,ic_invoice_one_tick_mark_on,null);
                    tv_payment_options_cash.setCompoundDrawablesWithIntrinsicBounds(ic_payment_cash_icon,null,ic_payment_tick_unselector_icon,null);
                    tv_payment_options_wallet_money.setCompoundDrawablesWithIntrinsicBounds(null,null,ic_payment_tick_unselector_icon,null);
                    break;

                case 2:
                    tv_payment_options_card.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,ic_payment_tick_unselector_icon,null);
                    tv_payment_options_cash.setCompoundDrawablesWithIntrinsicBounds(ic_payment_cash_icon,null,ic_invoice_one_tick_mark_on,null);
                    tv_payment_options_wallet_money.setCompoundDrawablesWithIntrinsicBounds(null,null,ic_payment_tick_unselector_icon,null);
                    break;
            }
        }
    }
}