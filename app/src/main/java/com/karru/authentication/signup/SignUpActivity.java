package com.karru.authentication.signup;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.karru.authentication.verification.VerifyOTPActivity;
import com.karru.countrypic.CountryPicker;
import com.heride.rider.R;
import com.karru.authentication.privacy.WebViewActivity;
import com.karru.booking_flow.address.view.AddressSelectionActivity;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.util.image_upload.ImageOperation;
import com.karru.utility.Constants;
import com.karru.utility.HandlePictureEvents;
import com.karru.utility.Scaler;
import com.karru.utility.Utility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;

import static com.karru.utility.Constants.CAMERA_PIC;
import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.COUNTRY_CODE;
import static com.karru.utility.Constants.CROP_IMAGE;
import static com.karru.utility.Constants.EMAIL;
import static com.karru.utility.Constants.GALLERY_PIC;
import static com.karru.utility.Constants.LOCATION_PERMISSION_CODE;
import static com.karru.utility.Constants.LOGIN_TYPE;
import static com.karru.utility.Constants.MOBILE;
import static com.karru.utility.Constants.NAME;
import static com.karru.utility.Constants.OREO_PERMISSION;
import static com.karru.utility.Constants.OTP_TIMER;
import static com.karru.utility.Constants.PASSWORD;
import static com.karru.utility.Constants.PERMISSION_BLOCKED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PERMISSION_REQUEST;
import static com.karru.utility.Constants.PHONE;
import static com.karru.utility.Constants.PICTURE;
import static com.karru.utility.Constants.REFERRAL_CODE;
import static com.karru.utility.Constants.REQUEST_CHECK_SETTINGS;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.SOCIAL_MEDIA_ID;
import static com.karru.utility.Constants.WEB_LINK;
import static com.karru.utility.Constants.WRITE_STORAGE_PERMISSION_CODE;

/**
 * <h1>SignUp Activity</h1>
 * <p>This is a Controller class for SignUp Activity</p>
 * This class is used to provide the SignUp screen, where we can register our user and after that we get an OTP on our mobile and
 * this class is give a call to SignUpPresenter class.
 */
public class SignUpActivity extends DaggerAppCompatActivity implements SignUpContract.View
{
    private static final String TAG = "SignUpActivity";
    @BindView(R.id.pBar_sign_up_imgProgress) ProgressBar progressBar;
    @BindView (R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView (R.id.iv_sign_up_pic) ImageView iv_sign_up_pic;
    @BindView (R.id.tv_sign_up_code) TextView tv_sign_up_code;
    @BindView (R.id.iv_sign_up_flag) ImageView iv_sign_up_flag;
    @BindView (R.id.rg_sign_up_account_type) RadioGroup rg_sign_up_account_type;
    @BindView (R.id.tv_sign_up_terms_conds) TextView tv_sign_up_terms_conds;
    @BindView (R.id.tv_sign_up_phone_error) TextView tv_sign_up_phone_error;
    @BindView (R.id.btn_sign_up_create_account) Button btn_sign_up_create_account;
    @BindView (R.id.swtch_ind_sign_up_terms_conds) Switch swtch_ind_sign_up_terms_conds;
    @BindView (R.id.swtch_buis_sign_up_terms_conds) Switch swtch_buis_sign_up_terms_conds;
    @BindView(R.id.view_edit_phone_line) View view_edit_phone_line;
    @BindString(R.string.accept_terms_privacy_policy) String accept_terms_privacy_policy;
    @BindString(R.string.signup) String signup;
    @BindString(R.string.Countrypicker) String countrypicker;
    @BindString(R.string.mandatory) String mandatory;
    @BindString(R.string.failImageUpload) String failImageUpload;
    @BindString(R.string.invalid_phone) String invalid_phone;
    @BindString(R.string.pleaseWait) String pleaseWait;
    @BindString(R.string.permission_alert_storage) String permission_alert_storage;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.permission_alert_camera) String permission_alert_camera;
    @BindString(R.string.permission_alert_gallery) String permission_alert_gallery;
    @BindColor(R.color.shadow_color) int shadow_color;
    @BindColor(R.color.tiet_under_line) int tiet_under_line;
    @BindColor(R.color.red) int red;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;

    @BindViews({R.id.rb_sign_up_individual , R.id.rb_sign_up_corporate})
    List<RadioButton> rb_sign_up_account_type;

    @BindViews({ R.id.et_sign_up_name , R.id.et_sign_up_email , R.id.et_sign_up_phone ,
            R.id.et_sign_up_password , R.id.et_sign_up_referral , R.id.et_sign_up_company_name ,
            R.id.et_sign_up_company_address})
    List<EditText> et_sign_up_user_details;

