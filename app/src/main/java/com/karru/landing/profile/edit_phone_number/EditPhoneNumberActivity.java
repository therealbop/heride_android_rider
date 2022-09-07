package com.karru.landing.profile.edit_phone_number;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.karru.authentication.verification.VerifyOTPActivity;
import com.karru.countrypic.Country;
import com.karru.countrypic.CountryPicker;
import com.heride.rider.R;
import com.karru.util.ActivityUtils;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.TextUtil;

import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.OTP_TIMER;

/**
 * <h1>EdidPhoneNumberActivity</h1>
 * <p>This is the activity used to edit phone number</p>
 */
public class EditPhoneNumberActivity extends DaggerAppCompatActivity implements EditPhoneNumberActivityContract.EditPhoneNumberView {
    @BindView(R.id.tv_edit_phone_countryCode) TextView tv_edit_phone_countryCode;
    @BindView(R.id.et_edit_phone_phoneNo) EditText et_edit_phone_phoneNo;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_edit_phone_phoneNoMsg) TextView tv_edit_phone_phoneNoMsg;
    @BindView(R.id.tv_edit_phone_label) TextView tv_edit_phone_label;
    @BindView(R.id.btn_edit_phone_saveMobileNum) Button btn_edit_phone_saveMobileNum;
    @BindView(R.id.rl_edit_phone_main) RelativeLayout rl_edit_phone_main;
    @BindView(R.id.ll_edit_phone_countryFlag) LinearLayout ll_edit_phone_countryFlag;
    @BindView(R.id.iv_edit_phone_countryFlag) ImageView iv_edit_phone_countryFlag;
    @BindString(R.string.change_number) String title;
    @BindString(R.string.Countrypicker) String countryPicker;
    @BindString(R.string.select_country) String select_country;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.phone_validating) String phone_validating;
    @BindString(R.string.something_went_wrong) String something_went_wrong;
    @BindString(R.string.fixInputFields) String fixInputFields;
    @BindString(R.string.phone_cant_empty) String phone_cant_empty;
    @BindString(R.string.phone_invalid) String phone_invalid;
    @BindDrawable(R.drawable.selector_layout) Drawable selector_layout;
    @BindDrawable(R.drawable.ic_chevron_right_white_24dp) Drawable ic_chevron_right_white_24dp;
    @BindDrawable(R.drawable.ic_chevron_left_white_24dp) Drawable ic_chevron_left_white_24dp;
    @BindColor(R.color.grey_bg) int grey_bg;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject EditPhoneNumberActivityContract.EditPhoneNuberPresenter presenter;

    public ProgressDialog pDialog;
    private CountryPicker mCountryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_number);
        ButterKnife.bind(this);
        initProviders();
        initializeView();
        getUserCountryInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    /**
     * <h3>initializeView()</h3>
     * <p> This method is used to initialize views on this Activity </p>
     */
    private void initializeView()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_edit_phone_phoneNoMsg.setTypeface(appTypeface.getPro_News());
        et_edit_phone_phoneNo.setTypeface(appTypeface.getPro_News());
        tv_edit_phone_countryCode.setTypeface(appTypeface.getPro_News());
        tv_edit_phone_label.setTypeface(appTypeface.getPro_News());
        btn_edit_phone_saveMobileNum.setTypeface(appTypeface.getPro_narMedium());

        tv_all_tool_bar_title.setText(title);
        mCountryPicker = CountryPicker.newInstance(select_country);
        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID, min, max) ->
        {
            et_edit_phone_phoneNo.setError(null);
            tv_edit_phone_countryCode.setText(dialCode);
            iv_edit_phone_countryFlag.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });

        showSoftKeyBoard();
    }

    @OnClick({R.id.btn_edit_phone_saveMobileNum, R.id.ll_edit_phone_countryFlag,R.id.rl_back_button,
            R.id.iv_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_edit_phone_saveMobileNum:
                et_edit_phone_phoneNo.clearFocus();
                presenter.checkForMobileValidation(et_edit_phone_phoneNo.getText().toString(),
                        tv_edit_phone_countryCode.getText().toString());
                break;

            case R.id.ll_edit_phone_countryFlag:
                mCountryPicker.show(getSupportFragmentManager(), countryPicker);
                break;

            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }

    @OnTouch({R.id.rl_edit_phone_main})
    public boolean onTouchListnerEvent(View v, MotionEvent event) {
        if (v.getId() == R.id.rl_edit_phone_main) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ActivityUtils.hideSoftKeyBoard(v);
            }
        }
        return false;
    }

    private void initProviders() {
        pDialog = alerts.getProcessDialog(EditPhoneNumberActivity.this);
        pDialog.setMessage(wait);
        pDialog.setCancelable(false);
    }

    /**
     * <h2>getUserCountryInfo</h2>
     * <p> This method provide the current user's country code. </p>
     */
    private void getUserCountryInfo()
    {
        Country country = mCountryPicker.getUserCountryInfo(this);
        iv_edit_phone_countryFlag.setImageResource(country.getFlag());
        tv_edit_phone_countryCode.setText(country.getDialCode());
    }

    @OnTextChanged(R.id.et_edit_phone_phoneNo)
    public void textWatcher(CharSequence s)
    {
        if(!TextUtil.isEmpty(s.toString()))
            enableButton();
        else
            disableButton();
    }

    public void enableButton()
    {
        btn_edit_phone_saveMobileNum.setBackground(selector_layout);
        btn_edit_phone_saveMobileNum.setEnabled(true);

    }

    public void disableButton()
    {
        btn_edit_phone_saveMobileNum.setEnabled(false);
        btn_edit_phone_saveMobileNum.setBackgroundColor(grey_bg);
    }
    /**
     * <h2>showSoftKeyBoard</h2>
     * <p> This method is used for opening the soft key board. </p>
     */
    private void showSoftKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void loadingAlert(String message, final boolean flag) {
        final Dialog dialog = new Dialog(EditPhoneNumberActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok = dialog.findViewById( R.id.tv_alert_ok);
        TextView tv_text = dialog.findViewById(R.id.tv_alert_body);
        tv_text.setText(message);
        tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            if (flag)
                onBackPressed();
        });
    }

    @Override
    public void setProgressDialog()
    {
        pDialog.setMessage(phone_validating);
        if(pDialog!=null && !pDialog.isShowing() && !isFinishing())
            pDialog.show();
    }

    @Override
    public void dismissDialog()
    {
        if(pDialog!=null && pDialog.isShowing() && !isFinishing())
            pDialog.dismiss();
    }

    @Override
    public void startVerifyOTPActivity(String otpTimer)
    {
        Intent intent = new Intent(this, VerifyOTPActivity.class);
        intent.putExtra("ent_mobile", et_edit_phone_phoneNo.getText().toString());
        intent.putExtra("ent_country_code", tv_edit_phone_countryCode.getText().toString());
        intent.putExtra(COMING_FROM, "EditPhoneNumberActivity");
        intent.putExtra(OTP_TIMER, otpTimer);
        startActivity(intent);
    }

    @Override
    public void setPhoneNumberEmptyError() {
        et_edit_phone_phoneNo.setError(phone_cant_empty);
    }

    @Override
    public void setInvalidPhoneNumberError()
    {
        et_edit_phone_phoneNo.setError(phone_invalid);
    }

    @Override
    public void errorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }

    /**
     * <h3>hideKeyboard()</h3>
     * <p> This method is used to hide keyboard </p>
     */
    public void hideKeyboard()
    {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
