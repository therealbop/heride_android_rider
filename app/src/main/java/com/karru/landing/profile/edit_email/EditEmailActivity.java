package com.karru.landing.profile.edit_email;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.rider.R;
import com.karru.util.ActivityUtils;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;

import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>EditEmailActivity</h1>
 * In this activity we have to edit our email id
 */
public class EditEmailActivity extends DaggerAppCompatActivity implements  EditEmailActivityContract.EditEmailView
{
    @BindView(R.id.tiet_edit_mail_Email) TextInputEditText tiet_edit_mail_Email;
    @BindView(R.id.tv_edit_mail_emailMsg) TextView tv_edit_mail_emailMsg;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.btn_edit_mail_save) Button btn_edit_mail_save;
    @BindView(R.id.rl_edit_mail_main) RelativeLayout rl_edit_mail_main;
    @BindString(R.string.edit_email) String edit_email;
    @BindString(R.string.mail_validating) String mail_validating;
    @BindString(R.string.email_empty_error) String email_empty_error;
    @BindString(R.string.invalid_mail_error) String invalid_mail_error;
    @BindString(R.string.wrong_email) String wrong_email;
    @BindString(R.string.wait) String wait;
    @BindColor(R.color.grey_bg) int grey_bg;
    @BindDrawable(R.drawable.selector_layout) Drawable selector;

    @Inject Alerts alerts;
    @Inject EditEmailActivityContract.EditEmailPresenter presenter;
    @Inject AppTypeface appTypeface;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        ButterKnife.bind(this);
        initializeProgressBar();
        initializeView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    /**
     * <h>ProgressBar Initializer</h>
     * <p>this method is using to initialize the progress bar</p>
     */
    public void initializeProgressBar()
    {
        pDialog = alerts.getProcessDialog(EditEmailActivity.this);
        pDialog.setMessage(wait);
        pDialog.setCancelable(false);
    }
    @Override
    public void showProgressDialog()
    {
        pDialog.setMessage(mail_validating);
        if(pDialog !=null && !pDialog.isShowing() && !isFinishing())
            pDialog.show();
    }

    @Override
    public void dismissProgressDialog()
    {
        if(pDialog !=null && pDialog.isShowing() && !isFinishing())
            pDialog.dismiss();
    }

    /**
     * <h3>initializeView()</h3>
     * <p> This method is used to initialize views of this activity </p>
     */
    private void initializeView()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setText(edit_email);
        tiet_edit_mail_Email.setTypeface(appTypeface.getPro_News());
        tv_edit_mail_emailMsg.setTypeface(appTypeface.getPro_News());
        btn_edit_mail_save.setTypeface(appTypeface.getPro_narMedium());

        showSoftKeyBoard();
    }


    @OnFocusChange(R.id.tiet_edit_mail_Email)
    public void onFocusChangeListener(View view, boolean hasFocus) {
        String temp = ((TextInputEditText) view).getText().toString();
        presenter.emailValidator(hasFocus, temp);
    }

    @OnTextChanged(R.id.tiet_edit_mail_Email)
    public void textWatcher(CharSequence s)
    {
        String mail=s.toString();
        if(!TextUtils.isEmpty(mail))
            presenter.isValidEmail(mail);
        else
            onNameEmpty();
    }
    @Override
    public void setEmailEmptyError() {
        tiet_edit_mail_Email.setError(email_empty_error);
    }

    public void setInvalidEmailError() {
        tiet_edit_mail_Email.setError(invalid_mail_error);
    }

    public void wrongMailFormat() {
        tiet_edit_mail_Email.setError(wrong_email);
    }

    public void clearMailError() {
        tiet_edit_mail_Email.setError(null);
    }

    @OnClick({R.id.btn_edit_mail_save,R.id.rl_back_button,R.id.iv_back_button})
    public void clickListener(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_edit_mail_save:
                emailValidationRequest();
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }

    @OnTouch(R.id.rl_edit_mail_main)
    public boolean touchListener(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ActivityUtils.hideSoftKeyBoard(rl_edit_mail_main);
        }
        return false;
    }

    /**
     * <h2>showSoftKeyBoard</h2>
     * <P> This method is used for opening the soft key board. </p>
     */
    private void showSoftKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * <h2>emailValidationRequest</h2>
     * <p> checking  email already registered or not calling email validation service using okhttp </p>
     */
    public void emailValidationRequest() {
        presenter.EditEmailService(tiet_edit_mail_Email.getText().toString().trim());
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

    /**
     * <h3>onBackPressed()</h3>
     * <P> This is the overridden onBackPressed method This is used to hide keyboard before moving to other screen </P>
     */
    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }

    @Override
    public void onNameEmpty()
    {
        btn_edit_mail_save.setBackgroundColor(grey_bg);
        btn_edit_mail_save.setEnabled(false);
    }

    @Override
    public void onNameNonEmpty()
    {
        btn_edit_mail_save.setBackground(selector);
        btn_edit_mail_save.setEnabled(true);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadingAlert(String message)
    {
        Dialog dialog = alerts.userPromptWithOneButton(message,this);
        TextView tv_alert_ok =  dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_ok.setOnClickListener(v ->
        {
            Intent intentReturn = getIntent();
            setResult(RESULT_OK, intentReturn);
            onBackPressed();
        });
        dialog.show();
    }
}