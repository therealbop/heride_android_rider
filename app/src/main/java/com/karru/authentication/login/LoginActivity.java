package com.karru.authentication.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.karru.authentication.forgot_password.ForgotPasswordActivity;
import com.heride.rider.R;
import com.karru.splash.second.SecondSplashActivity;
import com.karru.authentication.signup.SignUpActivity;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.EMAIL;
import static com.karru.utility.Constants.LOCATION_PERMISSION_CODE;
import static com.karru.utility.Constants.LOGIN_TYPE;
import static com.karru.utility.Constants.NAME;
import static com.karru.utility.Constants.NORMAL_LOGIN;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PICTURE;
import static com.karru.utility.Constants.REQUEST_CHECK_SETTINGS;
import static com.karru.utility.Constants.SOCIAL_MEDIA_ID;

/**
 * <h1>Login Activity</h1>
 * This class is used to provide the Login screen, where we can do our login and if we forget
 * <p>iv_login_google
 *     our etNewPassword then here we also can make a request to forgot etNewPassword
 *      and if login successful, then it directly opens Main Activity.
 * </p>
 * @author 3embed
 * @since 3 Jan 2017.*/
public class LoginActivity extends DaggerAppCompatActivity implements
        View.OnFocusChangeListener,TextWatcher, GoogleApiClient.OnConnectionFailedListener,
        LoginContract.View
{
    private static final String TAG = "LoginActivity";
    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject LoginContract.Presenter loginPresenter;
    @Inject AppPermissionsRunTime permissionsRunTime;

    @BindView (R.id.btn_login) Button btn_login;
    @BindView (R.id.et_login_email) EditText et_login_email;
    @BindView (R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView (R.id.et_login_password) EditText et_login_password;
    @BindView (R.id.til_login_email) TextInputLayout til_login_email;
    @BindView (R.id.tv_login_forgot_pass) TextView tv_login_forgot_pass;
    @BindView (R.id.til_login_password) TextInputLayout til_login_password;
    @BindView (R.id.iv_login_fb) ImageView iv_login_fb;
    @BindView (R.id.iv_login_google) ImageView iv_login_google;
    @BindView (R.id.ll_login_or_decor) LinearLayout ll_login_or_decor;

    @BindString (R.string.phone_invalid) String phone_invalid;
    @BindString (R.string.email_invalid) String email_invalid;
    @BindString (R.string.password_mandatory) String password_mandatory;
    @BindString (R.string.login) String login;
    @BindString (R.string.logging_in) String logging_in;

    private ProgressDialog pDialog;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        setFonts();
    }

    /**
     * <h2>initialization</h2>
     * <p> This method is used to initializing all views of our layout. </p>
     */
    private void initialization()
    {
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        loginPresenter.initializeFBGoogle(callbackManager);
        loginPresenter.checkLoginType(getIntent().getIntExtra(LOGIN_TYPE,NORMAL_LOGIN));
        initProgressBar();
    }

    /**
     * <h2>InitProgress</h2>
     * <p> This method is used for initialising the Progress bar. </p>
     */
    private void initProgressBar()
    {
        pDialog = alerts.getProcessDialog(this);
        pDialog.setMessage(logging_in);
        pDialog.setCancelable(false);
    }

    @Override
    public void showPartnerUI() {
        iv_login_fb.setVisibility(View.GONE);
        iv_login_google.setVisibility(View.GONE);
        ll_login_or_decor.setVisibility(View.GONE);
        tv_login_forgot_pass.setVisibility(View.GONE);
    }

    /**
     * <h2>setFonts</h2>
     * <p>This method is used to set the fonts to the views</p>
     */
    private void setFonts()
    {
        btn_login.setTypeface(appTypeface.getPro_News());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        et_login_email.setTypeface(appTypeface.getPro_News());
        et_login_password.setTypeface(appTypeface.getPro_News());
        tv_login_forgot_pass.setTypeface(appTypeface.getPro_News());
        tv_all_tool_bar_title.setText(login);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loginPresenter.checkRTLConversion();
        loginPresenter.initializeFacebook();
        loginPresenter.checkDefaultUser();
        loginPresenter.validateUserCreds(et_login_email.getText().toString(), et_login_password.getText().toString() );
    }

    @OnClick ({ R.id.btn_login ,R.id.iv_login_fb, R.id.iv_login_google, R.id.tv_login_forgot_pass,
            R.id.rl_all_tool_bar_close,R.id.ib_all_tool_bar_close})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_login:
                hideKeyboard();
                loginPresenter.storeLoginType(1);
                loginPresenter.checkIfLocationEnabled(et_login_email.getText().toString(),
                        et_login_password.getText().toString());
                break;

            case R.id.iv_login_fb:
                loginPresenter.storeLoginType(2);
                loginPresenter.checkIfLocationEnabled(et_login_email.getText().toString(),
                        et_login_password.getText().toString());
                break;

            case R.id.iv_login_google:
                loginPresenter.storeLoginType(3);
                loginPresenter.checkIfLocationEnabled(et_login_email.getText().toString(),
                        et_login_password.getText().toString());
                break;

            case R.id.tv_login_forgot_pass:
                Intent intent=new Intent(this,ForgotPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_all_tool_bar_close:
            case R.id.ib_all_tool_bar_close:
                onBackPressed();
                break;
        }
    }

    @Override
    public void checkLocationPermission()
    {
        if (permissionsRunTime.checkIfPermissionNeeded())
        {
            if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.ACCESS_FINE_LOCATION,this))
                loginPresenter.startLocationService(et_login_email.getText().toString(),
                        et_login_password.getText().toString());
            else
            {
                String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
                permissionsRunTime.requestForPermission(strings,this,LOCATION_PERMISSION_CODE);
            }
        }
        else
            loginPresenter.startLocationService(et_login_email.getText().toString(),
                    et_login_password.getText().toString());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        hideProgressDialog();
        loginPresenter.disposeObservable();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Utility.printLog(TAG+" onRequestPermissionsResult "+requestCode);

        switch (requestCode)
        {
            case LOCATION_PERMISSION_CODE:
                int status =  permissionsRunTime.getPermissionStatus(this,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
                switch (status)
                {
                    case PERMISSION_GRANTED:
                        loginPresenter.startLocationService(et_login_email.getText().toString(),
                                et_login_password.getText().toString());
                        showProgressDialog();
                        break;

                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        break;
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            et_login_email.setText("");
            et_login_password.setText("");
        }

        if (requestCode == Constants.RC_SIGN_IN)
        {
            et_login_email.setText("");
            et_login_password.setText("");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            loginPresenter.handleResultFromGoogle(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Utility.printLog("onConnectionFailed:" + connectionResult);
    }

    @OnFocusChange ( {R.id.et_login_email,R.id.et_login_password})
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        switch (v.getId())
        {
            case R.id.et_login_email:
                if (!hasFocus)
                    loginPresenter.validateUserName(et_login_email.getText().toString());
                break;

            case R.id.et_login_password:
                if (!hasFocus) {
                    loginPresenter.validateNonEmptyPass(et_login_password.getText().toString());
                }
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @OnTextChanged ({ R.id.et_login_email, R.id.et_login_password})
    @Override
    public void afterTextChanged(Editable editable)
    {
        if (editable == et_login_email.getEditableText())
        {
            til_login_email.setErrorEnabled(false);
            loginPresenter.isEmailError(et_login_email.getText().toString());
            loginPresenter.validateUserCreds(et_login_email.getText().toString(),
                    et_login_password.getText().toString() );
        }
        if (editable == et_login_password.getEditableText())
        {
            til_login_password.setErrorEnabled(false);
            loginPresenter.validateUserCreds(et_login_email.getText().toString(),
                    et_login_password.getText().toString() );
        }
    }


    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }

    @Override
    public void saveUserCreds(String userId, String password) {
        et_login_email.setText (userId);
        et_login_password.setText (password);
    }

    @Override
    public void enableLoginBtn() {
        btn_login.setEnabled(true);
        btn_login.setBackgroundResource(R.drawable.signin_login_selector);
    }

    @Override
    public void disableLoginBtn() {
        btn_login.setEnabled(false);
        btn_login.setBackgroundResource(R.drawable.grey_login_selector);
    }

    @Override
    public void showProgressDialog()
    {
        try {
            if (pDialog != null && !pDialog.isShowing() && !isFinishing())
                pDialog.show();
        }
        catch (Exception e)
        {
            Utility.printLog(TAG+" Exception "+e);
        }
    }

    @Override
    public void hideProgressDialog()
    {
        if (pDialog != null && !isFinishing() && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void invalidPhoneNumber() {
        til_login_email.setError(phone_invalid);
    }

    @Override
    public void invalidEmailId() {
        til_login_email.setError(email_invalid);
    }

    @Override
    public void emptyPassword() {
        til_login_password.setError(password_mandatory);
    }

    @Override
    public void nonEmptyPassword() {
        til_login_password.setErrorEnabled(false);
    }

    @Override
    public void showErrorFromAPI(String errMsg)
    {
        alerts.problemLoadingAlert(this, errMsg);
    }

    @Override
    public void showToast(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoginSuccessful()
    {
        Intent intent = new Intent(this, SecondSplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserNotLoggedIn(String name, String email, String picture, int login_type,
                                  String ent_socialMedia_id)
    {
        Intent intentData = new Intent(this, SignUpActivity.class);
        intentData.putExtra(NAME, name);
        intentData.putExtra(EMAIL, email);
        intentData.putExtra(PICTURE, picture);
        intentData.putExtra(LOGIN_TYPE, login_type);
        intentData.putExtra(SOCIAL_MEDIA_ID, ent_socialMedia_id);
        startActivity(intentData);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void openGoogleActivity(Intent intent)
    {
        startActivityForResult(intent,Constants.RC_SIGN_IN);
    }

    @Override
    public void promptUserWithLocationAlert(Status status) {
        try
        {
            status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        }
        catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
}
