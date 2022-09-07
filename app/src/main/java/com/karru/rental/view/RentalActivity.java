package com.karru.rental.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.booking_flow.ride.request.RequestingActivity;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileAccountActivity;
import com.karru.landing.home.model.VehicleTypesDetails;
import com.karru.landing.home.promo_code.PromoCodeActivity;
import com.karru.landing.home.view.CorporateAccountsDialog;
import com.karru.landing.home.view.DriverPreferenceBottomSheet;
import com.karru.landing.home.view.FareBreakdownDialog;
import com.karru.landing.home.view.OutstandingBalanceBottomSheet;
import com.karru.landing.home.view.PaymentOptionsBottomSheet;
import com.karru.landing.home.view.RideRateCardDialog;
import com.karru.landing.home.view.WalletAlertBottomSheet;
import com.karru.landing.home.view.WalletHardLimitAlert;
import com.karru.landing.payment.PaymentActivity;
import com.karru.landing.wallet.WalletActivity;
import com.karru.rental.RentCarContract;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.Utility;
import com.heride.rider.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static android.view.View.VISIBLE;
import static com.karru.utility.Constants.CHANGE_CARD_REQUEST;
import static com.karru.utility.Constants.IS_FROM_BOOKING;
import static com.karru.utility.Constants.PROMO_CODE_REQUEST;
import static com.karru.utility.Constants.REQUESTING_BUSY_DRIVERS;
import static com.karru.utility.Constants.REQUESTING_SCREEN;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

/**
 * <h1>RentalActivity</h1>
 * @author Anurag
 */
public class RentalActivity extends DaggerAppCompatActivity implements RentCarContract.View{

