package com.karru.authentication.verification;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.karru.authentication.change_password.ChangePasswordActivity;
import com.heride.rider.R;
import com.karru.splash.second.SecondSplashActivity;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.MainActivity;
import com.karru.util.ActivityUtils;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import dagger.android.support.DaggerAppCompatActivity;
import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.COUNTRY_CODE;
import static com.karru.utility.Constants.MOBILE;
import static com.karru.utility.Constants.OTP_TIMER;
import static com.karru.utility.Constants.PASSWORD;

/**
 * <h1>VerifyOTPActivity</h1>
 * <h4>This is a Model class for SignUp Activity</h4>
 * This class is used to provide the Confirm Number screen, where we can enter our OTP number.
 * this class is give a  call to VerifyOTPActivity Controller class.
 * @version 1.0
 * @since 17/08/17
 */
public class VerifyOTPActivity extends DaggerAppCompatActivity implements View.OnClickListener,
        VerifyOTPContract.View ,View.OnTouchListener
{
    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject AppPermissionsRunTime permissionsRunTime;
    @Inject VerifyOTPContract.Presenter verifyOTPPresenter;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @BindView(R.id.rl_rootView) RelativeLayout rl_rootView;
    @BindView(R.id.iv_mobileOtpImg) ImageView iv_mobileOtpImg;
    @BindView(R.id.tv_verify_otp_info) TextView tv_verify_otp_info;
    @BindView(R.id.tv_verify_otp_timer) TextView tv_verify_otp_timer;
    @BindView(R.id.btn_verify_otp_verify) Button btn_verify_otp_verify;
    @BindView(R.id.tv_verify_otp_resend) TextView tv_verify_otp_resend;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.rl_verify_otp_layout) RelativeLayout rl_verify_otp_layout;
    @BindViews({R.id.et_verify_otp_first_digit, R.id.et_verify_otp_second_digit , R.id.et_verify_otp_third_digit
            ,R.id.et_verify_otp_fourth_digit}) List<EditText> et_verify_otp_digits;
    @BindView(R.id.sv_verify_otp_container) ScrollView sv_verify_otp_container;
    @BindString(R.string.verify_number) String verify_number;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.otp_info1) String otp_info1;
    @BindString(R.string.otp_info2) String otp_info2;
    @BindColor(R.color.color333333) int color333333;

    private ProgressDialog pDialog;

    /**
     * This is the on_Create method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
        initViews();
        setTypeface();
        extractDataFromBundle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        verifyOTPPresenter.disposeObservable();
    }

    /**
     * <p>initViews</p>
     * This method is used for initializing the listeners
     */
    private void initViews()
    {
        ButterKnife.bind(this);
        pDialog = alerts.getProcessDialog(this);
        pDialog.setMessage(wait);
        pDialog.setCancelable(false);

        tv_all_tool_bar_title.setText(verify_number);
        et_verify_otp_digits.get(0).requestFocus();
        handleKeyboard();
    }

    /**
     * <h2>handleKeyboard</h2>
     * used to handle the virtual keyboard open or close
     */
    private void handleKeyboard()
    {
        rl_rootView.getViewTreeObserver().addOnGlobalLayoutListener(() ->
        {
            int heightDiff = rl_rootView.getRootView().getHeight() - rl_rootView.getHeight();
            if (heightDiff > dpToPx(VerifyOTPActivity.this, 200))
                // if more than 200 dp, it's probably a keyboard...
                iv_mobileOtpImg.setVisibility(View.GONE);
            else
                iv_mobileOtpImg.setVisibility(View.VISIBLE);
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyOTPPresenter.checkRTLConversion();
    }

    /**
     * <h2>setTypeface</h2>
     * This method is used to set the typeface for the texts
     */
    private void setTypeface()
    {
        tv_verify_otp_info.setTypeface(appTypeface.getPro_News());
        tv_verify_otp_resend.setTypeface(appTypeface.getPro_News());
        btn_verify_otp_verify.setTypeface(appTypeface.getPro_News());
        tv_verify_otp_timer.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        et_verify_otp_digits.get(0).setTypeface(appTypeface.getPro_News());
        et_verify_otp_digits.get(1).setTypeface(appTypeface.getPro_News());
        et_verify_otp_digits.get(2).setTypeface(appTypeface.getPro_News());
        et_verify_otp_digits.get(3).setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h2>extractDataFromBundle</h2>
     * <p>
     * This method is used for getting the data
     * from bundles those are coming from previous screens.
     * </p>
     */
    private void extractDataFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            verifyOTPPresenter.extractAndStoreData(bundle.getString(MOBILE) ,bundle.getString(COMING_FROM),
                    bundle.getString(COUNTRY_CODE),bundle.getString(PASSWORD),bundle.getString(OTP_TIMER));
        }
    }

    @Override
    public void disableResend()
    {
        tv_verify_otp_timer.setVisibility(View.VISIBLE);
        tv_verify_otp_resend.setEnabled(false);
        tv_verify_otp_resend.setTextColor(getResources().getColor(R.color.gray));

        verifyOTPPresenter.handleResendOTP();
    }


    @OnClick ({R.id.tv_verify_otp_resend , R.id.btn_verify_otp_verify ,R.id.iv_back_button,
            R.id.rl_back_button})
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_back_button:
            case R.id.rl_back_button:
                finish();
                overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
                break;

            case R.id.tv_verify_otp_resend:
                verifyOTPPresenter.getVerificationCode();
                break;

            case R.id.btn_verify_otp_verify:
                verifyOTPPresenter.addDataAndCallAPI(et_verify_otp_digits.get(0).getText().toString() +
                        et_verify_otp_digits.get(1).getText().toString() + et_verify_otp_digits.get(2).getText().toString() +
                        et_verify_otp_digits.get(3).getText().toString());
                break;
        }
    }

    @OnTextChanged ({R.id.et_verify_otp_first_digit ,R.id.et_verify_otp_second_digit,
            R.id.et_verify_otp_third_digit ,R.id.et_verify_otp_fourth_digit})
    public void afterTextChanged(Editable s)
    {
        if(s.hashCode() == et_verify_otp_digits.get(0).getText().hashCode())
        {
            verifyOTPPresenter.validateEnteredDigit(et_verify_otp_digits.get(0).getText().toString(),
                    1,0,false);
        }
        else if (s.hashCode() == et_verify_otp_digits.get(1).getText().hashCode())
        {
            verifyOTPPresenter.validateEnteredDigit(et_verify_otp_digits.get(1).getText().toString(),
                    2,0,false);
        }
        else if (s.hashCode() == et_verify_otp_digits.get(2).getText().hashCode())
        {
            verifyOTPPresenter.validateEnteredDigit(et_verify_otp_digits.get(2).getText().toString(),
                    3,1,false);
        }
        if (s.hashCode() == et_verify_otp_digits.get(3).getText().hashCode())
        {
            verifyOTPPresenter.validateEnteredDigit(et_verify_otp_digits.get(3).getText().toString(),
                    3,2,true);
        }
    }

    @Override
    public void onBackPressed() {
        iv_mobileOtpImg.setVisibility(View.VISIBLE);
        super.onBackPressed();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
    }

    @Override
    public void onGettingAutomaticOTP(String ... otpDigits)
    {
        et_verify_otp_digits.get(0).setText(otpDigits[0]);
        et_verify_otp_digits.get(1).setText(otpDigits[1]);
        et_verify_otp_digits.get(2).setText(otpDigits[2]);
        et_verify_otp_digits.get(3).setText(otpDigits[3]);
    }

    @SuppressLint("ClickableViewAccessibility")
    @OnTouch(R.id.rl_verify_otp_layout)
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                ActivityUtils.hideSoftKeyBoard(v);
                break;
        }
        return false;
    }


    @Override
    public void enableResendButton()
    {
        tv_verify_otp_resend.setEnabled(true);
        tv_verify_otp_resend.setTextColor(color333333);
        tv_verify_otp_timer.setVisibility(View.GONE);
    }

    @Override
    public void setElapsedTime(String elapsedTime)
    {
        tv_verify_otp_timer.setText(elapsedTime);
    }

    @Override
    public void showProgressDialog(String message)
    {
        pDialog.setMessage(message);
        pDialog.show();
    }

    @Override
    public void hideProgressDialog()
    {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void showAlertWithMsg(String errorMsg) {
        alerts.problemLoadingAlert(this, errorMsg);
    }

    @Override
    public void clearFields()
    {
        et_verify_otp_digits.get(0).setText("");
        et_verify_otp_digits.get(1).setText("");
        et_verify_otp_digits.get(2).setText("");
        et_verify_otp_digits.get(3).setText("");
        et_verify_otp_digits.get(0).requestFocus();
    }

    @Override
    public void openSecondSplashScreen()
    {
        hideKeyboard();
        Intent intent = new Intent(this, SecondSplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * <h2>hideKeyboard</h2>
     * <p>This method is used to hide the virtual keyboard</p>
     */
    private void hideKeyboard()
    {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitleForScreen(String phone)
    {
        tv_verify_otp_info.setText(otp_info1+ " " + phone + " "+ otp_info2);
        disableResend();
    }

    @Override
    public void openChangePasswordActivity(String otp, String phone, String comingFrom)
    {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("otp", otp);
        intent.putExtra("ent_mobile", phone);
        intent.putExtra("comingFrom", comingFrom);
        startActivity(intent);
    }

    @Override
    public void openMainActivityWithoutClear()
    {
        hideKeyboard();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public void requestFocus(int position) {
        et_verify_otp_digits.get(position).requestFocus();
    }

    @Override
    public void loadingAlert(String message)
    {
        final Dialog dialog = new Dialog(VerifyOTPActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tvOk =  dialog.findViewById(R.id.tv_alert_ok);
        TextView tvText =  dialog.findViewById(R.id.tv_alert_body);
        tvText.setText(message);
        tvOk.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(VerifyOTPActivity.this,MainActivity.class));
        });
    }
}
