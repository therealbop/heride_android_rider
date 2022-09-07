package com.karru.authentication.forgot_password;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.rider.R;
import com.karru.authentication.verification.VerifyOTPActivity;
import com.karru.countrypic.Country;
import com.karru.countrypic.CountryPicker;
import com.karru.util.ActivityUtils;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;

import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.COUNTRY_CODE;
import static com.karru.utility.Constants.MOBILE;
import static com.karru.utility.Constants.OTP_TIMER;

public class ForgotPasswordActivity extends DaggerAppCompatActivity implements
        TextView.OnEditorActionListener, ForgotPasswordContract.ForgotPasswordView {

    @BindView(R.id.btn_forgot_paswd_next) Button btn_forgot_paswd_next;
    @BindView(R.id.iv_forgot_paswd_countryFlag) ImageView iv_forgot_paswd_countryFlag;
    @BindView(R.id.tv_forgot_paswd_Info) TextView tv_forgot_paswd_Info;
    @BindView(R.id.tv_forgot_paswd_countryCode) TextView tv_forgot_paswd_countryCode;
    @BindView(R.id.til_forgot_paswd_Email) TextInputLayout til_forgot_paswd_Email;
    @BindView(R.id.til_forgot_paswd_phoneNo) TextInputLayout til_forgot_paswd_phoneNo;
    @BindView(R.id.et_forgot_paswd_email) EditText et_forgot_paswd_email;
    @BindView(R.id.et_forgot_paswd_phoneNo) EditText et_forgot_paswd_phoneNo;
    @BindView(R.id.rl_forgot_paswd_Phone) RelativeLayout rl_forgot_paswd_Phone;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.ll_forgot_paswd_main) LinearLayout ll_forgot_paswd_main;
    @BindView(R.id.rb_forgot_paswd_PhoneNO) RadioButton rb_forgot_paswd_PhoneNO;
    @BindView(R.id.rb_forgot_paswd_Email) RadioButton rb_forgot_paswd_Email;
    @BindView(R.id.rg_forgot_paswd_group) RadioGroup rg_forgot_paswd_group;
    @BindView(R.id.tv_forgot_pass_phoneError) TextView tv_forgot_pass_phoneError;
    @BindView(R.id.tv_forgot_pass_phoneLabel) TextView tv_forgot_pass_phoneLabel;
    @BindView(R.id.view_forgot_pass_line) View view_forgot_pass_line;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.resetPswdUsingEmailLabel) String resetPswdUsingEmailLabel;
    @BindString(R.string.resetPswdUsingPhNoLabel) String resetPswdUsingPhNoLabel;
    @BindString(R.string.get_code) String get_code;
    @BindString(R.string.mandatory) String mandatory;
    @BindString(R.string.phone_invalid) String phone_invalid;
    @BindString(R.string.email_invalid) String email_invalid;
    @BindString(R.string.enter_phone_mail) String enter_phone_mail;
    @BindString(R.string.forget_pass_header) String forget_pass_header;
    @BindString(R.string.get_email) String get_email;
    @BindColor(R.color.grey_bg) int grey_bg;
    @BindColor(R.color.red) int red;
    @BindColor(R.color.brown) int brown;

    @Inject Alerts alerts;
    @Inject ForgotPasswordContract.ForgotPasswdPresenter presenter;
    @Inject AppTypeface appTypeface;
    @Inject CountryPicker mCountryPicker;
    @Inject AppPermissionsRunTime permissionsRunTime;

    static boolean[] isEmailValidTemp = {true};
    static boolean[] isPhoneValidTemp = {true};
    private boolean isEmailValidation, isPhoneNoValid = false;
    private Dialog dialog;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        initDialog();
        initializeVar();
    }

    /**
     * <h>Initialize Dialog views</h>
     * <p>this method is using to initialize Dialog and Progress Dialog Views</p>
     */
    private void initDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        pDialog = alerts.getProcessDialog(this);
        pDialog.setMessage(wait);
        pDialog.setCancelable(false);
    }


    @Override
    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismissProgressDialog()
    {
        if(pDialog!=null && pDialog.isShowing() && !isFinishing())
            pDialog.dismiss();
    }

    @Override
    public void showProgress()
    {
        pDialog.setMessage(wait);
        if(pDialog!=null && !pDialog.isShowing() && !isFinishing())
            pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    public void onError(String errorMsg)
    {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * <h>Initialize variables</h>
     * <p>this method is using to initialize variables of this Activity</p>
     */
    public void initializeVar()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", getResources().getString(R.string.select_country));
        isEmailValidation = false;

        initView();
        initSwitchAndRadioButtons();
        getUserCountryInfo();
    }

    @Override
    public void startOtpClass(String phone_Mail,String countryCode,String otpCode)
    {
        Intent intent = new Intent(this, VerifyOTPActivity.class);
        intent.putExtra(MOBILE, phone_Mail);
        intent.putExtra(COMING_FROM, "forgotPassword");
        intent.putExtra(COUNTRY_CODE, countryCode);
        intent.putExtra(OTP_TIMER, otpCode);
        startActivity(intent);
    }

    /**
     * <h3>initializeView()</h3>
     * <p> This method is used to initialize views on this activity </p>
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView()
    {
        tv_forgot_paswd_Info.setTypeface(appTypeface.getPro_News());
        tv_forgot_paswd_countryCode.setTypeface(appTypeface.getPro_News());
        til_forgot_paswd_phoneNo.setTypeface(appTypeface.getPro_News());
        et_forgot_paswd_phoneNo.setTypeface(appTypeface.getPro_News());
        til_forgot_paswd_Email.setTypeface(appTypeface.getPro_News());
        et_forgot_paswd_email.setTypeface(appTypeface.getPro_News());
        btn_forgot_paswd_next.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        et_forgot_paswd_phoneNo.requestFocus();
        tv_all_tool_bar_title.setText(forget_pass_header);
    }

    @Override
    public void hideSoftKeyBoard() {
        ActivityUtils.hideSoftKeyBoard(ll_forgot_paswd_main);
    }

    /**
     * <h2>initSwitchAndRadioButtons</h2>
     * <p> method to init radio group views, radioButtons, switches and buttons</p>
     */
    private void initSwitchAndRadioButtons()
    {
        rb_forgot_paswd_PhoneNO.setTypeface(appTypeface.getPro_News());
        rb_forgot_paswd_Email.setTypeface(appTypeface.getPro_News());

        rg_forgot_paswd_group.setOnCheckedChangeListener((group, checkedId) ->
        {
            isEmailValidation = checkedId == R.id.rb_forgot_paswd_Email;
            toggleViewsForAccountType();
        });

        if (isEmailValidation)
            rb_forgot_paswd_Email.setChecked(true);
        else
            rb_forgot_paswd_PhoneNO.setChecked(true);
    }

    /**
     * <h2>toggleViewsForAccountType</h2>
     * <p> method to toggle views on the basis of account types </p>
     */
    private void toggleViewsForAccountType()
    {
        Log.d("ForgotPassword", "toggleViewsForAccountType isEmailValidation: " + isEmailValidation);
        presenter.isEmailValidation(isEmailValidation, isPhoneNoValid);
    }

    @Override
    public void setMailHeaderText()
    {
        tv_forgot_pass_phoneLabel.setVisibility(View.GONE);
        rl_forgot_paswd_Phone.setVisibility(View.GONE);
        til_forgot_paswd_Email.setVisibility(View.VISIBLE);
        tv_forgot_pass_phoneError.setVisibility(View.GONE);
        tv_forgot_paswd_Info.setText(resetPswdUsingEmailLabel);
        btn_forgot_paswd_next.setText(get_email);
        et_forgot_paswd_email.requestFocus();
        validateEmailId();
    }

    @Override
    public void setPhNumberHeaderText()
    {
        tv_forgot_pass_phoneLabel.setVisibility(View.VISIBLE);
        tv_forgot_pass_phoneError.setVisibility(View.VISIBLE);
        til_forgot_paswd_Email.setVisibility(View.GONE);
        rl_forgot_paswd_Phone.setVisibility(View.VISIBLE);
        btn_forgot_paswd_next.setText(get_code);
        tv_forgot_paswd_Info.setText(resetPswdUsingPhNoLabel);
        et_forgot_paswd_phoneNo.requestFocus();
        presenter.phoneMailValidation(false, et_forgot_paswd_phoneNo.getText().toString(),
                tv_forgot_paswd_countryCode.getText().toString());
    }

    /**
     * <h2>getUserCountryInfo</h2>
     * <p> This method provide the current user's country code. </p>
     */
    private void getUserCountryInfo()
    {
        Country country = mCountryPicker.getUserCountryInfo(this);
        iv_forgot_paswd_countryFlag.setImageResource(country.getFlag());
        tv_forgot_paswd_countryCode.setText(country.getDialCode());
    }

    /**
     * <h2>enableButton</h2>
     * <p> This method is used for enable and disable the button. </p>
     */
    @Override
    public void enableButton() {
        btn_forgot_paswd_next.setEnabled(true);
        btn_forgot_paswd_next.setBackgroundResource(R.drawable.selector_layout);
    }

    @Override
    public void disableButton() {
        btn_forgot_paswd_next.setEnabled(false);
        btn_forgot_paswd_next.setBackgroundColor(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.grey_bg));
    }

    @Override
    public void checkVersionTOContinue(String phNumTxt)
    {
        presenter.validatePhoneEmail(phNumTxt, tv_forgot_paswd_countryCode.getText().toString());
    }

    @OnTextChanged({R.id.et_forgot_paswd_phoneNo,R.id.et_forgot_paswd_email})
    public void afterTextChanged(Editable editable) {
        if (editable == et_forgot_paswd_phoneNo.getEditableText()) {
            validatePhoneNo();
        } else if (editable == et_forgot_paswd_email.getEditableText()) {
            validateEmailId();
        }
    }

    @OnClick({R.id.rl_back_button, R.id.iv_back_button, R.id.ll_forgot_paswd_CountryFlag,
            R.id.btn_forgot_paswd_next})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;

            case R.id.ll_forgot_paswd_CountryFlag:
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
                mCountryPicker.setListener((name, code, dialCode, flagDrawableResID, min, max) ->
                {
                    tv_forgot_paswd_countryCode.setText(dialCode);
                    iv_forgot_paswd_countryFlag.setImageResource(flagDrawableResID);
                    mCountryPicker.dismiss();
                    validatePhoneNo();
                });
                break;

            case R.id.btn_forgot_paswd_next:
                Log.d("ForgotPassword", "onClick isEmailValidation: " + isEmailValidation);
                String mailTxt = et_forgot_paswd_email.getText().toString();
                String phNumTxt = et_forgot_paswd_phoneNo.getText().toString();
                presenter.isValid(isEmailValidation, mailTxt, phNumTxt, tv_forgot_paswd_countryCode.getText().toString());
                break;
        }
    }

    /**
     * <h2>validatePhoneNo</h2>
     * <p>method to validate the input phone number</p>
     */
    private void validatePhoneNo()
    {
        isPhoneValidTemp[0] = true;
        presenter.phoneMailValidation(false, et_forgot_paswd_phoneNo.getText().toString(),
                tv_forgot_paswd_countryCode.getText().toString());
        if (isPhoneValidTemp[0])
            til_forgot_paswd_phoneNo.setErrorEnabled(false);
        isPhoneNoValid = isPhoneValidTemp[0];
    }

    @Override
    public void errorMandatoryNotifier()
    {
        disableButton();
        isEmailValidTemp[0] = false;
        isPhoneValidTemp[0] = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }

    @Override
    public void phoneErrorInvalidNotifier() {
        disableButton();
        isPhoneValidTemp[0] = false;
        isEmailValidTemp[0] = false;
    }

    @Override
    public void phoneValidNotifier()
    {
        enableButton();
        tv_forgot_pass_phoneError.setText("");
        tv_forgot_pass_phoneError.setVisibility(View.GONE);
        view_forgot_pass_line.setBackgroundColor(brown);
    }

    @Override
    public void emailErrorInvalidNotifier() {
        disableButton();
        isPhoneValidTemp[0] = false;
        isEmailValidTemp[0] = false;
    }

    /**
     * <h2>validateEmailId</h2>
     * <p> to validate whether entered email id is a valid email id or not
     * and this email id is available or not</p>
     */
    private void validateEmailId()
    {
        isEmailValidTemp[0] = true;
        presenter.phoneMailValidation(true, et_forgot_paswd_email.getText().toString(),
                tv_forgot_paswd_countryCode.getText().toString());
    }

    @Override
    public void onValidMail()
    {
        til_forgot_paswd_Email.setErrorEnabled(false);
        enableButton();
    }

    @Override
    public void showEmptyPhNumberAlert()
    {
        ((TextView) dialog.findViewById(R.id.tv_alert_body)).setText(enter_phone_mail);
        dialog.findViewById( R.id.tv_alert_ok).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showWrongPhNumberAlert() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        ((TextView) dialog.findViewById( R.id.tv_alert_body)).setText(R.string.phone_invalid);
        dialog.findViewById( R.id.tv_alert_ok).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    @Override
    public void showAlert(String msg)
    {
        ((TextView) dialog.findViewById( R.id.tv_alert_body)).setText(msg);
        dialog.findViewById( R.id.tv_alert_ok).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    @Override
    public void openMessageDialog(final String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok = dialog.findViewById(R.id.tv_alert_ok);
        TextView tv_text = dialog.findViewById( R.id.tv_alert_body);
        tv_text.setText(message);
        tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            ForgotPasswordActivity.this.onBackPressed();
        });
    }

    /**
     * <h3>onEditorAction()</h3>
     * <p> method to watch the editor status </p>
     *
     * @param v        TextView on which the Editor status is clicked
     * @param actionId Type of actionId
     * @param event    event
     * @return Returns true if the status is done successfully else false
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btn_forgot_paswd_next.performClick();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
