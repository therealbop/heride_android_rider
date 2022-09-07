package com.karru.authentication.change_password;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.rider.R;
import com.karru.splash.first.SplashActivity;
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
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>ChangePasswordActivity</h1>
 * In this activity used can change etNewPassword
 */
public class ChangePasswordActivity extends DaggerAppCompatActivity
        implements  ChangePasswordActivityContract.View{
    @BindView(R.id.til_editPassword_newPassword) TextInputLayout til_editPassword_newPassword;
    @BindView(R.id.til_editPassword_oldPassword) TextInputLayout til_editPassword_oldPassword;
    @BindView(R.id.til_editPassword_reEnterPassword) TextInputLayout til_editPassword_reEnterPassword;
    @BindView(R.id.tiet_editPassword_reEnterPassword) TextInputEditText tiet_editPassword_reEnterPassword;
    @BindView(R.id.tiet_editPassword_newPassword) TextInputEditText tiet_editPassword_newPassword;
    @BindView(R.id.tiet_editPassword_oldPassword) TextInputEditText tiet_editPassword_oldPassword;
    @BindView(R.id.iv_edit_pass_icon) ImageView iv_edit_pass_icon;
    @BindView(R.id.tv_eitPassword_passwordMsg) TextView tv_eitPassword_passwordMsg;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.btn_editPassword_submit) Button btn_editPassword_submit;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.change_passwd) String changePassword;
    @BindString(R.string.passwordCantBeEmpty) String passwordCantEmpty;
    @BindString(R.string.passwordInvalid) String passwordInvalid;
    @BindString(R.string.password_not_match) String passwrodMismatch;
    @BindString(R.string.forgot_password_text) String forgot_password_text;
    @BindString(R.string.password_match) String passwrodMatch;
    @BindString(R.string.password_changed) String passwrodChanged;
    @BindDrawable(R.drawable.selector_layout) Drawable selector;
    @BindDrawable(R.drawable.ic_chevron_right_white_24dp) Drawable ic_chevron_right_white_24dp;
    @BindDrawable(R.drawable.ic_chevron_left_white_24dp) Drawable ic_chevron_left_white_24dp;
    @BindColor(R.color.grey_bg) int grey_bg;

    @Inject Alerts alerts;
    @Inject ChangePasswordActivityContract.Presenter changePasswordPresenter;
    @Inject AppTypeface appTypeface;

    private String comingFrom;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            comingFrom = bundle.getString("comingFrom", "null");
            changePasswordPresenter.checkSource(comingFrom);
        }
        initToolBar();
        initProgressBar();
        initializeView();
    }

    @Override
    public void enableOldPasswod() {
        iv_edit_pass_icon.setVisibility(View.GONE);
        tv_eitPassword_passwordMsg.setVisibility(View.GONE);
        til_editPassword_oldPassword.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changePasswordPresenter.checkRTLConversion();
    }

    @Override
    public void setForgotPasswordViews()
    {
        iv_edit_pass_icon.setVisibility(View.VISIBLE);
        tv_eitPassword_passwordMsg.setVisibility(View.VISIBLE);
        tv_eitPassword_passwordMsg.setText(forgot_password_text);
    }

    @Override
    public void showProgressDialog()
    {
        if(pDialog!=null && !pDialog.isShowing() && !isFinishing())
            pDialog.show();
    }

    @OnTextChanged({R.id.tiet_editPassword_newPassword, R.id.tiet_editPassword_oldPassword,
            R.id.tiet_editPassword_reEnterPassword})
    public void textWatcher(CharSequence s) {
        if (!TextUtil.isEmpty(tiet_editPassword_reEnterPassword.getText().toString()) &&
                !TextUtil.isEmpty(tiet_editPassword_newPassword.getText().toString())
                && !TextUtil.isEmpty(tiet_editPassword_oldPassword.getText().toString()) && !TextUtil.isEmpty(s.toString()))
            enableButton();
        else if (!TextUtil.isEmpty(tiet_editPassword_newPassword.getText().toString())
                && !TextUtil.isEmpty(tiet_editPassword_reEnterPassword.getText().toString()) && !TextUtil.isEmpty(s.toString())
                && !comingFrom.equals("Profile"))
            enableButton();
        else
            disableButton();
    }

    /**
     * <h2>disableButton</h2>
     * This method is used to enable the submit button
     */
    public void enableButton() {
        til_editPassword_newPassword.setErrorEnabled(false);
        til_editPassword_oldPassword.setErrorEnabled(false);
        til_editPassword_reEnterPassword.setErrorEnabled(false);
        btn_editPassword_submit.setBackground(selector);
        btn_editPassword_submit.setEnabled(true);
    }

    /**
     * <h2>disableButton</h2>
     * This method is used to disable the submit button
     */
    public void disableButton() {
        btn_editPassword_submit.setBackgroundColor(grey_bg);
        btn_editPassword_submit.setEnabled(false);
    }

    @Override
    public void hideProgressDialog()
    {
        if (pDialog != null && pDialog.isShowing() && !isFinishing())
            pDialog.dismiss();
    }

    /**
     * <h2>initToolBar</h2>
     * <p> method to initialize the tool bar of this screen </p>
     */
    private void initToolBar()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setText(changePassword);
    }

    public void initProgressBar()
    {
        pDialog = alerts.getProcessDialog(this);
        pDialog.setMessage(wait);
        pDialog.setCancelable(false);
    }
    /**
     * <h3>initializeView()</h3>
     * <p> This method is used to initialize views on this activity </p>
     */
    private void initializeView() {

        tiet_editPassword_newPassword.setTypeface(appTypeface.getPro_News());
        tiet_editPassword_reEnterPassword.setTypeface(appTypeface.getPro_News());
        tv_eitPassword_passwordMsg.setTypeface(appTypeface.getPro_News());

        btn_editPassword_submit.setTypeface(appTypeface.getPro_News());
    }

    @OnClick({R.id.btn_editPassword_submit,R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_editPassword_submit:
                validatePasswordFields();
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;
        }
    }

    @OnFocusChange({R.id.tiet_editPassword_reEnterPassword, R.id.tiet_editPassword_newPassword, R.id.tiet_editPassword_oldPassword})
    public void focushangeListner(View v, boolean hasFocus) {
        String temp = ((TextInputEditText) v).getText().toString();
        switch (v.getId()) {
            case R.id.tiet_editPassword_oldPassword:
                if (!hasFocus)
                    changePasswordPresenter.validateOldPasswordInputValue(temp);
                break;

            case R.id.tiet_editPassword_newPassword:
                if (!hasFocus)
                    changePasswordPresenter.validateInputValue(temp);
                break;

            case R.id.tiet_editPassword_reEnterPassword:
                if (!hasFocus)
                    changePasswordPresenter.validateReEnterInputValue(temp);
                break;
        }
    }

    public void oldPasswordDataEmpty() {
        til_editPassword_oldPassword.setErrorEnabled(true);
        til_editPassword_oldPassword.setError(passwordCantEmpty);
    }

    @Override
    public void newPasswordDataEmpty() {
        til_editPassword_newPassword.setErrorEnabled(true);
        til_editPassword_newPassword.setError(passwordCantEmpty);
    }

    public void setOldPaswdError(String message) {
        til_editPassword_oldPassword.setErrorEnabled(true);
        til_editPassword_oldPassword.setError(message);
        tiet_editPassword_oldPassword.requestFocus();
    }

    @Override
    public void newPasswordDataNotValid() {
        til_editPassword_newPassword.setErrorEnabled(true);
        til_editPassword_newPassword.setError(passwordInvalid);
    }

    @Override
    public void newPasswordDataValid() {
        til_editPassword_newPassword.setErrorEnabled(false);
    }

    public void oldPasswordDataValid() {
        til_editPassword_oldPassword.setErrorEnabled(false);
    }

    @Override
    public void reEnterPasswordDataEmpty() {
        til_editPassword_reEnterPassword.setErrorEnabled(true);
        til_editPassword_reEnterPassword.setError(passwordCantEmpty);
    }

    @Override
    public void reEnterPasswordDataNotValid() {
        til_editPassword_reEnterPassword.setErrorEnabled(true);
        til_editPassword_reEnterPassword.setError(passwordInvalid);
    }

    @Override
    public void reEnterPasswordDataValid() {
        til_editPassword_reEnterPassword.setErrorEnabled(false);
    }

    @OnEditorAction(R.id.btn_editPassword_submit)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE)
            btn_editPassword_submit.performClick();
        return true;
    }


    /**
     * <h2>validatePasswordFields</h2>
     * <p></p>
     */
    private void validatePasswordFields() {
        tiet_editPassword_newPassword.clearFocus();
        tiet_editPassword_reEnterPassword.clearFocus();
        changePasswordPresenter.isValidPassword(comingFrom);
    }

    @Override
    public void setSamePasswordError() {
        til_editPassword_newPassword.setErrorEnabled(true);
        til_editPassword_newPassword.setError(passwrodMatch);
        tiet_editPassword_newPassword.requestFocus();
        disableButton();
    }

    @Override
    public void getPassword() {
        String pass = tiet_editPassword_newPassword.getText().toString();
        String rePass = tiet_editPassword_reEnterPassword.getText().toString();
        changePasswordPresenter.checkPasswordMatch(pass, rePass);
    }

    public void getPasswordForProfile() {
        String oldPass = tiet_editPassword_oldPassword.getText().toString();
        String pass = tiet_editPassword_newPassword.getText().toString();
        String rePass = tiet_editPassword_reEnterPassword.getText().toString();
        changePasswordPresenter.checkPasswordMatchforProfile(pass, rePass, oldPass);
    }

    @Override
    public void missMatchError() {
        til_editPassword_reEnterPassword.setErrorEnabled(true);
        til_editPassword_reEnterPassword.setError(passwrodMismatch);
        tiet_editPassword_reEnterPassword.requestFocus();
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        changePasswordPresenter.disposeObservable();
    }

    /**
     * <h3>hideKeyboard()</h3>
     * <p> This method is used to hide keyboard </p>
     */
    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void loadAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok = dialog.findViewById(R.id.tv_alert_ok);
        TextView tv_text = dialog.findViewById(R.id.tv_alert_body);
        tv_text.setText(passwrodChanged);
        tv_ok.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent;
            intent = new Intent(ChangePasswordActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadErrorAlert(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok = dialog.findViewById( R.id.tv_alert_ok);
        TextView tv_text = dialog.findViewById( R.id.tv_alert_body);
        tv_text.setText(message);
        tv_ok.setOnClickListener(v -> {
                    dialog.dismiss();
                    Intent intentReturn = getIntent();
                    setResult(RESULT_OK, intentReturn);
                    onBackPressed();
                }
        );
    }
}