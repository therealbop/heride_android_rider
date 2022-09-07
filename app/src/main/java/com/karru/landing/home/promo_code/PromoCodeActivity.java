package com.karru.landing.home.promo_code;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.landing.home.model.promo_code_model.PromoCodeDataModel;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromoCodeActivity extends DaggerAppCompatActivity implements PromoCodeContract.View, TextWatcher {

    @BindView(R.id.ib_all_tool_bar_close) ImageButton ib_all_tool_bar_close;
    @BindView(R.id.rl_all_tool_bar_close) RelativeLayout rl_all_tool_bar_close;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_all_tool_apply) TextView tv_all_tool_apply;
    @BindView(R.id.et_promo_code) EditText et_promo_code;
    @BindView(R.id.rv_promo_code) RecyclerView rv_promo_code;
    @BindView(R.id.til_promo_code) TextInputLayout til_promo_code;
    @Inject AppTypeface appTypeface;
    @Inject PromoCodeContract.Presenter presenter;
    @Inject PromoCodeAdapter promoCodeAdapter;
    @Inject Alerts alerts;
    private ProgressDialog progressDialog;
    private Unbinder unbinder;
    private boolean fareEstimate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocode);
        ButterKnife.bind(this);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    private void initialize() {
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
        tv_all_tool_bar_title.setText(getResources().getString(R.string.apply_promo));
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_apply.setTypeface(appTypeface.getPro_News());
        et_promo_code.addTextChangedListener(this);
        presenter.getPromoCodeData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_promo_code.setLayoutManager(layoutManager);
        rv_promo_code.setAdapter(promoCodeAdapter);
    }


    @OnClick({R.id.ib_all_tool_bar_close,R.id.rl_all_tool_bar_close,R.id.tv_all_tool_apply})
    public void onClockListner(View view)
    {
        switch(view.getId())
        {
            case R.id.ib_all_tool_bar_close:
            case R.id.rl_all_tool_bar_close:
                onBackPressed();
                break;
            case R.id.tv_all_tool_apply:
                String promoCode = et_promo_code.getText().toString().trim();
                if(!promoCode.isEmpty())
                presenter.validatePromoCode(promoCode);
                break;
        }
    }


    @Override
    public void showProgressDialog() {
        progressDialog.setMessage(getString(R.string.pleaseWait));
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPromoCodeList(ArrayList<PromoCodeDataModel> promoCodeDataModel) {
        promoCodeAdapter.notifyDataSetChanged();
        for(int i = 0;i<promoCodeDataModel.size()-1;i++)
        {
            Utility.printLog("promoMessage"+promoCodeDataModel.get(i).getCode());
        }
    }

    @Override
    public void setPromoCodeValue(String promoCode) {
        et_promo_code.getText().clear();
        et_promo_code.setText(promoCode);
        presenter.validatePromoCode(promoCode);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
    }


    /**
     * <h2>invalidPromo</h2>
     * used to show invalid promo code
     * @param error error text to be shown
     */
    public void invalidPromo(String error)
    {
        til_promo_code.setErrorEnabled(true);
        til_promo_code.setError(error);
        /*pb_promo_code_progress.setVisibility(View.GONE);
        tv_promo_code_apply.setVisibility(View.VISIBLE);*/
    }

    /**
     * <h2>validPromo</h2>
     * used to show valid promo code
     */
    public void validPromo(String code)
    {
        til_promo_code.setErrorEnabled(false);
        til_promo_code.setError(null);
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result",code);
        returnIntent.putExtra("FareEstimate",fareEstimate);
        setResult(RESULT_OK,returnIntent);
        finish();

        /*pb_promo_code_progress.setVisibility(View.GONE);
        tv_promo_code_apply.setVisibility(View.VISIBLE);
        getDialog().dismiss();*/
    }

    @Override
    public void fareEstimate(boolean fareEstimate) {
        this.fareEstimate = fareEstimate;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count>0)
        {
            tv_all_tool_apply.setVisibility(View.VISIBLE);
        }else
        {
            tv_all_tool_apply.setVisibility(View.INVISIBLE);
            til_promo_code.setErrorEnabled(false);
            til_promo_code.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
