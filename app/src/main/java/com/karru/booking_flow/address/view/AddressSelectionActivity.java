package com.karru.booking_flow.address.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.karru.authentication.signup.SignUpActivity;
import com.karru.booking_flow.address.AddressSelectContract;
import com.karru.booking_flow.address.model.AddressDataModel;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.booking_flow.address.model.PlaceAutoCompleteModel;
import com.karru.booking_flow.location_from_map.LocationFromMapActivity;
import com.karru.landing.MainActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DROP_ADDRESS_REQUEST;
import static com.karru.utility.Constants.DROP_LAT;
import static com.karru.utility.Constants.DROP_LONG;
import static com.karru.utility.Constants.PICK_ID;
import static com.karru.utility.Constants.RECENT_TYPE_LIST;

/**
 * <h1>AddDropLocation Activity</h1>
 * This class is used to provide the AddDropLocation screen, where we can search or select our address.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class AddressSelectionActivity extends DaggerAppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks , AddressSelectContract.View,TextWatcher
{
    private String TAG = "AddressSelectionActivity";

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private FavAddressAdapter favAddressAdapter;
    private DropAddressAdapter dropAddressAdapter;
    private String pickLtrTime, comingFrom;
    private ArrayList<String> nearestDriversList;
    private int keyId;
    private String flag = "";

    private String name, phone, email, picture, password, company_name, referralCode;
    private int login_type = 1;
    private boolean isItBusinessAccount = true;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject AddressSelectContract.Presenter presenter;

    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    @BindView(R.id.et_select_address_search) EditText et_select_address_search;
    @BindView(R.id.iv_select_address_clear) ImageView iv_select_address_clear;
    @BindView(R.id.tv_select_address_map) TextView tv_select_address_map;
    @BindView(R.id.tv_select_address_recent_title) TextView tv_select_address_recent_title;
    @BindView(R.id.cv_select_address_recent) CardView cv_select_address_recent;
    @BindView(R.id.cv_address_special) CardView cv_address_special;
    @BindView(R.id.cv_select_address_fav) CardView cv_select_address_fav;
    @BindView(R.id.tv_select_address_fav_title) TextView tv_select_address_fav_title;
    @BindView(R.id.tv_address_special_title) TextView tv_address_special_title;
    @BindView(R.id.rv_select_address_fav_list) RecyclerView rv_select_address_fav_list;
    @BindView(R.id.rv_select_address_recent_list) RecyclerView rv_select_address_recent_list;
    @BindView(R.id.rv_address_special_list) RecyclerView rv_address_special_list;
    @BindView(R.id.iv_back_button) ImageView iv_select_address_back;
    @BindView(R.id.iv_select_address_type) ImageView iv_select_address_type;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_select_address_title;
    @BindString(R.string.pleaseWait) String pleaseWait;
    @BindString(R.string.add_drop_note) String add_drop_note;
    @BindString(R.string.company_address) String company_address;
    @BindString(R.string.add_pick_note) String add_pick_note;

    private ProgressDialog pDialog;
    private Bundle mBundle;
    private String key;
    private ArrayList<AddressDataModel> recentAddressList = new ArrayList<>();
    private ArrayList<AddressDataModel> specialAddressList = new ArrayList<>();
    private ArrayList <FavAddressDataModel> favAddressList = new ArrayList<>();
    private boolean isIndividual,isBuisness;
    private String constraint;

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500))
            {
                if(iv_select_address_clear.getVisibility() != View.VISIBLE)
                    iv_select_address_clear.setVisibility(View.VISIBLE);
               // presenter.checkForLoginType(constraint);
                presenter.toggleFavAddressField(false);
                filterAddress();
            }
        }
    };

    @Override
    public void filterAddress()
    {
        if (!constraint.equals("") && mGoogleApiClient.isConnected()){
           // presenter.checkForLoginType(constraint);
            placeAutoCompleteAdapter.getFilter().filter(constraint);
           //
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        setTypeface();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        //to get all the data from mBundle from previous activity
        mBundle = getIntent().getExtras();
        if(mBundle !=null)
        {
            comingFrom=  mBundle.getString("comingFrom");
            keyId=  mBundle.getInt("keyId");
            pickLtrTime= mBundle.getString("pickltrtime");
            nearestDriversList =   mBundle.getStringArrayList("NearestDriverstoSend");
            if (mBundle.getString("ANimation") != null)
                flag = mBundle.getString("ANimation");
            key = mBundle.getString("key");
            Utility.printLog(TAG+" flag in address adctivity "+flag);

            if(keyId == PICK_ID)
                tv_select_address_map.setVisibility(View.GONE);
        }

        if (comingFrom != null && comingFrom.equals("signup"))
        {
            login_type = getIntent().getIntExtra("login_type", 1);
            isItBusinessAccount = getIntent().getBooleanExtra("is_business_Account", true);
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            referralCode = getIntent().getStringExtra("referral_code");
            company_name = getIntent().getStringExtra("company_name");
            picture = getIntent().getStringExtra("picture");
            isIndividual=getIntent().getBooleanExtra("is_Individual_checked",false);
            isBuisness=getIntent().getBooleanExtra("is_buisness_checked",false);
        }
        initViews();
    }

    /**
     * <h2>setTypeface</h2>
     * This method is used to set the type face for the views
     */
    private void setTypeface()
    {
        et_select_address_search.setTypeface(appTypeface.getPro_News());
        tv_select_address_title.setTypeface(appTypeface.getPro_narMedium());
        tv_select_address_map.setTypeface(appTypeface.getPro_News());
        tv_select_address_recent_title.setTypeface(appTypeface.getPro_News());
        tv_select_address_fav_title.setTypeface(appTypeface.getPro_News());
        tv_address_special_title.setTypeface(appTypeface.getPro_News());
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * <h>initViews</h>
     * <p>
     * This method initialize the all UI elements of our layout.
     * </p>
     */
    private void initViews()
    {
        presenter.initializeVariables(comingFrom);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_select_address_recent_list.setHasFixedSize(true);
        rv_select_address_recent_list.setLayoutManager(linearLayoutManager);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this,this,presenter);

        et_select_address_search.addTextChangedListener(this);

        rv_select_address_fav_list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_select_address_fav_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_address_special_list.setLayoutManager(linearLayoutManager);

        presenter.toggleFavAddressField(true);
        pDialog = alerts.getProcessDialog(this);
        pDialog.setCancelable(false);

        presenter.checkForLoginType("0");
    }

    @Override
    public void beforeTextChanged (CharSequence s,int start, int count, int after){
    }

    @Override
    public void onTextChanged ( final CharSequence s, int start, int before,
                                int count){
        //You need to remove this to run only once
        handler.removeCallbacks(input_finish_checker);

    }
    @Override
    public void afterTextChanged ( final Editable s){
        //avoid triggering event when text is empty
        if (s.length() > 0)
        {
            constraint = s.toString();
            last_text_edit = System.currentTimeMillis();
            handler.postDelayed(input_finish_checker, delay);
        }
        else {
            presenter.checkForLoginType("0");
            presenter.toggleFavAddressField(true);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onPlaceClick(final ArrayList<PlaceAutoCompleteModel> mAddressList, final int position)
    {
        presenter.handleAutoSuggestAddressClick(mAddressList, position ,recentAddressList,
                mBundle.getInt("keyId"),mBundle.getString("key"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        presenter.checkRTLConversion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }

    @Override
    public void onBackPressed()
    {
        Utility.printLog("value of flag: "+flag);
        InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (flag.equals("signup"))
        {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra("drop_addr", "");//Last these 6 data, we used only for SIGN UP ACTIVITY.
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            intent.putExtra("picture",picture);
            intent.putExtra("company_name",company_name);
            intent.putExtra("login_type",login_type);
            intent.putExtra("is_Individual_checked",isIndividual);
            intent.putExtra("is_buisness_checked",isBuisness);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if (flag.equals("ANimation"))
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void showProgressDialog() {
        pDialog.setMessage(pleaseWait);
        pDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        pDialog.dismiss();
    }

    @Override
    public void deleteAddressNotify(int position) {
        favAddressList.remove(position);
        if(favAddressList.isEmpty())
            tv_select_address_fav_title.setVisibility(View.GONE);
        favAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void openShipmentScreen(String latitude , String longitude ,String address)
    {
        //to open shipment activity
    }

    @Override
    public void notifyPickAddressChangeUI(String latitude , String longitude ,String address)
    {
        Intent shipmentIntent = new Intent(this, MainActivity.class);
        shipmentIntent.putExtra(DROP_LAT, latitude);
        shipmentIntent.putExtra(DROP_LONG, longitude);
        shipmentIntent.putExtra(DROP_ADDRESS, address);
        setResult(RESULT_OK, shipmentIntent);
        finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }

    @Override
    public void notifySignUpBusinessAddressUI(String latitude, String longitude, String address)
    {
        Intent intentReturn = getIntent();
        intentReturn.putExtra(DROP_LAT,  latitude);
        intentReturn.putExtra(DROP_LONG,  longitude);
        intentReturn.putExtra(DROP_ADDRESS, address);
        setResult(RESULT_OK, intentReturn);
        finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }

    @Override
    public void notifyLaterBookingClick()
    {
        //to open shipment activity
    }

    @OnClick ({R.id.tv_select_address_map, R.id.iv_select_address_clear,R.id.iv_back_button})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_select_address_map:
                Intent shipmentIntent = new Intent(AddressSelectionActivity.this, LocationFromMapActivity.class);
                shipmentIntent.putExtra("pickltrtime", pickLtrTime);
                shipmentIntent.putExtra("NearestDriverstoSend", nearestDriversList);
                shipmentIntent.putExtra("ANimation",flag);
                shipmentIntent.putExtra("comingFrom", comingFrom);
                shipmentIntent.putExtra("name",name);
                shipmentIntent.putExtra("phone",phone);
                shipmentIntent.putExtra("email",email);
                shipmentIntent.putExtra("password",password);
                shipmentIntent.putExtra("picture",picture);
                shipmentIntent.putExtra("company_name",company_name);
                shipmentIntent.putExtra("login_type",login_type);
                shipmentIntent.putExtra("referral_code", referralCode);
                shipmentIntent.putExtra("is_Individual_checked",isIndividual);
                shipmentIntent.putExtra("is_buisness_checked",isBuisness);
                shipmentIntent.putExtra("is_business_Account",isItBusinessAccount);
                startActivityForResult(shipmentIntent,DROP_ADDRESS_REQUEST);
                break;

            case R.id.iv_select_address_clear:
                et_select_address_search.setText("");
                presenter.toggleFavAddressField(true);
                break;

            case R.id.iv_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    public void setTitleForPickAddress() {
        tv_select_address_title.setText(add_pick_note);
        iv_select_address_type.setImageDrawable(getResources().getDrawable(R.drawable.shape_box_square_green));
    }

    @Override
    public void setTitleForDropAddress() {
        iv_select_address_type.setImageDrawable(getResources().getDrawable(R.drawable.shape_box_square_red));
        tv_select_address_title.setText(add_drop_note);
    }

    @Override
    public void setTitleForBusinessAddress() {
        tv_select_address_title.setText(company_address);
    }

    @Override
    public void onAddressItemViewClicked(View view, int position, int listType)
    {
        switch (view.getId())
        {
            case R.id.iv_select_address_right_icon:
                Utility.printLog(TAG+ "onRVItemViewClicked() "+ " "+favAddressList.get(position).getAddressId());
                presenter.deleteFavAddressAPI(favAddressList.get(position).getAddressId(), position);
                break;

            case R.id.rl_select_address_layout:
                presenter.handleClickOfAddress(listType,recentAddressList,favAddressList,specialAddressList,position,keyId,key);
                break;

            default:
                break;
        }
    }

    @Override
    public void replaceDataNotifyAdapter(ArrayList<AddressDataModel> dropAddressList,
                                         ArrayList<FavAddressDataModel> favAddressList,
                                         ArrayList <AddressDataModel> specialAddressDataModels)
    {
        this.recentAddressList = dropAddressList;
        this.favAddressList = favAddressList;
        this.specialAddressList = specialAddressDataModels;

        dropAddressAdapter = new DropAddressAdapter(this, recentAddressList, this,
                appTypeface);
        rv_select_address_recent_list.setAdapter(dropAddressAdapter);
        DropAddressAdapter specialAddressAdapter = new DropAddressAdapter(this, specialAddressList, this,
                appTypeface);
        rv_address_special_list.setAdapter(specialAddressAdapter);
        favAddressAdapter = new FavAddressAdapter(this,favAddressList ,this,
                appTypeface);
        rv_select_address_fav_list.setAdapter(favAddressAdapter);
    }

    @Override
    public void showFavAddressListUI() {
        if(favAddressList.size() > 0)
            cv_select_address_fav.setVisibility(View.VISIBLE);
        else if(cv_select_address_fav.getVisibility() == View.VISIBLE)
            cv_select_address_fav.setVisibility(View.GONE);

        if(recentAddressList.size() >0)
            tv_select_address_recent_title.setVisibility(View.VISIBLE);
        else if(cv_select_address_recent.getVisibility() == View.VISIBLE)
            cv_select_address_recent.setVisibility(View.GONE);
        else if(cv_address_special.getVisibility() == View.VISIBLE)
            cv_address_special.setVisibility(View.GONE);
        iv_select_address_clear.setVisibility(View.GONE);
        rv_select_address_recent_list.setAdapter(dropAddressAdapter);
    }

    @Override
    public void hideSpecialAddress() {
        cv_address_special.setVisibility(View.GONE);
    }

    @Override
    public void hideFavAddressListUI(int visibility) {
        cv_select_address_recent.setVisibility(View.VISIBLE);
        cv_address_special.setVisibility(visibility);
        tv_select_address_recent_title.setVisibility(View.GONE);
        cv_select_address_fav.setVisibility(View.GONE);
        iv_select_address_clear.setVisibility(View.VISIBLE);
        rv_select_address_recent_list.setAdapter(placeAutoCompleteAdapter);
    }

    @Override
    public void hideKeyboard()
    {
        InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleResultData(requestCode,resultCode);
    }

    @Override
    public void sendAddressBack(Bundle bundle) {
        Intent intentReturn = getIntent();
        intentReturn.putExtras(bundle);
        setResult(RESULT_OK, intentReturn);
        finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }
}
