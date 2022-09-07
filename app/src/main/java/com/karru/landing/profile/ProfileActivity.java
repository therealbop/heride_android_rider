package com.karru.landing.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.karru.authentication.change_password.ChangePasswordActivity;
import com.karru.landing.profile.edit_email.EditEmailActivity;
import com.karru.landing.profile.edit_name.EditNameActivity;
import com.karru.landing.profile.edit_phone_number.EditPhoneNumberActivity;
import com.karru.splash.first.LanguagesList;
import com.karru.splash.first.SplashActivity;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.utility.HandlePictureEvents;
import com.karru.utility.Utility;
import com.heride.rider.R;
import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;

import static com.karru.utility.Constants.CAMERA_PIC;
import static com.karru.utility.Constants.CROP_IMAGE;
import static com.karru.utility.Constants.GALLERY_PIC;
import static com.karru.utility.Constants.LANGUAGE;
import static com.karru.utility.Constants.OREO_PERMISSION;
import static com.karru.utility.Constants.PERMISSION_BLOCKED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PERMISSION_REQUEST;
import static com.karru.utility.Constants.PROFILE_CODE;

/**
 * <h1>Profile Screen</h1>
 * This class is used to provide the Profile screen, where we can show the user pictures and their information details.
 */
public class ProfileActivity extends DaggerAppCompatActivity implements ProfileContract.ProfileView
{
    private static final String TAG = "ProfileActivity";
    @BindView(R.id.iv_profile_profilePic) ImageView iv_profile_profilePic;
    @BindView(R.id.tiet_profile_name) TextInputEditText tiet_profile_name;
    @BindView(R.id.tiet_profile_phoneNo) TextInputEditText tiet_profile_phoneNo;
    @BindView(R.id.tiet_profile_email) TextInputEditText tiet_profile_email;
    @BindView(R.id.tiet_profile_password) TextInputEditText tiet_profile_password;
    @BindView(R.id.tv_profile_logout) TextView tv_profile_logout;
    @BindView(R.id.tv_landing_language_title) TextView tv_landing_language_title;
    @BindView(R.id.tv_profile_tbTitle) TextView tv_profile_tbTitle;
    @BindView(R.id.pBar_profileFrag) ProgressBar pBar_profileFrag;
    @BindView(R.id.tv_landing_languages) AppCompatTextView tv_landing_languages;
    @BindView(R.id.rl_hotel_field_layout) RelativeLayout rl_hotel_field_layout;
    @BindView(R.id.tv_profile_hotelName) TextInputEditText tv_profile_hotelName;
    @BindView(R.id.viewHotelDivider) View viewHotelDivider;

    @BindString(R.string.wait) String wait;
    @BindString(R.string.my_profile) String my_profile;
    @BindString(R.string.confirmLogout) String confirmPayment;
    @BindString(R.string.logout) String logout;
    @BindString(R.string.logout_msg) String logout_msg;
    @BindString(R.string.action_cancel) String action_cancel;
    @BindString(R.string.failImageUpload) String failImageUpload;
    @BindString(R.string.permission_alert_camera) String permission_alert_camera;
    @BindString(R.string.permission_alert_gallery) String permission_alert_gallery;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject ProfileContract.Presenter presenter;
    @Inject Context mContext;
    @Inject com.karru.util.Utility utility;
    @Inject AppPermissionsRunTime permissionsRunTime;
    @Inject @Named(LANGUAGE)
    ArrayList<LanguagesList> languagesLists = new ArrayList<>();

