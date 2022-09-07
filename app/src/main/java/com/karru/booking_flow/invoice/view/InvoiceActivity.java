package com.karru.booking_flow.invoice.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.heride.rider.R;
import com.karru.landing.MainActivity;
import com.karru.booking_flow.invoice.InvoiceContract;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.IS_INVOICE_OPEN;
import static com.karru.utility.Constants.LIVE_TRACKING_OPEN;

/**
 * @since  22/08/17.
 */
public class InvoiceActivity extends DaggerAppCompatActivity implements InvoiceContract.View,
        RatingBar.OnRatingBarChangeListener
{
    private static final String TAG = "InvoiceActivity";

    @Inject Alerts alerts;
    @Inject Context mContext;
    @Inject AppTypeface appTypeface;
    @Inject ReceiptDetailsDialog receiptDialog;
    @Inject InvoiceTipValueAdapter invoiceTipValueAdapter;
    @Inject FeedbackReasonsListAdapter feedbackReasonsListAdapter;
    @Inject ReceiptDetailsAdapter receiptDetailsAdapter;
    @Inject InvoiceContract.Presenter invoicePresenter;

    @BindView(R.id.iv_invoice_driver_pic) ImageView iv_invoice_driver_pic;
    @BindView(R.id.tv_invoice_title) TextView tv_invoice_title;
    @BindView(R.id.tv_invoice_fav_driver) TextView tv_invoice_fav_driver;
    @BindView(R.id.tv_invoice_appnt_date) TextView tv_invoice_appnt_date;
    @BindView(R.id.tv_invoice_driver_name) TextView tv_invoice_driver_name;
    @BindView(R.id.tv_invoice_vehicle_model) TextView tv_invoice_vehicle_model;
    @BindView(R.id.tv_invoice_amount) TextView tv_invoice_amount;
    @BindView(R.id.tv_invoice_distance) TextView tv_invoice_distance;
    @BindView(R.id.tv_invoice_time) TextView tv_invoice_time;
    @BindView(R.id.tv_invoice_receipt) TextView tv_invoice_receipt;
    @BindView(R.id.tv_invoice_rating_title) TextView tv_invoice_rating_title;
    @BindView(R.id.btn_invoice_submit) Button btn_invoice_submit;
    @BindView(R.id.tv_invoice_feedback_description) TextView tv_invoice_feedback_description;
    @BindView(R.id.rv_invoice_feedback) RecyclerView rv_invoice_feedback;
    @BindView(R.id.rb_invoice_rating_bar) RatingBar rb_invoice_rating_bar;
    @BindView(R.id.pb_invoice_progress) ProgressBar pb_invoice_progress;
    @BindView(R.id.tv_invoice_feedback_title) TextView tv_invoice_feedback_title;
    @BindView(R.id.ll_invoice_tip_layout) LinearLayout ll_invoice_tip_layout;
    @BindView(R.id.rv_invoice_rating_tip) RecyclerView rv_invoice_rating_tip;
    @BindView(R.id.tv_invoice_rate_tip_label) TextView tv_invoice_rate_tip_label;
    @BindView(R.id.et_invoice_comment)
    AppCompatEditText et_invoice_comment;
    @BindString(R.string.rating_1) String rating_1;
    @BindString(R.string.rating_desc_1) String rating_desc_1;
    @BindString(R.string.rating_2) String rating_2;
    @BindString(R.string.rating_desc_2) String rating_desc_2;
    @BindString(R.string.rating_3) String rating_3;
    @BindString(R.string.rating_desc_3) String rating_desc_3;
    @BindString(R.string.rating_4) String rating_4;
    @BindString(R.string.rating_desc_4) String rating_desc_4;
    @BindString(R.string.rating_5) String rating_5;
    @BindString(R.string.rating_desc_5) String rating_desc_5;
    @BindString(R.string.add_fav) String add_fav;
    @BindString(R.string.remove_fav) String remove_fav;
    @BindColor(R.color.SignInbuttonGradStart12cca2) int green;
    @BindColor(R.color.red) int red;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable arrived_to_drop_profile_default_image;

    private String pickDate;
    private String pickAddress;
    private String dropDate;
    private String dropAddress;
    private boolean isRental;
    private String packageTitle;
    private ArrayList<ReceiptDetails> receiptDetailsList;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        initViews();
        setTypeface();
    }

    @OnClick({R.id.tv_invoice_receipt,R.id.btn_invoice_submit,R.id.tv_invoice_fav_driver})
    public void onCLickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_invoice_receipt:
                Utility.printLog(TAG+" clicked receipt ");
                receiptDialog.show();
                break;

            case R.id.btn_invoice_submit:
                invoicePresenter.updateReviewForDriver(rb_invoice_rating_bar.getRating(),
                        et_invoice_comment.getText().toString());
                break;

            case R.id.tv_invoice_fav_driver:
                invoicePresenter.handleFavDriverClick();
                break;
        }
    }

    /**
     * <h2>setTypeface</h2>
     * This method is used to set the typeface for the views
     */
    private void setTypeface()
    {
        btn_invoice_submit.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_driver_name.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_title.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_appnt_date.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_rate_tip_label.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_amount.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_receipt.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_feedback_description.setTypeface(appTypeface.getPro_News());
        tv_invoice_feedback_title.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_rating_title.setTypeface(appTypeface.getPro_narMedium());
        tv_invoice_vehicle_model.setTypeface(appTypeface.getPro_News());
        tv_invoice_distance.setTypeface(appTypeface.getPro_News());
        tv_invoice_time.setTypeface(appTypeface.getPro_News());
        tv_invoice_fav_driver.setTypeface(appTypeface.getPro_News());
        et_invoice_comment.setTypeface(appTypeface.getPro_News());
    }

    /**
     *<h2>initViews</h2>
     * This method is used to initialize views
     */
    private void initViews()
    {
        ButterKnife.bind(this);
        IS_INVOICE_OPEN = true;
        rb_invoice_rating_bar.setOnRatingBarChangeListener(this);
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);

        receiptDialog.setOnShowListener(dialog ->
        {
            receiptDialog.setDetailsForReceipt(pickDate, pickAddress, dropDate, dropAddress, receiptDetailsList,isRental,packageTitle);

            receiptDetailsAdapter.populateReceiptList(receiptDetailsList);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_invoice_feedback.setLayoutManager(layoutManager);
        rv_invoice_feedback.setAdapter(feedbackReasonsListAdapter);

        LinearLayoutManager layoutManagerTip
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_invoice_rating_tip.setLayoutManager(layoutManagerTip);
        rv_invoice_rating_tip.setAdapter(invoiceTipValueAdapter);

        if(getIntent().getExtras()!=null)
        {
            Utility.printLog(TAG+ "onCreate : "+getIntent().getExtras());
            Bundle bundle = getIntent().getExtras();
            invoicePresenter.extractData(bundle);
        }
        else
            Utility.finishAndRestartMainActivity(this);
    }

    @Override
    public void populateFeedbackList(ArrayList<String> feedbackDatList)
    {
        feedbackReasonsListAdapter.provideFeedbackList(feedbackDatList,invoicePresenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        invoicePresenter.handleBackState();
    }

    @Override
    public void enableTipOption(ArrayList<Integer> listOfTips,int tipType,String currency,int currencyType)
    {
        ll_invoice_tip_layout.setVisibility(View.VISIBLE);
        invoiceTipValueAdapter.populateTipsList(listOfTips,tipType,this,appTypeface,
                currency,currencyType,invoicePresenter);
        invoiceTipValueAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPaymentCash(String cashAmount)
    {
        receiptDialog.setPaymentCash(cashAmount);
    }

    @Override
    public void setPaymentWallet(String walletAmount) {
        receiptDialog.setPaymentWallet(walletAmount);
    }

    @Override
    public void setPaymentCorporate(String corporateAmount) {
        receiptDialog.setPaymentCorporate(corporateAmount);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setPaymentCard(String cardNumber, String cardType,String cardAmount)
    {
        receiptDialog.setPaymentCard(cardNumber,cardType,cardAmount);
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        IS_INVOICE_OPEN = false;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
        if (fromUser)
        {
            invoicePresenter.onRatingChanged((int) rating);

            switch ((int) rating)
            {
                case 1:
                    tv_invoice_feedback_title.setTextColor(red);
                    tv_invoice_feedback_title.setText(rating_1);
                    tv_invoice_feedback_description.setText(rating_desc_1);
                    break;

                case 2:
                    tv_invoice_feedback_title.setTextColor(red);
                    tv_invoice_feedback_title.setText(rating_2);
                    tv_invoice_feedback_description.setText(rating_desc_2);
                    break;

                case 3:
                    tv_invoice_feedback_title.setTextColor(red);
                    tv_invoice_feedback_title.setText(rating_3);
                    tv_invoice_feedback_description.setText(rating_desc_3);
                    break;

                case 4:
                    tv_invoice_feedback_title.setTextColor(green);
                    tv_invoice_feedback_title.setText(rating_4);
                    tv_invoice_feedback_description.setText(rating_desc_4);
                    break;

                case 5:
                    tv_invoice_feedback_title.setTextColor(green);
                    tv_invoice_feedback_title.setText(rating_5);
                    tv_invoice_feedback_description.setText(rating_desc_5);
                    break;
            }
        }
    }

    @Override
    public void enableAddFavDriver()
    {
        tv_invoice_fav_driver.setVisibility(View.VISIBLE);
        tv_invoice_fav_driver.setSelected(false);
        tv_invoice_fav_driver.setText(add_fav);
    }

    @Override
    public void enableRemoveFavDriver()
    {
        tv_invoice_fav_driver.setVisibility(View.VISIBLE);
        tv_invoice_fav_driver.setSelected(true);
        tv_invoice_fav_driver.setText(remove_fav);
    }

    @Override
    public void hideFavDriverButton()
    {
        tv_invoice_fav_driver.setVisibility(View.GONE);
    }

    @Override
    public void setUIForInvoice(String driverName,String vehicleMakeModel,String totalAmount,String distance,
                                String time,String appntDate,String driverProfilePic)
    {
        tv_invoice_driver_name.setText(driverName);
        tv_invoice_vehicle_model.setText(vehicleMakeModel);
        tv_invoice_amount.setText(totalAmount);
        tv_invoice_distance.setText(distance);
        tv_invoice_time.setText(time);
        tv_invoice_appnt_date.setText(appntDate);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.optionalCircleCrop();
        requestOptions = requestOptions.placeholder(arrived_to_drop_profile_default_image);
        Glide.with(mContext)
                .load(driverProfilePic)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pb_invoice_progress.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pb_invoice_progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(requestOptions)
                .into(iv_invoice_driver_pic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invoicePresenter.checkRTLConversion();
    }

    @Override
    public void showProgress()
    {
        if(progressDialog != null && !progressDialog.isShowing() && !isFinishing())
            progressDialog.show();
    }

    @Override
    public void dismissProgress()
    {
        if(progressDialog != null && progressDialog.isShowing() && !isFinishing())
            progressDialog.dismiss();
    }

    @Override
    public void setUIForReceipt(String pickDate, String pickAddress, String dropDate, String dropAddress,
                                boolean isRental, String packageTitle, ArrayList<ReceiptDetails> receiptDetailsList)
    {
        this.pickDate =pickDate;
        this.pickAddress =pickAddress;
        this.dropDate =dropDate;
        this.dropAddress =dropAddress;
        this.isRental = isRental;
        this.packageTitle = packageTitle;
        this.receiptDetailsList =receiptDetailsList;
    }

    @Override
    public void finishActivity() {
        Utility.printLog(TAG+" open live tracking "+LIVE_TRACKING_OPEN);
        if(LIVE_TRACKING_OPEN)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else
        {
            finish();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