    @BindViews({ R.id.til_sign_up_name , R.id.til_sign_up_email , R.id.til_sign_up_phone ,
            R.id.til_sign_up_password , R.id.til_sign_up_referral , R.id.til_sign_up_company_name ,
            R.id.til_sign_up_company_address})
    List<TextInputLayout> til_sign_up_user_details;

    @Inject Alerts alerts;
    @Inject Context mContext;
    @Inject AppTypeface appTypeface;
    @Inject CountryPicker mCountryPicker;
    @Inject com.karru.util.Utility utility;
    @Inject ImageOperation mImageOperation;
    @Inject SignUpContract.Presenter mSignUpPresenter;
    @Inject AppPermissionsRunTime permissionsRunTime;

    private Unbinder unbinder;
    private String picture;
    private File newFile;
    private ProgressDialog pDialog;
    private HandlePictureEvents handlePicEvent;
    private boolean isIndividual,isBuisness;
    private int imageType = CAMERA_PIC;
    private Uri galleryImage;

    /**
     * This is the onCreateHomeActivity method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initVariables();
        initializeTypeFace();
        if(getIntent() != null)
        {
            Intent savedInstanceBundle = getIntent();
            retrieveSavedInstance(savedInstanceBundle);
        }
    }

    @OnFocusChange(R.id.et_sign_up_company_address)
    public void textHasFocus(boolean hasFocus)
    {
        if(hasFocus)
        {
            mSignUpPresenter.hideKeyboardAndClearFocus();
            mSignUpPresenter.getUserCompanyAddress();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSignUpPresenter.checkRTLConversion();
        et_sign_up_user_details.get(0).requestFocus();
        this.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
    }

    /**
     * <h2>initializeTypeFace</h2>
     *<p> This method is used to set the type face to the text views</p>
     */
    private void initializeTypeFace()
    {
        handlePicEvent = new HandlePictureEvents(this,null);
        tv_sign_up_code.setTypeface(appTypeface.getPro_News());
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());

        et_sign_up_user_details.get(0).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(0).setTypeface(appTypeface.getPro_News());

        et_sign_up_user_details.get(1).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(1).setTypeface(appTypeface.getPro_News());

        et_sign_up_user_details.get(2).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(2).setTypeface(appTypeface.getPro_News());

        et_sign_up_user_details.get(3).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(3).setTypeface(appTypeface.getPro_News());

        et_sign_up_user_details.get(4).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(4).setTypeface(appTypeface.getPro_News());

        et_sign_up_user_details.get(5).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(5).setTypeface(appTypeface.getPro_News());

        et_sign_up_user_details.get(6).setTypeface(appTypeface.getPro_News());
        til_sign_up_user_details.get(6).setTypeface(appTypeface.getPro_News());

        rb_sign_up_account_type.get(0).setTypeface(appTypeface.getPro_News());
        rb_sign_up_account_type.get(1).setTypeface(appTypeface.getPro_News());

