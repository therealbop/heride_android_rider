package com.karru.landing.corporate.add_corporate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

public class AddCorporateProfileAccountActivity extends DaggerAppCompatActivity implements
        AddCorporateProfileAccountContract.CorporateProfileAddMailView
{
    @BindView(R.id.tv_corpo_mail_submit) TextView tv_corpo_mail_submit;
    @BindView(R.id.et_corpo_mail_add) EditText et_corpo_mail_add;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.til_corpo_mail_add) TextInputLayout til_corpo_mail_add;
    @BindString(R.string.emailCantBeEmpty) String emailCantBeEmpty;
    @BindString(R.string.email_invalid) String email_invalid;
    @BindString(R.string.corporate_mail_add) String corporate_mail_add;
    @BindString(R.string.setting_account) String setting_account;
    @BindString(R.string.success) String success;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject AddCorporateProfileAccountContract.CorporateProfileAddMailPresenter presenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_corporate_account);
        ButterKnife.bind(this);
        setAppTypeface();
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(setting_account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    /**
     * <h>Set Font TypeFace</h>
     * <p>This Method is using to Set the Font Style</p>
     */
    public void setAppTypeface()
    {
        tv_all_tool_bar_title.setText(corporate_mail_add);
        et_corpo_mail_add.setTypeface(appTypeface.getPro_News());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_corpo_mail_submit.setTypeface(appTypeface.getPro_narMedium());

        et_corpo_mail_add.setOnEditorActionListener((v1, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.checkIsMailEmpty(et_corpo_mail_add.getText().toString());
            }            return false;
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog()
    {
        if(!isFinishing() && !progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void dismissProgressDialog()
    {
        if(!isFinishing() && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.disposeObservable();
    }

    @Override
    public void mailEmptyError()
    {
        til_corpo_mail_add.setErrorEnabled(true);
        til_corpo_mail_add.setError(emailCantBeEmpty);
    }

    @Override
    public void setInvalidMailError(String error)
    {
        til_corpo_mail_add.setErrorEnabled(true);
        til_corpo_mail_add.setError(error);
    }

    @Override
    public void showAlert(String message)
    {
        Dialog dialog = alerts.userPromptWithOneButton(message,this);
        TextView tv_alert_title =  dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_ok =  dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_title.setText(success);
        tv_alert_ok.setOnClickListener(view ->
        {
            dialog.dismiss();
            onBackPressed();
        });
        dialog.show();
    }

    @OnFocusChange({R.id.et_corpo_mail_add})
    public void focusChangeListener(View view, boolean hasFocus)
    {
        switch (view.getId())
        {
            case R.id.et_corpo_mail_add:
                if(!hasFocus)
                    presenter.checkIsMailEmpty(et_corpo_mail_add.getText().toString());
                break;
        }
    }

    @OnTextChanged({R.id.et_corpo_mail_add})
    public void onTextChanged()
    {
        til_corpo_mail_add.setErrorEnabled(true);
        til_corpo_mail_add.setError(null);
    }

    @OnClick({R.id.tv_corpo_mail_submit,R.id.rl_back_button})
    public void clickEvent(View view)
    {
        switch(view.getId())
        {
            case R.id.rl_back_button:
                this.onBackPressed();
                break;

            case R.id.tv_corpo_mail_submit:
                presenter.checkIsMailEmpty(et_corpo_mail_add.getText().toString());
                break;
        }
    }
}