    private ProgressDialog pDialog;
    private HandlePictureEvents handlePicEvent;
    private int imageType = CAMERA_PIC;
    private Uri galleryImage;
    private RequestOptions requestOptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initializeProgressDialog();
        initializeViews();
        handlePicEvent = new HandlePictureEvents(this, null);
        updateUI();
    }



    /**
     * <h2>initializeProgressDialog</h2>
     * used to initialize the progress dialog
     */
    private void initializeProgressDialog()
    {
        pDialog = alerts.getProcessDialog(this);
        pDialog.setCancelable(false);
    }

    @Override
    public void dismissProgressDialog()
    {
        if(pDialog!=null && pDialog.isShowing() && !isFinishing())
            pDialog.dismiss();
    }

    @Override
    public void showProgressDialog()
    {
        pDialog.setMessage(wait);
        if(pDialog!=null && !pDialog.isShowing() && !isFinishing())
            pDialog.show();
    }

    /**
     * <h2>initViews</h2>
     * <p>Initializing profileView elements</p>
     */
    private void initializeViews()
    {
        ButterKnife.bind(this);
        tv_profile_tbTitle.setText(my_profile);
        tv_profile_tbTitle.setTypeface(appTypeface.getPro_narMedium());
        tiet_profile_name.setTypeface(appTypeface.getPro_News());
        tiet_profile_phoneNo.setTypeface(appTypeface.getPro_News());
        tiet_profile_email.setTypeface(appTypeface.getPro_News());
        tiet_profile_password.setTypeface(appTypeface.getPro_News());
        tv_landing_language_title.setTypeface(appTypeface.getPro_News());
        tv_profile_logout.setTypeface(appTypeface.getPro_News());
        tv_landing_languages.setTypeface(appTypeface.getPro_News());
        requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(signup_profile_default_image);
        requestOptions = requestOptions.optionalCircleCrop();
    }

    @OnClick({R.id.iv_profile_menuBtn, R.id.iv_profile_editName, R.id.iv_profile_password, R.id.iv_profile_email,
            R.id.iv_profile_phoneNo, R.id.iv_profile_profilePic,R.id.pBar_profileFrag,R.id.iv_profile_addIcon,
            R.id.tv_profile_logout,R.id.rl_landing_languages})
    public void clickEvent(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_profile_menuBtn:
                onBackPressed();
                break;

            case R.id.iv_profile_editName:
                validateRespectiveField(1);
                break;

            case R.id.iv_profile_password:
                validateRespectiveField(2);
                break;

            case R.id.iv_profile_email:
                validateRespectiveField(3);
                break;

            case R.id.iv_profile_phoneNo:
                validateRespectiveField(4);
                break;

            case R.id.iv_profile_profilePic:
            case R.id.pBar_profileFrag:
            case R.id.iv_profile_addIcon:
                if (permissionsRunTime.checkIfPermissionNeeded())
                {
                    if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.CAMERA,this)
                            && permissionsRunTime.checkIfPermissionGrant(Manifest.permission.READ_EXTERNAL_STORAGE,this))
                        selectImage();
                    else
                    {
                        String[] strings = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                        permissionsRunTime.requestForPermission(strings,this,PERMISSION_REQUEST);
                    }
                }
                else
                    selectImage();
                break;

            case R.id.tv_profile_logout:
                logoutConfirmationAlert();
                break;

            case R.id.rl_landing_languages:
                presenter.getLanguages();
                break;
        }
    }

    /**
     * <h2>checkCurrentPassword</h2>
     * <p> This method is used to open a dialog for asking the current etNewPassword and then will start operating based on our requirements like, changing name, etNewPassword, phone or email. </p>
     */
    private void checkCurrentPassword()
    {
        Intent intent=new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("comingFrom","Profile");
        startActivityForResult(intent,PROFILE_CODE);
    }

    /**
     * <h2>validateRespectiveField</h2>
     * <p> This method is used to open a dialog for asking the current etNewPassword and then will start operating based on our requirements like, changing name, etNewPassword, phone or email. </p>
     * @param flag, contains the flag, for checking. 1->Name, 2->Password, 3-> Email, 4-> Phone.
     */
    private void validateRespectiveField(final int flag) {
        Intent intent;
        switch (flag) {
            case 1:
                intent = new Intent(this, EditNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ent_name", tiet_profile_name.getText().toString());
                bundle.putString("ent_password", tiet_profile_password.getText().toString());
                bundle.putString("ent_email", tiet_profile_email.getText().toString());
                bundle.putString("ent_mobile", tiet_profile_phoneNo.getText().toString());
                bundle.putString("ent_profile", presenter.getImageUrl());
                intent.putExtras(bundle);
                startActivityForResult(intent,PROFILE_CODE);
                break;

            case 2:
                checkCurrentPassword();
                break;

            case 3:
                intent = new Intent(this, EditEmailActivity.class);
                startActivityForResult(intent,PROFILE_CODE);
                break;

            case 4:
                intent = new Intent(this, EditPhoneNumberActivity.class);
                startActivityForResult(intent,PROFILE_CODE);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }

    /**
     * This method got called, once we give any permission to our required permission.
     *
     * @param requestCode  contains request code.
     * @param permissions  contains Permission list.
     * @param grantResults contains the grant permission result.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case OREO_PERMISSION:
                handlePicEvent.checkForVersion(imageType,galleryImage);
                break;

            case PERMISSION_REQUEST:
                int status =  permissionsRunTime.getPermissionStatus(this,
                        Manifest.permission.CAMERA, false);
                switch (status)
                {
                    case PERMISSION_GRANTED:
                        int statusGallery =  permissionsRunTime.getPermissionStatus(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE, false);
                        switch (statusGallery)
                        {
                            case PERMISSION_GRANTED:
                                selectImage();
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

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    /**
     * <h1>selectImage</h1>
     *
     * @see HandlePictureEvents
     * <p>
     * This mehtod is used to show the popup where we can select our images.
     * </p>
     */
    public void selectImage()
    {
        handlePicEvent.openDialog();
    }

    /**
     * This is an overrided method, got a call, when an activity opens by StartActivityForResult(), and return something back to its calling activity.
     *
     * @param requestCode returning the request code.
     * @param resultCode  returning the result code.
     * @param data        contains the actual data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)    //result code to check is the result is ok or not
        {
            return;
        }
        switch (requestCode)
        {
            case CAMERA_PIC:
                handlePicEvent.startCropImage(handlePicEvent.newFile);
                break;

            case GALLERY_PIC:
                Utility.printLog(TAG+ "onActivityResult: " + data);
                if (data != null)
                {
                    imageType = GALLERY_PIC;
                    galleryImage = data.getData();
                    handlePicEvent.gallery(data.getData());
                }
                break;

            case CROP_IMAGE:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                iv_profile_profilePic.setImageBitmap(utility.cropImageCircle(this,path));
                if (path != null)
                {
                    try {
                        pBar_profileFrag.setVisibility(View.VISIBLE);
                        presenter.uploadToAmazon(new File(path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PROFILE_CODE:
                updateUI();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    public void updateUI()
    {
        if(presenter.getIsHotelBooking() != 0)
        {
            rl_hotel_field_layout.setVisibility(View.VISIBLE);
            tv_profile_hotelName.setText(presenter.getHotelName());
            viewHotelDivider.setVisibility(View.VISIBLE);
        }
        else {
            rl_hotel_field_layout.setVisibility(View.GONE);
            viewHotelDivider.setVisibility(View.GONE);
        }
        String img_url = presenter.getImageUrl();
        tiet_profile_name.setText(presenter.getUserName());
        Utility.printLog("USEREMAIL :- " + presenter.getUserEmail() );
        tiet_profile_email.setText(presenter.getUserEmail());
        tiet_profile_phoneNo.setText(presenter.getMobileNumber());
        tiet_profile_password.setText(presenter.getPassword());
        Utility.printLog("value of image url: *" + img_url + "*");
        if (img_url != null && !img_url.isEmpty() && !img_url.equals(" "))
        {
            Glide.with(mContext).load(img_url)
                    .listener(new RequestListener<Drawable>()
                    {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pBar_profileFrag.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pBar_profileFrag.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(iv_profile_profilePic);

        }
        else
            pBar_profileFrag.setVisibility(View.GONE);
    }

    /**
     * <h2>logoutConfirmationAlert</h2>
     * <p>
     * method to show an alert for logout confirmation
     * </p>
     */
    public void logoutConfirmationAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(confirmPayment);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(logout_msg);

        alertDialog.setPositiveButton(logout, (dialog, which) ->
        {
            dialog.dismiss();
            presenter.logoutAPI();
        });
        alertDialog.setNegativeButton(action_cancel, (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void showLanguagesDialog(int indexSelected)
    {
        alerts.showLanguagesAlert(this,languagesLists,null,presenter,indexSelected);
    }

    @Override
    public void setLanguage(String language,boolean restart)
    {
        tv_landing_languages.setText(language);
        if(restart)
        {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }

    @Override
    public void onAPICallError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageUploadSuccess(String imageUrl)
    {
        Utility.printLog(TAG+ " showToast: " + imageUrl);
        presenter.setImageUrl(imageUrl);
        Glide.with(mContext).load(imageUrl)
                .apply(requestOptions)
                .into(iv_profile_profilePic);
        pBar_profileFrag.setVisibility(View.GONE);
        presenter.updateProfilePic(imageUrl);
    }

    @Override
    public void onImageUploadFailure()
    {
        Toast.makeText(mContext,failImageUpload,Toast.LENGTH_LONG).show();
        pBar_profileFrag.setVisibility(View.GONE);
    }
}