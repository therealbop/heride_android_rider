package com.karru.landing.home.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.karru.countrypic.CountryPicker;
import com.karru.landing.my_vehicles.MyVehiclesActivity;
import com.karru.rental.view.RentalActivity;
import com.heride.rider.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.VisibleRegion;
import com.karru.booking_flow.address.view.AddressSelectionActivity;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.booking_flow.ride.request.RequestingActivity;
import com.karru.dagger.ActivityScoped;
import com.karru.landing.MainActivity;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileAccountActivity;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.model.OnGoingBookingsModel;
import com.karru.landing.home.model.PickUpGates;
import com.karru.landing.home.model.VehicleTypesDetails;
import com.karru.landing.home.promo_code.PromoCodeActivity;
import com.karru.landing.payment.PaymentActivity;
import com.karru.landing.wallet.WalletActivity;
import com.karru.util.ActivityUtils;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.util.path_plot.LatLongBounds;
import com.karru.utility.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.karru.util.ActivityUtils.getBitmapFromShape;
import static com.karru.utility.Constants.ADD_VEHICLE;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.CHANGE_CARD_REQUEST;
import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DROP_ADDRESS_REQUEST;
import static com.karru.utility.Constants.DROP_ADDRESS_SCREEN;
import static com.karru.utility.Constants.DROP_LAT;
import static com.karru.utility.Constants.DROP_LONG;
import static com.karru.utility.Constants.IS_FROM_BOOKING;
import static com.karru.utility.Constants.LATER_BOOKING_TYPE;
import static com.karru.utility.Constants.NOW_BOOKING_TYPE;
import static com.karru.utility.Constants.PERMISSION_BLOCKED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.PICK_ID;
import static com.karru.utility.Constants.PROMO_CODE_REQUEST;
import static com.karru.utility.Constants.READ_CONTACTS_PERMISSIONS_REQUEST;
import static com.karru.utility.Constants.REQUESTING_BUSY_DRIVERS;
import static com.karru.utility.Constants.REQUESTING_DATA;
import static com.karru.utility.Constants.REQUESTING_SCREEN;
import static com.karru.utility.Constants.REQUEST_CHECK_SETTINGS;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.SHOW_HOTEL;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

/**
 * <h1>HomeFragment</h1>
 * <p>
 *  This fragment is used to show the vehicle type and driver info
 * </p>
 * @since on 20/11/15.
 */
