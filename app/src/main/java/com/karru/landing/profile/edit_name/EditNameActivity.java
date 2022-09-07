package com.karru.landing.profile.edit_name;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>EditNameActivity</h>
 * <P> Class to help user to update Name </P>
 */
public class EditNameActivity extends DaggerAppCompatActivity implements EditNameActivityContract.EditNameView
{
    @BindView(R.id.tiet_edit_name_name)  TextInputEditText tiet_edit_name_name;
    @BindView(R.id.tv_edit_name_title)  TextView tv_edit_name_title;
    @BindView(R.id.tv_all_tool_bar_title)  TextView tv_all_tool_bar_title;
    @BindView(R.id.btn_edit_name_save)  Button btn_edit_name_save;
    @BindView(R.id.rl_edit_name_main)  RelativeLayout rl_edit_name_main;
    @BindDrawable(R.drawable.selector_layout) Drawable selector_layout;
    @BindColor(R.color.grey_bg) int grey_bg;
    @BindString(R.string.edit_name)  String edit_name;
    @BindString(R.string.wait) String wait;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.fixInputFields) String fixInputFields;
    @BindString(R.string.something_went_wrong) String something_went_wrong;
    @BindDrawable(R.drawable.ic_chevron_right_white_24dp) Drawable ic_chevron_right_white_24dp;
    @BindDrawable(R.drawable.ic_chevron_left_white_24dp) Drawable ic_chevron_left_white_24dp;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject EditNameActivityContract.EditNamePresenter editNamePresenter;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        ButterKnife.bind(this);
        initializeView();
        initializeProgressBar();
    }

    @Override
    public void onNameEmpty()
    {
        btn_edit_name_save.setBackgroundColor(grey_bg);
        btn_edit_name_save.setEnabled(false);
    }

    @Override
    public void onNameNonEmpty()
    {
        btn_edit_name_save.setBackground(selector_layout);
        btn_edit_name_save.setEnabled(true);
    }

    /**
     * <h3>initializeView()</h3>
     * <p> This method is used to initialize views on this activity </p>
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initializeView()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tiet_edit_name_name.setTypeface(appTypeface.getPro_News());
        tv_edit_name_title.setTypeface(appTypeface.getPro_News());
        btn_edit_name_save.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setText(edit_name);
        showSoftKeyBoard();
    }

    /**
     * <h>ProgressBar Initializer</h>
     * <p>this method is using to initialize the progress bar</p>
     */
    public void initializeProgressBar()
    {
        pDialog = alerts.getProcessDialog(EditNameActivity.this);
        pDialog.setMessage(wait);
        pDialog.setCancelable(false);
    }

    @OnTouch(R.id.rl_edit_name_main)
    public boolean onTouch(MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            ActivityUtils.hideSoftKeyBoard(rl_edit_name_main);
        return false;
    }

    @OnClick({R.id.btn_edit_name_save,R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_edit_name_save:
                editNamePresenter.nameUpdateRequest(tiet_edit_name_name.getText().toString());
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;
        }
    }

    @OnTextChanged(R.id.tiet_edit_name_name)
    public void textWatcher(CharSequence s)
    {
        editNamePresenter.isNameEmpty(s.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        editNamePresenter.disposeObservable();
    }

    /**<h2>showSoftKeyBoard</h2>
     * <p> This method is used for opening the soft key board. </p>
     */
    private void showSoftKeyBoard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void showProgressDialog()
    {
        pDialog.setMessage(wait);
        if(pDialog != null && !pDialog.isShowing() && !isFinishing())
            pDialog.show();
    }

    @Override
    public void dismissProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing() && !isFinishing())
            pDialog.dismiss();
    }

    @Override
    public void loadingAlert(String message)
    {
        final Dialog dialog = new Dialog(EditNameActivity.this);
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
            Intent intentReturn = getIntent();
            setResult(RESULT_OK, intentReturn);
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editNamePresenter.checkRTLConversion();
    }

    @Override
    public void onResponseError(String errorMessage)
    {
        Toast.makeText(EditNameActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    /**<h3>hideKeyboard()</h3>
     * <p> This method is used to hide keyboard </p>
     */

    public void hideKeyboard(){
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed()
    {
        hideKeyboard();
        super.onBackPressed();
    }
}