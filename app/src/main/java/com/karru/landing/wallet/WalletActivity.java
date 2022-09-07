package com.karru.landing.wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;
import com.karru.landing.payment.PaymentActivity;
import com.karru.landing.wallet.wallet_transaction.view.WalletTransactionActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.Utility;

import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;
import static com.karru.utility.Constants.IS_FROM_BOOKING;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

public class WalletActivity extends DaggerAppCompatActivity implements
        WalletActivityContract.WalletView
{
    @BindView(R.id.tv_wallet_balance)  TextView tv_wallet_balance;
    @BindView(R.id.tv_wallet_softLimitValue) TextView tv_wallet_softLimitValue;
    @BindView(R.id.tv_wallet_hardLimitValue) TextView tv_wallet_hardLimitValue;
    @BindView(R.id.tv_wallet_cardNo) TextView tv_wallet_cardNo;
    @BindView(R.id.tv_wallet_currencySymbol_prefix) TextView tv_wallet_currencySymbol_prefix;
    @BindView(R.id.tv_wallet_currencySymbol_post) TextView tv_wallet_currencySymbol_post;
    @BindView(R.id.et_wallet_payAmountValue) EditText et_wallet_payAmountValue;
    @BindView(R.id.tv_wallet_curCredit_label) TextView tv_wallet_curCredit_label;
    @BindView(R.id.tv_wallet_softLimitLabel) TextView tv_wallet_softLimitLabel;
    @BindView(R.id.tv_wallet_hardLimitLabel) TextView tv_wallet_hardLimitLabel;
    @BindView(R.id.btn_wallet_recentTransactions) Button btn_wallet_recentTransactions;
    @BindView(R.id.tv_wallet_payUsing_cardLabel) TextView tv_wallet_payUsing_cardLabel;
    @BindView(R.id.tv_wallet_payAmountLabel) TextView tv_wallet_payAmountLabel;
    @BindView(R.id.tv_wallet_softLimitMsgLabel) TextView tv_wallet_softLimitMsgLabel;
    @BindView(R.id.tv_wallet_softLimitMsg) TextView tv_wallet_softLimitMsg;
    @BindView(R.id.tv_wallet_hardLimitMsgLabel) TextView tv_wallet_hardLimitMsgLabel;
    @BindView(R.id.tv_wallet_hardLimitMsg) TextView tv_wallet_hardLimitMsg;
    @BindView(R.id.tv_wallet_credit_desc) TextView tv_wallet_credit_desc;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.btn_wallet_ConfirmAndPay) Button btn_wallet_ConfirmAndPay;
    @BindView(R.id.rl_wallet_card) RelativeLayout rl_wallet_card;
    @BindString(R.string.paymentMsg1) String paymentMsg1;
    @BindString(R.string.app_name) String app_name;
    @BindString(R.string.paymentMsg2) String paymentMsg2;
    @BindString(R.string.confirmPayment) String confirmPayment;
    @BindString(R.string.cardNoHidden) String cardNoHidden;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.wallet) String wallet;
    @BindString(R.string.add_card_debit) String choose_card;
    @BindString(R.string.select_card) String select_card;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.amount_to_be_recharged) String amount_to_be_recharged;
    @BindString(R.string.non_zero_recharged) String non_zero_recharged;
    @BindColor(R.color.gray) int gray;

    @BindDrawable(R.drawable.ic_payment_card_icon_selector) Drawable ic_payment_card_icon;
    @BindDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp_selector) Drawable home_next_arrow_icon_off;
    @BindDrawable(R.drawable.selector_layout) Drawable selector_layout;

    @Inject Utility utility;
    @Inject WalletActivityContract.WalletPresenter walletPresenter;
    @Inject AppTypeface appTypeface;
    @Inject Alerts alerts;

    private ProgressDialog pDialog;
    private Unbinder unbinder;
    private  String currency;
    private int currencyAbbr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        unbinder = ButterKnife.bind(this);
        initViews();
        initPayViews();
        setTypeface();
    }

    @OnClick({R.id.btn_wallet_recentTransactions,R.id.tv_wallet_cardNo,R.id.btn_wallet_ConfirmAndPay,
            R.id.iv_back_button,R.id.rl_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_wallet_recentTransactions:
                Intent intent = new Intent(this, WalletTransactionActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.tv_wallet_cardNo:
                Intent cardsIntent = new Intent(this, PaymentActivity.class);
                cardsIntent.putExtra(SCREEN_TITLE,select_card);
                cardsIntent.putExtra(IS_FROM_BOOKING,true);
                startActivityForResult(cardsIntent, 1);
                break;

            case R.id.btn_wallet_ConfirmAndPay:
                if(!et_wallet_payAmountValue.getText().toString().isEmpty())
                {
                    if(Double.parseDouble(et_wallet_payAmountValue.getText().toString())>0)
                        showRechargeConfirmationAlert(utility.currencyAdjustment(currencyAbbr,currency,et_wallet_payAmountValue.getText().toString()),
                                et_wallet_payAmountValue.getText().toString());
                    else
                        showToast(non_zero_recharged,Toast.LENGTH_LONG);
                }
                else
                    showToast(amount_to_be_recharged,Toast.LENGTH_LONG);
                break;

            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
        }
    }

    /**
     *<h2>initViews</h2>
     * <P> custom method to load tooth_top views of the screen </P>
     */
    private void initViews()
    {
        walletPresenter.getLastCardNo();
        tv_wallet_credit_desc.setTypeface(appTypeface.getPro_News());
        tv_wallet_curCredit_label.setTypeface(appTypeface.getPro_News());
        tv_wallet_balance.setTypeface(appTypeface.getPro_News());
        tv_wallet_softLimitLabel.setTypeface(appTypeface.getPro_News());
        tv_wallet_softLimitValue.setTypeface(appTypeface.getPro_News());
        tv_wallet_hardLimitLabel.setTypeface(appTypeface.getPro_News());
        tv_wallet_hardLimitValue.setTypeface(appTypeface.getPro_News());
        btn_wallet_recentTransactions.setTypeface(appTypeface.getPro_News());
        btn_wallet_ConfirmAndPay.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        et_wallet_payAmountValue.setTypeface(appTypeface.getPro_News());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setText(wallet);
    }

    @Override
    public void prefixCurrency(String currency)
    {
        tv_wallet_currencySymbol_prefix.setVisibility(View.VISIBLE);
        tv_wallet_currencySymbol_post.setVisibility(View.GONE);
        tv_wallet_currencySymbol_prefix.setText(currency);
    }

    @Override
    public void postfixCurrency(String currency)
    {
        tv_wallet_currencySymbol_prefix.setVisibility(View.GONE);
        tv_wallet_currencySymbol_post.setVisibility(View.VISIBLE);
        tv_wallet_currencySymbol_post.setText(currency);
    }

    @Override
    public void setBalanceValues(String balance,String hardLimit,String softLimit,String currency,
                                 int currencyAbbr)
    {
        this.currency = currency;
        this.currencyAbbr = currencyAbbr;
        tv_wallet_softLimitValue.setText(softLimit);
        tv_wallet_hardLimitValue.setText(hardLimit);
        tv_wallet_balance.setText(balance);
        et_wallet_payAmountValue.setText("");
    }

    /**
     *<h2>initPayViews</h2>
     * <P> custom method to load payment views of the screen </P>
     */
    private void initPayViews()
    {
        tv_wallet_payUsing_cardLabel.setTypeface(appTypeface.getPro_narMedium());
        tv_wallet_cardNo.setTypeface(appTypeface.getPro_News());
        tv_wallet_payAmountLabel.setTypeface(appTypeface.getPro_narMedium());
        tv_wallet_currencySymbol_prefix.setTypeface(appTypeface.getPro_News());
        tv_wallet_currencySymbol_post.setTypeface(appTypeface.getPro_News());
        tv_wallet_hardLimitLabel.setTypeface(appTypeface.getPro_News());
        tv_wallet_hardLimitValue.setTypeface(appTypeface.getPro_News());
    }

    /**
     *<h2>setTypeface</h2>
     * <P> custom method to init soft and hard limit description views of the screen </P>
     */
    private void setTypeface()
    {
        tv_wallet_softLimitMsgLabel.setTypeface(appTypeface.getPro_News());
        tv_wallet_softLimitMsg.setTypeface(appTypeface.getPro_News());
        tv_wallet_hardLimitMsgLabel.setTypeface(appTypeface.getPro_News());
        tv_wallet_hardLimitMsg.setTypeface(appTypeface.getPro_News());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        walletPresenter.checkRTLConversion();
        walletPresenter.getWalletDetails();
    }

    @Override
    public void showCardOption() {
        rl_wallet_card.setVisibility(View.VISIBLE);
        btn_wallet_ConfirmAndPay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCardOption() {
        rl_wallet_card.setVisibility(View.GONE);
        btn_wallet_ConfirmAndPay.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void showProgressDialog()
    {
        if(pDialog == null)
            pDialog = alerts.getProcessDialog(this);

        if(!pDialog.isShowing())
        {
            pDialog = alerts.getProcessDialog(this);
            pDialog.show();
        }
    }

    @Override
    public void showAlert(String message)
    {
        Dialog dialog = alerts.userPromptWithOneButton(message,this);
        TextView tv_alert_ok =  dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_ok.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showToast(String msg, int duration)
    {
        Toast.makeText(this, msg, duration).show();
    }

    @Override
    public void showAlert(String title, String msg)
    {
        hideProgressDialog();
        Toast.makeText(this, "SHOW ALERT", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        walletPresenter.makeCardAsDefault();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void setNoCard()
    {
        btn_wallet_ConfirmAndPay.setEnabled(false);
        btn_wallet_ConfirmAndPay.setBackgroundColor(gray);
        Drawable cardBrand = ic_payment_card_icon;
        tv_wallet_cardNo.setText(choose_card);
        tv_wallet_cardNo.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,home_next_arrow_icon_off,null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setCard(String cardNum,String cardType)
    {
        btn_wallet_ConfirmAndPay.setEnabled(true);
        btn_wallet_ConfirmAndPay.setBackground(selector_layout);
        Drawable cardBrand = ic_payment_card_icon;
        if(cardType!=null){
            cardBrand = getResources().getDrawable(BRAND_RESOURCE_MAP.get(cardType));
            tv_wallet_cardNo.setText(cardNoHidden+" "+cardNum);
        }
        tv_wallet_cardNo.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,home_next_arrow_icon_off,null);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        walletPresenter.disposeObservable();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * <h2>showRechargeConfirmationAlert</h2>
     * <p> method to show an alert dialog to take user confirmation to proceed to recharge </p>
     */
    @SuppressLint("SetTextI18n")
    public void showRechargeConfirmationAlert(String amount,String amountToBeSent)
    {
        Dialog dialog = alerts.userPromptWithTwoButtons(this);
        TextView tv_alert_yes =  dialog.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById(R.id.tv_alert_body);
        tv_alert_title.setText(confirmPayment);
        tv_alert_body.setText(paymentMsg1 + " "+ app_name +" "+paymentMsg2+" "+amount);

        tv_alert_yes.setOnClickListener(v ->
        {
            walletPresenter.rechargeWallet(amountToBeSent);
            dialog.dismiss();
        });
        dialog.show();
    }
}