@ActivityScoped
public class HomeFragment extends DaggerFragment implements HomeFragmentContract.View,
        OnMapReadyCallback,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveStartedListener
{
    private final String  TAG = "HomeFragment";

    @BindView(R.id.iv_home_fav_icon) ImageView iv_home_fav_icon;
    @BindView(R.id.et_home_fav_title) EditText et_home_fav_title;
    @BindView(R.id.tv_home_ride_now) TextView tv_home_ride_now;
    @BindView(R.id.tv_home_ride_later) TextView tv_home_ride_later;
    @BindView(R.id.tv_home_pick_location) TextView tv_home_pick_location;
    @BindView(R.id.tv_home_not_available) TextView tv_home_not_available;
    @BindView(R.id.ll_home_fav_save) LinearLayout ll_home_fav_save;
    @BindView(R.id.ll_home_vehicle_types) LinearLayout ll_home_vehicle_types;
    @BindView(R.id.ll_home_menu) LinearLayout ll_home_menu;
    @BindView(R.id.iv_nav_Drawer) ImageView iv_nav_Drawer;
    @BindView(R.id.iv_nav_Drawer_hotel) ImageView iv_nav_Drawer_hotel;
    @BindView(R.id.ll_home_bottom_view) LinearLayout ll_home_bottom_view;
    @BindView(R.id.tv_home_fav_save) TextView tv_home_fav_save;
    @BindView(R.id.ll_home_top_views) LinearLayout ll_home_top_views;
    @BindView(R.id.ll_home_default_bottom) LinearLayout ll_home_default_bottom;
    @BindView(R.id.rl_home_dotted_line) RelativeLayout rl_home_dotted_line;
    @BindView(R.id.iv_home_cross_icon) ImageView iv_home_cross_icon;
    @BindView(R.id.ll_home_pick_address) LinearLayout ll_home_pick_address;
    @BindView(R.id.tv_home_fav_cancel) TextView tv_home_fav_cancel;
    @BindView(R.id.iv_home_curr_location) ImageView iv_home_curr_location;
    @BindView(R.id.iv_home_confirm_curr_location) ImageView iv_home_confirm_curr_location;
    @BindView(R.id.ll_home_default_address) LinearLayout ll_home_default_address;
    @BindView(R.id.rl_home_fav_address) RelativeLayout rl_home_fav_address;
    @BindView(R.id.cl_home_confirm_address)
    ConstraintLayout cl_home_confirm_address;
    @BindView(R.id.ll_home_confirm_bottom) LinearLayout ll_home_confirm_bottom;
    @BindView(R.id.tv_home_select_payment) TextView tv_home_select_payment;
    @BindView(R.id.tv_home_time_value) TextView tv_home_time_value;
    @BindView(R.id.tv_home_select_promo) TextView tv_home_select_promo;
    @BindView(R.id.tv_home_fare_price) TextView tv_home_fare_price;
    @BindView(R.id.tv_home_fare_title) TextView tv_home_fare_title;
    @BindView(R.id.tv_home_ride_request) TextView tv_home_ride_request;
    @BindView(R.id.tv_home_confirm_action_title) TextView tv_home_confirm_action_title;
    @BindView(R.id.tv_home_confirm_pick_address) TextView tv_home_confirm_pick_address;
    @BindView(R.id.tv_home_confirm_drop_address) TextView tv_home_confirm_drop_address;
    @BindView(R.id.rl_home_confirm_action_close) RelativeLayout rl_home_confirm_action_close;
    @BindView(R.id.rl_home_current_address) RelativeLayout rl_home_current_address;
    @BindView(R.id.tv_fav_address_text) TextView tv_fav_address_text;
    @BindView(R.id.tv_home_confirm_surge) TextView tv_home_confirm_surge;
    @BindView(R.id.tv_home_zone_name) TextView tv_home_zone_name;
    @BindView(R.id.tv_home_zone_title) TextView tv_home_zone_title;
    @BindView(R.id.tv_home_zone_change) TextView tv_home_zone_change;
    @BindView(R.id.iv_fav_address_icon) ImageView iv_fav_address_icon;
    @BindView(R.id.rb_fav_type_home) AppCompatRadioButton rb_fav_type_home;
    @BindView(R.id.rb_fav_type_work) AppCompatRadioButton rb_fav_type_work;
    @BindView(R.id.rb_fav_type_others) AppCompatRadioButton rb_fav_type_others;
    @BindView(R.id.rl_home_confirm_drop_address) RelativeLayout rl_home_confirm_drop_address;
    @BindView(R.id.ll_home_fav_title) LinearLayout ll_home_fav_title;
    @BindView(R.id.ll_home_current_pickups) LinearLayout ll_home_current_pickups;
    @BindView(R.id.rv_home_ongoing_list) RecyclerView rv_home_ongoing_list;
    @BindView(R.id.rv_home_zones) RecyclerView rv_home_zones;
    @BindView(R.id.tv_home_fare_info) ImageView tv_home_fare_info;
    @BindView(R.id.tv_home_account_type) AppCompatTextView tv_home_account_type;
    @BindView(R.id.vw_home_account_divider) View vw_home_account_divider;
    @BindView(R.id.vw_pref_divider) View vw_pref_divider;
    @BindView(R.id.rl_home_pref_seats) RelativeLayout rl_home_pref_seats;
    @BindView(R.id.tv_home_select_driver_pref) AppCompatTextView tv_home_select_driver_pref;
    @BindView(R.id.tv_home_vehicle) AppCompatTextView tv_home_vehicle;
    @BindView(R.id.tv_home_time_text) AppCompatTextView tv_home_time_text;
    @BindView(R.id.tv_home_ride_advance_fee) TextView tv_home_ride_advance_fee;
    @BindView(R.id.ll_home_bottom_later) LinearLayout ll_home_bottom_later;
    @BindView(R.id.ll_home_bottom_payment) LinearLayout ll_home_bottom_payment;
    @BindView(R.id.ll_home_bottom_preference) LinearLayout ll_home_bottom_preference;
    @BindView(R.id.spnr_home_select_capacity) Spinner spnr_home_select_capacity;
    @BindView(R.id.rl_home_seats) RelativeLayout rl_home_seats;
    @BindView(R.id.rl_home_vehicles) RelativeLayout rl_home_vehicles;
    @BindView(R.id.rl_hotel_layout) RelativeLayout rl_hotel_layout;
    @BindView(R.id.tv_hotel_title) TextView tv_hotel_title;
    @BindView(R.id.tv_hotel_name) TextView tv_hotel_name;
    @BindView(R.id.btn_hotel_book) Button btn_hotel_book;
    @BindView(R.id.et_guest_name) EditText et_guest_name;
    @BindView(R.id.et_guest_phone) EditText et_guest_phone;
    @BindView(R.id.et_hotel_room_no) EditText et_hotel_room_no;
    @BindView(R.id.tv_guest_code) TextView tv_guest_code;
    @BindView(R.id.tv_hotel_phone_error) TextView tv_hotel_phone_error;
    @BindView(R.id.iv_hotel_logo) ImageView iv_hotel_logo;
    @BindView(R.id.iv_guest_flag) ImageView iv_guest_flag;
    @BindView(R.id.pb_hotel_logo) ProgressBar pb_hotel_logo;
    @BindView(R.id.view_edit_phone_line) View view_edit_phone_line;
    @BindString(R.string.no_drivers) String no_drivers;
    @BindString(R.string.fetching_location) String fetching_location;
    @BindString(R.string.away) String away;
    @BindString(R.string.no_drivers_available) String no_drivers_available;
    @BindString(R.string.get_total_fare) String get_total_fare;
    @BindString(R.string.card_ending_with) String card_ending_with;
    @BindString(R.string.cash) String cash;
    @BindString(R.string.total_fare) String total_fare;
    @BindString(R.string.ride_estimate) String ride_estimate;
    @BindString(R.string.advance_fee1) String advance_fee1;
    @BindString(R.string.advance_fee2) String advance_fee2;
    @BindString(R.string.advance_fee3) String advance_fee3;
    @BindString(R.string.at) String at;
    @BindString(R.string.seat_single) String seat_single;
    @BindString(R.string.seat_multiple) String seat_multiple;
    @BindString(R.string.emergency_alert) String emergency_alert;
    @BindString(R.string.emergency_alert_multiple) String emergency_alert_multiple;
    @BindString(R.string.sorry_alert) String sorry_alert;
    @BindString(R.string.add_card_booking) String add_card_booking;
    @BindString(R.string.wallet_1) String wallet_1;
    @BindString(R.string.promo_code) String promo_code;
    @BindString(R.string.services) String services;
    @BindString(R.string.driver_preference) String driver_preference;
    @BindString(R.string.services_set) String services_set;
    @BindString(R.string.driver_preference_set) String driver_preference_set;
    @BindColor(R.color.vehicle_unselect_color) int vehicle_unselect_color;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.fare_text_color) int fare_text_color;
    @BindColor(R.color.darkGray) int darkGray;
    @BindColor(R.color.fav_disable) int fav_disable;
    @BindColor(R.color.search_color) int search_color;
    @BindDrawable(R.drawable.ic_fav_briefcase_icon) Drawable ic_fav_briefcase_icon;
    @BindDrawable(R.drawable.ic_fav_home_icon) Drawable ic_fav_home_icon;
    @BindDrawable(R.drawable.selector_layout) Drawable selector_layout;
    @BindDrawable(R.drawable.ic_payment_card_icon_selector) Drawable ic_payment_card_icon;
    @BindDrawable(R.drawable.ic_payment_cash_icon_selector) Drawable ic_payment_cash_icon;
    @BindDrawable(R.drawable.ic_payment_wallet_icon_selector) Drawable ic_payment_wallet_icon;
    @BindDrawable(R.drawable.ic_invoice_one_tick_mark_on) Drawable ic_invoice_one_tick_mark_on;
    @BindDrawable(R.drawable.ic_warning_black_24dp_selector) Drawable ic_warning_black_24dp;
    @BindDrawable(R.drawable.confirmation_payment_icon) Drawable confirmation_payment_icon;
    @BindDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp_selector) Drawable ic_arrow_drop_down_black_24dp;
    @BindDrawable(R.drawable.confirmation_personal_icon) Drawable confirmation_personal_icon;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;
    @BindString(R.string.emergency_contact_alert) String emergency_contact_alert;
    @BindString(R.string.select_card) String select_payment_option;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.personal) String personal;
    @BindString(R.string.Countrypicker) String countrypicker;

    @Inject Context mContext;
    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface ;
    @Inject com.karru.util.Utility utility;
    @Inject RideRateCardDialog rideRateCardDialog;
    @Inject FareBreakdownDialog fareBreakdownDialog;
    @Inject TimePickerDialog timePickerDialog;
    @Inject AppPermissionsRunTime appPermissionsRunTime;
    @Inject SomeOneRideBottomSheet someOneRideBottomSheet;
    @Inject DriverPreferenceBottomSheet driverPreferenceBottomSheet;
    @Inject CorporateAccountsDialog corporateAccountsDialog;
    @Inject HomeFragmentContract.Presenter homeFragmentPresenter;
    @Inject PaymentOptionsBottomSheet paymentOptionsBottomSheet;
    @Inject OutstandingBalanceBottomSheet outstandingBalanceBottomSheet;
    @Inject WalletAlertBottomSheet walletAlertBottomSheet;
    @Inject ChooseFavDriverDialog chooseFavDriverDialog;
    @Inject WalletHardLimitAlert walletHardLimitAlert;
    @Inject OnGoingBookingsAdapter onGoingBookingsAdapter;
    @Inject CurrentZonePickupsAdapter zonePickupsAdapter;
    @Inject ArrayList<ContactDetails> favDriversList;
    @Inject PromoCodeBottomSheet promoCodeBottomSheet;
    @Inject CountryPicker mCountryPicker;

    private LatLngBounds latLngBounds;
    private Polyline confirmPathPlotLine;
    private LayoutInflater layoutInflater;
    private GoogleMap googleMap;
    private Animation anim_homepage_down_movement,anim_in,
            anim_homepage_up_movement,slide_in_up, slide_down_animation;
    private ProgressDialog progressDialog;
    private Marker pickUpMarker , dropMarker ;
    private Polygon currentUserZone;
    private HashMap<String,Marker> currentZonePickMarkers = new HashMap<>();
    private HashMap<String,Marker> driverCarMarkers = new HashMap<>();
    private HashMap<String,Bitmap> driverCarBitMaps = new HashMap<>();
    private ArrayList<PickUpGates> pickUpGates = new ArrayList<>();
    private Unbinder unbinder;

    @Inject
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        unbinder= ButterKnife.bind(this,view);
        initializeMap();
        setTypeface();
        initAnimationFiles();

        homeFavChecked();
        progressDialog = alerts.getProcessDialog(getActivity());
        progressDialog.setCancelable(false);
        homeFragmentPresenter.attachView(this);
        homeFragmentPresenter.onCreateHomeActivity();
        tv_home_pick_location.setSelected(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_home_ongoing_list.setLayoutManager(layoutManager);
        rv_home_ongoing_list.setAdapter(onGoingBookingsAdapter);
        layoutInflater = LayoutInflater.from(getActivity());

        LinearLayoutManager layoutManagerZones = new LinearLayoutManager(getActivity());
        rv_home_zones.setLayoutManager(layoutManagerZones);
        rv_home_zones.setAdapter(zonePickupsAdapter);

        homeFragmentPresenter.getCountryInfo(mCountryPicker);
        Utility.printLog("Rooted : - " + isRooted()+"");
        return view;
    }


    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    @Override
    public void onGettingOfCountryInfo(int countryFlag, String countryCode,boolean isDefault)
    {
        if(mCountryPicker.isVisible())
            mCountryPicker.dismiss();
        tv_guest_code.setText(countryCode);
        iv_guest_flag.setImageResource(countryFlag);
        if(!isDefault)
            homeFragmentPresenter.validateMobileNumber(et_guest_phone.getText().toString(),
                    tv_guest_code.getText().toString());
    }

    @Override
    public void onInvalidMobile(String errorMsg)
    {
        view_edit_phone_line.setBackgroundColor(getResources().getColor(R.color.red));
        tv_hotel_phone_error.setVisibility(View.VISIBLE);
        tv_hotel_phone_error.setText(errorMsg);
    }

    @Override
    public void showHotelUI(String hotelLogo,String hotelName) {
        rl_hotel_layout.setVisibility(VISIBLE);
        iv_nav_Drawer_hotel.setVisibility(VISIBLE);
        iv_nav_Drawer.setVisibility(GONE);
        tv_hotel_name.setText(hotelName);
        et_guest_name.setText("");
        et_guest_phone.setText("");
        et_hotel_room_no.setText("");
        RequestOptions requestOptions = new RequestOptions();
        /*requestOptions = requestOptions.placeholder(signup_profile_default_image);
        requestOptions = requestOptions.optionalCircleCrop();*/
        if(!hotelLogo.equals(""))
        {
            pb_hotel_logo.setVisibility(VISIBLE);
            Glide.with(mContext)
                    .load(hotelLogo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb_hotel_logo.setVisibility(GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pb_hotel_logo.setVisibility(GONE);
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(iv_hotel_logo);
        }
    }

    @Override
    public void hideHotelUI() {
        rl_hotel_layout.setVisibility(GONE);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void checkForWallet(boolean show,String walletBal)
    {
        if(isAdded())
            ((MainActivity)getActivity()).checkForWallet(show,walletBal);
    }

    @Override
    public void checkForCard(boolean show)
    {
        if(isAdded())
            ((MainActivity)getActivity()).checkForCard(show);
    }

    @Override
    public void checkForFavorite(boolean show)
    {
        if(isAdded())
            ((MainActivity)getActivity()).checkForFavorite(show);
    }

    @Override
    public void checkForCorporate(boolean show)
    {
        if(isAdded())
            ((MainActivity)getActivity()).checkForCorporate(show);
    }

    @Override
    public void checkForReferral(boolean show)
    {
        if(isAdded())
            ((MainActivity)getActivity()).checkForReferral(show);
    }

    @Override
    public void checkForTowing(boolean show)
    {
        if(isAdded())
            ((MainActivity)getActivity()).checkForTowing(show);
    }

    @Override
    public void showCorporate()
    {
        tv_home_account_type.setVisibility(VISIBLE);

        LinearLayout.LayoutParams childParam3 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        childParam3.weight = (float) 4.95;
        tv_home_account_type.setLayoutParams(childParam3);

        LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        childParam2.weight = (float) 0.05;
        childParam2.topMargin = 20;
        childParam2.bottomMargin = 20;
        vw_home_account_divider.setLayoutParams(childParam2);

        LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        childParam1.weight = (float) 4.95;
        tv_home_select_payment.setLayoutParams(childParam1);

        ll_home_bottom_payment.setWeightSum(10);
        ll_home_bottom_payment.removeAllViews();
        ll_home_bottom_payment.addView(tv_home_account_type);
        ll_home_bottom_payment.addView(vw_home_account_divider);
        ll_home_bottom_payment.addView(tv_home_select_payment);
    }

    @Override
    public void showDriverPreferenceView()
    {
        tv_home_select_driver_pref.setVisibility(VISIBLE);

        LinearLayout.LayoutParams childParam3 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        childParam3.weight = (float) 4.95;
        tv_home_select_driver_pref.setLayoutParams(childParam3);

        LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        childParam2.weight = (float) 0.05;
        childParam2.topMargin = 20;
        childParam2.bottomMargin = 20;
        vw_pref_divider.setLayoutParams(childParam2);

        LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        childParam1.weight = (float) 4.95;
        rl_home_pref_seats.setLayoutParams(childParam1);

        ll_home_bottom_preference.setWeightSum(10);
        ll_home_bottom_preference.removeAllViews();
        ll_home_bottom_preference.addView(tv_home_select_driver_pref);
        ll_home_bottom_preference.addView(vw_pref_divider);
        ll_home_bottom_preference.addView(rl_home_pref_seats);
    }

    @Override
    public void hideCorporate()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.weight=10;
        tv_home_select_payment.setLayoutParams(param);
        tv_home_account_type.setVisibility(GONE);
    }

    @Override
    public void hideDriverPreferenceView()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.weight=10;
        rl_home_pref_seats.setLayoutParams(param);
        tv_home_select_driver_pref.setVisibility(GONE);
    }

    /**
     * <h1>startCurrLocation</h1>
     * This method is used to get the current location
     */
    private void startCurrLocation()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                homeFragmentPresenter.getCurrentLocation();
            }
        }
        else
            homeFragmentPresenter.getCurrentLocation();
    }

    /**
     * <h1>onResume</h1>
     * This method is keep on calling each time
     */
    @Override
    public void onResume()
    {
        super.onResume();
        Utility.printLog(TAG+ " TESTING OTHER onResume called ");
        startCurrLocation();
        homeFragmentPresenter.attachView(this);
        homeFragmentPresenter.onResumeHomeActivity();
    }

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap()
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_home_google_map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }

    /**
     * <h1>setTypeface</h1>
     * Initializing all views with the typeface
     */
    private void setTypeface()
    {
        et_home_fav_title.setTypeface(appTypeface.getPro_narMedium());
        tv_home_pick_location.setTypeface(appTypeface.getPro_News());
        tv_home_fav_cancel.setTypeface(appTypeface.getPro_narMedium());
        tv_home_fav_save.setTypeface(appTypeface.getPro_narMedium());
        tv_home_not_available.setTypeface(appTypeface.getPro_News());
        tv_home_ride_now.setTypeface(appTypeface.getPro_narMedium());
        tv_home_ride_later.setTypeface(appTypeface.getPro_narMedium());
        tv_home_select_payment.setTypeface(appTypeface.getPro_News());
        tv_home_time_value.setTypeface(appTypeface.getPro_News());
        tv_home_select_promo.setTypeface(appTypeface.getPro_News());
        tv_home_fare_price.setTypeface(appTypeface.getPro_narMedium());
        tv_home_ride_request.setTypeface(appTypeface.getPro_narMedium());
        tv_home_confirm_action_title.setTypeface(appTypeface.getPro_narMedium());
        tv_home_zone_name.setTypeface(appTypeface.getPro_narMedium());
        tv_home_zone_title.setTypeface(appTypeface.getPro_News());
        tv_home_zone_change.setTypeface(appTypeface.getPro_News());
        tv_home_account_type.setTypeface(appTypeface.getPro_News());
        tv_home_time_text.setTypeface(appTypeface.getPro_News());
        tv_home_fare_title.setTypeface(appTypeface.getPro_News());
        tv_home_confirm_pick_address.setTypeface(appTypeface.getPro_News());
        tv_home_confirm_drop_address.setTypeface(appTypeface.getPro_News());
        tv_fav_address_text.setTypeface(appTypeface.getPro_News());
        tv_home_confirm_surge.setTypeface(appTypeface.getPro_News());
        tv_home_ride_advance_fee.setTypeface(appTypeface.getPro_News());
        tv_home_vehicle.setTypeface(appTypeface.getPro_News());
        tv_home_select_driver_pref.setTypeface(appTypeface.getPro_News());
        tv_hotel_title.setTypeface(appTypeface.getPro_narMedium());
        tv_hotel_name.setTypeface(appTypeface.getPro_narMedium());
        btn_hotel_book.setTypeface(appTypeface.getPro_narMedium());
        et_guest_name.setTypeface(appTypeface.getPro_News());
        tv_guest_code.setTypeface(appTypeface.getPro_News());
        et_guest_phone.setTypeface(appTypeface.getPro_News());
        et_hotel_room_no.setTypeface(appTypeface.getPro_News());
        tv_hotel_phone_error.setTypeface(appTypeface.getPro_News());
    }

    @Override
    public void showSeatsForRide() {
        rl_home_seats.setVisibility(VISIBLE);
        rl_home_vehicles.setVisibility(GONE);
        if(!tv_home_select_driver_pref.getText().toString().equals(driver_preference_set))
            tv_home_select_driver_pref.setText(driver_preference);
    }

    @Override
    public void showSurgeBar(String surgePriceText)
    {
        tv_home_confirm_surge.setVisibility(VISIBLE);
        tv_home_confirm_surge.setText(surgePriceText);
    }

    @Override
    public void showMyVehiclesForTowing(String defaultVehicleName) {
        rl_home_seats.setVisibility(GONE);
        rl_home_vehicles.setVisibility(VISIBLE);
        tv_home_vehicle.setText(defaultVehicleName);
        if(!tv_home_select_driver_pref.getText().toString().equals(services_set))
            tv_home_select_driver_pref.setText(services);
    }

    @Override
    public void hideSurgeBar()
    {
        tv_home_confirm_surge.setVisibility(GONE);
    }

    /**
     *<h1>initAnimationFiles</h1>
     * This method is used to initialize the animation files
     */
    private void initAnimationFiles()
    {
        anim_homepage_down_movement= AnimationUtils.loadAnimation(getActivity(), R.anim.anim_homepage_down_movement);
        anim_in= AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in);
        anim_homepage_up_movement= AnimationUtils.loadAnimation(getActivity(), R.anim.anim_homepage_up_movement);
        slide_in_up= AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
        slide_down_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Utility.printLog(TAG+" google Map onMapReady");
        this.googleMap = googleMap;
        homeFragmentPresenter.setMapReady(true);

        if (this.googleMap == null)
            return;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnCameraMoveStartedListener(this);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.setMyLocationEnabled(true);

        homeFragmentPresenter.findCurrentLocation();
    }

    @Override
    public void moveGoogleMapToLocation(double newLatitude, double newLongitude)
    {
        Utility.printLog(TAG+" moveGoogleMapToLocation newLat: "+newLatitude+" newLong: "+newLongitude);
        if(googleMap == null)
            return;

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(newLatitude, newLongitude)).zoom(16.00f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void initGeoDecoder()
    {
        Handler handler = new Handler();
        handler.post(this::getLatLngFromMapMarker);
    }

    private void getLatLngFromMapMarker()
    {
        Log.d(TAG, "GoogleMap getLatLngFromMapMarker()");
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();

        Point x1 = googleMap.getProjection().toScreenLocation(visibleRegion.farRight);
        Point y = googleMap.getProjection().toScreenLocation(visibleRegion.nearLeft);
        Point centerPoint = new Point(x1.x / 2, y.y / 2);

        LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(centerPoint);
        //to update new address from center of map
        homeFragmentPresenter.verifyAndUpdateNewLocation(centerFromPoint);
    }

    @Override
    public void startAnimationWhenMapMoves()
    {
        ll_home_default_bottom.clearAnimation();
        ll_home_top_views.startAnimation(anim_homepage_up_movement);
        ll_home_default_bottom.startAnimation(slide_down_animation);
        iv_home_cross_icon.setVisibility(VISIBLE);
    }

    @Override
    public void startAnimationWhenMapStops()
    {
        ll_home_top_views.startAnimation(anim_homepage_down_movement);
        ll_home_default_bottom.startAnimation(slide_in_up);
        iv_home_cross_icon.setVisibility(GONE);
    }

    /**
     * adding rootView to horizantal scroll rootView
     * A string is passing to this method from which it is calling, if it coming from on create method then
     * oncreate string is passed to set first vehicle is in onstate.
     */
    private void addVehicleTypes()
    {
        ll_home_vehicle_types.removeAllViews();
        homeFragmentPresenter.checkForVehicleTypes();
    }

    @Override
    public void openSetupCorporateScreen()
    {
        if(corporateAccountsDialog.getDialog().isShowing())
            corporateAccountsDialog.getDialog().dismiss();

        Intent intent = new Intent(getActivity(), AddCorporateProfileAccountActivity.class);
        startActivity(intent);
    }

    /**
     * <h1></h1>
     * This method is used to set the width for each child of scrollview
     * @param viewCreated view created for linear layout
     * @param size size of the vehicle types
     */
    public void setWidthToTypesScrollView(View viewCreated, int size)
    {
        switch (size)
        {
            case 1:
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            case 2:
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/2, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            case 3:
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/3, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            case 4:
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/4, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            case 5:
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/5, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            default:
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams((int) (Utility.returnDisplayWidth(getActivity())/5.5), ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
        }
    }

    /**
     *
     * @param ivVehicle:respective image view to load vehicle image
     * @param url: contains the respective vehicle image url to be downloaded
     */
    private void loadImage(ImageView ivVehicle, String url,boolean isMapImage)
    {
        try
        {
            url = url.replace(" ", "%20");
            if (!url.equals(""))
            {
                Glide.with(mContext)
                        .asBitmap()
                        .load(url)
                        .apply(new RequestOptions().override((int) homeFragmentPresenter.fetchVehicleImageWidth(),
                                (int) homeFragmentPresenter.fetchVehicleImageHeight()))
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(ivVehicle);
            }
        }
        catch (Exception e)
        {
            Utility.printLog(TAG+ " Exception in image " + e);
        }
    }

    @Override
    public void updateCameraPosition(Double currentLat, Double currentLng)
    {
        moveGoogleMapToLocation(currentLat, currentLng);
    }

    @Override
    public void showProgressDialog()
    {
        progressDialog.setMessage(getString(R.string.pleaseWait));
        if(isAdded() && !progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void populateProfiles(ArrayList<CorporateProfileData> corporateProfileData)
    {
        corporateAccountsDialog.populateProfiles(corporateProfileData,this,null);
        corporateAccountsDialog.show(getChildFragmentManager(),"CORPORATE");
    }

    @Override
    public void dismissProgressDialog()
    {
        if(isAdded() && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void enableFavAddress(String favAddressName,boolean isToAnimate)
    {
        tv_home_pick_location.setText(favAddressName);
        tv_fav_address_text.setText(favAddressName);
        tv_home_confirm_pick_address.setText(favAddressName);
        iv_home_fav_icon.setImageResource(R.drawable.ic_book_a_truck_heart_icon_on_selector);
        homeFragmentPresenter.checkForFavAddress(false,isToAnimate);
    }

    @Override
    public void disableFavAddress(String pickUpAddress)
    {
        tv_home_pick_location.setText(pickUpAddress);
        tv_fav_address_text.setText(pickUpAddress);
        tv_home_confirm_pick_address.setText(pickUpAddress);
        iv_home_fav_icon.setImageResource(R.drawable.ic_book_a_truck_heart_icon_off);
        et_home_fav_title.setText("");
    }

    @Override
    public void updateEachVehicleTypeETA()
    {
        Utility.printLog(TAG+" updateEachVehicleTypeETA inside ");
        if(!(ll_home_vehicle_types == null || ll_home_vehicle_types.getChildCount() <=0))
            homeFragmentPresenter.checkForETAOfVehicleTypes();
    }

    @Override
    public void openPickAddressScreen()
    {
        Intent addressIntent = new Intent(getActivity(), AddressSelectionActivity.class);
        addressIntent.putExtra("key","startActivityForResultHOME");
        addressIntent.putExtra("keyId", PICK_ID);
        addressIntent.putExtra("comingFrom","pick");
        startActivityForResult(addressIntent, PICK_ID);
    }

    @OnClick({R.id.tv_home_ride_now, R.id.tv_home_ride_later, R.id.rl_home_current_address,R.id.tv_home_zone_change,
            R.id.iv_home_fav_icon, R.id.tv_home_fav_cancel, R.id.tv_home_fav_save, R.id.img_map_button,
            R.id.tv_home_pick_location, R.id.iv_home_curr_location,R.id.iv_home_confirm_curr_location,R.id.rl_home_confirm_action_close,
            R.id.ib_home_confirm_action_close,R.id.rl_home_confirm_drop_address,R.id.tv_home_ride_request,
            R.id.tv_home_select_payment,R.id.ll_home_confirmation_fare,R.id.tv_home_time_value,R.id.iv_nav_Drawer,
            R.id.tv_home_select_driver_pref,R.id.tv_home_account_type,R.id.tv_home_select_promo,R.id.rl_home_vehicles,
            R.id.iv_switching_hamburger,R.id.btn_hotel_book,R.id.iv_nav_Drawer_hotel,R.id.ll_guest_country_code})
    void onClickEvent(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_nav_Drawer:
            case R.id.iv_switching_hamburger:
                DrawerLayout mDrawerLayout=  getActivity().findViewById(R.id.dl_main_container);
                ((MainActivity)getActivity()).moveDrawer(mDrawerLayout);
                break;

            case R.id.tv_home_ride_now:
                if(!tv_home_pick_location.getText().toString().isEmpty() && !tv_home_pick_location.getText().toString().equals(fetching_location))
                    homeFragmentPresenter.checkSomeOneBookingEligible(NOW_BOOKING_TYPE,true);
                break;

            case R.id.tv_home_ride_later:
                if(!tv_home_pick_location.getText().toString().isEmpty() && !tv_home_pick_location.getText().toString().equals(fetching_location))
                    homeFragmentPresenter.checkSomeOneBookingEligible(LATER_BOOKING_TYPE,true);
                break;

            case R.id.btn_hotel_book:
                ActivityUtils.hideSoftKeyBoard(tv_home_ride_now);
                homeFragmentPresenter.checkForGuestData(et_guest_name.getText().toString(),et_guest_phone.getText().toString(),
                        et_hotel_room_no.getText().toString(),tv_guest_code.getText().toString(),tv_hotel_phone_error.getVisibility());
                break;

            case R.id.iv_nav_Drawer_hotel:
                rl_hotel_layout.setVisibility(VISIBLE);
                break;

            case R.id.tv_home_time_value:
                if(!tv_home_pick_location.getText().toString().isEmpty() && !tv_home_pick_location.getText().toString().equals(fetching_location))
                    homeFragmentPresenter.checkForBookingType(LATER_BOOKING_TYPE,false);
                break;

            case R.id.tv_home_zone_change:
            case R.id.rl_home_current_address:
                homeFragmentPresenter.handleClickEventForAddress(NOW_BOOKING_TYPE);
                break;

            case R.id.iv_home_fav_icon:
                homeFragmentPresenter.validateFavAddress();
                break;

            case R.id.tv_home_fav_cancel:
                ActivityUtils.hideSoftKeyBoard(tv_home_ride_now);
                homeFragmentPresenter.checkForFavAddress(false,true);
                break;

            case R.id.tv_home_fav_save:
                ActivityUtils.hideSoftKeyBoard(tv_home_ride_now);
                homeFragmentPresenter.addAsFavAddress(et_home_fav_title.getText().toString().trim());
                break;

            case R.id.img_map_button:
            case R.id.tv_home_pick_location:
                homeFragmentPresenter.handleClickEventForAddress(NOW_BOOKING_TYPE);
                break;

            case R.id.iv_home_confirm_curr_location:
                Utility.printLog(TAG+"curr latlong in fragment onclick ");
                homeFragmentPresenter.handleCurrLocationButtonClick(true);
                break;

            case R.id.iv_home_curr_location:
                Utility.printLog(TAG+"curr latlong in fragment onclick ");
                homeFragmentPresenter.handleCurrLocationButtonClick(false);
                break;

            case R.id.ib_home_confirm_action_close:
            case R.id.rl_home_confirm_action_close:
                clearConfirmationScreen();
                break;

            case R.id.rl_home_confirm_drop_address:
                openDropAddressScreen();
                break;

            case R.id.tv_home_ride_request:
                homeFragmentPresenter.checkForOutstandingAmount();
                break;

            case R.id.tv_home_select_payment:
                if(!paymentOptionsBottomSheet.isAdded())
                    paymentOptionsBottomSheet.show(getChildFragmentManager(),"BottomSheet Fragment");
                break;

            case R.id.tv_home_select_driver_pref:
                homeFragmentPresenter.fetchDriverPreferences();
                break;

            case R.id.ll_home_confirmation_fare:
                if(!tv_home_fare_price.getText().toString().equals(get_total_fare))
                    fareBreakdownDialog.show();
                else
                    openDropAddressScreen();
                break;

            case R.id.tv_home_account_type:
                homeFragmentPresenter.getCorporateProfiles();
                break;

            case R.id.tv_home_select_promo:
                Intent intent = new Intent(getActivity(), PromoCodeActivity.class);
                startActivityForResult(intent,PROMO_CODE_REQUEST);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.rl_home_vehicles:
                Intent intentVehicle = new Intent(getActivity(), MyVehiclesActivity.class);
                intentVehicle.putExtra(COMING_FROM,1);
                startActivityForResult(intentVehicle,ADD_VEHICLE);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.ll_guest_country_code:
                mCountryPicker.show(getChildFragmentManager(), countrypicker);
                homeFragmentPresenter.addListenerForCountry(mCountryPicker);
                break;
        }
    }

    @Override
    public void showDriverPref() {
        if(!driverPreferenceBottomSheet.isAdded())
            driverPreferenceBottomSheet.show(getChildFragmentManager(),"driverPref");
    }

    @Override
    public void invalidPromo(String error)
    {
        tv_home_select_promo.setText(promo_code);
        promoCodeBottomSheet.invalidPromo(error,this);
    }

    @Override
    public void validPromo(String validPromo)
    {
        tv_home_select_promo.setText(validPromo);
        promoCodeBottomSheet.validPromo(this);
    }

    @Override
    public void showFavDriversDialog(int bookingType)
    {
        chooseFavDriverDialog.setBookingType(bookingType);
        if(favDriversList.size()>0)
            chooseFavDriverDialog.show(getChildFragmentManager(),"FAV_DRIVER");
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

    @SuppressLint("SetTextI18n")
    @Override
    public void showInstituteWallet(String walletAmount)
    {
        tv_home_select_payment.setText(wallet_1+" ("+walletAmount+")");
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(ic_payment_wallet_icon,null,ic_invoice_one_tick_mark_on,null);
        tv_home_select_payment.setEnabled(false);
    }

    @Override
    public void enablePaymentOptions() {
        tv_home_select_payment.setEnabled(true);
    }

    @Override
    public void openDropAddressScreen()
    {
        Intent intent1 = new Intent(getActivity(),AddressSelectionActivity.class);
        intent1.putExtra("keyId", DROP_ADDRESS_REQUEST);
        intent1.putExtra("key", "startActivityForResultHOME");
        intent1.putExtra("comingFrom",DROP_ADDRESS_SCREEN);
        startActivityForResult(intent1,DROP_ADDRESS_REQUEST);
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
    public void setMinDateToPicker(int day,int month,int year, int hour, int minute)
    {
        timePickerDialog.setMinDateToPicker( day, month, year,hour,minute);
    }

    @Override
    public void notifyAdapterWithFavDrivers()
    {
        if(favDriversList.size() == 0)
        {
            if(chooseFavDriverDialog.isVisible())
                chooseFavDriverDialog.getDialog().dismiss();
        }
        chooseFavDriverDialog.notifyViewWithLists();
    }

    @Override
    public void showOutstandingDialog(String title,String businessName,String bookingDate,String pickupAddress)
    {
        outstandingBalanceBottomSheet.populateValues(title,businessName,bookingDate,pickupAddress,
                getChildFragmentManager(),this,null);
    }

    @Override
    public void confirmToBook()
    {
        homeFragmentPresenter.handleRequestBooking();
    }

    @Override
    public void clearConfirmationScreen()
    {
        spnr_home_select_capacity.setSelection(0);
        if(googleMap!=null)
        {
            googleMap.setOnCameraIdleListener(this);
            googleMap.setOnCameraMoveStartedListener(this);
        }
        cl_home_confirm_address.clearAnimation();
        cl_home_confirm_address.setVisibility(GONE);
        ll_home_confirm_bottom.setVisibility(GONE);
        iv_home_confirm_curr_location.setVisibility(GONE);
        ll_home_default_address.setVisibility(VISIBLE);
        ll_home_default_bottom.setVisibility(VISIBLE);
        if(pickUpMarker!=null)
            pickUpMarker.remove();
        if(dropMarker!=null)
        {
            dropMarker.remove();
            dropMarker = null;
        }
        if(confirmPathPlotLine != null)
            confirmPathPlotLine.remove();
        if(latLngBounds!=null)
            latLngBounds = null;
        tv_home_account_type.setCompoundDrawablesWithIntrinsicBounds(confirmation_personal_icon,null,
                ic_arrow_drop_down_black_24dp,null);
        tv_home_account_type.setText(personal);
        tv_home_confirm_drop_address.setText("");
        homeFragmentPresenter.clearDropAddress();
        tv_home_fare_info.setVisibility(GONE);
        tv_home_fare_title.setText(ride_estimate);
        tv_home_fare_price.setText(get_total_fare);
        tv_home_confirm_surge.setVisibility(GONE);
        homeFragmentPresenter.handleCurrLocationButtonClick(false);
        disableBooking(1);
    }

    @Override
    public void hideBottomConfirm() {
        ll_home_confirm_bottom.setVisibility(GONE);
    }

    @Override
    public void setETAOfEachVehicleType(int position, String etaOfNearestDriver)
    {
        Utility.printLog(TAG+" position of vehicle type "+position);
        try
        {
            if(ll_home_vehicle_types.getChildAt(position) != null)
            {
                View eachVehicleTypeView = ll_home_vehicle_types.getChildAt(position);
                if(eachVehicleTypeView!=null)
                    ((TextView)eachVehicleTypeView.findViewById(R.id.tv_home_vehicle_eta)).setText(etaOfNearestDriver);
            }
        }
        catch (NullPointerException e)
        {
            Utility.printLog(TAG+" NullPointerException "+e);
        }
    }

    @Override
    public void addWalletMoneyLayout()
    {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void showWalletExcessAlert(int paymentType)
    {
        walletAlertBottomSheet.populateWalletAlert(true,null,paymentType,this,null);
        walletAlertBottomSheet.show(getChildFragmentManager(),"Wallet_alert");
    }

    @Override
    public void showAlertForOutZone(String message)
    {
        Dialog dialog = alerts.userPromptWithOneButton(message,getActivity());
        TextView tv_alert_ok = dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_ok.setOnClickListener(view ->
        {
            dialog.dismiss();
            clearConfirmationScreen();
        });
        dialog.show();
    }

    @Override
    public void showWalletUseAlert(String walletBal,int paymentType)
    {
        walletAlertBottomSheet.populateWalletAlert(false,walletBal,paymentType,this,null);
        walletAlertBottomSheet.show(getChildFragmentManager(),"Wallet_alert");
    }

    @Override
    public void showHardLimitAlert()
    {
        walletHardLimitAlert.addView(this,null);
        walletHardLimitAlert.show(getChildFragmentManager(),"Hard_Limit");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CHANGE_CARD_REQUEST:
                if(resultCode == RESULT_OK)
                    homeFragmentPresenter.makeCardAsDefault();
                else
                    homeFragmentPresenter.checkForDefaultPayment();
                break;

            case REQUESTING_SCREEN:
                switch (resultCode)
                {
                    case REQUESTING_BUSY_DRIVERS:
                    case RESULT_CANCELED:
                        clearConfirmationScreen();
                        break;

                    case SHOW_HOTEL:
                        clearConfirmationScreen();
                        et_guest_name.setText("");
                        et_guest_phone.setText("");
                        et_hotel_room_no.setText("");
                        rl_hotel_layout.setVisibility(VISIBLE);
                        break;
                }
                break;

            case PROMO_CODE_REQUEST:
                if(resultCode == RESULT_OK)
                {
                    String result=data.getStringExtra("result");
                    boolean fareEstimate = data.getBooleanExtra("FareEstimate",false);
                    if(result != null && !result.isEmpty())
                        tv_home_select_promo.setText(result);
                    if(fareEstimate)
                        homeFragmentPresenter.handleFinalBreakDown();
                }
                break;

            case ADD_VEHICLE:
                if(resultCode == RESULT_OK)
                    homeFragmentPresenter.checkForDefaultVehicle();
                break;
        }

        Utility.printLog(TAG+ " TESTING OTHER onActivityResult called "+resultCode);
        if(null != data && null != data.getExtras())
            homeFragmentPresenter.handleResultFromIntents(requestCode,resultCode,data.getExtras().getString(DROP_LAT),
                    data.getStringExtra(DROP_LONG),data.getStringExtra(DROP_ADDRESS),data.getExtras());
    }

    @Override
    public void promptUserWithLocationAlert( Status status)
    {
        try
        {
            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
        }
        catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @OnFocusChange({ R.id.et_guest_phone })
    public void onFocusChangeListener(View v, boolean hasFocus)
    {
        if (!hasFocus)
            homeFragmentPresenter.validateMobileNumber(et_guest_phone.getText().toString(),
                    tv_guest_code.getText().toString());
    }


    @OnTextChanged({R.id.et_guest_phone})
    public void afterTextChanged(Editable editable)
    {
        if (editable == et_guest_phone.getEditableText())
        {
            view_edit_phone_line.setBackgroundColor(getResources().getColor(R.color.tiet_under_line));
            tv_hotel_phone_error.setVisibility(GONE);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Utility.printLog(TAG+" onPause ");
        homeFragmentPresenter.handleHomeBackgroundState();
    }

    @OnTextChanged(R.id.et_home_fav_title )
    public void onTextChanged(CharSequence charSequence)
    {
        if(et_home_fav_title.hasFocus())
        {
            if(charSequence.toString().length()>0)
            {
                tv_home_fav_save.setTextColor(ContextCompat.getColor(getActivity(), R.color.order_status));
                tv_home_fav_save.setEnabled(true);
            }
            else
            {
                tv_home_fav_save.setEnabled(false);
                tv_home_fav_save.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkGray));
            }
        }
    }

    @Override
    public void onVehicleTypesFound()
    {
        tv_home_not_available.setVisibility(GONE);
        ll_home_bottom_view.setVisibility(VISIBLE);
        addVehicleTypes();
    }

    @Override
    public void onVehicleTypesNotFound()
    {
        if(tv_home_not_available.getVisibility() != VISIBLE)
        {
            ll_home_bottom_view.setVisibility(GONE);
            tv_home_not_available.setVisibility(VISIBLE);
            clearCurrentZone();
        }
    }

    @Override
    public void enableNowBooking()
    {
        tv_home_ride_now.setClickable(true);
        tv_home_ride_now.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.selector_layout));
    }

    @Override
    public void disableNowBooking()
    {
        tv_home_ride_now.setClickable(false);
        tv_home_ride_now.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.search_color));
    }

    @Override
    public void showToast(String message)
    {
        Toast.makeText(getActivity(), message , Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setVehicleTypes(int position , int size , VehicleTypesDetails vehicleItem, String ETA,
                                boolean vehicleViewClicked)
    {
        View inflatedLayout = layoutInflater.inflate(R.layout.item_home_vehicle_type, null, false);
        setWidthToTypesScrollView(inflatedLayout , size);

        ImageView iv_home_vehicle_image_off =  inflatedLayout.findViewById(R.id.iv_home_vehicle_image_off);
        loadImage(iv_home_vehicle_image_off, vehicleItem.getVehicleImgOff(),false);

        ImageView iv_home_vehicle_image_on =  inflatedLayout.findViewById(R.id.iv_home_vehicle_image_on);
        loadImage(iv_home_vehicle_image_on, vehicleItem.getVehicleImgOn(),false);

        TextView tv_home_vehicle_name =  inflatedLayout.findViewById(R.id.tv_home_vehicle_name);
        tv_home_vehicle_name.setTypeface(appTypeface.getPro_narMedium());
        tv_home_vehicle_name.setText(vehicleItem.getTypeName());
        tv_home_vehicle_name.setSelected(true);

        TextView tv_home_vehicle_eta =  inflatedLayout.findViewById(R.id.tv_home_vehicle_eta);
        tv_home_vehicle_eta.setTypeface(appTypeface.getPro_narMedium());
        tv_home_vehicle_eta.setText(ETA);

        if(vehicleViewClicked)
        {
            iv_home_vehicle_image_off.setVisibility(GONE);
            iv_home_vehicle_image_on.setVisibility(VISIBLE);
            tv_home_vehicle_eta.setTextColor(colorPrimary);
            tv_home_vehicle_name.setTextColor(colorPrimary);
            if(tv_home_vehicle_eta.getText().toString().equals(no_drivers))
                tv_home_confirm_action_title.setText(no_drivers_available);
            else
                tv_home_confirm_action_title.setText(tv_home_vehicle_name.getText().toString()+", "+
                        tv_home_vehicle_eta.getText().toString()+" "+away);

            homeFragmentPresenter.checkForBookingType(vehicleItem);
            if (!vehicleItem.isTowTruck())
                rideRateCardDialog.populateRates(vehicleItem.getRide().getBaseFare(),vehicleItem.getRide().getMileagePrice(),
                        vehicleItem.getRide().getTimeFare());
            else
                rideRateCardDialog.populateRates(vehicleItem.getTowTruck().getBaseFare(),vehicleItem.getTowTruck().getMileagePrice(),
                        vehicleItem.getTowTruck().getTimeFare());
        }
        else
        {
            iv_home_vehicle_image_on.setVisibility(GONE);
            iv_home_vehicle_image_off.setVisibility(VISIBLE);
            tv_home_vehicle_eta.setTextColor(vehicle_unselect_color);
            tv_home_vehicle_name.setTextColor(vehicle_unselect_color);
        }

        RelativeLayout rl_home_vehicle_type =  inflatedLayout.findViewById(R.id.rl_home_vehicle_type);
        rl_home_vehicle_type.setTag(vehicleItem.getTypeId());
        rl_home_vehicle_type.setOnClickListener((View v1) ->
                homeFragmentPresenter.handleClickOfVehicleType(vehicleItem,vehicleItem.getTypeId()));

        ImageView imageViewTemp = new ImageView(getActivity());
        //to download driver marker car icons and also save it to driversMarkerIconUrls hash map array
        loadImage(imageViewTemp, vehicleItem.getVehicleMapIcon(),true);
        ll_home_vehicle_types.addView(inflatedLayout);
    }

    @Override
    public void showShipmentRateCardDialog(final VehicleTypesDetails typeItemLoc, String... rateCardDetails)
    {
        Typeface clanproNarrNews = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ClanPro-NarrNews.otf");
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_home_car_details);

        TextView tvMinFareTitle =  dialog.findViewById(R.id.tvMinFareTitle);
        tvMinFareTitle.setTypeface(clanproNarrNews);

        TextView tvMinFare =  dialog.findViewById(R.id.tvMinFare);
        tvMinFare.setTypeface(clanproNarrNews);

        TextView tvAfterMilesTitle =  dialog.findViewById(R.id.tv_after_mi_title);
        tvAfterMilesTitle.setTypeface(clanproNarrNews);

        TextView tvAfterMiles =  dialog.findViewById(R.id.tvAfterMiles);
        tvAfterMiles.setTypeface(clanproNarrNews);

        TextView tvAfterMinTitle =  dialog.findViewById(R.id.tvAfterMinTitle);
        tvAfterMinTitle.setTypeface(clanproNarrNews);

        TextView tvAfterMin =  dialog.findViewById(R.id.tvAfterMin);
        tvAfterMin.setTypeface(clanproNarrNews);

        TextView tvCapacityTtile=  dialog.findViewById(R.id.tvCapacityTtile);
        tvCapacityTtile.setTypeface(clanproNarrNews);

        TextView tvCapacity=  dialog.findViewById(R.id.tvCapacity);
        tvCapacity.setTypeface(clanproNarrNews);
        tvCapacity.setText(typeItemLoc.getVehicleCapacity());

        TextView tv_l_b_hTitle =  dialog.findViewById(R.id.tv_l_b_hTitle);
        tv_l_b_hTitle.setTypeface(clanproNarrNews);

        TextView tv_l_b_h=  dialog.findViewById(R.id.tv_l_b_h);
        tv_l_b_h.setTypeface(clanproNarrNews);
        tv_l_b_h.setText(typeItemLoc.getVehicleDimension());

        tvMinFare.setText(rateCardDetails[0]);
        tvAfterMilesTitle.setText(rateCardDetails[1]);
        tvAfterMiles.setText(rateCardDetails[2]);
        tvAfterMinTitle.setText(rateCardDetails[3]);
        tvAfterMin.setText(rateCardDetails[4]);

        dialog.show();
    }

    @Override
    public void plotOnlineDriverMarkers(String driverId,LatLng latLng,double driverMarkerWidth,double driverMarkerHeight,
                                        Bitmap bitmap,long interval)
    {
        try
        {
            if(googleMap != null)
            {
                if(!driverCarBitMaps.containsValue(bitmap))
                {
                    if(null != driverCarMarkers.get(driverId))
                    {
                        driverCarMarkers.get(driverId).remove();
                        driverCarMarkers.remove(driverId);
                    }
                }

                if(!driverCarMarkers.keySet().contains(driverId))
                {
                    Utility.printLog(TAG + " plotOnlineDriverMarkers inside success ");
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) driverMarkerWidth,
                            (int) driverMarkerHeight, false);
                    Marker driverMarker = googleMap.addMarker(new MarkerOptions().position(latLng).
                            icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
                    driverCarMarkers.put(driverId, driverMarker);
                    driverCarBitMaps.put(driverId, bitmap);
                }
                else
                {
                    Marker marker = driverCarMarkers.get(driverId);
                    LatLng prevLatLong = new LatLng(driverCarMarkers.get(driverId).getPosition().latitude,
                            driverCarMarkers.get(driverId).getPosition().longitude);

                    double bearing = utility.bearingBetweenLocations(prevLatLong,latLng);
                    Utility.printLog(TAG+" bearing calculated "+bearing);
                    final long duration = 2000;
                    final Handler handler = new Handler();
                    final long start = SystemClock.uptimeMillis ();
                    final Interpolator interpolator = new LinearInterpolator();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            long elapsed = SystemClock.uptimeMillis() - start;
                            float t = interpolator.getInterpolation((float) elapsed
                                    / duration);
                            double lng = t * latLng.longitude + (1 - t) * prevLatLong.longitude;
                            double lat = t * latLng.latitude + (1 - t) * prevLatLong.latitude;
                            marker.setPosition(new LatLng(lat, lng));
                            marker.setAnchor(0.5f, 0.5f);
                            marker.setRotation((float) bearing);
                            marker.setFlat(true);
                            if (t < 1.0)
                            {
                                // Post again 16ms later.
                                handler.postDelayed(this, 16);
                            }
                        }
                    }, 1000);
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            Utility.printLog(TAG+" plotOnlineDriverMarkers inside catch "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void removeOfflineDriverMarker(String driverId)
    {
        if(driverCarMarkers.get(driverId)!=null)
            driverCarMarkers.get(driverId).remove();
        driverCarMarkers.remove(driverId);
        driverCarBitMaps.remove(driverId);
    }

    @Override
    public void onCameraIdle()
    {
        Utility.printLog(TAG+ "GoogleMap setOnCameraIdleListener() ");
        homeFragmentPresenter.handleGoogleMapIdle();
        initGeoDecoder();
    }

    @Override
    public void onCameraMoveStarted(int reason)
    {
        ActivityUtils.hideSoftKeyBoard(tv_home_ride_now);
        Utility.printLog(TAG+ "GoogleMap onCameraMoveStarted() ");
        homeFragmentPresenter.handleGoogleMapStartedMove(reason);
    }

    @Override
    public void showAppUpdateAlert(boolean mandateUpdate)
    {
        Utility.printLog(TAG+" homeActivityView homeActivityView "+getActivity());
        alerts.updateAppVersionAlert(getActivity(),mandateUpdate,null);
    }

    @Override
    public void handleFavAddressUI()
    {
        ll_home_default_address.clearAnimation();
        ll_home_default_address.setVisibility(GONE);
        rl_home_fav_address.setVisibility(VISIBLE);
        rl_home_fav_address.startAnimation(anim_homepage_down_movement);
        ll_home_default_bottom.clearAnimation();
        ll_home_default_bottom.startAnimation(slide_down_animation);
        if(rb_fav_type_others.isChecked())
        {
            et_home_fav_title.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void handleNonFavAddressUI()
    {
        rl_home_fav_address.clearAnimation();
        rl_home_fav_address.setVisibility(GONE);
        ll_home_default_address.setVisibility(VISIBLE);
        ll_home_default_address.startAnimation(anim_homepage_down_movement);
        tv_home_pick_location.setClickable(true);
        ll_home_default_bottom.startAnimation(slide_in_up);
    }

    @Override
    public void openConfirmScreen(double pickLat, double pickLong,String confirmText)
    {
        tv_home_ride_request.setText(confirmText);
        homeFragmentPresenter.checkForDefaultPayment();
        googleMap.setOnCameraIdleListener(null);
        googleMap.setOnCameraMoveStartedListener(null);
        if(pickUpMarker!=null)
            pickUpMarker.remove();
        pickUpMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(pickLat, pickLong))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.confirmation_pickup_pin_icon)));
        ll_home_default_address.clearAnimation();
        cl_home_confirm_address.clearAnimation();
        cl_home_confirm_address.setVisibility(VISIBLE);
        ll_home_confirm_bottom.setVisibility(VISIBLE);
        iv_home_confirm_curr_location.setVisibility(VISIBLE);
        ll_home_default_address.setVisibility(GONE);
        ll_home_default_bottom.setVisibility(GONE);
    }

    @Override
    public void openRentalScreen(Bundle bundle) {
        Intent intent = new Intent(getActivity(), RentalActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void setPromoCode(String promo) {
        tv_home_select_promo.setText(promo);
    }

    @Override
    public void showScheduleView(String dateToBeShown)
    {
        ll_home_bottom_later.setVisibility(VISIBLE);
        tv_home_time_value.setText(dateToBeShown);
    }

    @Override
    public void showAdvanceFee(ArrayList<String> extraFees)
    {
        StringBuilder extraFeesText = null;
        for(String extraTitle : extraFees)
        {
            if(extraFeesText!= null)
                extraFeesText.append(" ").append(extraTitle).append(" ");
            else
                extraFeesText = new StringBuilder(extraTitle );
        }
        extraFeesText.append(" ").append(advance_fee3);
        tv_home_ride_advance_fee.setText(extraFeesText);
        tv_home_ride_advance_fee.setVisibility(VISIBLE);
    }

    @Override
    public void hideAdvanceFee()
    {
        tv_home_ride_advance_fee.setVisibility(GONE);
    }

    @Override
    public void hideScheduleView()
    {
        ll_home_bottom_later.setVisibility(GONE);
    }

    @OnClick({ R.id.rb_fav_type_home, R.id.rb_fav_type_work ,R.id.rb_fav_type_others})
    void onRadioButtonCheckChanged(AppCompatRadioButton radioButton)
    {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId())
        {
            case R.id.rb_fav_type_home:
                if(checked)
                    homeFavChecked();
                break;

            case R.id.rb_fav_type_work:
                if(checked)
                {
                    tv_home_fav_save.setEnabled(true);
                    tv_home_fav_save.setTextColor(fare_text_color);
                    ActivityUtils.hideSoftKeyBoard(et_home_fav_title);
                    iv_fav_address_icon.setVisibility(VISIBLE);
                    iv_fav_address_icon.setImageDrawable(ic_fav_briefcase_icon);
                    rb_fav_type_work.setTextColor(fare_text_color);
                    rb_fav_type_work.setTypeface(appTypeface.getPro_narMedium());
                    rb_fav_type_home.setTextColor(fav_disable);
                    rb_fav_type_home.setTypeface(appTypeface.getPro_News());
                    rb_fav_type_others.setTextColor(fav_disable);
                    rb_fav_type_others.setTypeface(appTypeface.getPro_News());
                    ll_home_fav_title.setVisibility(GONE);
                    homeFragmentPresenter.setFavoriteType(2);
                }
                break;

            case R.id.rb_fav_type_others:
                if(checked)
                {
                    et_home_fav_title.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    tv_home_fav_save.setEnabled(false);
                    tv_home_fav_save.setTextColor(darkGray);
                    iv_fav_address_icon.setVisibility(View.INVISIBLE);
                    rb_fav_type_others.setTextColor(fare_text_color);
                    rb_fav_type_others.setTypeface(appTypeface.getPro_narMedium());
                    rb_fav_type_work.setTextColor(fav_disable);
                    rb_fav_type_work.setTypeface(appTypeface.getPro_News());
                    rb_fav_type_home.setTextColor(fav_disable);
                    rb_fav_type_home.setTypeface(appTypeface.getPro_News());
                    ll_home_fav_title.setVisibility(VISIBLE);
                    homeFragmentPresenter.setFavoriteType(3);
                }
                break;
        }
    }

    /**
     * <h2>homeFavChecked</h2>
     * This method is used to handle the home as fav type
     */
    private void homeFavChecked()
    {
        tv_home_fav_save.setEnabled(true);
        tv_home_fav_save.setTextColor(fare_text_color);
        ActivityUtils.hideSoftKeyBoard(et_home_fav_title);
        iv_fav_address_icon.setVisibility(VISIBLE);
        iv_fav_address_icon.setImageDrawable(ic_fav_home_icon);
        rb_fav_type_home.setChecked(true);
        rb_fav_type_home.setTextColor(fare_text_color);
        rb_fav_type_home.setTypeface(appTypeface.getPro_narMedium());
        rb_fav_type_work.setTextColor(fav_disable);
        rb_fav_type_work.setTypeface(appTypeface.getPro_News());
        rb_fav_type_others.setTextColor(fav_disable);
        rb_fav_type_others.setTypeface(appTypeface.getPro_News());
        ll_home_fav_title.setVisibility(GONE);
        homeFragmentPresenter.setFavoriteType(1);
    }

    @Override
    public void changePickAddressUI(String pickAddress, double pickLat, double pickLang)
    {
        tv_home_pick_location.setText(pickAddress);
        tv_fav_address_text.setText(pickAddress);
        tv_home_confirm_pick_address.setText(pickAddress);
        moveGoogleMapToLocation(pickLat, pickLang);
    }

    @Override
    public void changeDropAddressUI(String pickAddress, double dropLat, double dropLong,
                                    String pickLat,String pickLng,String confirmText)
    {
        tv_home_confirm_drop_address.setText(pickAddress);
        openConfirmScreen(Double.parseDouble(pickLat),Double.parseDouble(pickLng),confirmText);
        if(dropMarker!=null)
        {
            dropMarker.remove();
            dropMarker = null;
        }

        dropMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(dropLat, dropLong))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.confirmation_drop_pin_icon)));

        homeFragmentPresenter.getApproxFareEstimation();

        addLatLongBounds(true);
    }

    @Override
    public void addLatLongBounds(boolean isToPlotPath)
    {
        if(dropMarker!=null)
        {
            if(isToPlotPath)
                homeFragmentPresenter.plotPathRoute(pickUpMarker.getPosition(),dropMarker.getPosition());

            if(latLngBounds!=null)
            {
                int padding = 100;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
                // Move the map
                googleMap.moveCamera(cu);
            }
        }
    }

    @Override
    public void googlePathPlot(LatLongBounds latLongBounds)
    {
        if(ll_home_default_address.getVisibility() != VISIBLE)
        {
            if(confirmPathPlotLine != null)
                confirmPathPlotLine.remove();
            confirmPathPlotLine = googleMap.addPolyline(latLongBounds.getPolylineOptions());

            LatLng southWestLocation = new LatLng(Double.parseDouble(latLongBounds.getSouthwest().getLat()),
                    Double.parseDouble(latLongBounds.getSouthwest().getLng()));
            LatLng northEastLocation = new LatLng(Double.parseDouble(latLongBounds.getNortheast().getLat()),
                    Double.parseDouble(latLongBounds.getNortheast().getLng()));
            latLngBounds = new LatLngBounds(southWestLocation,northEastLocation);
            // Obtain a movement description object
            // offset from edges of the map in pixels
            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
            // Move the map
            googleMap.moveCamera(cu);
        }
    }

    @Override
    public void showFareEstimation( ArrayList<ReceiptDetails> listOfBreakDown,String amount)
    {
        tv_home_fare_info.setVisibility(VISIBLE);
        tv_home_fare_title.setText(total_fare);
        tv_home_fare_price.setText(amount);
        fareBreakdownDialog.populateData(listOfBreakDown);
    }

    @Override
    public void enableOnlyNowBookingType() {
        tv_home_ride_later.setVisibility(GONE);
        tv_home_ride_now.setVisibility(VISIBLE);
    }

    @Override
    public void enableOnlyLaterBookingType() {
        tv_home_ride_now.setVisibility(GONE);
        tv_home_ride_later.setVisibility(VISIBLE);
    }

    @Override
    public void enableBothBookingType() {
        tv_home_ride_now.setVisibility(VISIBLE);
        tv_home_ride_later.setVisibility(VISIBLE);
    }

    @Override
    public void openRequestingScreenNormal(Bundle bundle)
    {
        Intent requestIntent = new Intent(getActivity(), RequestingActivity.class);
        requestIntent.putExtras(bundle);
        startActivityForResult(requestIntent, REQUESTING_SCREEN);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void openRequestingScreenKilled(RequestBookingDetails requestBookingDetailsModel)
    {
        Intent requestIntent = new Intent(getActivity(), RequestingActivity.class);
        requestIntent.putExtra(REQUESTING_DATA,requestBookingDetailsModel);
        startActivityForResult(requestIntent, REQUESTING_SCREEN);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void notifyOnGoingBookings(ArrayList<OnGoingBookingsModel> onGoingBookingsModels)
    {
        onGoingBookingsAdapter.notifyOnGoingBookingDetails(onGoingBookingsModels,this);
    }

    @Override
    public void notifyZonePickups(ArrayList<PickUpGates> pickUpGates) {
        this.pickUpGates = pickUpGates;
        zonePickupsAdapter.notifyPickupPointsDetails(this.pickUpGates,this);
    }

    @Override
    public void notifyOnGoingItemDetails(int pos, OnGoingBookingsModel onGoingBookingsModels)
    {
        getActivity().runOnUiThread(() -> onGoingBookingsAdapter.notifyOnGoingItemDetails(pos,onGoingBookingsModels,this));
    }

    @Override
    public void openLiveTrackingScreen(String bookingId)
    {
        Intent intent = new Intent(getActivity(), LiveTrackingActivity.class);
        intent.putExtra(BOOKING_ID,bookingId);
        startActivity(intent);
    }

    @Override
    public void enableBooking()
    {
        tv_home_ride_request.setEnabled(true);
        tv_home_ride_request.setBackgroundDrawable(selector_layout);
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
                    null,null);
        }
    }

    @Override
    public void showRideRateCard()
    {
        Utility.printLog(TAG+" show rate card ");
        rideRateCardDialog.show();
    }

    @Override
    public void disableCashOption() {
        paymentOptionsBottomSheet.disableCashOption(this,null);
    }

    @Override
    public void disableCardOption(boolean disable) {
        paymentOptionsBottomSheet.disableCardOption(this,null,disable);
    }

    @Override
    public void disableWalletOption()
    {
        paymentOptionsBottomSheet.disableWalletOption(this,null);
    }

    @Override
    public void hideWalletOption()
    {
        paymentOptionsBottomSheet.hideWalletOption(this,null);
    }

    @Override
    public void enableWalletOption(String amount)
    {
        paymentOptionsBottomSheet.enableWalletOption(this,null,amount);
    }

    @Override
    public void openPaymentScreen()
    {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(IS_FROM_BOOKING,true);
        intent.putExtra(SCREEN_TITLE,add_card_booking);
        startActivityForResult(intent, CHANGE_CARD_REQUEST);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    @Override
    public void setCashPaymentOption()
    {
        tv_home_select_payment.setText(cash);
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(ic_payment_cash_icon,null,ic_invoice_one_tick_mark_on,null);
        homeFragmentPresenter.setCashPaymentOption();
    }

    @Override
    public void setWalletPaymentOption(String walletText)
    {
        homeFragmentPresenter.setWalletPaymentOption(walletText);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setWalletAmount(String walletAmount)
    {
        tv_home_select_payment.setText(wallet_1+" ("+walletAmount+")");
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(ic_payment_wallet_icon,null,ic_invoice_one_tick_mark_on,null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setSelectedCard(String lastDigits,String brand)
    {
        Utility.printLog(TAG+" CARD setSelectedCard ");
        Drawable cardBrand = getResources().getDrawable(BRAND_RESOURCE_MAP.get(brand));
        tv_home_select_payment.setText(card_ending_with+" "+lastDigits);
        tv_home_select_payment.setCompoundDrawablesWithIntrinsicBounds(cardBrand,null,ic_invoice_one_tick_mark_on,null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void plotCurrentZone(PolygonOptions polygonOptions,String zoneTitle,String plotCurrentZone)
    {
        if(currentUserZone == null)
            currentUserZone = googleMap.addPolygon(polygonOptions);
        else
        {
            currentUserZone.remove();
            currentUserZone = googleMap.addPolygon(polygonOptions);

            for(String marker : currentZonePickMarkers.keySet())
                currentZonePickMarkers.get(marker).remove();

            currentZonePickMarkers.clear();
        }
        ll_home_current_pickups.setVisibility(VISIBLE);
        ll_home_current_pickups.startAnimation(anim_in);
        rl_home_current_address.setVisibility(GONE);
        tv_home_zone_name.setText(at+" "+zoneTitle);
    }

    @Override
    public void clearCurrentZone() {
        if(currentUserZone!=null)
        {
            currentUserZone.remove();
            currentUserZone=null;
            for (String key : currentZonePickMarkers.keySet())
            {
                Marker marker = currentZonePickMarkers.get(key);
                if(marker!=null)
                    marker.remove();
            }
            currentZonePickMarkers.clear();
        }
        ll_home_current_pickups.clearAnimation();
        ll_home_current_pickups.setVisibility(GONE);
        rl_home_current_address.setVisibility(View.VISIBLE);
    }

    @Override
    public void addMarkerForPickup(LatLng latLng,String pickZoneId)
    {
        Set<String> markers=  currentZonePickMarkers.keySet();
        if(!markers.contains(pickZoneId) && googleMap!=null)
        {
            Utility.printLog(TAG+" marker contains "+pickZoneId);
            Drawable shape = getResources().getDrawable(R.drawable.circle_unselected_zone_marker);
            Bitmap mDotMarkerBitmap = getBitmapFromShape(getActivity(),shape);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .anchor(.5f, .5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)));

            currentZonePickMarkers.put(pickZoneId,marker);
        }
    }

    @Override
    public void nearestPickupGate(String pickupGateId,String pickTitle)
    {
        for (int i =0;i<pickUpGates.size();i++)
        {
            if(pickUpGates.get(i).getId().equals(pickupGateId))
            {
                pickUpGates.get(i).setSelected(true);
                rv_home_zones.scrollToPosition(i);
            }
            else
                pickUpGates.get(i).setSelected(false);

            zonePickupsAdapter.notifyPickupPointsDetails(pickUpGates,this);
        }
        homeFragmentPresenter.setPickUpZoneDetails(pickupGateId,pickTitle);
        Utility.printLog(TAG+" marker contains "+pickupGateId);
        unSelectMarkers();
        Drawable shape = getResources().getDrawable(R.drawable.circle_selected_zone_marker);
        Bitmap mDotMarkerBitmap = getBitmapFromShape(getActivity(),shape);
        currentZonePickMarkers.get(pickupGateId).setIcon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentZonePickMarkers.get(pickupGateId).getPosition()).zoom(16.00f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * <h2>unSelectMarkers</h2>
     * used to unselect all the pick marker zones
     */
    private void unSelectMarkers()
    {
        Set<String> stringSet = currentZonePickMarkers.keySet();
        Drawable shape = getResources().getDrawable(R.drawable.circle_unselected_zone_marker);
        Bitmap mDotMarkerBitmap = getBitmapFromShape(getActivity(),shape);
        for(String pickId : stringSet)
            currentZonePickMarkers.get(pickId).setIcon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
    }

    @Override
    public void showDriverCancelDialog(String message)
    {
        Dialog driverCancellationDialog = alerts.userPromptWithOneButton(message,getActivity());
        TextView tv_alert_ok =  driverCancellationDialog.findViewById(R.id.tv_alert_ok);
        tv_alert_ok.setOnClickListener(v -> driverCancellationDialog.dismiss());
        driverCancellationDialog.show();
    }

    @Override
    public void showDefaultCard(String lastDigits, String cardBrand,int paymentType,boolean isWalletSelected)
    {
        paymentOptionsBottomSheet.showPaymentOptions(lastDigits,cardBrand,paymentType,this,
                null,isWalletSelected);
    }

    @Override
    public void showLaterBookingTimer(boolean change)
    {
        timePickerDialog.isToChange(change);
        timePickerDialog.show();
    }

    @Override
    public void showSomeOneBookingLayout(int bookingType)
    {
        if(!someOneRideBottomSheet.isAdded())
            someOneRideBottomSheet.checkBookingTypeAndShow(getChildFragmentManager(),bookingType,this);
    }

    @Override
    public void populateUserDetails(String phNumber, String userName, String imageUrl)
    {
        someOneRideBottomSheet.setDefaultProfile(phNumber,userName,imageUrl);
    }

    @Override
    public void askContactPermission()
    {
        if (appPermissionsRunTime.checkIfPermissionNeeded())
        {
            if(appPermissionsRunTime.checkIfPermissionGrant(Manifest.permission.READ_CONTACTS,getActivity()))
                someOneRideBottomSheet.addContact();
            else
            {
                String[] strings = {Manifest.permission.READ_CONTACTS};
                appPermissionsRunTime.requestForPermission(strings,this,READ_CONTACTS_PERMISSIONS_REQUEST);
            }
        }
        else
            someOneRideBottomSheet.addContact();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST)
        {
            int status =  appPermissionsRunTime.getPermissionStatus(getActivity(),
                    Manifest.permission.READ_CONTACTS, false);
            switch (status)
            {
                case PERMISSION_GRANTED:
                    someOneRideBottomSheet.addContact();
                    break;

                case PERMISSION_BLOCKED:
                    Toast.makeText(getActivity(),emergency_contact_alert,Toast.LENGTH_LONG).show();
                    break;

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
            }
        }
    }

    @Override
    public void showAlertForInvalidNumber()
    {
        Dialog dialog = alerts.userPromptWithOneButton(emergency_alert,getActivity());
        TextView tv_alert_title = dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_ok = dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_title.setText(sorry_alert);
        tv_alert_ok.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showAlertForMultipleNumber()
    {
        Dialog dialog = alerts.userPromptWithOneButton(emergency_alert_multiple,getActivity());
        TextView tv_alert_title = dialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_ok = dialog.findViewById(R.id.tv_alert_ok);
        tv_alert_title.setText(sorry_alert);
        tv_alert_ok.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
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
        ArrayAdapter<String> monthDataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, seatSpinnerList);
        // Drop down layout style - list view with radio button
        monthDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnr_home_select_capacity.setAdapter(monthDataAdapter);

        //Set the listener for when each option is clicked.
        spnr_home_select_capacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) view).setTextColor(vehicle_unselect_color);
                ((TextView) view).setTextSize(11);
                ((TextView) view).setTypeface(appTypeface.getPro_News());
                position++;
                String numberOfSeatSelected = position+"";
                Utility.printLog("selected capacity "+numberOfSeatSelected);
                homeFragmentPresenter.handleCapacityChose(numberOfSeatSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public void addContact(String name, String number)
    {
        someOneRideBottomSheet.setData(name,number);
    }
}