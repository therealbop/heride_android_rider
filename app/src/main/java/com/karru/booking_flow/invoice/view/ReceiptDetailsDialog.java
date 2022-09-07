package com.karru.booking_flow.invoice.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;
import com.karru.util.TimezoneMapper;
import com.karru.utility.Utility;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

/**
 * <h1>TestDialog</h1>
 * This class is used to show the dialog
 * @author 3Embed
 * @since on 06-02-2018.
 */
public class ReceiptDetailsDialog extends Dialog
{
    private static final String TAG = "TestDialog";

    private AppTypeface appTypeface;
    private Context mContext;
    private DateFormatter dateFormatter;
    private ReceiptDetailsAdapter receiptDetailsAdapter;

    @BindView(R.id.tv_receipt_details) TextView tv_receipt_details;
    @BindView(R.id.tv_receipt_details_info) TextView tv_receipt_details_info;
    @BindView(R.id.tv_receipt_pick_date) TextView tv_receipt_pick_date;
    @BindView(R.id.tv_receipt_drop_date) TextView tv_receipt_drop_date;
    @BindView(R.id.tv_receipt_pick_address) TextView tv_receipt_pick_address;
    @BindView(R.id.tv_receipt_drop_address) TextView tv_receipt_drop_address;
    @BindView(R.id.tv_receipt_details_title) TextView tv_receipt_details_title;
    @BindView(R.id.tv_receipt_payment_title) TextView tv_receipt_payment_title;
    @BindView(R.id.rv_receipt_details) RecyclerView rv_receipt_details;
    @BindView(R.id.iv_receipt_close) AppCompatImageView iv_receipt_close;
    @BindView(R.id.tv_receipt_card_text) TextView tv_receipt_card_text;
    @BindView(R.id.tv_receipt_card_value) TextView tv_receipt_card_value;
    @BindView(R.id.tv_receipt_cash_text) TextView tv_receipt_cash_text;
    @BindView(R.id.tv_receipt_wallet_text) TextView tv_receipt_wallet_text;
    @BindView(R.id.tv_receipt_corporate_text) TextView tv_receipt_corporate_text;
    @BindView(R.id.tv_receipt_wallet_value) TextView tv_receipt_wallet_value;
    @BindView(R.id.tv_receipt_corporate_value) TextView tv_receipt_corporate_value;
    @BindView(R.id.tv_receipt_cash_value) TextView tv_receipt_cash_value;
    @BindView(R.id.iv_receipt_card_image) ImageView iv_receipt_card_image;
    @BindView(R.id.iv_receipt_wallet_image) ImageView iv_receipt_wallet_image;
    @BindView(R.id.iv_receipt_cash_image) ImageView iv_receipt_cash_image;
    @BindView(R.id.iv_receipt_corporate_image) ImageView iv_receipt_corporate_image;
    @BindView(R.id.rl_receipt_card) RelativeLayout rl_receipt_card;
    @BindView(R.id.rl_receipt_wallet) RelativeLayout rl_receipt_wallet;
    @BindView(R.id.rl_receipt_cash) RelativeLayout rl_receipt_cash;
    @BindView(R.id.rl_receipt_corporate) RelativeLayout rl_receipt_corporate;
    @BindString(R.string.cash) String cash;
    @BindString(R.string.card_ending_with1) String card_ending_with1;
    @BindDrawable(R.drawable.ic_payment_cash_icon_selector) Drawable ic_payment_cash_icon;
    @BindDrawable(R.drawable.ic_payment_wallet_icon_selector) Drawable ic_payment_wallet_icon;
    @BindDrawable(R.drawable.corporate_profile_off) Drawable corporate_profile_off;

    private String cardNumber,cardType;
    private String cashCollected = "0",cardDeduct="0",walletTransaction="0",totalAmount;
    private boolean cashApplied,cardApplied,walletApplied,isCorporateBooking;

