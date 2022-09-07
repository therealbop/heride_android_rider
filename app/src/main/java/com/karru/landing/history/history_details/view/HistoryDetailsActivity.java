package com.karru.landing.history.history_details.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.karru.landing.history.history_details.HistoryDetailsContract;
import com.heride.rider.R;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.booking_flow.invoice.view.ReceiptDetailsAdapter;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;

import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.BOOKING_ID;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

public class HistoryDetailsActivity extends DaggerAppCompatActivity implements HistoryDetailsContract.View
{
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_all_tool_bar_title2) TextView tv_all_tool_bar_title2;
    @BindView(R.id.tv_history_details_status) TextView tv_history_details_status;
    @BindView(R.id.tv_history_details_cancel) TextView tv_history_details_cancel;
    @BindView(R.id.ll_history_details_bottom) LinearLayout ll_history_details_bottom;
    @BindView(R.id.ll_history_detail_rating) LinearLayout ll_history_detail_rating;
    @BindView(R.id.tv_history_detail_name) TextView tv_history_detail_name;
    @BindView(R.id.tv_history_detail_rate) TextView tv_history_detail_rate;
    @BindView(R.id.tv_history_detail_amount) TextView tv_history_detail_amount;
    @BindView(R.id.tv_history_detail_type) TextView tv_history_detail_type;
    @BindView(R.id.tv_history_detail_type_name) TextView tv_history_detail_type_name;
    @BindView(R.id.tv_history_detail_plate) TextView tv_history_detail_plate;
    @BindView(R.id.tv_history_detail_distance) TextView tv_history_detail_distance;
    @BindView(R.id.tv_history_detail_distance_symbol) TextView tv_history_detail_distance_symbol;
    @BindView(R.id.tv_history_detail_hours) TextView tv_history_detail_hours;
    @BindView(R.id.tv_history_detail_hours_text) TextView tv_history_detail_hours_text;
    @BindView(R.id.tv_history_detail_minutes) TextView tv_history_detail_minutes;
    @BindView(R.id.tv_history_detail_minutes_value) TextView tv_history_detail_minutes_value;
    @BindView(R.id.tv_history_pick_address) TextView tv_history_pick_address;
    @BindView(R.id.tv_history_drop_address) TextView tv_history_drop_address;
    @BindView(R.id.tv_history_details_receipt) TextView tv_history_details_receipt;
    @BindView(R.id.tv_history_details_help) TextView tv_history_details_help;
    @BindView(R.id.tv_history_card_text) TextView tv_history_card_text;
    @BindView(R.id.tv_history_card_value) TextView tv_history_card_value;
    @BindView(R.id.tv_history_cash_text) TextView tv_history_cash_text;
    @BindView(R.id.tv_history_corporate_text) TextView tv_history_corporate_text;
    @BindView(R.id.tv_history_wallet_text) TextView tv_history_wallet_text;
    @BindView(R.id.tv_history_wallet_value) TextView tv_history_wallet_value;
    @BindView(R.id.tv_history_cash_value) TextView tv_history_cash_value;
    @BindView(R.id.tv_history_corporate_value) TextView tv_history_corporate_value;
    @BindView(R.id.tv_history_payment_title) TextView tv_history_payment_title;
    @BindView(R.id.atv_history_detail_rate) AppCompatTextView atv_history_detail_rate;
    @BindView(R.id.tv_history_detail_image) ImageView tv_history_detail_image;
    @BindView(R.id.iv_history_corporate_image) ImageView iv_history_corporate_image;
    @BindView(R.id.pb_history_detail_image) ProgressBar pb_history_detail_image;
    @BindView(R.id.rl_back_button) RelativeLayout rl_back_button;
    @BindView(R.id.rl_history_wallet) RelativeLayout rl_history_wallet;
    @BindView(R.id.rl_history_cash) RelativeLayout rl_history_cash;
    @BindView(R.id.rl_history_card) RelativeLayout rl_history_card;
    @BindView(R.id.rl_history_corporate) RelativeLayout rl_history_corporate;
    @BindView(R.id.ll_history_drop) LinearLayout ll_history_drop;
    @BindView(R.id.rv_receipt_details) RecyclerView rv_receipt_details;
    @BindView(R.id.iv_back_button) ImageView iv_back_button;
    @BindView(R.id.iv_history_wallet_image) ImageView iv_history_wallet_image;
    @BindView(R.id.iv_history_card_image) ImageView iv_history_card_image;
    @BindView(R.id.iv_history_cash_image) ImageView iv_history_cash_image;
    @BindView(R.id.ll_receipt_details) LinearLayout ll_receipt_details;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;
    @BindDrawable(R.drawable.ic_payment_cash_icon_selector) Drawable ic_payment_cash_icon;
    @BindDrawable(R.drawable.ic_payment_wallet_icon_selector) Drawable ic_payment_wallet_icon;
    @BindDrawable(R.drawable.corporate_profile_off) Drawable corporate_profile_off;
    @BindColor(R.color.green_history) int green_history;
    @BindColor(R.color.red_history) int red_history;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.tab_unselect) int tab_unselect;
    @BindString(R.string.cash) String  cash;
    @BindString(R.string.card_ending_with1) String  card_ending_with1;

    @Inject Context mContext;
    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject ReceiptDetailsAdapter receiptDetailsAdapter;
    @Inject HistoryDetailsHelpAdapter historyDetailsHelpAdapter;
    @Inject HistoryDetailsContract.Presenter presenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initializeViews();
    }

    /**
     * <h2>initializeViews</h2>
     * used to initialize views
     */
    private void initializeViews()
    {
        ButterKnife.bind(this);
        tv_all_tool_bar_title.setTextSize(13);
        tv_all_tool_bar_title2.setTextSize(13);
        tv_all_tool_bar_title2.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_receipt_details.setLayoutManager(layoutManager);
        rv_receipt_details.setAdapter(receiptDetailsAdapter);

        tv_all_tool_bar_title2.setTypeface(appTypeface.getPro_narMedium());
        tv_history_detail_amount.setTypeface(appTypeface.getPro_narMedium());
        tv_history_details_status.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_News());
        tv_history_detail_name.setTypeface(appTypeface.getPro_News());
        tv_history_detail_rate.setTypeface(appTypeface.getPro_News());
        atv_history_detail_rate.setTypeface(appTypeface.getPro_News());
        tv_history_detail_type.setTypeface(appTypeface.getPro_News());
        tv_history_detail_type_name.setTypeface(appTypeface.getPro_News());
        tv_history_detail_plate.setTypeface(appTypeface.getPro_News());
        tv_history_detail_distance.setTypeface(appTypeface.getPro_News());
        tv_history_detail_hours.setTypeface(appTypeface.getPro_News());
        tv_history_detail_distance_symbol.setTypeface(appTypeface.getPro_News());
        tv_history_detail_hours_text.setTypeface(appTypeface.getPro_News());
        tv_history_detail_minutes.setTypeface(appTypeface.getPro_News());
        tv_history_detail_minutes_value.setTypeface(appTypeface.getPro_News());
        tv_history_pick_address.setTypeface(appTypeface.getPro_News());
        tv_history_drop_address.setTypeface(appTypeface.getPro_News());
        tv_history_details_cancel.setTypeface(appTypeface.getPro_News());
        tv_history_details_receipt.setTypeface(appTypeface.getPro_News());
        tv_history_details_help.setTypeface(appTypeface.getPro_News());
        tv_history_card_text.setTypeface(appTypeface.getPro_News());
        tv_history_cash_text.setTypeface(appTypeface.getPro_News());
        tv_history_corporate_text.setTypeface(appTypeface.getPro_News());
        tv_history_wallet_text.setTypeface(appTypeface.getPro_News());
        tv_history_wallet_value.setTypeface(appTypeface.getPro_News());
        tv_history_card_value.setTypeface(appTypeface.getPro_News());
        tv_history_cash_value.setTypeface(appTypeface.getPro_News());
        tv_history_corporate_value.setTypeface(appTypeface.getPro_News());
        tv_history_payment_title.setTypeface(appTypeface.getPro_narMedium());

        String bookingId = getIntent().getStringExtra(BOOKING_ID);
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
        presenter.getBookingDetails(bookingId);
        presenter.getHelpDetails();
    }

    @Override
    public void setTitleActionBar(String date, String bookingId,String statusText,String cancelReason) {
        tv_all_tool_bar_title.setText(date);
        tv_all_tool_bar_title2.setText(bookingId);
        tv_history_details_status.setText(statusText);
        tv_history_details_cancel.setText(cancelReason);

        if(cancelReason.equals(""))
            tv_history_details_cancel.setVisibility(View.GONE);
    }

    @Override
    public void setCancelStatus()
    {
        tv_history_payment_title.setVisibility(View.GONE);
        ll_history_detail_rating.setVisibility(View.GONE);
        ll_history_details_bottom.setBackgroundColor(red_history);
    }

    @Override
    public void setCompletedStatus() {
        ll_history_details_bottom.setBackgroundColor(green_history);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setDriverDetails(String driverPic, String name, double rate, String amount,
                                 String businessName)
    {
        tv_history_detail_name.setText(name);
        atv_history_detail_rate.setText(rate+"");
        tv_history_detail_type.setText(businessName);
        tv_history_detail_amount.setText(amount);
        if(!driverPic.equals(""))
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(signup_profile_default_image);
            requestOptions = requestOptions.optionalCircleCrop();

            Glide.with(mContext).load(driverPic)
                    .listener(new RequestListener<Drawable>()
                    {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb_history_detail_image.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pb_history_detail_image.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(tv_history_detail_image);
        }
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button,R.id.tv_history_details_receipt,
            R.id.tv_history_details_help})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;

            case R.id.tv_history_details_receipt:
                tv_history_details_receipt.setTextColor(colorPrimary);
                tv_history_details_help.setTextColor(tab_unselect);
                ll_receipt_details.setVisibility(View.VISIBLE);
                rv_receipt_details.setAdapter(receiptDetailsAdapter);
                break;

            case R.id.tv_history_details_help:
                tv_history_details_receipt.setTextColor(tab_unselect);
                tv_history_details_help.setTextColor(colorPrimary);
                ll_receipt_details.setVisibility(View.VISIBLE);
                tv_history_payment_title.setVisibility(View.GONE);
                rl_history_cash.setVisibility(View.GONE);
                rv_receipt_details.setAdapter(historyDetailsHelpAdapter);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    public void setBookingDetails(String plateNo,String model,String distance, String distanceParameter,
                                  String timeInMinute,String timeInHour,String pickAddress,String dropAddress)
    {
        tv_history_detail_type_name.setText(model);
        tv_history_detail_plate.setText(plateNo);
        tv_history_detail_distance.setText(distance);
        tv_history_detail_distance_symbol.setText(distanceParameter);
        tv_history_detail_hours.setText(timeInHour);
        tv_history_detail_minutes.setText(timeInMinute);
        tv_history_pick_address.setText(pickAddress);
        tv_history_drop_address.setText(dropAddress);

        if(dropAddress.equals(""))
            ll_history_drop.setVisibility(View.GONE);
    }

    @Override
    public void showReceiptDetails(ArrayList<ReceiptDetails> receiptDetailsList)
    {
        receiptDetailsAdapter.populateReceiptList(receiptDetailsList);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
        finish();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void hideReceiptTab()
    {
        tv_history_details_receipt.setVisibility(View.GONE);
        ll_receipt_details.setVisibility(View.GONE);
        tv_history_details_help.setTextColor(colorPrimary);
    }

    @Override
    public void setPaymentCash(String cashCollected)
    {
        rl_history_cash.setVisibility(View.VISIBLE);
        tv_history_cash_text.setText(cash);
        tv_history_cash_value.setText(cashCollected);
        iv_history_cash_image.setImageDrawable(ic_payment_cash_icon);
    }

    @Override
    public void setPaymentCorporate(String totalAmount)
    {
        rl_history_corporate.setVisibility(View.VISIBLE);
        tv_history_corporate_value.setText(totalAmount);
        iv_history_corporate_image.setImageDrawable(corporate_profile_off);
    }

    @Override
    public void setPaymentWallet(String walletAmount)
    {
        rl_history_wallet.setVisibility(View.VISIBLE);
        tv_history_wallet_value.setText(walletAmount);
        iv_history_wallet_image.setImageDrawable(ic_payment_wallet_icon);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setPaymentCard(String cardNumber, String cardType,String cardDeduct)
    {
        rl_history_card.setVisibility(View.VISIBLE);
        tv_history_card_text.setText(card_ending_with1+" "+cardNumber);
        tv_history_card_value.setText(cardDeduct);
        if(BRAND_RESOURCE_MAP.get(cardType) !=null)
            iv_history_card_image.setImageResource(BRAND_RESOURCE_MAP.get(cardType));
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setHelpDetailsList() {
        historyDetailsHelpAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        presenter.handleScreenDestroy();
    }
}