        tv_sign_up_terms_conds.setTypeface(appTypeface.getPro_News());
        btn_sign_up_create_account.setTypeface(appTypeface.getPro_narMedium());
    }

    /**
     * <h2>initVariables</h2>
     * <p> method to initialize the required variables and other required classes references </p>
     */
    private void initVariables()
    {
        unbinder = ButterKnife.bind(this);
        et_sign_up_user_details.get(0).setNextFocusDownId(R.id.et_sign_up_phone);
        et_sign_up_user_details.get(0).setSelection(0);

        mImageOperation.initializeAmazon(this);
        tv_all_tool_bar_title.setText(signup);
        pDialog = alerts.getProcessDialog(this);

        tv_sign_up_terms_conds.setMovementMethod(LinkMovementMethod.getInstance());
        tv_sign_up_terms_conds.setHighlightColor(Color.TRANSPARENT);

        mSignUpPresenter.getSpannableString(accept_terms_privacy_policy);

        swtch_ind_sign_up_terms_conds.setEnabled(false);
        swtch_buis_sign_up_terms_conds.setEnabled(false);
        btn_sign_up_create_account.setEnabled(false);

        rg_sign_up_account_type.setOnCheckedChangeListener((group, checkedId) ->
                mSignUpPresenter.changeAccountType(checkedId != R.id.rb_sign_up_individual));

        swtch_ind_sign_up_terms_conds.setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            mSignUpPresenter.handleSignUpBtnStateEnabling(isChecked,swtch_ind_sign_up_terms_conds.isEnabled(),rb_sign_up_account_type.get(0).isChecked());
            mSignUpPresenter.isIndividual(isChecked,rb_sign_up_account_type.get(0).isChecked());
        });
        swtch_buis_sign_up_terms_conds.setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            mSignUpPresenter.handleSignUpBtnStateEnabling(isChecked,swtch_ind_sign_up_terms_conds.isEnabled(),rb_sign_up_account_type.get(0).isChecked());
            mSignUpPresenter.isIndividual(isChecked,rb_sign_up_account_type.get(0).isChecked());
        });

        mSignUpPresenter.getCountryInfo(mCountryPicker);
    }


    @OnFocusChange({ R.id.et_sign_up_name , R.id.et_sign_up_phone , R.id.et_sign_up_email ,
            R.id.et_sign_up_password  , R.id.et_sign_up_referral , R.id.et_sign_up_company_name,
            R.id.et_sign_up_company_address})
    public void onFocusChangeListener(View v, boolean hasFocus)
    {
        switch (v.getId())
        {
            case R.id.et_sign_up_name:
                if (!hasFocus)
                {
                    mSignUpPresenter.validateFullName(et_sign_up_user_details.get(0).getText().toString());
                    mSignUpPresenter.validateAllFieldsFlags();
                }
                break;

            case R.id.et_sign_up_phone:
                if (!hasFocus) {
                    mSignUpPresenter.validateMobileField(et_sign_up_user_details.get(2).getText().toString(),
                            tv_sign_up_code.getText().toString(), true);
                }
                break;

            case R.id.et_sign_up_email:
                if (!hasFocus) {
                    mSignUpPresenter.validateEmailFormat(et_sign_up_user_details.get(1).getText().toString(),
                            true);
                }
                break;

            case R.id.et_sign_up_password:
                if (!hasFocus)
                {
                    mSignUpPresenter.validatePassword(et_sign_up_user_details.get(3).getText().toString());
                    mSignUpPresenter.validateAllFieldsFlags();
                }
                break;

            case R.id.et_sign_up_referral:
                if (!hasFocus)
                    mSignUpPresenter.verifyReferralEntered(et_sign_up_user_details.get(4).getText().toString());
                break;

            case R.id.et_sign_up_company_name:
                if(!hasFocus && mSignUpPresenter.fetchAccountType())
                {
                    mSignUpPresenter.validateCompanyName(et_sign_up_user_details.get(5).getText().toString());
                    mSignUpPresenter.validateAllFieldsFlags();
                }
                break;

            case R.id.et_sign_up_company_address:
                if(!hasFocus && mSignUpPresenter.fetchAccountType())
                {
                    mSignUpPresenter.validateCompanyAddress(et_sign_up_user_details.get(6).getText().toString());
                    mSignUpPresenter.validateAllFieldsFlags();
                }
                break;
            default:
                break;
        }
    }

    @OnTextChanged({R.id.et_sign_up_name , R.id.et_sign_up_email, R.id.et_sign_up_phone ,
            R.id.et_sign_up_password , R.id.et_sign_up_referral , R.id.et_sign_up_company_name ,
            R.id.et_sign_up_company_address})
    public void afterTextChanged(Editable editable)
    {
        if (editable == et_sign_up_user_details.get(0).getEditableText())
        {
            mSignUpPresenter.validateFullName(et_sign_up_user_details.get(0).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
        else if (editable == et_sign_up_user_details.get(1).getEditableText())
        {
            mSignUpPresenter.validateEmailFormat(et_sign_up_user_details.get(1).getText().toString(),
                    false);
        }

        else if (editable == et_sign_up_user_details.get(2).getEditableText())
        {
            view_edit_phone_line.setBackgroundColor(getResources().getColor(R.color.tiet_under_line));
            til_sign_up_user_details.get(2).setErrorEnabled(false);
            tv_sign_up_phone_error.setVisibility(View.INVISIBLE);
        }

        else if(editable == et_sign_up_user_details.get(3).getEditableText())
        {
            mSignUpPresenter.validatePassword(et_sign_up_user_details.get(3).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
        else if(editable == et_sign_up_user_details.get(4).getEditableText())
        {
            mSignUpPresenter.verifyReferralEntered(et_sign_up_user_details.get(4).getText().toString());
        }
        else if(editable == et_sign_up_user_details.get(5).getEditableText()
                && mSignUpPresenter.fetchAccountType())
        {
            mSignUpPresenter.validateCompanyName(et_sign_up_user_details.get(5).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
        else if(editable == et_sign_up_user_details.get(6).getEditableText()
                && mSignUpPresenter.fetchAccountType())
        {
            mSignUpPresenter.validateCompanyAddress(et_sign_up_user_details.get(6).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
    }

    /**
     * <h2>retrieveSavedInstance</h2>
     * <p> method to retrieved stored data of input fields </p>
     * @param savedInstanceBundle: retrieved savedInstanceState from onCreate bundle
     */
    private void retrieveSavedInstance(Intent savedInstanceBundle)
    {
        mSignUpPresenter.restoreAccountType(savedInstanceBundle.getBooleanExtra("is_business_Account",
                false), savedInstanceBundle.getIntExtra(LOGIN_TYPE, 1));


        if (savedInstanceBundle.getStringExtra(NAME)!=null
                && !savedInstanceBundle.getStringExtra(NAME).isEmpty())
        {
            et_sign_up_user_details.get(0).setText(savedInstanceBundle.getStringExtra(NAME));
            mSignUpPresenter.validateFullName(et_sign_up_user_details.get(0).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
        if (savedInstanceBundle.getStringExtra(PHONE) != null
                && !savedInstanceBundle.getStringExtra(PHONE).isEmpty())
        {
            et_sign_up_user_details.get(2).setText(savedInstanceBundle.getStringExtra(PHONE));
            mSignUpPresenter.validateMobileField(et_sign_up_user_details.get(2).getText().toString(),
                    tv_sign_up_code.getText().toString(),true);
        }
        if (savedInstanceBundle.getStringExtra(EMAIL) != null
                && !savedInstanceBundle.getStringExtra(EMAIL).isEmpty())
        {
            et_sign_up_user_details.get(1).setText(savedInstanceBundle.getStringExtra(EMAIL));
            mSignUpPresenter.validateEmailFormat(et_sign_up_user_details.get(1).getText().toString(),
                    true);
        }
        if (savedInstanceBundle.getStringExtra("password") != null
                && !savedInstanceBundle.getStringExtra("password").isEmpty())
        {
            et_sign_up_user_details.get(3).setText(savedInstanceBundle.getStringExtra("password"));
            mSignUpPresenter.validatePassword(et_sign_up_user_details.get(3).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
        if (savedInstanceBundle.getStringExtra("referral_code") != null
                && !savedInstanceBundle.getStringExtra("referral_code").isEmpty())
        {
            et_sign_up_user_details.get(4).setText(savedInstanceBundle.getStringExtra("referral_code"));
            mSignUpPresenter.verifyReferralEntered(et_sign_up_user_details.get(4).getText().toString());
        }
        if (savedInstanceBundle.getStringExtra("company_name") != null
                && !savedInstanceBundle.getStringExtra("company_name").isEmpty())
        {
            et_sign_up_user_details.get(5).setText(savedInstanceBundle.getStringExtra("company_name"));
            mSignUpPresenter.validateCompanyName(et_sign_up_user_details.get(5).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }

        if (savedInstanceBundle.getStringExtra("drop_addr") != null
                && !savedInstanceBundle.getStringExtra("drop_addr").isEmpty())
        {
            mSignUpPresenter.storeCompanyAddress(savedInstanceBundle.getStringExtra("drop_addr"));
            et_sign_up_user_details.get(6).setText(mSignUpPresenter.fetchCompanyAddress());
            mSignUpPresenter.validateCompanyAddress(et_sign_up_user_details.get(6).getText().toString());
            mSignUpPresenter.validateAllFieldsFlags();
        }
        if (savedInstanceBundle.getStringExtra(SOCIAL_MEDIA_ID) != null)
        {
            mSignUpPresenter.storeSocialMediaID(savedInstanceBundle.getStringExtra(SOCIAL_MEDIA_ID));
        }

        if (savedInstanceBundle.getStringExtra(PICTURE) != null &&
                !savedInstanceBundle.getStringExtra(PICTURE).equals(""))
        {
            picture = savedInstanceBundle.getStringExtra(PICTURE);
            if (permissionsRunTime.checkIfPermissionNeeded())
            {
                if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.WRITE_EXTERNAL_STORAGE,this))
                    uploadImage(picture);
                else
                {
                    String[] strings = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    permissionsRunTime.requestForPermission(strings,this,WRITE_STORAGE_PERMISSION_CODE);
                }
            }
            else
                uploadImage(picture);
        }

        isIndividual=savedInstanceBundle.getBooleanExtra("is_Individual_checked",false);
        isBuisness=savedInstanceBundle.getBooleanExtra("is_buisness_checked",false);
        Utility.printLog("MyStats1"+isIndividual+isBuisness);
        swtch_ind_sign_up_terms_conds.setChecked(isIndividual);
        swtch_buis_sign_up_terms_conds.setChecked(isBuisness);
        clearFocus();
    }

    /**
     * <h2>uploadImage</h2>
     * used to upload image
     */
    private void uploadImage(String picture)
    {
        double size[]= Scaler.getScalingFactor(SignUpActivity.this);
        double height = (130)*size[1];
        double width = (130)*size[0];
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.optionalCircleCrop();
        requestOptions = requestOptions.placeholder(signup_profile_default_image);
        requestOptions = requestOptions.override((int)width, (int)height);
        Glide.with(mContext)
                .asBitmap()
                .load(picture)
                .listener(new RequestListener<Bitmap>()
                {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        runOnUiThread(() -> iv_sign_up_pic.setImageBitmap(bitmap));

                        new Thread(() ->
                        {
                            String takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
                            File newFile1 = new File(Environment.getExternalStorageDirectory()+"/"+ Constants.PARENT_FOLDER+"/Profile_Pictures/",takenNewImage);
                            try {
                                FileOutputStream ostream = new FileOutputStream(newFile1);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.flush();
                                ostream.close();
                                mSignUpPresenter.uploadToAmazon(Uri.fromFile(newFile1));
                            } catch (IOException e) {
                                Utility.printLog(TAG+" IOException "+e.getLocalizedMessage());
                            }
                        }).start();
                        return false;
                    }
                })
                .apply(requestOptions)
                .submit();

        if (picture != null)
            mSignUpPresenter.storeProfilePicUrl(picture);
    }

    @OnClick ({R.id.ib_all_tool_bar_close, R.id.rl_all_tool_bar_close, R.id.scrollView, R.id.iv_sign_up_pic ,
            R.id.ivAddProfilePic , R.id.ll_sign_up_country_code , R.id.et_sign_up_company_address
            , R.id.til_sign_up_company_address, R.id.btn_sign_up_create_account})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_all_tool_bar_close:
            case R.id.rl_all_tool_bar_close:
                onBackPressed();
                break;

            case R.id.scrollView:
                mSignUpPresenter.hideKeyboardAndClearFocus();
                break;

            case R.id.iv_sign_up_pic:
            case R.id.ivAddProfilePic:
                mSignUpPresenter.hideKeyboardAndClearFocus();

                if (permissionsRunTime.checkIfPermissionNeeded())
                {
                    if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.CAMERA,this)
                            && permissionsRunTime.checkIfPermissionGrant(Manifest.permission.READ_EXTERNAL_STORAGE,this))
                        handlePicEvent.openDialog();
                    else
                    {
                        String[] strings = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                        permissionsRunTime.requestForPermission(strings,this,PERMISSION_REQUEST);
                    }
                }
                else
                    handlePicEvent.openDialog();
                break;

            case R.id.ll_sign_up_country_code:
                mSignUpPresenter.hideKeyboardAndClearFocus();
                mCountryPicker.show(getSupportFragmentManager(), countrypicker);
                mSignUpPresenter.addListenerForCountry(mCountryPicker);
                break;

            case R.id.et_sign_up_company_address:
            case R.id.til_sign_up_company_address:
                mSignUpPresenter.hideKeyboardAndClearFocus();
                mSignUpPresenter.getUserCompanyAddress();
                break;

            case R.id.btn_sign_up_create_account:
                mSignUpPresenter.hideKeyboardAndClearFocus();
                mSignUpPresenter.storeUserDetails( et_sign_up_user_details.get(0).getText().toString(),
                        et_sign_up_user_details.get(1).getText().toString(),et_sign_up_user_details.get(3).getText().toString(),
                        tv_sign_up_code.getText().toString(),et_sign_up_user_details.get(2).getText().toString(),
                        et_sign_up_user_details.get(4).getText().toString(),et_sign_up_user_details.get(5).getText().toString());
                mSignUpPresenter.checkIfLocationEnabled();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void openAddressActivity()
    {
        Intent addrIntent = new Intent(this, AddressSelectionActivity.class);
        addrIntent.putExtra("key","startActivityForResultAddr");
        addrIntent.putExtra("keyId", Constants.PICK_ID);
        addrIntent.putExtra("comingFrom","signup");
        addrIntent.putExtra("login_type",mSignUpPresenter.fetchLoginType());
        addrIntent.putExtra("is_business_Account", mSignUpPresenter.fetchAccountType());

        addrIntent.putExtra("name", et_sign_up_user_details.get(0).getText().toString());
        addrIntent.putExtra("phone", et_sign_up_user_details.get(2).getText().toString());
        addrIntent.putExtra("email", et_sign_up_user_details.get(1).getText().toString());
        addrIntent.putExtra("password", et_sign_up_user_details.get(3).getText().toString());
        addrIntent.putExtra("referral_code", et_sign_up_user_details.get(4).getText().toString());

        addrIntent.putExtra("company_name", et_sign_up_user_details.get(5).getText().toString());
        addrIntent.putExtra("picture",mSignUpPresenter.fetchProfilePicUrl());
        isBuisness=swtch_buis_sign_up_terms_conds.isChecked();
        isIndividual=swtch_ind_sign_up_terms_conds.isChecked();
        addrIntent.putExtra("is_Individual_checked",isIndividual);
        addrIntent.putExtra("is_buisness_checked",isBuisness);

        startActivityForResult(addrIntent, Constants.COMPANY_ADDR_ID);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSignUpPresenter.disposeObservable();
        hideKeyboard();
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
    public void checkForPermissionAndSendOTP()
    {
        if (permissionsRunTime.checkIfPermissionNeeded())
        {
            if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.ACCESS_FINE_LOCATION,this))
                mSignUpPresenter.startLocationService();
            else
            {
                String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
                permissionsRunTime.requestForPermission(strings,this,LOCATION_PERMISSION_CODE);
            }
        }
        else
            mSignUpPresenter.startLocationService();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            Utility.printLog("MyAdddress"+data.toString());
            mSignUpPresenter.handleResult(requestCode, resultCode, data.getStringExtra("drop_addr")
                    ,data.getData());
        }

        if (resultCode != Activity.RESULT_OK)    //result code to check is the result is ok or not
        {
            return;
        }

        Utility.printLog("onSuccessInSignUp"+requestCode+data);
        switch (requestCode)
        {
            case CAMERA_PIC:
                handlePicEvent.startCropImage(handlePicEvent.newFile);
                break;

            case GALLERY_PIC:
                if (data != null)
                {
                    imageType = GALLERY_PIC;
                    galleryImage = data.getData();
                    handlePicEvent.gallery(data.getData());
                }
                break;

            case CROP_IMAGE:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                iv_sign_up_pic.setImageBitmap(utility.cropImageCircle(this,path));
                try
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mSignUpPresenter.uploadToAmazon(Uri.fromFile(new File(path)));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onImageUploadSuccess(String imageUrl)
    {
        Utility.printLog( " onSuccessInSignUp: " + imageUrl);
        mSignUpPresenter.setImageUrl(imageUrl);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case LOCATION_PERMISSION_CODE:
            {
                int status =  permissionsRunTime.getPermissionStatus(this,
                        Manifest.permission.ACCESS_FINE_LOCATION,true);
                switch (status)
                {
                    case PERMISSION_GRANTED:
                        showProgressDialog(pleaseWait);
                        mSignUpPresenter.startLocationService();
                        break;

                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        break;
                }
                break;
            }

            case WRITE_STORAGE_PERMISSION_CODE:
                int status =  permissionsRunTime.getPermissionStatus(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,false);
                switch (status)
                {
                    case PERMISSION_GRANTED:
                        progressBar.setVisibility(View.VISIBLE);
                        uploadImage(picture);
                        break;

                    case PERMISSION_BLOCKED:
                        showToast(permission_alert_storage);
                        break;

                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        break;
                }
                break;

            case PERMISSION_REQUEST:
                int statusImage =  permissionsRunTime.getPermissionStatus(this,
                        Manifest.permission.CAMERA, false);
                switch (statusImage)
                {
                    case PERMISSION_GRANTED:
                        int statusGallery =  permissionsRunTime.getPermissionStatus(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE, false);
                        switch (statusGallery)
                        {
                            case PERMISSION_GRANTED:
                                handlePicEvent.openDialog();
                                break;

                            case PERMISSION_BLOCKED:
                                Toast.makeText(this,permission_alert_gallery,Toast.LENGTH_LONG).show();
                                break;

                            default:
                                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                                break;
                        }
                        break;

                    case PERMISSION_BLOCKED:
                        Toast.makeText(this,permission_alert_camera,Toast.LENGTH_LONG).show();
                        break;

                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        break;
                }
                break;

            case OREO_PERMISSION:
                handlePicEvent.checkForVersion(imageType,galleryImage);
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        mSignUpPresenter.onBackPressed();
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }

    @Override
    public void showProgressDialog(String message)
    {
        if(!pDialog.isShowing())
        {
            pDialog.setMessage(message);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public void hideProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void showAlertWithMsg(String errorMsg)
    {
        alerts.problemLoadingAlert(this,errorMsg);
    }

    @Override
    public void showToast(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidEmailIdFromDB()
    {
        til_sign_up_user_details.get(1).setErrorEnabled(false);
        mSignUpPresenter.validateAllFieldsFlags();
    }

    @Override
    public void onInvalidEmailIdFromDB(String errorMsg)
    {
        til_sign_up_user_details.get(1).setErrorEnabled(true);
        til_sign_up_user_details.get(1).setError(errorMsg);
        mSignUpPresenter.validateAllFieldsFlags();
    }


    @Override
    public void onValidMobileFromDB()
    {
        view_edit_phone_line.setBackgroundColor(getResources().getColor(R.color.tiet_under_line));
        til_sign_up_user_details.get(2).setErrorEnabled(false);
        tv_sign_up_phone_error.setVisibility(View.INVISIBLE);
        mSignUpPresenter.validateAllFieldsFlags();
    }

    @Override
    public void onInvalidMobileFromDB(String errorMsg)
    {
        view_edit_phone_line.setBackgroundColor(getResources().getColor(R.color.red));
        tv_sign_up_phone_error.setVisibility(View.VISIBLE);
        tv_sign_up_phone_error.setText(errorMsg);
        mSignUpPresenter.validateAllFieldsFlags();
    }

    @Override
    public void onReferralError(String errorMessage) {
        til_sign_up_user_details.get(4).setErrorEnabled(true);
        til_sign_up_user_details.get(4).setError(errorMessage);
    }



    @Override
    public void onSuccessOfGettingOTP(String otpTime)
    {
        Bundle mBundle = new Bundle();
        mBundle.putString(COMING_FROM, "SignUp");
        mBundle.putString(COUNTRY_CODE, tv_sign_up_code.getText().toString());
        mBundle.putString(MOBILE, et_sign_up_user_details.get(2).getText().toString());
        mBundle.putString(PASSWORD, et_sign_up_user_details.get(3).getText().toString());
        mBundle.putString(REFERRAL_CODE, et_sign_up_user_details.get(4).getText().toString());
        mBundle.putString(OTP_TIMER, otpTime);
        Intent intent = new Intent(this, VerifyOTPActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void onValidFullName()
    {
        til_sign_up_user_details.get(0).setErrorEnabled(false);
    }

    @Override
    public void onInValidFullName()
    {
        til_sign_up_user_details.get(0).setErrorEnabled(true);
        til_sign_up_user_details.get(0).setError(mandatory);
    }

    @Override
    public void onValidPassword()
    {
        til_sign_up_user_details.get(3).setErrorEnabled(false);
    }

    @Override
    public void onInValidPassword()
    {
        til_sign_up_user_details.get(3).setErrorEnabled(true);
        til_sign_up_user_details.get(3).setError(mandatory);
    }

    @Override
    public void onValidCompanyAddress(String address)
    {
        til_sign_up_user_details.get(6).setErrorEnabled(false);
    }

    @Override
    public void onInValidCompanyAddress()
    {
        til_sign_up_user_details.get(6).setErrorEnabled(true);
        til_sign_up_user_details.get(6).setError(mandatory);
    }

    @Override
    public void onValidCompanyName()
    {
        til_sign_up_user_details.get(5).setErrorEnabled(false);
    }

    @Override
    public void onInValidCompanyName()
    {
        til_sign_up_user_details.get(5).setErrorEnabled(true);
        til_sign_up_user_details.get(5).setError(mandatory);
    }

    @Override
    public void hideSoftKeyboard()
    {
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
    }

    @Override
    public void clearFocus()
    {
        if(et_sign_up_user_details.get(0).hasFocus())
            et_sign_up_user_details.get(0).clearFocus();

        if(et_sign_up_user_details.get(1).hasFocus())
            et_sign_up_user_details.get(1).clearFocus();

        if(et_sign_up_user_details.get(2).hasFocus())
            et_sign_up_user_details.get(2).clearFocus();

        if(et_sign_up_user_details.get(3).hasFocus())
            et_sign_up_user_details.get(3).clearFocus();

        if(et_sign_up_user_details.get(4).hasFocus())
            et_sign_up_user_details.get(4).clearFocus();

        if(et_sign_up_user_details.get(5).hasFocus())
            et_sign_up_user_details.get(5).clearFocus();

        if(et_sign_up_user_details.get(6).hasFocus())
            et_sign_up_user_details.get(6).clearFocus();
    }

    @Override
    public void switchToBusinessAccount()
    {
        swtch_ind_sign_up_terms_conds.setVisibility(View.INVISIBLE);
        swtch_buis_sign_up_terms_conds.setVisibility(View.VISIBLE);
        mSignUpPresenter.validateAllFieldsFlags();
        rb_sign_up_account_type.get(0).setChecked(false);
        rb_sign_up_account_type.get(1).setChecked(true);
        til_sign_up_user_details.get(5).setVisibility(View.VISIBLE);
        til_sign_up_user_details.get(6).setVisibility(View.VISIBLE);
    }


    @Override
    public void switchToIndividualAccount()
    {
        swtch_ind_sign_up_terms_conds.setVisibility(View.VISIBLE);
        swtch_buis_sign_up_terms_conds.setVisibility(View.INVISIBLE);
        mSignUpPresenter.validateAllFieldsFlags();
        rb_sign_up_account_type.get(0).setChecked(true);
        rb_sign_up_account_type.get(1).setChecked(false);
        til_sign_up_user_details.get(5).setVisibility(View.GONE);
        til_sign_up_user_details.get(6).setVisibility(View.GONE);
    }

    @Override
    public void enableSignUpButton()
    {
        btn_sign_up_create_account.setEnabled(true);
        btn_sign_up_create_account.setBackgroundResource(R.drawable.selector_layout);
    }

    @Override
    public void disableSignUpButton()
    {
        btn_sign_up_create_account.setEnabled(false);
        btn_sign_up_create_account.setBackgroundColor(shadow_color);
    }

    @Override
    public void onInvalidEmail(String errorMsg)
    {
        til_sign_up_user_details.get(1).setErrorEnabled(true);
        til_sign_up_user_details.get(1).setError(errorMsg);
    }

    @Override
    public void onValidEmail()
    {
        til_sign_up_user_details.get(1).setErrorEnabled(false);
    }


    @Override
    public void agreeToSignUpAccount()
    {
        swtch_ind_sign_up_terms_conds.setEnabled(true);
        mSignUpPresenter.handleSignUpBtnStateEnabling(swtch_ind_sign_up_terms_conds.isChecked(),swtch_ind_sign_up_terms_conds.isEnabled()
                ,rb_sign_up_account_type.get(0).isChecked());
    }

    @Override
    public void disAgreeToSignUpAccountBuisness()
    {
        swtch_buis_sign_up_terms_conds.setChecked(false);
        swtch_buis_sign_up_terms_conds.setEnabled(false);
        mSignUpPresenter.handleSignUpBtnStateEnabling(false,false,
                rb_sign_up_account_type.get(0).isChecked());
    }

    @Override
    public void agreeToSignUpAccountBuisness()
    {
        swtch_buis_sign_up_terms_conds.setEnabled(true);
        mSignUpPresenter.handleSignUpBtnStateEnabling(swtch_buis_sign_up_terms_conds.isChecked(),swtch_ind_sign_up_terms_conds.isEnabled()
                ,rb_sign_up_account_type.get(0).isChecked());
    }

    @Override
    public void disAgreeToSignUpAccount()
    {
        swtch_ind_sign_up_terms_conds.setChecked(false);
        swtch_ind_sign_up_terms_conds.setEnabled(false);
        mSignUpPresenter.handleSignUpBtnStateEnabling(false,false,
                rb_sign_up_account_type.get(0).isChecked());
    }

    public void setBuisnessSwitch()
    {
        swtch_buis_sign_up_terms_conds.setChecked(true);
    }


    public void setIndividualAwitch()
    {
        swtch_ind_sign_up_terms_conds.setChecked(true);
    }
    @Override
    public void onReferralEntered()
    {
        til_sign_up_user_details.get(4).setErrorEnabled(false);
    }

    @Override
    public void onReferralEmpty()
    {
        til_sign_up_user_details.get(4).setErrorEnabled(false);
    }

    @Override
    public void onGettingOfCountryInfo(int countryFlag, String countryCode, int phoneMaxLength
            ,boolean isDefault)
    {
        if(mCountryPicker.isVisible())
            mCountryPicker.dismiss();
        tv_sign_up_code.setText(countryCode);
        iv_sign_up_flag.setImageResource(countryFlag);
        //et_sign_up_user_details.get(2).setFilters(Utility.getInputFilterForPhoneNo(phoneMaxLength));
        if(!isDefault)
            mSignUpPresenter.validateMobileField(et_sign_up_user_details.get(2).getText().toString(),
                    tv_sign_up_code.getText().toString(),false);
    }

    @Override
    public void captureImage()
    {
        mImageOperation.takePicFromCamera(file -> newFile = file);
    }

    @Override
    public void removeImageCaptured() {
        mSignUpPresenter.storeProfilePicUrl("");
        iv_sign_up_pic.setImageResource(R.drawable.signup_profile_default_image);
    }

    @Override
    public void onSuccessOfImageUpload(String imageUrl)
    {
        mSignUpPresenter.storeIfImageUploaded(true);
        mSignUpPresenter.storeProfilePicUrl(imageUrl);
    }

    @Override
    public void onFailureOfImageUpload() {
        mSignUpPresenter.storeIfProfilePicSelected(false);
        mSignUpPresenter.storeIfImageUploaded(false);
        Toast.makeText(SignUpActivity.this,failImageUpload,Toast.LENGTH_LONG).show();
        iv_sign_up_pic.setImageResource(R.drawable.signup_profile_default_image);
    }

    @Override
    public void onGettingOfCompanyAddress(String address) {
        mSignUpPresenter.storeCompanyAddress( address);
        et_sign_up_user_details.get(6).setText(mSignUpPresenter.fetchCompanyAddress());
        mSignUpPresenter.validateCompanyAddress(mSignUpPresenter.fetchCompanyAddress());
    }


    @Override
    public void openWebView(String webLink,String title)
    {
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra(WEB_LINK, webLink);
        intent.putExtra(SCREEN_TITLE, title);
        intent.putExtra(COMING_FROM, "web");
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    public void setSpannableString(SpannableString spannableString) {
        tv_sign_up_terms_conds.setText(spannableString);
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