    public ReceiptDetailsDialog(@NonNull Context context,AppTypeface appTypeface,
                                ReceiptDetailsAdapter receiptDetailsAdapter,DateFormatter dateFormatter)
    {
        super(context);
        this.mContext = context;
        this.appTypeface = appTypeface;
        this.dateFormatter = dateFormatter;
        this.receiptDetailsAdapter = receiptDetailsAdapter;
        Utility.printLog(TAG+" constructor receipt ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_receipt_details);
        initialize();
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize
     */
    private void initialize( )
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_receipt_details.setLayoutManager(layoutManager);
        rv_receipt_details.setAdapter(receiptDetailsAdapter);

        tv_receipt_details.setTypeface(appTypeface.getPro_narMedium());
        tv_receipt_details_info.setTypeface(appTypeface.getPro_narMedium());
        tv_receipt_pick_date.setTypeface(appTypeface.getPro_narMedium());
        tv_receipt_drop_date.setTypeface(appTypeface.getPro_narMedium());
        tv_receipt_pick_address.setTypeface(appTypeface.getPro_News());
        tv_receipt_drop_address.setTypeface(appTypeface.getPro_News());
        tv_receipt_details_title.setTypeface(appTypeface.getPro_narMedium());
        tv_receipt_payment_title.setTypeface(appTypeface.getPro_narMedium());
        tv_receipt_card_text.setTypeface(appTypeface.getPro_News());
        tv_receipt_card_value.setTypeface(appTypeface.getPro_News());
        tv_receipt_cash_text.setTypeface(appTypeface.getPro_News());
        tv_receipt_wallet_text.setTypeface(appTypeface.getPro_News());
        tv_receipt_corporate_text.setTypeface(appTypeface.getPro_News());
        tv_receipt_wallet_value.setTypeface(appTypeface.getPro_News());
        tv_receipt_corporate_value.setTypeface(appTypeface.getPro_News());
        tv_receipt_cash_value.setTypeface(appTypeface.getPro_News());
        tv_receipt_drop_address.setSelected(true);
        Utility.printLog(TAG+" initialize dialog ");
    }

    @OnClick(R.id.iv_receipt_close)
    void onClickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_receipt_close:
                dismiss();
                break;
        }
    }

    /**
     * <h2>setPaymentCash</h2>
     * used to set the payment option as cash
     */
    void setPaymentCash(String cash)
    {
        cashApplied = true;
        this.cashCollected = cash;
    }

    /**
     * <h2>setPaymentWallet</h2>
     * used to set the payment option as wallet
     */
    void setPaymentWallet(String wallet)
    {
        walletApplied = true ;
        this.walletTransaction = wallet;
    }

    /**
     * <h2>setPaymentWallet</h2>
     * used to set the payment option as wallet
     */
    void setPaymentCorporate(String amount)
    {
        isCorporateBooking = true ;
        this.totalAmount = amount;
    }

    /**
     * <h2>setPaymentCard</h2>
     * used to set the payment option as card
     */
    void setPaymentCard(String cardNumber, String cardType,String cardDeduct)
    {
        cardApplied = true;
        this.cardDeduct = cardDeduct;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void show()
    {
        super.show();
        if(isCorporateBooking)
        {
            rl_receipt_corporate.setVisibility(View.VISIBLE);
            tv_receipt_corporate_value.setText(totalAmount);
            iv_receipt_corporate_image.setImageDrawable(corporate_profile_off);
        }
        else
        {
            if(cashApplied)
            {
                rl_receipt_cash.setVisibility(View.VISIBLE);
                tv_receipt_cash_value.setText(cashCollected+"");
                iv_receipt_cash_image.setImageDrawable(ic_payment_cash_icon);
            }

            if(cardApplied)
            {
                rl_receipt_card.setVisibility(View.VISIBLE);
                tv_receipt_card_text.setText(card_ending_with1+" "+cardNumber);
                tv_receipt_card_value.setText(cardDeduct+"");
                if(BRAND_RESOURCE_MAP.get(cardType) !=null)
                    iv_receipt_card_image.setImageResource(BRAND_RESOURCE_MAP.get(cardType));
            }

            if(walletApplied)
            {
                rl_receipt_wallet.setVisibility(View.VISIBLE);
                tv_receipt_wallet_value.setText(walletTransaction+"");
                iv_receipt_wallet_image.setImageDrawable(ic_payment_wallet_icon);
            }
        }
    }

    /**
     * <h2>setDetailsForReceipt</h2>
     * This method is used to set the Ui for receipt
     */
    void setDetailsForReceipt(String pickDate, String pickAddress, String dropDate, String dropAddress,
                              ArrayList<ReceiptDetails> receiptDetailsList, boolean isRental, String packageTitle)
    {
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(Double.parseDouble(GMT_CURRENT_LAT),
                Double.parseDouble(GMT_CURRENT_LNG));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        Utility.printLog(TAG+" time zone  date "+timeZone+" "+GMT_CURRENT_LAT+" "+GMT_CURRENT_LNG);

        if(tv_receipt_drop_date!=null)
        {
            Utility.printLog(TAG+" pick date "+pickDate);
            Utility.printLog(TAG+" drop date "+dropDate);
            tv_receipt_pick_date.setText(dateFormatter.getDateInSpecificFormatWithTime(pickDate,timeZone));
            tv_receipt_drop_date.setText(dateFormatter.getDateInSpecificFormatWithTime(dropDate,timeZone));
            tv_receipt_pick_address.setText(pickAddress);
            tv_receipt_drop_address.setText(dropAddress);
            if(isRental && !packageTitle.isEmpty()) {
                tv_receipt_details.setText(mContext.getResources().getString(R.string.rental_details));
                tv_receipt_details_info.setVisibility(View.VISIBLE);
                tv_receipt_details_info.setText(packageTitle);
            }

            receiptDetailsAdapter.populateReceiptList(receiptDetailsList);
        }
    }
}