    private static final String TAG = "RentalActivity";
    @BindView(R.id.tv_all_tool_bar_title) TextView tvAllToolBarTitle;
    @BindView(R.id.iv_back_button) ImageView ivBackButton;
    @BindView(R.id.rl_back_button) RelativeLayout rlBackButton;
    @BindView(R.id.tv_all_tool_bar_title2) TextView tvAllToolBarTitle2;
    @BindView(R.id.tvToolBarEnd) TextView tvToolBarEnd;
    @BindView(R.id.rlToolBarEnd) RelativeLayout rlToolBarEnd;
    @BindView(R.id.tv_rent_car_pickup_location) TextView tv_rent_car_pickup_location;
    @BindView(R.id.tv_rent_car_pickup_label) TextView tv_rent_car_pickup_label;
    @BindView(R.id.ll_rent_car_location) LinearLayout llRentCarLocation;
    @BindView(R.id.ll_divider) LinearLayout llDivider;
    @BindView(R.id.tv_rent_car_pickup_package) TextView tvRentCarPickupPackage;
    @BindView(R.id.rl_rent_car_pickup_package) RelativeLayout rlRentCarPickupPackage;
    @BindView(R.id.tv_rent_car_pickup_package_selected) TextView tvRentCarPickupPackageSelected;
    @BindView(R.id.iv_rent_car_selector) ImageView ivRentCarSelector;
    @BindView(R.id.rl_rent_car_package) RelativeLayout rlRentCarPackage;
    @BindView(R.id.rl_rent_car_fare_details_heading) RelativeLayout rlRentCarFareDetailsHeading;
    @BindView(R.id.ll_divider2) LinearLayout llDivider2;
    @BindView(R.id.tv_rent_car_pickup_type) TextView tvRentCarPickupType;
    @BindView(R.id.rl_rent_car_pickup_type) RelativeLayout rlRentCarPickupType;
    @BindView(R.id.ll_rent_car_pickup_type) LinearLayout llRentCarPickupType;
    @BindView(R.id.tv_rent_car_pickup_type_selected) TextView tvRentCarPickupTypeSelected;
    @BindView(R.id.tv_rent_car_pickup_type_minutes_away) TextView tvRentCarPickupTypeMinutesAway;
    @BindView(R.id.iv_rent_car_selector2) ImageView ivRentCarSelector2;
    @BindView(R.id.iv_rent_car_pickup_type_selected) ImageView ivRentCarPickupTypeSelected;
    @BindView(R.id.tv_rent_car_pickup_type_cost) TextView tvRentCarPickupTypeCost;
    @BindView(R.id.tv_rent_car_pickup_type_cost_symbol) TextView tvRentCarPickupTypeCostSymbol;
    @BindView(R.id.tv_rent_car_rules) TextView tv_rent_car_rules;
    @BindView(R.id.ll_rent_car_type) LinearLayout llRentCarType;
    @BindView(R.id.ll_divider3) LinearLayout llDivider3;
    @BindView(R.id.ll_home_confirmation_fare) LinearLayout ll_home_confirmation_fare;
    @BindView(R.id.iv_rent_car_about) ImageView ivRentCarAbout;
    @BindView(R.id.tv_rent_car_about) TextView tv_rent_car_about;
    @BindView(R.id.tv_rent_car_about_details) TextView tv_rent_car_about_details;
    @BindView(R.id.ll_rent_car_about) LinearLayout llRentCarAbout;
    @BindView(R.id.ll_divider5) LinearLayout llDivider5;
    @BindView(R.id.rv_rental_packages) RecyclerView rv_rental_packages;
    @BindView(R.id.rv_rental_vehicles) RecyclerView rv_rental_vehicles;
    @BindView(R.id.rv_rental_rules) RecyclerView rv_rental_rules;
    @BindView(R.id.view_car_selector) View viewCarSelector;
    @BindView(R.id.view_rent_car_package) View viewRentCarPackage;
    @BindView(R.id.view_rules) View viewRules;
    @BindView(R.id.tv_home_fare_price) TextView tvHomeFarePrice;
    @BindView(R.id.tv_home_fare_info) ImageView tvHomeFareInfo;
    @BindView(R.id.tv_home_fare_title) TextView tvHomeFareTitle;
    @BindView(R.id.vw_home_confirmation_divider) View vwHomeConfirmationDivider;
    @BindView(R.id.tv_home_time_text) AppCompatTextView tvHomeTimeText;
    @BindView(R.id.tv_home_time_value) TextView tvHomeTimeValue;
    @BindView(R.id.tv_home_divider2) View tvHomeDivider2;
    @BindView(R.id.ll_home_bottom_later) LinearLayout llHomeBottomLater;
    @BindView(R.id.tv_home_select_promo) TextView tv_home_select_promo;
    @BindView(R.id.tv_home_divide2) View tvHomeDivide2;
    @BindView(R.id.tv_home_account_type) AppCompatTextView tv_home_account_type;
    @BindView(R.id.vw_home_account_divider) View vwHomeAccountDivider;
    @BindView(R.id.tv_home_select_payment) TextView tv_home_select_payment;
    @BindView(R.id.ll_home_bottom_payment) LinearLayout llHomeBottomPayment;
    @BindView(R.id.tv_home_divider) View tvHomeDivider;
    @BindView(R.id.tv_home_select_driver_pref) AppCompatTextView tv_home_select_driver_pref;
    @BindView(R.id.vw_pref_divider) View vwPrefDivider;
    @BindView(R.id.iv_home_select_icon) ImageView ivHomeSelectIcon;
    @BindView(R.id.iv_home_select_arrow) ImageView ivHomeSelectArrow;
    @BindView(R.id.rl_home_seats) RelativeLayout rl_home_seats;
    @BindView(R.id.iv_home_vehicle_icon) ImageView ivHomeVehicleIcon;
    @BindView(R.id.tv_home_vehicle) AppCompatTextView tvHomeVehicle;
    @BindView(R.id.iv_home_vehicle_arrow) ImageView ivHomeVehicleArrow;
    @BindView(R.id.rl_home_vehicles) RelativeLayout rl_home_vehicles;
    @BindView(R.id.rl_home_pref_seats) RelativeLayout rlHomePrefSeats;
    @BindView(R.id.ll_home_bottom_preference) LinearLayout llHomeBottomPreference;
    @BindView(R.id.tv_home_ride_advance_fee) TextView tvHomeRideAdvanceFee;
    @BindView(R.id.tv_home_ride_request) TextView tv_home_ride_request;
    @BindView(R.id.rl_rent_car_include_layout) RelativeLayout rlRentCarIncludeLayout;
    @BindView(R.id.spnr_home_select_capacity) Spinner spnr_rental_select_capacity;
    @BindView(R.id.iv_rent_car_rules) ImageView iv_rent_car_rules;
    @BindString(R.string.rent_a_car) String rent_a_car;
    @BindString(R.string.select_package) String select_package;
    @BindString(R.string.choose_cab_type) String choose_cab_type;
    @BindString(R.string.card_ending_with) String card_ending_with;
    @BindString(R.string.wallet_1) String wallet_1;
    @BindDrawable(R.drawable.ic_invoice_one_tick_mark_on) Drawable ic_invoice_one_tick_mark_on;
    @BindDrawable(R.drawable.ic_payment_wallet_icon_selector) Drawable ic_payment_wallet_icon;
    @BindString(R.string.cash) String cash;
    @BindDrawable(R.drawable.ic_payment_cash_icon_selector) Drawable ic_payment_cash_icon;
    @BindColor(R.color.search_color) int search_color;
    @BindString(R.string.select_payment_option) String select_payment_option;
    @BindDrawable(R.drawable.confirmation_payment_icon) Drawable confirmation_payment_icon;
    @BindDrawable(R.drawable.ic_warning_black_24dp_selector) Drawable ic_warning_black_24dp;
    @BindDrawable(R.drawable.selector_layout) Drawable selector_layout;
    @BindString(R.string.add_card_booking) String add_card_booking;
    @BindString(R.string.seat_single) String seat_single;
    @BindString(R.string.seat_multiple) String seat_multiple;
    @BindColor(R.color.vehicle_unselect_color) int vehicle_unselect_color;
    @BindDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp_selector) Drawable ic_arrow_drop_down_black_24dp;
    @BindString(R.string.driver_preference) String driver_preference;
    @BindString(R.string.services_set) String services_set;
    @BindString(R.string.driver_preference_set) String driver_preference_set;
    @BindString(R.string.services) String services;

    @Inject AppTypeface appTypeface;
    @Inject Alerts alerts;
    @Inject RentCarContract.Presenter presenter;
    @Inject Utility utility;
    @Inject RentalPackageAdapter rentalPackageAdapter;
    @Inject RentalVehicleTypesAdapter rentalVehicleTypesAdapter;
    @Inject PaymentOptionsBottomSheet paymentOptionsBottomSheet;
    @Inject WalletHardLimitAlert walletHardLimitAlert;
    @Inject WalletAlertBottomSheet walletAlertBottomSheet;
    @Inject CorporateAccountsDialog corporateAccountsDialog;
    @Inject DriverPreferenceBottomSheet driverPreferenceBottomSheet;
    @Inject OutstandingBalanceBottomSheet outstandingBalanceBottomSheet;

    private String checkId;
    private List<String> rentalRules;
    private ProgressDialog progressDialog;
    private String baseFare;
    private String distanceFare;
    private String durationFare;
    private String advanceFare;
    private String name;
    private int abbr;
    private String symbol;
    private List<String> myRules;
    private RecyclerView.Adapter rentalRulesadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        ButterKnife.bind(this);
        initViews();
        initializeFonts();
        displayPackageList(true);
        hideBookingViews();
        presenter.extractData(getIntent().getExtras());
        populateSeatsSpinner(4);
    }

    @Override
    public void setPickAddress(String address) {
        tv_rent_car_pickup_location.setText(address);
    }

    /**
     * <h2>initViews</h2>
     * this method is used to initialize
     */
    private void initViews() {
        progressDialog = alerts.getProcessDialog(this);
        presenter.initializeCompositeDisposable();
        tvAllToolBarTitle.setText(rent_a_car);
        ll_home_confirmation_fare.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_rental_packages.smoothScrollToPosition(0);
        rv_rental_packages.setLayoutManager(layoutManager);
        rv_rental_packages.setHasFixedSize(true);
        rv_rental_packages.setItemAnimator(new DefaultItemAnimator());
        rv_rental_packages.setAdapter(rentalPackageAdapter);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rv_rental_vehicles.smoothScrollToPosition(0);
        rv_rental_vehicles.setLayoutManager(layoutManager1);
        rv_rental_vehicles.setHasFixedSize(true);
        rv_rental_vehicles.setItemAnimator(new DefaultItemAnimator());
        rv_rental_vehicles.setAdapter(rentalVehicleTypesAdapter);
    }

    /**
     * <h2>initializeFonts</h2>
     * used to initialize the fonts
     */
    private void initializeFonts() {
        tv_rent_car_pickup_location.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupPackage.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupPackageSelected.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupType.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupTypeSelected.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupTypeMinutesAway.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupTypeCost.setTypeface(appTypeface.getPro_narMedium());
        tvRentCarPickupTypeCostSymbol.setTypeface(appTypeface.getPro_narMedium());
        tv_rent_car_rules.setTypeface(appTypeface.getPro_narMedium());
        tv_rent_car_about.setTypeface(appTypeface.getPro_narMedium());
        tv_rent_car_about_details.setTypeface(appTypeface.getPro_News());
        tv_rent_car_pickup_label.setTypeface(appTypeface.getPro_News());
        tv_home_select_promo.setTypeface(appTypeface.getPro_News());
        tv_home_account_type.setTypeface(appTypeface.getPro_News());
        tv_home_select_payment.setTypeface(appTypeface.getPro_News());
        tv_home_select_driver_pref.setTypeface(appTypeface.getPro_News());
        tv_home_ride_request.setTypeface(appTypeface.getPro_News());
        tvAllToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
    }

    @Override
    public void hideBookingViews() {
        llRentCarType.setVisibility(View.GONE);
        llDivider3.setVisibility(View.GONE);
        llRentCarAbout.setVisibility(View.GONE);
        llDivider5.setVisibility(View.GONE);
        rlRentCarIncludeLayout.setVisibility(View.GONE);
    }

    @Override
    public void notifyPackages(List<String> objects) {
        rentalRules = objects;
        rentalPackageAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyRentalVehicleTypes() {
        rentalVehicleTypesAdapter.notifyDataSetChanged();
        setRulesRecycler(rentalRules);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setWalletAmount(String walletAmount)
    {
        tv_home_select_payment.setText(wallet_1+" ("+walletAmount+")");
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(ic_payment_wallet_icon,null,ic_invoice_one_tick_mark_on,null);
    }

    @Override
    public void showWalletUseAlert(String walletBal,int paymentType)
    {
        walletAlertBottomSheet.populateWalletAlert(false,walletBal,paymentType,null,this);
        walletAlertBottomSheet.show(getSupportFragmentManager(),"Wallet_alert");
    }

    @Override
    public void disableCardOption(boolean disable) {
        paymentOptionsBottomSheet.disableCardOption(null,this,disable);
    }

    @Override
    public void showWalletExcessAlert(int paymentType)
    {
        walletAlertBottomSheet.populateWalletAlert(true,null,paymentType,null,this);
        walletAlertBottomSheet.show(getSupportFragmentManager(),"Wallet_alert");
    }

    @Override
    public void showDefaultCard(String lastDigits, String cardBrand,int paymentType,boolean isWalletSelected)
    {
        paymentOptionsBottomSheet.showPaymentOptions(lastDigits,cardBrand,paymentType,null,this,
                isWalletSelected);
    }

    @Override
    public void enableWalletOption(String amount)
    {
        paymentOptionsBottomSheet.enableWalletOption(null,this,amount);
    }

    @Override
    public void disableWalletOption()
    {
        paymentOptionsBottomSheet.disableWalletOption(null,this);
    }

    @Override
    public void hideWalletOption()
    {
        paymentOptionsBottomSheet.hideWalletOption(null,this);
    }

    @Override
    public void setCurrency(String currency) {
        tvRentCarPickupTypeCost.setText(currency);
    }

    /**
     * <h2>setRulesRecycler</h2>
     * this method is used to set the Rules Recycler
     */
    private void setRulesRecycler(List<String> myRules) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_rental_rules.smoothScrollToPosition(0);
        rv_rental_rules.setLayoutManager(layoutManager);
        rv_rental_rules.setHasFixedSize(true);
        rv_rental_rules.setItemAnimator(new DefaultItemAnimator());
        this.myRules = myRules;
        rentalRulesadapter = new RentalRulesAdapter(myRules, this, appTypeface,baseFare,distanceFare,durationFare,advanceFare,name,abbr,symbol,
                tvRentCarPickupPackageSelected.getText().toString());
        rv_rental_rules.setAdapter(rentalRulesadapter);
        rentalRulesadapter.notifyDataSetChanged();
    }

    @Override
    public void showMessageForSelectingPackage() {
        Toast.makeText(this, "Please Select Package and then Cab Type", Toast.LENGTH_LONG).show();
        rv_rental_vehicles.setVisibility(View.GONE);
        ivRentCarSelector2.setVisibility(View.VISIBLE);
        viewCarSelector.setVisibility(View.VISIBLE);
        rlRentCarPickupType.setVisibility(View.VISIBLE);
        llRentCarPickupType.setVisibility(View.VISIBLE);
        rlRentCarFareDetailsHeading.setVisibility(View.GONE);
        tvRentCarPickupType.setText(getString(R.string.cab));
    }

    @Override
    public void showProgressDialog()
    {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.pleaseWait));
        if(!progressDialog.isShowing())
            progressDialog.show();
    }


    @Override
    public void dismissProgressDialog()
    {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSurgeBar(String surgePriceText) {

    }

    @Override
    public void hideSurgeBar() {

    }

    @Override
    public void showAdvanceFee(ArrayList<String> advanceFee) {

    }

    @Override
    public void hideAdvanceFee() {

    }

    @OnClick({R.id.iv_back_button, R.id.rl_back_button, R.id.iv_rent_car_selector, R.id.rl_rent_car_pickup_package,
            R.id.iv_rent_car_selector2, R.id.rl_rent_car_pickup_type,R.id.rl_rent_car_package,
            R.id.tv_rent_car_pickup_package_selected,R.id.view_rent_car_package,R.id.tv_rent_car_pickup_package,
            R.id.ll_rent_car_type,R.id.tv_home_select_promo,R.id.tv_home_select_payment,R.id.tv_home_account_type,
            R.id.tv_home_select_driver_pref,R.id.tv_home_ride_request,R.id.iv_rent_car_rules,R.id.rv_rental_rules})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
                break;

            case R.id.ll_rent_car_type:
                displayRentalVehicleTypes(false);
                break;

            case R.id.tv_home_select_promo:
                Intent intent = new Intent(this, PromoCodeActivity.class);
                startActivityForResult(intent,PROMO_CODE_REQUEST);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.tv_rent_car_pickup_package_selected:
            case R.id.iv_rent_car_selector:
            case R.id.view_rent_car_package:
            case R.id.tv_rent_car_pickup_package:
                displayPackageList(false);
                break;

            case R.id.tv_home_account_type:
                presenter.getCorporateProfiles();
                break;

            case R.id.tv_home_select_payment:
                if(!paymentOptionsBottomSheet.isAdded())
                    paymentOptionsBottomSheet.show(getSupportFragmentManager(),"BottomSheet Fragment");
                break;

            case R.id.tv_home_select_driver_pref:
                presenter.fetchDriverPreferences();
                break;

            case R.id.rl_home_seats:
                Toast.makeText(this, "Seats Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_home_ride_request:
                presenter.checkForOutstandingAmount();
                break;

            case R.id.iv_rent_car_rules:
            case R.id.rv_rental_rules:
                Toast.makeText(this, "Icon is Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
        presenter.subscribeConfirmClick();
    }

    @Override
    public void showOutstandingDialog(String title,String businessName,String bookingDate,String pickupAddress)
    {
        outstandingBalanceBottomSheet.populateValues(title,businessName,bookingDate,pickupAddress,
                getSupportFragmentManager(),null,this);
    }

    @Override
    public void confirmToBook()
    {
        presenter.handleRequestBooking();
    }

    @Override
    public void finishRental() {
        onBackPressed();
    }

    @Override
    public void showSelectedDriverPref(String driverPref)
    {
        if(rl_home_seats.getVisibility() == VISIBLE)
            tv_home_select_driver_pref.setText(driverPref);
        else if(rl_home_vehicles.getVisibility() == VISIBLE)
        {
            if(driverPref.equals(driver_preference))
                tv_home_select_driver_pref.setText(services);
            else if(driverPref.equals(driver_preference_set))
                tv_home_select_driver_pref.setText(services_set);
        }
    }

    @Override
    public void showDriverPref() {
        if(!driverPreferenceBottomSheet.isAdded())
            driverPreferenceBottomSheet.show(getSupportFragmentManager(),"driverPref");
    }

    @Override
    public void populateProfiles(ArrayList<CorporateProfileData> corporateProfileData)
    {
        corporateAccountsDialog.populateProfiles(corporateProfileData,null,this);
        corporateAccountsDialog.show(getSupportFragmentManager(),"CORPORATE");
    }

    @Override
    public void openSetupCorporateScreen()
    {
        if(corporateAccountsDialog.getDialog().isShowing())
            corporateAccountsDialog.getDialog().dismiss();

        Intent intent = new Intent(this, AddCorporateProfileAccountActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showInstituteWallet(String walletAmount)
    {
        tv_home_select_payment.setText(wallet_1+" ("+walletAmount+")");
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(ic_payment_wallet_icon,null,ic_invoice_one_tick_mark_on,null);
        tv_home_select_payment.setEnabled(false);
    }

    @Override
    public void setSelectedProfile(String corporateProfile,Drawable icon)
    {
        if(corporateAccountsDialog.getDialog().isShowing())
            corporateAccountsDialog.getDialog().dismiss();

        tv_home_account_type.setCompoundDrawablesWithIntrinsicBounds(icon,null,
                ic_arrow_drop_down_black_24dp,null);
        tv_home_account_type.setText(corporateProfile);
    }

    @Override
    public void enablePaymentOptions() {
        tv_home_select_payment.setEnabled(true);
    }

    /**
     * <h2>populateSeatsSpinner</h2>
     * used to populate the seats spinner
     * @param maxCapacity max capacity
     */
    public void populateSeatsSpinner(int maxCapacity)
    {
        List<String> seatSpinnerList=new ArrayList<>();
        for(int i=1; i<=maxCapacity; i++)
        {
            if(i==1)
                seatSpinnerList.add(i+" "+seat_single);
            else
                seatSpinnerList.add(i+" "+seat_multiple);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> monthDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, seatSpinnerList);
        // Drop down layout style - list view with radio button
        monthDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnr_rental_select_capacity.setAdapter(monthDataAdapter);

        //Set the listener for when each option is clicked.
        spnr_rental_select_capacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) view).setTextColor(vehicle_unselect_color);
                ((TextView) view).setTextSize(11);
                ((TextView) view).setTypeface(appTypeface.getPro_News());
                position++;
                String numberOfSeatSelected = position+"";
                com.karru.utility.Utility.printLog("selected capacity "+numberOfSeatSelected);
                presenter.handleCapacityChose(numberOfSeatSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public void enableBooking()
    {
        tv_home_ride_request.setEnabled(true);
        tv_home_ride_request.setBackgroundDrawable(selector_layout);
    }

    @Override
    public void disableCashOption() {
        paymentOptionsBottomSheet.disableCashOption(null,this);
    }

    @Override
    public void setWalletPaymentOption(String walletText)
    {
        presenter.setWalletPaymentOption(walletText);
    }

    @Override
    public void showHardLimitAlert()
    {
        walletHardLimitAlert.addView(null,this);
        walletHardLimitAlert.show(getSupportFragmentManager(),"Hard_Limit");
    }

    @Override
    public void openPaymentScreen()
    {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(IS_FROM_BOOKING,true);
        intent.putExtra(SCREEN_TITLE,add_card_booking);
        startActivityForResult(intent, CHANGE_CARD_REQUEST);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CHANGE_CARD_REQUEST:
                if(resultCode == RESULT_OK)
                    presenter.makeCardAsDefault();
                else
                    presenter.checkForDefaultPayment();
                break;

            case REQUESTING_SCREEN:
                switch (resultCode)
                {
                    case REQUESTING_BUSY_DRIVERS:
                    case RESULT_CANCELED:
                        onBackPressed();
                        break;
                }
                break;

            case PROMO_CODE_REQUEST:
                if(resultCode == RESULT_OK)
                {
                    String result=data.getStringExtra("result");
                    if(result != null && !result.isEmpty())
                        tv_home_select_promo.setText(result);
                }
                break;
        }
    }

    @Override
    public void addWalletMoneyLayout()
    {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setSelectedCard(String lastDigits,String brand)
    {
        com.karru.utility.Utility.printLog(TAG+" CARD setSelectedCard ");
        Drawable cardBrand = getResources().getDrawable(BRAND_RESOURCE_MAP.get(brand));
        tv_home_select_payment.setText(card_ending_with+" "+lastDigits);
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,ic_invoice_one_tick_mark_on,null);
    }

    @Override
    public void setPromoCode(String promo) {
        tv_home_select_promo.setText(promo);
    }

    @Override
    public void setCashPaymentOption()
    {
        tv_home_select_payment.setText(cash);
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(ic_payment_cash_icon,null,ic_invoice_one_tick_mark_on,null);
        presenter.setCashPaymentOption();
    }

    @Override
    public void disableBooking(int reason)
    {
        tv_home_ride_request.setEnabled(false);
        tv_home_ride_request.setBackgroundColor(search_color);
        if(reason == 1)
        {
            tv_home_select_payment.setText(select_payment_option);
            tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(confirmation_payment_icon,null,
                    ic_warning_black_24dp,null);
        }
    }

    /**
     * <h2>displayPackageList</h2>
     * this method is used to display the Package List(API CALl)
     */
    private void displayPackageList(boolean isToCallAPI) {
        rv_rental_packages.setVisibility(View.VISIBLE);
        ivRentCarSelector.setVisibility(View.GONE);
        tvRentCarPickupPackageSelected.setVisibility(View.GONE);
        viewRentCarPackage.setVisibility(View.GONE);
        tvRentCarPickupPackage.setText(getString(R.string.select_package));
        if(isToCallAPI)
            presenter.fetchRentalPackages();
    }

    /**
     * <h2>displayRentalVehicleTypes</h2>
     * this method is used to display the CarType List(API CALl)
     */
    private void displayRentalVehicleTypes(boolean isToCallAPI) {
        ivRentCarSelector2.setVisibility(View.GONE);
        rv_rental_vehicles.setVisibility(View.VISIBLE);
        rlRentCarPickupType.setVisibility(View.GONE);
        llRentCarPickupType.setVisibility(View.GONE);
        viewCarSelector.setVisibility(View.GONE);
        tvRentCarPickupType.setText(choose_cab_type);
        if(isToCallAPI)
            presenter.fetchRentVehicleTypes(checkId);
    }

    @Override
    public void openRequestingScreenNormal(Bundle bundle)
    {
        Intent requestIntent = new Intent(this, RequestingActivity.class);
        requestIntent.putExtras(bundle);
        startActivityForResult(requestIntent, REQUESTING_SCREEN);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void packageSelected(String name, String id) {
        tvRentCarPickupPackage.setText(select_package);
        tvRentCarPickupPackageSelected.setText(name);
        rv_rental_packages.setVisibility(View.GONE);
        ivRentCarSelector.setVisibility(View.VISIBLE);
        tvRentCarPickupPackageSelected.setVisibility(View.VISIBLE);
        viewRentCarPackage.setVisibility(View.VISIBLE);
        llRentCarType.setVisibility(View.VISIBLE);
        llDivider2.setVisibility(View.VISIBLE);
        checkId = id;
        displayRentalVehicleTypes(true);
        rlRentCarPickupType.setVisibility(View.GONE);
        llRentCarPickupType.setVisibility(View.GONE);
        rv_rental_rules.setVisibility(View.GONE);
        rlRentCarFareDetailsHeading.setVisibility(View.GONE);
        viewRules.setVisibility(View.GONE);
    }

    @Override
    public void handleClickOfVehicleType(Context context, String vehicleName, String minutes, double cost,
                                         String offImage,String onImage, String vehicleId, int abbr, String symbol) {
        tvRentCarPickupType.setText(getString(R.string.cab));
        tvRentCarPickupTypeSelected.setText(vehicleName);
        tvRentCarPickupTypeMinutesAway.setText(minutes);
        rv_rental_vehicles.setVisibility(View.GONE);
        ivRentCarSelector2.setVisibility(View.VISIBLE);
        rlRentCarPickupType.setVisibility(View.VISIBLE);
        llRentCarPickupType.setVisibility(View.VISIBLE);
        rlRentCarFareDetailsHeading.setVisibility(View.VISIBLE);
        viewCarSelector.setVisibility(View.VISIBLE);
        tvRentCarPickupTypeCostSymbol.setVisibility(View.GONE);
        Glide.with(context)
                .load(offImage)
                .into(ivRentCarPickupTypeSelected);
        showRestList();
        presenter.updateSelectedVehicle(abbr,symbol, offImage,onImage, cost,vehicleId,vehicleName, minutes);
    }

    /**
     * <h2>showRestList</h2>
     * this method is used to display all the layouts below Car Type Layout
     */
    private void showRestList() {
        llDivider3.setVisibility(View.VISIBLE);
        llRentCarAbout.setVisibility(View.VISIBLE);
        llDivider5.setVisibility(View.VISIBLE);
        rlRentCarIncludeLayout.setVisibility(View.VISIBLE);
        rv_rental_rules.setVisibility(View.VISIBLE);
        rlRentCarFareDetailsHeading.setVisibility(View.VISIBLE);
        viewRules.setVisibility(View.VISIBLE);
        presenter.checkForDefaultPayment();
    }

    public void storeRentalFares(String baseFare, String distanceFare, String durationFare, String advanceFare,String name,int abbr,String symbol) {
        this.baseFare = baseFare;
        this.distanceFare = distanceFare;
        this.durationFare = durationFare;
        this.advanceFare = advanceFare;
        this.name = name;
        this.abbr = abbr;
        this.symbol = symbol;
        setRulesRecycler(myRules);
    }
}
