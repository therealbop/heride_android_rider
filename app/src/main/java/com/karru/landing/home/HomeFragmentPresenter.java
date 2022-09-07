package com.karru.landing.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.ApplicationVersion;
import com.karru.RxAppVersionObserver;
import com.karru.api.NetworkService;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.countrypic.Country;
import com.karru.countrypic.CountryPicker;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.corporate.CorporateProfileModel;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.landing.home.model.AreaZoneDetails;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.landing.home.model.DriverDetailsModel;
import com.karru.landing.home.model.DriverPreferenceDataModel;
import com.karru.landing.home.model.DriverPreferenceModel;
import com.karru.landing.home.model.HomeActivityModel;
import com.karru.landing.home.model.MQTTResponseDataModel;
import com.karru.landing.home.model.OnGoingBookingsModel;
import com.karru.landing.home.model.PickUpGates;
import com.karru.landing.home.model.PromoCodeModel;
import com.karru.landing.home.model.VehicleTypesDetails;
import com.karru.landing.home.model.eta_model.ETADataModel;
import com.karru.landing.home.model.eta_model.ElementsForEtaModel;
import com.karru.landing.home.model.fare_estimate_model.ExtraFeesModel;
import com.karru.landing.home.model.fare_estimate_model.FareEstimateModel;
import com.karru.landing.payment.model.CardDetails;
import com.karru.managers.RxConfirmPickNotifier;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.karru.managers.booking.RxOnGoingBookingsDetailsObserver;
import com.karru.managers.booking.RxRequestingDetailsObserver;
import com.karru.managers.location.AddressProviderFromLocation;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxAddressObserver;
import com.karru.managers.location.RxLocationObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.managers.user_vehicles.RxCurrentZoneObserver;
import com.karru.managers.user_vehicles.RxVehicleDetailsObserver;
import com.karru.util.ActivityUtils;
import com.karru.util.DataParser;
import com.karru.util.DateFormatter;
import com.karru.util.ExpireSession;
import com.karru.util.TextUtil;
import com.karru.util.TimezoneMapper;
import com.karru.util.Validation;
import com.karru.util.path_plot.DownloadPathUrl;
import com.karru.util.path_plot.GetGoogleDirectionsUrl;
import com.karru.util.path_plot.LatLongBounds;
import com.karru.util.path_plot.RotateKey;
import com.karru.util.path_plot.RxRoutePathObserver;
import com.karru.utility.Scaler;
import com.karru.utility.Utility;
import com.heride.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static com.karru.utility.Constants.AMOUNT;
import static com.karru.utility.Constants.APP_VERSION;
import static com.karru.utility.Constants.BOOKING_DATE;
import static com.karru.utility.Constants.BOOKING_TYPE;
import static com.karru.utility.Constants.BOTH;
import static com.karru.utility.Constants.CARD;
import static com.karru.utility.Constants.CARD_BRAND;
import static com.karru.utility.Constants.CARD_TOKEN;
import static com.karru.utility.Constants.CASH;
import static com.karru.utility.Constants.DISTANCE;
import static com.karru.utility.Constants.DISTANCE_FARE;
import static com.karru.utility.Constants.DONT_PAY_BY_WALLET;
import static com.karru.utility.Constants.DRIVER_HEIGHT;
import static com.karru.utility.Constants.DRIVER_PREF;
import static com.karru.utility.Constants.DRIVER_WIDTH;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DROP_ADDRESS_REQUEST;
import static com.karru.utility.Constants.DURATION;
import static com.karru.utility.Constants.ESTIMATE_ID;
import static com.karru.utility.Constants.FAV_DRIVERS;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.GUEST_COUNRY_CODE;
import static com.karru.utility.Constants.GUEST_NAME;
import static com.karru.utility.Constants.GUEST_NUMBER;
import static com.karru.utility.Constants.GUEST_ROOM_NUMBER;
import static com.karru.utility.Constants.HOME_FRAG;
import static com.karru.utility.Constants.HOTEL_USER_ID;
import static com.karru.utility.Constants.HOTEL_USER_TYPE;
import static com.karru.utility.Constants.INSTITUTE_ID;
import static com.karru.utility.Constants.IS_PARTNER_TYPE;
import static com.karru.utility.Constants.IS_TOWING_ENABLE;
import static com.karru.utility.Constants.LAST_DIGITS;
import static com.karru.utility.Constants.LATER_BOOKING_TYPE;
import static com.karru.utility.Constants.NOW_BOOKING_TYPE;
import static com.karru.utility.Constants.PAYMENT_OPTION;
import static com.karru.utility.Constants.PAYMENT_TYPE;
import static com.karru.utility.Constants.PAY_BY_WALLET;
import static com.karru.utility.Constants.PICK_ADDRESS;
import static com.karru.utility.Constants.PICK_GATE_ID;
import static com.karru.utility.Constants.PICK_GATE_TITLE;
import static com.karru.utility.Constants.PICK_ID;
import static com.karru.utility.Constants.PICK_LAT;
import static com.karru.utility.Constants.PICK_LONG;
import static com.karru.utility.Constants.PICK_ZONE_ID;
import static com.karru.utility.Constants.PICK_ZONE_TITLE;
import static com.karru.utility.Constants.PROMO_CODE;
import static com.karru.utility.Constants.RESPONSE_IN_MQTT;
import static com.karru.utility.Constants.SOMEONE_ELSE_BOOKING;
import static com.karru.utility.Constants.SOMEONE_NAME;
import static com.karru.utility.Constants.SOMEONE_NUMBER;
import static com.karru.utility.Constants.TIME_FARE;
import static com.karru.utility.Constants.UI_ANIMATION_DELAY;
import static com.karru.utility.Constants.VEHICLE_CAPACITY;
import static com.karru.utility.Constants.VEHICLE_ID;
import static com.karru.utility.Constants.VEHICLE_IMAGE;
import static com.karru.utility.Constants.VEHICLE_NAME;
import static com.karru.utility.Constants.isToUpdateAlertVisible;

/**
 * <h1>HomeFragmentPresenter</h1>
 * This class is used to do operations of Homepage
 *
 * @since 17/08/17.
 */
public class HomeFragmentPresenter implements HomeFragmentContract.Presenter, RotateKey {
    private final String TAG = "HomeFragmentPresenter";

    @Inject
    Context mContext;
    @Inject
    Gson gson;
    @Inject
    com.karru.util.Utility utility;
    @Inject
    MQTTManager mqttManager;
    @Inject
    NetworkService networkService;
    @Inject
    SQLiteDataSource addressDataSource;
    @Inject
    HomeActivityModel mHomActivityModel;
    @Inject
    NetworkStateHolder networkStateHolder;
    @Inject
    FavAddressDataModel favAddressDataModel;
    @Inject
    @Named(HOME_FRAG)
    CompositeDisposable compositeDisposable;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    LocationProvider locationProvider;
    @Inject
    DateFormatter dateFormatter;
    @Inject
    AddressProviderFromLocation addressProvider;
    @Inject
    MQTTResponseDataModel mqttResponseDataModel;
    @Inject
    ArrayList<ContactDetails> favDriversList;
    @Inject
    DriverPreferenceModel driverPreferenceModel;
    @Inject
    ArrayList<DriverPreferenceDataModel> tempPreferenceDataModels;

    private FareEstimateModel fareEstimateModel;
    private HomeFragmentContract.View homeActivityView;
    private RxLocationObserver rxLocationObserver;
    private RxAddressObserver rxAddressObserver;
    private String zoneId = "";
    private RxCurrentZoneObserver rxCurrentZoneObserver;
    private RxAppVersionObserver rxAppVersionObserver;
    private RxVehicleDetailsObserver rxVehicleDetailsObserver;
    private RxLiveBookingDetailsObserver rxBookingDetailsObserver;
    private RxRequestingDetailsObserver rxRequestingDetailsObserver;
    private RxOnGoingBookingsDetailsObserver rxOnGoingBookingDetailsObserver;
    private RxConfirmPickNotifier rxConfirmPickNotifier;
    private RxRoutePathObserver rxRoutePathObserver;
    private ApplicationVersion applicationVersion;
    private ArrayList<ReceiptDetails> listOfBreakDown;

    // Delayed removal of status and navigation bar
    // Note that some of these constants are new as of API 16 (Jelly Bean)
    // and API 19 (KitKat). It is safe to use them, as they are inlined
    // at compile-time and do nothing on earlier devices.
    private final Runnable startAnimationThread;
    // Delayed display of UI elements
    private final Runnable hideAnimationThread;

    @Nullable
    private Timer myTimer_publish;
    private String selectedPickId = "", favoriteDriverId = "";
    private Disposable disposable, locationDisposable, addressDisposable, confirmClickDisposable,
            vehicleTypeDisposable, appVersionDispose, pathPlotDisposable, etaDisposable;
    private int maxCapacity;
    private LatLng pathPlotOrigin, pathPlotDest;
    private final Handler mHideHandler = new Handler();
    private ArrayList<String> prevDriverPrefModel = new ArrayList<>();
    /*https://github.com/antoniolg/androidmvp/blob/master/app/src/main/java/com/antonioleiva/mvpexample/app/Login/LoginInteractorImpl.java*/

    @Inject
    HomeFragmentPresenter(RxLocationObserver rxLocationObserver, RxAddressObserver rxAddressObserver,
                          RxVehicleDetailsObserver rxVehicleDetailsObserver, RxLiveBookingDetailsObserver rxBookingDetailsObserver,
                          RxOnGoingBookingsDetailsObserver rxOnGoingBookingDetailsObserver,
                          RxCurrentZoneObserver rxCurrentZoneObserver, RxConfirmPickNotifier rxConfirmPickNotifier,
                          RxRoutePathObserver rxRoutePathObserver, RxRequestingDetailsObserver rxRequestingDetailsObserver,
                          RxAppVersionObserver rxAppVersionObserver, ApplicationVersion applicationVersion) {
        this.rxVehicleDetailsObserver = rxVehicleDetailsObserver;
        this.rxLocationObserver = rxLocationObserver;
        this.rxAddressObserver = rxAddressObserver;
        this.rxCurrentZoneObserver = rxCurrentZoneObserver;
        this.rxRoutePathObserver = rxRoutePathObserver;
        this.applicationVersion = applicationVersion;
        this.rxAppVersionObserver = rxAppVersionObserver;
        this.rxRequestingDetailsObserver = rxRequestingDetailsObserver;
        this.rxBookingDetailsObserver = rxBookingDetailsObserver;
        this.rxOnGoingBookingDetailsObserver = rxOnGoingBookingDetailsObserver;
        this.rxConfirmPickNotifier = rxConfirmPickNotifier;
        startAnimationThread = () -> homeActivityView.startAnimationWhenMapMoves();
        hideAnimationThread = () -> homeActivityView.startAnimationWhenMapStops();
        fareEstimateModel = FareEstimateModel.getInstance();
    }

    /**
     * <h2>subscribeBookingDetails</h2>
     * This method is used to subscribe to the booking details published
     */
    private void subscribeBookingDetails() {
        rxBookingDetailsObserver.subscribeOn(Schedulers.io());
        rxBookingDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        disposable = rxBookingDetailsObserver.subscribeWith(new DisposableObserver<BookingDetailsDataModel>() {
            @Override
            public void onNext(BookingDetailsDataModel bookingDetailsDataModel) {
                Utility.printLog(TAG + " booking details observed  " + bookingDetailsDataModel.getBookingId());
                Utility.printLog(TAG + " booking details on going  observed  " + mqttResponseDataModel.getOngoingBookings().size());

                for (int bookingsCount = 0; bookingsCount < mqttResponseDataModel.getOngoingBookings().size(); bookingsCount++) {
                    if (bookingDetailsDataModel.getBookingId().equals(mqttResponseDataModel.getOngoingBookings().get(bookingsCount).getBookingId())) {
                        mqttResponseDataModel.getOngoingBookings().get(bookingsCount).setBookingId(bookingDetailsDataModel.getBookingId());
                        mqttResponseDataModel.getOngoingBookings().get(bookingsCount).setBookingStatus(bookingDetailsDataModel.getStatus());
                        mqttResponseDataModel.getOngoingBookings().get(bookingsCount).setBookingStatusText(bookingDetailsDataModel.getStatusText());
                        mqttResponseDataModel.getOngoingBookings().get(bookingsCount).setDriverName(bookingDetailsDataModel.getDriver().getName());

                        OnGoingBookingsModel onGoingBookingsModel = new OnGoingBookingsModel();
                        onGoingBookingsModel.setDriverName(bookingDetailsDataModel.getDriver().getName());
                        onGoingBookingsModel.setBookingId(bookingDetailsDataModel.getBookingId());
                        onGoingBookingsModel.setBookingStatus(bookingDetailsDataModel.getStatus());
                        onGoingBookingsModel.setBookingStatusText(bookingDetailsDataModel.getStatusText());

                        homeActivityView.notifyOnGoingItemDetails(bookingsCount, onGoingBookingsModel);
                        break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " booking details onAddCardError  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(disposable);
            }
        });
    }

    /**
     * <h2>subscribeRequestBookingDetails</h2>
     * This method is used to subscribe to the booking details for the booking requested published
     */
    private void subscribeRequestBookingDetails() {
        rxRequestingDetailsObserver.subscribeOn(Schedulers.io());
        rxRequestingDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        disposable = rxRequestingDetailsObserver.subscribeWith(new DisposableObserver<RequestBookingDetails>() {
            @Override
            public void onNext(RequestBookingDetails requestBookingDetailsModel) {
                Utility.printLog(TAG + " booking details requesting observed  " +
                        requestBookingDetailsModel.getBookingId() + " " + disposable.isDisposed());
                if (!disposable.isDisposed())
                    homeActivityView.openRequestingScreenKilled(requestBookingDetailsModel);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " booking details requesting  " + e);
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });
    }

    /**
     * <h2>subscribeCurrentZone</h2>
     * This method is used to subscribe to the current zone published
     */
    private void subscribeCurrentZone() {
        rxCurrentZoneObserver.subscribeOn(Schedulers.io());
        rxCurrentZoneObserver.observeOn(AndroidSchedulers.mainThread());
        disposable = rxCurrentZoneObserver.subscribeWith(new DisposableObserver<AreaZoneDetails>() {
            @Override
            public void onNext(AreaZoneDetails areaZoneDetails) {
                Utility.printLog(TAG + " area zone observed  " + areaZoneDetails.getId() + " " + zoneId);
                if (mHomActivityModel.isMapReady()) {
                    if (areaZoneDetails.getId() != null) {
                        if (!areaZoneDetails.getId().equals(zoneId))
                            populateZoneAndPickups(areaZoneDetails);

                        calculateNearestPickUp(areaZoneDetails);
                    } else
                        clearAllZoneDetails();
                } else
                    triggerVehicleDetails(true);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " area zone onAddCardError  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(disposable);
            }
        });
    }

    /**
     * <h2>subscribeRoutePoints</h2>
     * This method is used to subscribe to the path points from origin to destination
     */
    private void subscribeRoutePoints() {
        rxRoutePathObserver.subscribeOn(Schedulers.io());
        rxRoutePathObserver.observeOn(AndroidSchedulers.mainThread());
        pathPlotDisposable = rxRoutePathObserver.subscribeWith(new DisposableObserver<LatLongBounds>() {
            @Override
            public void onNext(LatLongBounds latLongBounds) {
                Utility.printLog(TAG + "  path plot route " + latLongBounds.getNortheast());
                homeActivityView.googlePathPlot(latLongBounds);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " path plot route  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(pathPlotDisposable);
            }
        });
    }

    /**
     * <h2>calculateNearestPickUp</h2>
     * used to calculate nearest pickup point from user current point
     *
     * @param areaZoneDetails area zone details
     */
    private void calculateNearestPickUp(AreaZoneDetails areaZoneDetails) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(Double.parseDouble(preferenceHelperDataSource.getCurrLatitude()));
        startPoint.setLongitude(Double.parseDouble(preferenceHelperDataSource.getCurrLongitude()));

        ArrayList<Float> listOfDistances = new ArrayList<>();
        for (PickUpGates pickUpGates : areaZoneDetails.getPickups()) {
            LatLng latLng = new LatLng(pickUpGates.getLocation().getLatitude(), pickUpGates.getLocation().getLongitude());
            homeActivityView.addMarkerForPickup(latLng, pickUpGates.getId());
            Location endPoint = new Location("locationB");
            endPoint.setLatitude(latLng.latitude);
            endPoint.setLongitude(latLng.longitude);
            listOfDistances.add(startPoint.distanceTo(endPoint));
        }

        int indexForSelectionPoint = listOfDistances.indexOf(Collections.min(listOfDistances));
        Utility.printLog(TAG + " marker unselected pick ID " + areaZoneDetails.getPickups().get(indexForSelectionPoint).getId());
        if (!selectedPickId.equals(areaZoneDetails.getPickups().get(indexForSelectionPoint).getId())) {
            selectedPickId = areaZoneDetails.getPickups().get(indexForSelectionPoint).getId();
            homeActivityView.nearestPickupGate(selectedPickId,
                    areaZoneDetails.getPickups().get(indexForSelectionPoint).getTitle());
        }
    }

    /**
     * <h2>populateZoneAndPickups</h2>
     * used to plot the zone and populate related zones
     *
     * @param areaZoneDetails area zone details
     */
    private void populateZoneAndPickups(AreaZoneDetails areaZoneDetails) {
        zoneId = areaZoneDetails.getId();
        ArrayList<LatLng> arrayPoints = new ArrayList<>();
        for (com.karru.landing.home.model.Location latLng : areaZoneDetails.getPointsProps().getPaths()) {
            arrayPoints.add(new LatLng(Double.parseDouble(latLng.getLat()), Double.parseDouble(latLng.getLng())));
        }
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(arrayPoints);
        polygonOptions.strokeColor(Color.parseColor(areaZoneDetails.getPointsProps().getStrokeColor()));
        polygonOptions.strokeWidth(areaZoneDetails.getPointsProps().getStrokeWeight());

        mHomActivityModel.setAreaZoneId(areaZoneDetails.getId());
        mHomActivityModel.setAreaZoneTitle(areaZoneDetails.getTitle());
        homeActivityView.plotCurrentZone(polygonOptions, areaZoneDetails.getTitle() + ", " + areaZoneDetails.getCity(),
                areaZoneDetails.getId());

        homeActivityView.notifyZonePickups(areaZoneDetails.getPickups());
    }

    /**
     * <h2>clearAllZoneDetails</h2>
     * used to clear all zone related details
     */
    private void clearAllZoneDetails() {
        selectedPickId = "";
        zoneId = "";
        mHomActivityModel.setAreaZoneId("");
        mHomActivityModel.setAreaZoneTitle("");
        mHomActivityModel.setAreaPickupId("");
        mHomActivityModel.setAreaPickupTitle("");
        homeActivityView.clearCurrentZone();
    }

    @Override
    public void setPickUpZoneDetails(String pickId, String pickTitle) {
        mHomActivityModel.setAreaPickupId(pickId);
        mHomActivityModel.setAreaPickupTitle(pickTitle);
    }

    @Override
    public void attachView(Object view) {
        this.homeActivityView = (HomeFragmentContract.View) view;
    }

    @Override
    public void detachView() {
        this.homeActivityView = null;
    }

    /**
     * <h2>subscribeVehicleTypesDetails</h2>
     * This method is used to subscribe to the vehicle types published
     */
    private void subscribeVehicleTypesDetails() {
        rxVehicleDetailsObserver.subscribeOn(Schedulers.io());
        rxVehicleDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        vehicleTypeDisposable = rxVehicleDetailsObserver.subscribeWith(new DisposableObserver<ArrayList<VehicleTypesDetails>>() {
            @Override
            public void onNext(ArrayList<VehicleTypesDetails> vehicleTypesDetails) {
                Utility.printLog(TAG + " MQTT TESTING vehicle types size observed  " + vehicleTypesDetails.size() + " " + homeActivityView);
                if (vehicleTypesDetails.size() > 0) {
                    int currencyAbbr = preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr();
                    String currencySymbol = preferenceHelperDataSource.getWalletSettings().getCurrencySymbol();
                    boolean enable = preferenceHelperDataSource.getWalletSettings().isWalletEnable();
                    if ((preferenceHelperDataSource.getLoginType() == 0 || (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))
                            && enable)
                        homeActivityView.checkForWallet(true, utility.currencyAdjustment(currencyAbbr, currencySymbol,
                                preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                    else
                        homeActivityView.checkForWallet(false, utility.currencyAdjustment(currencyAbbr, currencySymbol,
                                preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                    if ((preferenceHelperDataSource.getLoginType() == 0 || (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))
                            && preferenceHelperDataSource.isCardEnable())
                        homeActivityView.checkForCard(true);
                    else
                        homeActivityView.checkForCard(false);


                    if (preferenceHelperDataSource.getLoginType() != 0 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2) {
                        homeActivityView.checkForFavorite(false);
                    } else {
                        homeActivityView.checkForFavorite(preferenceHelperDataSource.isFavDriverEnable());
                    }
                    if (preferenceHelperDataSource.getLoginType() == 0 && preferenceHelperDataSource.isCorporateEnable())
                        homeActivityView.checkForCorporate(true);
                    else
                        homeActivityView.checkForCorporate(false);


                    if (preferenceHelperDataSource.getLoginType() != 0 && preferenceHelperDataSource.isTowingEnable()) {
                        homeActivityView.checkForTowing(false);
                    } else {
                        homeActivityView.checkForTowing(true);
                    }
                    if (preferenceHelperDataSource.getLoginType() != 0 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2) {
                        homeActivityView.checkForReferral(false);
                    } else {
                        homeActivityView.checkForReferral(preferenceHelperDataSource.isReferralCodeEnabled());
                    }

                    mHomActivityModel.setVehicleTypesDetails(vehicleTypesDetails);
                    homeActivityView.onVehicleTypesFound();

                    if (mHomActivityModel.isPreferenceEnabled())
                        homeActivityView.showDriverPreferenceView();
                    else
                        homeActivityView.hideDriverPreferenceView();

                    if (preferenceHelperDataSource.isCorporateEnable() && preferenceHelperDataSource.getLoginType() == 0)
                        homeActivityView.showCorporate();
                    else
                        homeActivityView.hideCorporate();

                    if (mHomActivityModel.getSelectedVehicleId() != null) {
                        boolean driversListSelectedType = false;
                        mHomActivityModel.getVehicleIdsHavingDrivers().clear();
                        mHomActivityModel.setNearestDriverLatLngEachType("");
                        Utility.printLog(TAG + " TESTING OTHER vehicle types selected ");

                        for (int vehicleTypesCount = 0; vehicleTypesCount < vehicleTypesDetails.size(); vehicleTypesCount++) {
                            Utility.printLog(TAG + " onNext homeFragmentPresenter.getSelectedVehicleId() "
                                    + mHomActivityModel.getEtaOfEachType().get(vehicleTypesDetails.get(vehicleTypesCount).getTypeId()));

                            if (!driversListSelectedType && vehicleTypesDetails.get(vehicleTypesCount).getTypeId().equals(mHomActivityModel.getSelectedVehicleId())) {
                                driversListSelectedType = true;
                                ArrayList<String> latestDrivers = new ArrayList<>();
                                for (DriverDetailsModel driverDetailsModel : vehicleTypesDetails.get(vehicleTypesCount).getDrivers()) {
                                    latestDrivers.add(driverDetailsModel.getMasterId());
                                }
                                checkForOfflineDrivers(latestDrivers);

                                if (!vehicleTypesDetails.get(vehicleTypesCount).isSpecialType()) {
                                    Utility.printLog(TAG + " onBitmapLoaded of image download if " + vehicleTypesDetails.get(vehicleTypesCount).getTypeId());
                                    mHomActivityModel.setDriversListSelectedType(vehicleTypesDetails.get(vehicleTypesCount).getDrivers());
                                    checkForOnlineDrivers();
                                } else {
                                    Utility.printLog(TAG + " onBitmapLoaded of image download else " + vehicleTypesDetails.get(vehicleTypesCount).getTypeId());
                                    mHomActivityModel.getDriversListSelectedType().clear();
                                    for (int specialType = 0; specialType < vehicleTypesDetails.get(vehicleTypesCount).getSpecialTypes().size();
                                         specialType++) {
                                        for (int vehicle = 0; vehicle < vehicleTypesDetails.size(); vehicle++) {
                                            if (vehicleTypesDetails.get(vehicle).getTypeId().equals(vehicleTypesDetails.get(vehicleTypesCount).
                                                    getSpecialTypes().get(specialType))) {
                                                for (DriverDetailsModel driverDetailsModel : vehicleTypesDetails.get(vehicle).getDrivers()) {
                                                    if (driverDetailsModel.getVehicleTypeIds().contains(vehicleTypesDetails.get(vehicleTypesCount).getTypeId())) {
                                                        mHomActivityModel.getDriversListSelectedType().add(driverDetailsModel);
                                                        favDriversList.clear();
                                                        favoriteDriverId = "";
                                                        Utility.printLog(TAG + " special type vehicls 1 " + vehicleTypesDetails.get(vehicle).getVehicleMapIcon());
                                                        Utility.printLog(TAG + " special type vehicls 2 " + vehicleTypesDetails.get(vehicleTypesCount).getVehicleMapIcon());
                                                        LatLng latLng = new LatLng(driverDetailsModel.getLatitude(), driverDetailsModel.getLongitude());
                                                        Glide.with(mContext)
                                                                .asBitmap()
                                                                .load(vehicleTypesDetails.get(vehicleTypesCount).getVehicleMapIcon())
                                                                .listener(new RequestListener<Bitmap>() {
                                                                    @Override
                                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                                        Utility.printLog(TAG + " TEST bitmap 2 of onBitmapFailed download ");
                                                                        return false;
                                                                    }

                                                                    @Override
                                                                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                                        if (driverDetailsModel.isFavouriteDriver()) {
                                                                            if (favoriteDriverId.equals(""))
                                                                                favoriteDriverId = driverDetailsModel.getMasterId();
                                                                            else
                                                                                favoriteDriverId = favoriteDriverId + "," + driverDetailsModel.getMasterId();

                                                                            ContactDetails contactDetails = new ContactDetails(driverDetailsModel.getFullName());
                                                                            contactDetails.setImgUrl(driverDetailsModel.getProfilePic());
                                                                            contactDetails.setMasterId(driverDetailsModel.getMasterId());
                                                                            favDriversList.add(contactDetails);

                                                                            Bitmap background = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.home_star_icon);
                                                                            bitmap = createSingleImageFromMultipleImages(bitmap, background);
                                                                            Utility.printLog(TAG + " bitmap of custom image 2 " + bitmap.toString());
                                                                            homeActivityView.plotOnlineDriverMarkers(driverDetailsModel.getMasterId(), latLng, mHomActivityModel.getDriverMarkerWidth(),
                                                                                    mHomActivityModel.getDriverMarkerHeight(), bitmap, preferenceHelperDataSource.getCustomerApiInterval());
                                                                        } else
                                                                            homeActivityView.plotOnlineDriverMarkers(driverDetailsModel.getMasterId(), latLng, mHomActivityModel.getDriverMarkerWidth(),
                                                                                    mHomActivityModel.getDriverMarkerHeight(), bitmap, preferenceHelperDataSource.getCustomerApiInterval());

                                                                        homeActivityView.dismissProgressDialog();
                                                                        Utility.printLog(TAG + " TEST bitmap 2 of image download ");
                                                                        return false;
                                                                    }
                                                                }).submit();
                                                        if (favDriversList.size() > 0) {
                                                            ArrayList<ContactDetails> tempContactDetails = new ArrayList<>();
                                                            if (favDriversList.size() > 1)
                                                                tempContactDetails.add(new ContactDetails(mContext.getString(R.string.any_fav_driver)));
                                                            tempContactDetails.addAll(favDriversList);
                                                            tempContactDetails.add(new ContactDetails(mContext.getString(R.string.all_drivers)));
                                                            favDriversList.clear();
                                                            favDriversList.addAll(tempContactDetails);
                                                        }
                                                        homeActivityView.notifyAdapterWithFavDrivers();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Utility.printLog(TAG + " drivers online " + mHomActivityModel.getDriversListSelectedType().size());
                            }

                            if (!vehicleTypesDetails.get(vehicleTypesCount).isSpecialType()) {
                                if (vehicleTypesDetails.get(vehicleTypesCount).getDrivers().size() > 0)
                                    mHomActivityModel.getVehicleIdsHavingDrivers().add(vehicleTypesDetails.get(vehicleTypesCount).getTypeId());

                                if (vehicleTypesDetails.get(vehicleTypesCount).getDrivers().size() > 0) {
                                    mHomActivityModel.setNearestDriverLatLngEachType(mHomActivityModel.getNearestDriverLatLngEachType()
                                            + "|" + vehicleTypesDetails.get(vehicleTypesCount).getDrivers().get(0).getLatitude() + ","
                                            + vehicleTypesDetails.get(vehicleTypesCount).getDrivers().get(0).getLongitude());
                                }
                            } else {
                                TreeMap<Double, DriverDetailsModel> specialVehicleTypeDrivers = new TreeMap<>();
                                boolean isVehicleIdAdded = false;
                                for (int specialType = 0; specialType < vehicleTypesDetails.get(vehicleTypesCount).getSpecialTypes().size();
                                     specialType++) {
                                    for (int vehicle = 0; vehicle < vehicleTypesDetails.size(); vehicle++) {
                                        if (vehicleTypesDetails.get(vehicle).getTypeId().equals(vehicleTypesDetails.get(vehicleTypesCount).
                                                getSpecialTypes().get(specialType))) {
                                            for (DriverDetailsModel driverDetailsModel : vehicleTypesDetails.get(vehicle).getDrivers()) {
                                                if (driverDetailsModel.getVehicleTypeIds().contains(vehicleTypesDetails.get(vehicleTypesCount).getTypeId())) {
                                                    specialVehicleTypeDrivers.put(driverDetailsModel.getDistance(), driverDetailsModel);
                                                    if (!isVehicleIdAdded) {
                                                        isVehicleIdAdded = true;
                                                        mHomActivityModel.getVehicleIdsHavingDrivers().add(vehicleTypesDetails.get(vehicleTypesCount).getTypeId());
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                if (isVehicleIdAdded) {
                                    Map.Entry<Double, DriverDetailsModel> entry = specialVehicleTypeDrivers.entrySet().iterator().next();
                                    DriverDetailsModel detailsModel = entry.getValue();
                                    mHomActivityModel.setNearestDriverLatLngEachType(mHomActivityModel.getNearestDriverLatLngEachType()
                                            + "|" + detailsModel.getLatitude() + "," + detailsModel.getLongitude());
                                }
                            }
                        }

                        if (mHomActivityModel.getDriversListSelectedType().size() > 0)
                            homeActivityView.enableNowBooking();
                        else
                            homeActivityView.disableNowBooking();

                        prepareToGetETA();
                    }
                } else
                    homeActivityView.onVehicleTypesNotFound();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " vehicle types size onAddCardError  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(vehicleTypeDisposable);
            }
        });
    }

    /**
     * <h2>checkForOfflineDrivers</h2>
     * used to remove the offline drivers
     *
     * @param latestDrivers latest drivers MIDs
     */
    private void checkForOfflineDrivers(ArrayList<String> latestDrivers) {
        for (DriverDetailsModel driverDetailsModel : mHomActivityModel.getDriversListSelectedType()) {
            if (!latestDrivers.contains(driverDetailsModel.getMasterId()))
                homeActivityView.removeOfflineDriverMarker(driverDetailsModel.getMasterId());
        }
    }

    /**
     * <h2>subscribeOnGoingBookingDetails</h2>
     * This method is used to subscribe to the on going bookings published
     */
    private void subscribeOnGoingBookingDetails() {
        rxOnGoingBookingDetailsObserver.subscribeOn(Schedulers.io());
        rxOnGoingBookingDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        vehicleTypeDisposable = rxOnGoingBookingDetailsObserver.subscribeWith(new DisposableObserver<ArrayList<OnGoingBookingsModel>>() {
            @Override
            public void onNext(ArrayList<OnGoingBookingsModel> onGoingBookingsModels) {
                if (preferenceHelperDataSource.getLoginType() == 0) // for normal login show it
                {
                    mqttResponseDataModel.setOngoingBookings(onGoingBookingsModels);
                    Utility.printLog(TAG + " ongoing bookings size observed  " + onGoingBookingsModels.size());
                    homeActivityView.notifyOnGoingBookings(onGoingBookingsModels);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + "  ongoing bookings size onAddCardError  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(vehicleTypeDisposable);
            }
        });
    }

    /**
     * <h2>subscribeAppVersionConfig</h2>
     * This method is used to subscribe to the app verions settings
     */
    private void subscribeAppVersionConfig() {
        rxAppVersionObserver.subscribeOn(Schedulers.io());
        rxAppVersionObserver.observeOn(AndroidSchedulers.mainThread());
        appVersionDispose = rxAppVersionObserver.subscribeWith(new DisposableObserver<ApplicationVersion>() {
            @Override
            public void onNext(ApplicationVersion applicationVersion) {
                Utility.printLog(TAG + " subscribeAppVersionConfig observed  " +
                        applicationVersion.getCurrenctAppVersion() + " " + applicationVersion.isMandatoryUpdateEnable());
                checkForAppVersion();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + "  subscribeAppVersionConfig onError  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(appVersionDispose);
            }
        });
    }

    /**
     * <h2>checkForAppVersion</h2>
     * used to check for app version and show popup
     */
    private void checkForAppVersion() {
        Utility.printLog(TAG + " checkForAppVersion " + applicationVersion.getCurrenctAppVersion() + " "
                + applicationVersion.isMandatoryUpdateEnable());
        if (!isToUpdateAlertVisible && Validation.updateAppVersion
                (APP_VERSION, applicationVersion.getCurrenctAppVersion())) {
            isToUpdateAlertVisible = true;
            homeActivityView.showAppUpdateAlert(applicationVersion.isMandatoryUpdateEnable());
        }
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeLocationChange() {
        rxLocationObserver.subscribeOn(Schedulers.io());
        rxLocationObserver.observeOn(AndroidSchedulers.mainThread());
        locationDisposable = rxLocationObserver.subscribeWith(new DisposableObserver<Location>() {
            @Override
            public void onNext(Location location) {
                Utility.printLog(TAG + " onNext location oberved  " + location.getLatitude()
                        + " " + location.getLongitude());
                mHomActivityModel.setCurrentLatitude(location.getLatitude());
                mHomActivityModel.setCurrentLongitude(location.getLongitude());
                GMT_CURRENT_LAT = location.getLatitude() + "";
                GMT_CURRENT_LNG = location.getLongitude() + "";

                if (location.getLatitude() != 0 && location.getLatitude() != mHomActivityModel.getMapCenterLatitude()
                        && location.getLongitude() != 0 && location.getLongitude() != mHomActivityModel.getMapCenterLongitude()) {
                    preferenceHelperDataSource.setCurrLatitude(String.valueOf(mHomActivityModel.getMapCenterLatitude()));
                    preferenceHelperDataSource.setCurrLongitude(String.valueOf(mHomActivityModel.getMapCenterLongitude()));

                    if (mHomActivityModel.isMoveToCurrLocation()) {
                        mHomActivityModel.setMoveToCurrLocation(false);
                        mHomActivityModel.setLastKnownLocation(location);
                        mHomActivityModel.setMapCenterLatitude(location.getLatitude());
                        mHomActivityModel.setMapCenterLongitude(location.getLongitude());
                        mHomActivityModel.setPrevMapCenterLatitude(mHomActivityModel.getMapCenterLatitude());
                        mHomActivityModel.setPrevMapCenterLongitude(mHomActivityModel.getMapCenterLongitude());
                        homeActivityView.updateCameraPosition(mHomActivityModel.getMapCenterLatitude(), mHomActivityModel.getMapCenterLongitude());
                        Utility.printLog(TAG + " ETA TEST called from subscribeLocationChange ");
                        prepareToGetETA();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " onAddCardError location oberved  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(locationDisposable);
            }
        });
    }

    /**
     * <h2>subscribeConfirmClick</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeConfirmClick() {
        rxConfirmPickNotifier.subscribeOn(Schedulers.io());
        rxConfirmPickNotifier.observeOn(AndroidSchedulers.mainThread());
        confirmClickDisposable = rxConfirmPickNotifier.subscribeWith(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean isClicked) {
                Utility.printLog(TAG + " onNext confirm clicked  " + isClicked);
                homeActivityView.hideBottomConfirm();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " onAddCardError confirm clicked  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(confirmClickDisposable);
            }
        });
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeAddressChange() {
        rxAddressObserver.subscribeOn(Schedulers.io());
        rxAddressObserver.observeOn(AndroidSchedulers.mainThread());
        addressDisposable = rxAddressObserver.subscribeWith(new DisposableObserver<Address>() {
            @Override
            public void onNext(Address address) {
                Utility.printLog(TAG + " onNext address observed  " + address.toString());
                String full_address = DataParser.getStringAddress(address);
                Utility.printLog(TAG + " onNext getAddressFromLocation full_address: " + full_address);
                favAddressDataModel.setAddress(full_address);
                preferenceHelperDataSource.setPickUpAddress(favAddressDataModel.getAddress());

                verifyFavADrs(favAddressDataModel.getAddress());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG + " onAddCardError address observed  " + e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(addressDisposable);
            }
        });
    }

    @Override
    public void onCreateHomeActivity() {
        initVariables();
        mqttManager.handleVehiclesData(preferenceHelperDataSource.getVehicleDetailsResponse());
        triggerVehicleDetails(false);
        switch (preferenceHelperDataSource.getLoginType()) {
            case 0: // normal login
                mHomActivityModel.setPartnerUser(0);
                mHomActivityModel.setHotelUserType(0);
                mHomActivityModel.setGuestName("");
                mHomActivityModel.setGuestCountryCode("");
                mHomActivityModel.setGuestNumber("");
                mHomActivityModel.setGuestRoomNumber("");
                mHomActivityModel.setSelectedProfile(null);
                mHomActivityModel.setHotelUserId("");
                break;

            case 1: // hotel login
                mHomActivityModel.setHotelUserId(preferenceHelperDataSource.getHotelDataModel().getPartnerUserId());
                mHomActivityModel.setPartnerUser(1);
                mHomActivityModel.setHotelUserType(preferenceHelperDataSource.getHotelDataModel().getHotelUserType());
                homeActivityView.showHotelUI(preferenceHelperDataSource.getHotelDataModel().getHotelLogo(), preferenceHelperDataSource.getHotelDataModel().getHotelName());
                break;
        }
    }

    /**
     * <h2>initVariables</h2>
     * This method is used to initialize the variables
     */
    private void initVariables() {
        mHomActivityModel.setMapReady(false);
        clearDropAddress();
        mqttManager.handleVehiclesData(preferenceHelperDataSource.getVehicleDetailsResponse());
        mHomActivityModel.setLastKnownLocation(new Location("OLD"));
        mHomActivityModel.setFromOnCreateView(true);
        if (preferenceHelperDataSource.getCurrLatitude() != null && !preferenceHelperDataSource.getCurrLatitude().isEmpty()
                && preferenceHelperDataSource.getCurrLongitude() != null && !preferenceHelperDataSource.getCurrLongitude().isEmpty()) {
            mHomActivityModel.setPrevMapCenterLatitude(Double.parseDouble(preferenceHelperDataSource.getCurrLatitude()));
            mHomActivityModel.setMapCenterLatitude(mHomActivityModel.getPrevMapCenterLatitude());
            mHomActivityModel.setPrevMapCenterLongitude(Double.parseDouble(preferenceHelperDataSource.getCurrLongitude()));
            mHomActivityModel.setMapCenterLongitude(mHomActivityModel.getPrevMapCenterLongitude());
        }

        //to refresh the fav address list
        refreshFavAddressList(false);

        double[] size = Scaler.getScalingFactor(mContext);
        mHomActivityModel.setVehicleImageWidth((60) * size[0]);
        mHomActivityModel.setVehicleImageHeight((60) * size[1]);
        mHomActivityModel.setDriverMarkerWidth(size[0] * DRIVER_WIDTH);
        mHomActivityModel.setDriverMarkerHeight(size[1] * DRIVER_HEIGHT);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation() {
        Utility.printLog(TAG + " inside getCurrentLocation ");
        locationProvider.startLocation(this);
    }

    /**
     * <h2>getAddressFromLocation</h2>
     * This method is used to get the address from latlong
     *
     * @param currentLat latitude of the location
     * @param currentLng longitude of the location
     */
    private void getAddressFromLocation(final double currentLat, final double currentLng) {
        Utility.printLog(TAG + " address getAddressFromLocation  " + currentLat + " " + currentLng);
        favAddressDataModel.setLatitude(currentLat);
        favAddressDataModel.setLongitude(currentLng);
        addressProvider.getAddressFromLocation(currentLat, currentLng, preferenceHelperDataSource.getGoogleServerKey());
    }

    @Override
    public void addDriverPreferences(boolean isDoneClicked) {
        if (isDoneClicked) {
            driverPreferenceModel.setData(tempPreferenceDataModels);
            ArrayList<String> latestDriverPrefModel = new ArrayList<>();
            StringBuilder preferenceSelected = new StringBuilder();
            boolean firstPos = false;
            for (DriverPreferenceDataModel driverPreferenceDataModel : driverPreferenceModel.getData()) {
                if (driverPreferenceDataModel.isSelected()) {
                    if (!firstPos) {
                        firstPos = true;
                        preferenceSelected = new StringBuilder(driverPreferenceDataModel.getId());
                    } else
                        preferenceSelected.append(",").append(driverPreferenceDataModel.getId());
                    latestDriverPrefModel.add(driverPreferenceDataModel.getId());
                }
            }

            if (!String.valueOf(preferenceSelected).equals("")) {
                mHomActivityModel.setDriverPreference(String.valueOf(preferenceSelected));
                homeActivityView.showSelectedDriverPref(mContext.getString(R.string.driver_preference_set));
            } else {
                mHomActivityModel.setDriverPreference("");
                homeActivityView.showSelectedDriverPref(mContext.getString(R.string.driver_preference));
            }

            if (!(prevDriverPrefModel.containsAll(latestDriverPrefModel) && latestDriverPrefModel.containsAll(prevDriverPrefModel)) &&
                    !preferenceHelperDataSource.getDropAddress().equals(""))
                getApproxFareEstimation();

            if (!(prevDriverPrefModel.containsAll(latestDriverPrefModel) && latestDriverPrefModel.containsAll(prevDriverPrefModel)) &&
                    mHomActivityModel.isTowingEnabled() && preferenceHelperDataSource.getDropAddress().equals("") &&
                    !latestDriverPrefModel.contains("3"))
                getApproxFareEstimation();

            prevDriverPrefModel = latestDriverPrefModel;
            driverPreferenceModel.setSelectedDriverPref(prevDriverPrefModel);
        }
    }

    @Override
    public void refreshFavAddressList(boolean toRefreshAddress) {
        mHomActivityModel.getFavAddressDataList().clear();
        mHomActivityModel.getFavAddressDataList().addAll(addressDataSource.getFavAddresses());
        mHomActivityModel.setFavAddressDataList(mHomActivityModel.getFavAddressDataList());
        Utility.printLog(TAG + "fav address list size " + mHomActivityModel.getFavAddressDataList().size()
                + " boolean " + toRefreshAddress);
        if (toRefreshAddress)
            getAddressFromLocation(mHomActivityModel.getLastKnownLocation().getLatitude(),
                    mHomActivityModel.getLastKnownLocation().getLongitude());
    }

    @Override
    public void checkForGuestData(String guestName, String guestNumber, String guestRoomNo, String countryCode,
                                  int visibility) {
        if (TextUtil.isEmpty(guestName) || TextUtil.isEmpty(guestNumber) || TextUtil.isEmpty(guestRoomNo) ||
                visibility == VISIBLE)
            homeActivityView.showToast(mContext.getString(R.string.enter_details));
        else {
            mHomActivityModel.setGuestName(guestName);
            mHomActivityModel.setGuestCountryCode(countryCode);
            mHomActivityModel.setGuestNumber(guestNumber);
            mHomActivityModel.setGuestRoomNumber(guestRoomNo);
            homeActivityView.hideHotelUI();
        }
    }

    @Override
    public double fetchVehicleImageWidth() {
        return mHomActivityModel.getVehicleImageWidth();
    }

    @Override
    public void getCountryInfo(CountryPicker countryPicker) {
        Country country = countryPicker.getUserCountryInfo(mContext);
        homeActivityView.onGettingOfCountryInfo(country.getFlag(), country.getDialCode(), true);
    }

    @Override
    public void validateMobileNumber(String mobileNumber, String countryCode) {
        if (!TextUtil.isEmpty(mobileNumber))
            if (!TextUtil.phoneNumberLengthValidation(mobileNumber, countryCode))
                homeActivityView.onInvalidMobile(mContext.getString(R.string.phone_invalid));
    }

    @Override
    public void addListenerForCountry(CountryPicker countryPicker) {
        countryPicker.setListener((name, code, dialCode, flagDrawableResID, min, max) ->
        {
            homeActivityView.onGettingOfCountryInfo(flagDrawableResID, dialCode, false);
        });
    }

    @Override
    public double fetchVehicleImageHeight() {
        return mHomActivityModel.getVehicleImageHeight();
    }

    @Override
    public void onResumeHomeActivity() {
        Utility.printLog(TAG + " map ready " + mHomActivityModel.isMapReady());
        subscribeBookingDetails();
        subscribeRequestBookingDetails();
        subscribeLocationChange();
        subscribeAddressChange();
        subscribeCurrentZone();
        subscribeRoutePoints();
        subscribeConfirmClick();
        subscribeVehicleTypesDetails();
        subscribeOnGoingBookingDetails();
        subscribeAppVersionConfig();

        Utility.printLog(TAG + "isFromOnCreateView out " + mHomActivityModel.isFromOnCreateView());
        if (mHomActivityModel.isFromOnCreateView()) {
            Utility.printLog(TAG + "isFromOnCreateView inside " + mHomActivityModel.isFromOnCreateView());
            mHomActivityModel.setFromOnCreateView(false);
            mqttManager.postNewVehicleTypes();
        }
        checkForAppVersion();
        triggerVehicleDetails(true);
        pushStoredVehicles();
    }

    /**
     * <h2>pushStoredVehicles</h2>
     * used to push the stored vehicles data
     */
    private void pushStoredVehicles() {
        MQTTResponseDataModel storedResponse = gson.fromJson(preferenceHelperDataSource.getVehicleDetailsResponse(),
                MQTTResponseDataModel.class);
        ArrayList<VehicleTypesDetails> vehicleTypesArrayList = new ArrayList<>();
        for (int typeCount = 0; typeCount < storedResponse.getVehicleTypes().size(); typeCount++) {
            switch (preferenceHelperDataSource.getBusinessType()) {
                case 2:
                    switch (storedResponse.getVehicleTypes().get(typeCount).getBusinessType()) {
                        case 3:
                        case 2:
                            vehicleTypesArrayList.add(storedResponse.getVehicleTypes().get(typeCount));
                            break;
                    }
                    break;

                case 1:
                    switch (storedResponse.getVehicleTypes().get(typeCount).getBusinessType()) {
                        case 3:
                        case 1:
                            vehicleTypesArrayList.add(storedResponse.getVehicleTypes().get(typeCount));
                            break;
                    }
                    break;
            }
        }
        Utility.printLog(TAG + " MQTT TESTING handleVehiclesData() VEHICLE TYPES  "
                + vehicleTypesArrayList.toString());
        mqttResponseDataModel.setVehicleTypes(vehicleTypesArrayList);
        mqttResponseDataModel.setOngoingBookings(storedResponse.getOngoingBookings());
        mqttResponseDataModel.setAreaZone(storedResponse.getAreaZone());

        rxVehicleDetailsObserver.publishVehicleTypes(mqttResponseDataModel.getVehicleTypes());
        if (!mqttResponseDataModel.getVehicleTypes().isEmpty()) {
            Utility.printLog(TAG + "postNewVehicleTypes posted id " + mqttResponseDataModel.getAreaZone().getId());
            rxCurrentZoneObserver.publishAreaZones(mqttResponseDataModel.getAreaZone());
        }
    }

    /**
     * <h1>vehicleTypesTriggerTimer</h1>
     * this method is used for calling API to publish in pubnub with every interval set in configuration
     */
    private void vehicleTypesTriggerTimer() {
        Utility.printLog(TAG + "MyGeoDecoder startTimer() called: " + preferenceHelperDataSource.getCustomerApiInterval() * 1000);
        myTimer_publish = new Timer();
        TimerTask myTimerTask_publish = new TimerTask() {
            @Override
            public void run() {
                if (mHomActivityModel.getMapCenterLatitude() != 0.0 || mHomActivityModel.getMapCenterLongitude() != 0.0)
                    getVehicleDetails();
            }
        };
        myTimer_publish.schedule(myTimerTask_publish, 0, preferenceHelperDataSource.getCustomerApiInterval() * 1000);
    }

    /**
     * <h2>stopTimer</h2>
     * This method is used to stop the timer
     */
    private void stopTimer() {
        Utility.printLog(TAG + " stopTimer");
        if (myTimer_publish != null) {
            myTimer_publish.cancel();
            myTimer_publish = null;
        }
    }

    @Override
    public void triggerVehicleDetails(boolean restartTimer) {
        if (restartTimer)
            stopTimer();

        if (myTimer_publish == null) {
            vehicleTypesTriggerTimer();
            Utility.printLog(TAG + "restartTimer vehicleTypesTriggerTimer");
        }
    }

    @Override
    public void verifyAndUpdateNewLocation(LatLng centerFromPoint) {
        if (centerFromPoint != null) {
            mHomActivityModel.setMapCenterLatitude(centerFromPoint.latitude);
            mHomActivityModel.setMapCenterLongitude(centerFromPoint.longitude);
        }

        if (mHomActivityModel.getMapCenterLatitude() == 0 || mHomActivityModel.getMapCenterLongitude() == 0 ||
                (mHomActivityModel.getMapCenterLatitude() == mHomActivityModel.getPrevMapCenterLatitude() &&
                        mHomActivityModel.getMapCenterLongitude() == mHomActivityModel.getPrevMapCenterLongitude())) {
            Utility.printLog(TAG + "verifyAndUpdateNewLocation() FALSE");
        } else {
            preferenceHelperDataSource.setCurrLatitude(String.valueOf(mHomActivityModel.getMapCenterLatitude()));
            preferenceHelperDataSource.setCurrLongitude(String.valueOf(mHomActivityModel.getMapCenterLongitude()));

            Utility.printLog(TAG + "verifyAndUpdateNewLocation() TRUE");
            getAddressFromLocation(mHomActivityModel.getMapCenterLatitude(),
                    mHomActivityModel.getMapCenterLongitude());

            mHomActivityModel.setPrevMapCenterLatitude(mHomActivityModel.getMapCenterLatitude());
            mHomActivityModel.setPrevMapCenterLongitude(mHomActivityModel.getMapCenterLongitude());
            mHomActivityModel.getLastKnownLocation().setLatitude(mHomActivityModel.getPrevMapCenterLatitude());
            mHomActivityModel.getLastKnownLocation().setLongitude(mHomActivityModel.getPrevMapCenterLongitude());

            triggerVehicleDetails(true);
            Utility.printLog(TAG + " ETA TEST called from verifyAndUpdateNewLocation ");
            prepareToGetETA();
        }
    }

    /**
     * <h>verifyFavADrs</h>
     * to verify the address whether it is favorite
     *
     * @param selectedAddress input the selected address
     */
    private void verifyFavADrs(final String selectedAddress) {
        Utility.printLog(TAG + "verifyFavADrs selectedAddress: " + mHomActivityModel.getFavAddressDataList().size());
        for (FavAddressDataModel favAddressDataModel : mHomActivityModel.getFavAddressDataList()) {
            if (favAddressDataModel.getAddress().equals(selectedAddress)) {
                Utility.printLog(TAG + "GoogleMap verifyFavADrs Adrs found: " + favAddressDataModel.getAddress());
                mHomActivityModel.setFavAddress(true);
                if (preferenceHelperDataSource.isHotelExists())
                    homeActivityView.enableFavAddress(preferenceHelperDataSource.getHotelName(), false);
                else
                    homeActivityView.enableFavAddress(favAddressDataModel.getName(), false);
                return;
            }
        }
        mHomActivityModel.setFavAddress(false);
        if (preferenceHelperDataSource.isHotelExists())
            homeActivityView.disableFavAddress(preferenceHelperDataSource.getHotelName());
        else
            homeActivityView.disableFavAddress(selectedAddress);
    }

    @Override
    public void addAsFavAddress(final String favAddress) {
        if (favAddressDataModel != null) {
            mHomActivityModel.setFavAddress(false);
            if (preferenceHelperDataSource.isHotelExists())
                homeActivityView.disableFavAddress(preferenceHelperDataSource.getHotelName());
            else
                homeActivityView.disableFavAddress(favAddressDataModel.getAddress());
            switch (mHomActivityModel.getFavoriteType()) {
                case 1:
                    favAddressDataModel.setName(mContext.getString(R.string.home));
                    break;
                case 2:
                    favAddressDataModel.setName(mContext.getString(R.string.work));
                    break;
                case 3:
                    favAddressDataModel.setName(favAddress);
                    break;
            }
            addFavAddress(favAddressDataModel);
        } else
            Utility.printLog(TAG + "onclick favAddressDataModel == null");
    }

    /**
     * <h2>addFavAddress</h2>
     * <p>
     * method to make an api call to add new favourite address
     * </p>
     *
     * @param favAddressDataModel: contains new address details to be added as favourite
     */
    private void addFavAddress(final FavAddressDataModel favAddressDataModel) {
        if (networkStateHolder.isConnected()) {
            homeActivityView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.addFavAddressAPI(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), favAddressDataModel);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result) {
                            Utility.printLog(TAG + " addFavAddress onNext" + result.code());
                            switch (result.code()) {
                                case 200:
                                    favAddressDataModel.setAddressId(DataParser.fetchIDFromData(result));
                                    long temp = addressDataSource.insertFavAddress(favAddressDataModel);
                                    favAddressDataModel.setAddressId(null);
                                    mHomActivityModel.getFavAddressDataList().clear();
                                    mHomActivityModel.getFavAddressDataList().addAll(addressDataSource.getFavAddresses());
                                    mHomActivityModel.setFavAddressDataList(mHomActivityModel.getFavAddressDataList());
                                    if (preferenceHelperDataSource.isHotelExists())
                                        homeActivityView.enableFavAddress(preferenceHelperDataSource.getHotelName(), true);
                                    else
                                        homeActivityView.enableFavAddress(favAddressDataModel.getName(), true);
                                    break;

                                case 502:
                                    homeActivityView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    homeActivityView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            Utility.printLog(TAG + " addFavAddress onAddCardError " + errorMsg);
                            homeActivityView.dismissProgressDialog();
                            homeActivityView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            homeActivityView.dismissProgressDialog();
                            Utility.printLog(TAG + " addFavAddress onComplete ");
                        }
                    });
        } else
            homeActivityView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void validatePromoCode(String promoCode) {
        if (networkStateHolder.isConnected()) {
            Utility.printLog(TAG + " validatePromoCode payment " + mHomActivityModel.getPaymentType() + " " +
                    mHomActivityModel.getPayByWallet());
            Observable<Response<ResponseBody>> request =
                    networkService.validatePromo(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),
                            mHomActivityModel.getMapCenterLatitude(), mHomActivityModel.getMapCenterLongitude(),
                            Double.parseDouble(mHomActivityModel.getAmount()), mHomActivityModel.getSelectedVehicleId(),
                            mHomActivityModel.getPaymentType(), mHomActivityModel.getPayByWallet(), promoCode);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result) {
                            Utility.printLog(TAG + " validatePromoCode onNext" + result.code());
                            switch (result.code()) {
                                case 401:
                                    ExpireSession.refreshApplication(mContext, mqttManager,
                                            preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    String response;
                                    try {
                                        response = result.body().string();
                                        Utility.printLog(" data string " + response);
                                        JSONObject object = new JSONObject(response);
                                        String successResponse = object.getJSONObject("data").toString();
                                        String successMessage = object.getString("message");
                                        PromoCodeModel promoCodeModel = gson.fromJson(successResponse, PromoCodeModel.class);
                                        Utility.printLog(" validatePromoCode promo id " + promoCodeModel.getPromoId());
                                        mHomActivityModel.setPromoCodeModel(promoCodeModel);
                                        homeActivityView.validPromo(promoCode);
                                        homeActivityView.showToast(successMessage);
                                        if (fareEstimateModel != null) {
                                            fareEstimateModel.setPromoCodeApplied(true);
                                            fareEstimateModel.setPromoCodeData(promoCodeModel);
                                            handleFinalBreakDown();
                                        }
                                    } catch (IOException e) {
                                        Utility.printLog(" validatePromoCode error " + e);
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        Utility.printLog(" validatePromoCode error " + e);
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    mHomActivityModel.setPromoCodeModel(null);
                                    homeActivityView.invalidPromo(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    mHomActivityModel.setPromoCodeModel(null);
                                    homeActivityView.invalidPromo(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            mHomActivityModel.setPromoCodeModel(null);
                            Utility.printLog(TAG + " validatePromoCode onError " + errorMsg);
                            homeActivityView.invalidPromo(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            Utility.printLog(TAG + " validatePromoCode onComplete ");
                        }
                    });
        } else
            homeActivityView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h>prepareToGetETA</h>
     * method is used to calculate eta of all types for 1st driver
     */
    public void prepareToGetETA() {
        Utility.printLog(TAG + " each type driver size " + mHomActivityModel.getNearestDriverLatLngEachType());
        if (!mHomActivityModel.getNearestDriverLatLngEachType().isEmpty() && mHomActivityModel.getMapCenterLatitude() != 0.0
                && mHomActivityModel.getMapCenterLongitude() != 0.0) {
            String[] params = new String[3];
            params[0] = String.valueOf(mHomActivityModel.getMapCenterLatitude());
            params[1] = String.valueOf(mHomActivityModel.getMapCenterLongitude());
            params[2] = mHomActivityModel.getNearestDriverLatLngEachType();
            driversDistanceMatrixETAAPI(params);
        } else {
            int vehicleTypesSize = mHomActivityModel.getVehicleTypesDetails().size();
            mHomActivityModel.getEtaOfEachType().clear();
            for (int i = 0; i < vehicleTypesSize; i++) {
                VehicleTypesDetails vehicleItem = mHomActivityModel.getVehicleTypesDetails().get(i);
                mHomActivityModel.getEtaOfEachType().put(vehicleItem.getTypeId(), mContext.getString(R.string.no_drivers));
            }
            homeActivityView.updateEachVehicleTypeETA();
        }
    }

    /**
     * <h2>driversDistanceMatrixETAAPI</h2>
     * This class is used to get the ETA from google
     */
    private void driversDistanceMatrixETAAPI(String... params) {
        if (networkStateHolder.isConnected()) {
            if (etaDisposable != null)
                etaDisposable.dispose();

            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + params[0] + "," + params[1]
                    + "&" + "destinations=" + params[2] + "&mode=driving" + "&" + "key=" + preferenceHelperDataSource.getGoogleServerKey();
            Utility.printLog(TAG + " ETA TEST  Distance matrix driversDistanceMatrixETAAPI() url: called " + url);
            Observable<Response<ResponseBody>> request = networkService.getETAMatrix(url);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            etaDisposable = d;
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result) {
                            Utility.printLog(TAG + " ETA TEST Distance matrix driversDistanceMatrixETAAPI() url response : " + url);
                            Utility.printLog(TAG + " ETA TEST current API key  " + preferenceHelperDataSource.getGoogleServerKey());
                            Utility.printLog(TAG + " ETA TEST current result code   " + result.code());
                            switch (result.code()) {
                                case 200:
                                    ETADataModel etaDataModel;
                                    try {
                                        etaDataModel = gson.fromJson(result.body().string(), ETADataModel.class);
                                        if (etaDataModel.getStatus().equals("OK")) {
                                            if (etaDataModel.getRows().get(0).getElements().get(0).getStatus().equals("OK")) {
                                                int vehicleTypesSize = mHomActivityModel.getVehicleTypesDetails().size();
                                                try {
                                                    if (etaDataModel.getRows().get(0).getElements().size() > 0) {
                                                        ArrayList<ElementsForEtaModel> elementsForEtaList = new ArrayList<>();
                                                        elementsForEtaList.addAll(etaDataModel.getRows().get(0).getElements());
                                                        for (int i = 0, j = 0; i < vehicleTypesSize; i++) {
                                                            VehicleTypesDetails vehicleItem = mHomActivityModel.getVehicleTypesDetails().get(i);
                                                            Utility.printLog(TAG + " Distance matrix vehicleTypes ids : " + vehicleItem.getTypeId());

                                                            if (mHomActivityModel.getVehicleIdsHavingDrivers().contains(vehicleItem.getTypeId())) {
                                                                Utility.printLog(TAG + " Distance matrix getVehicleIdsHavingDrivers() if : ");
                                                                mHomActivityModel.getEtaOfEachType().put(vehicleItem.getTypeId(), elementsForEtaList.get(j).getDuration().getText());

                                                                homeActivityView.setETAOfEachVehicleType(i, mHomActivityModel.getEtaOfEachType().get
                                                                        (mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId()));
                                                                j++;
                                                            } else {
                                                                Utility.printLog(TAG + " Distance matrix getVehicleIdsHavingDrivers() else : ");
                                                                mHomActivityModel.getEtaOfEachType().put(vehicleItem.getTypeId(), mContext.getString(R.string.no_drivers));

                                                                homeActivityView.setETAOfEachVehicleType(i, mHomActivityModel.getEtaOfEachType().get
                                                                        (mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId()));
                                                            }
                                                        }
                                                    }
                                                } catch (IndexOutOfBoundsException e) {
                                                    Utility.printLog(TAG + "Distance matrix IndexOutOfBoundsException " + e);
                                                }
                                            }
                                        } else {
                                            Utility.printLog(TAG + " ETA TEST Distance matrix key exceeded ");
                                            //if the stored key is exceeded then rotate to next key
                                            List<String> googleServerKeys = preferenceHelperDataSource.getGoogleServerKeys();
                                            if (googleServerKeys.size() > 0) {
                                                Utility.printLog(TAG + " ETA TEST Distance matrix keys size before remove " + googleServerKeys.size());
                                                googleServerKeys.remove(0);
                                                if (googleServerKeys.size() > 0) {
                                                    Utility.printLog(TAG + " ETA TEST Distance matrix keys next key " + googleServerKeys.get(0));
                                                    //store next key in shared pref
                                                    preferenceHelperDataSource.setGoogleServerKey(googleServerKeys.get(0));
                                                    Utility.printLog(TAG + " ETA TEST Distance matrix set key " + preferenceHelperDataSource.getGoogleServerKey());
                                                    Utility.printLog(TAG + " ETA TEST called from distance  ");
                                                    //if the stored key is exceeded then rotate to next and call eta API
                                                    prepareToGetETA();
                                                }
                                                //to store the google keys array by removing exceeded key from list
                                                preferenceHelperDataSource.setGoogleServerKeys(googleServerKeys);
                                            }
                                        }
                                    } catch (IOException e) {
                                        Utility.printLog(TAG + " TEST ETA Distance matrix IOException " + e);
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Utility.printLog(TAG + " TEST ETA Distance matrix IOException " + e);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else
            homeActivityView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void checkForETAOfVehicleTypes() {
        for (int i = 0; i < mHomActivityModel.getVehicleTypesDetails().size(); i++) {
            if (mHomActivityModel.getEtaOfEachType().containsKey(mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId())) {
                Utility.printLog(TAG + " latlong updateEachVehicleTypeETA if");
                homeActivityView.setETAOfEachVehicleType(i, mHomActivityModel.getEtaOfEachType().get
                        (mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId()));
            } else {
                Utility.printLog(TAG + " latlong updateEachVehicleTypeETA else");
                mHomActivityModel.getEtaOfEachType().put(mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId(),
                        mContext.getString(R.string.no_drivers));
                homeActivityView.setETAOfEachVehicleType(i, mHomActivityModel.getEtaOfEachType().get
                        (mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId()));
            }
        }
    }

    @Override
    public void handleClickEventForAddress(final int rideType) {
        preferenceHelperDataSource.setPickUpLatitude("" + mHomActivityModel.getMapCenterLatitude());
        preferenceHelperDataSource.setPickUpLongitude("" + mHomActivityModel.getMapCenterLongitude());
        preferenceHelperDataSource.setVehicleID(mHomActivityModel.getSelectedVehicleId());
        preferenceHelperDataSource.setVehicleName(mHomActivityModel.getSelectedVehicleName());
        preferenceHelperDataSource.setVehicleImage(mHomActivityModel.getSelectedVehicleImage());
        preferenceHelperDataSource.setBookingType(rideType);

        homeActivityView.openPickAddressScreen();
    }

    @Override
    public void handleClickEventForBooking(final int rideType, String dateFormatToShow,
                                           String dateToBeSent, boolean toShowAddress) {
        Utility.printLog(TAG + " bookingDate TEST " + dateToBeSent);
        preferenceHelperDataSource.setPickUpLatitude("" + mHomActivityModel.getMapCenterLatitude());
        preferenceHelperDataSource.setPickUpLongitude("" + mHomActivityModel.getMapCenterLongitude());
        preferenceHelperDataSource.setVehicleID(mHomActivityModel.getSelectedVehicleId());
        preferenceHelperDataSource.setVehicleName(mHomActivityModel.getSelectedVehicleName());
        preferenceHelperDataSource.setVehicleImage(mHomActivityModel.getSelectedVehicleImage());
        preferenceHelperDataSource.setBookingType(rideType);
        mHomActivityModel.setBookingType(rideType);

        switch (rideType) {
            case NOW_BOOKING_TYPE:
                mHomActivityModel.setBookingDate(dateFormatter.getCurrTime(preferenceHelperDataSource));
                break;

            case LATER_BOOKING_TYPE:
                mHomActivityModel.setBookingDate(dateToBeSent);
                break;
        }

        switch (preferenceHelperDataSource.getBusinessType()) {
            case 1:
                homeActivityView.openDropAddressScreen();
                break;

            case 2:
                switch (rideType) {
                    case LATER_BOOKING_TYPE:
                        homeActivityView.showScheduleView(dateFormatToShow);

                        if (!mHomActivityModel.isRentalEnabled()) {
                            if (mHomActivityModel.getAdvanceFee().equals(""))
                                homeActivityView.hideAdvanceFee();
                            else {
                                ArrayList<String> list = new ArrayList<>();
                                list.add(mContext.getString(R.string.advance_fee1) + " " + mHomActivityModel.getAdvanceFee() + " " +
                                        mContext.getString(R.string.advance_fee2));

                                homeActivityView.showAdvanceFee(list);
                            }
                        }
                        break;

                    case NOW_BOOKING_TYPE:
                        homeActivityView.hideScheduleView();
                        homeActivityView.hideAdvanceFee();
                        break;
                }

                String confirmText = "";
                if (mHomActivityModel.isTowingEnabled())
                    confirmText = mContext.getString(R.string.towing_request);
                else
                    confirmText = mContext.getString(R.string.ride_request);
                if (toShowAddress) {
                    if (mHomActivityModel.isDropLocationMandatory() && !mHomActivityModel.isTowingEnabled())
                        homeActivityView.openDropAddressScreen();
                    else if (!mHomActivityModel.isRentalEnabled())
                        homeActivityView.openConfirmScreen(Double.parseDouble(preferenceHelperDataSource.getPickUpLatitude()),
                                Double.parseDouble(preferenceHelperDataSource.getPickUpLongitude()), confirmText);

                    else if (mHomActivityModel.isRentalEnabled()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(BOOKING_DATE, mHomActivityModel.getBookingDate());
                        bundle.putString(PICK_ZONE_ID, mHomActivityModel.getAreaZoneId());
                        bundle.putString(PICK_ZONE_TITLE, mHomActivityModel.getAreaZoneTitle());
                        bundle.putString(PICK_GATE_ID, mHomActivityModel.getAreaPickupId());
                        bundle.putString(PICK_GATE_TITLE, mHomActivityModel.getAreaPickupTitle());
                        bundle.putString(SOMEONE_NAME, mHomActivityModel.getCustomerName());
                        bundle.putString(SOMEONE_NUMBER, mHomActivityModel.getCustomerNumber());
                        bundle.putInt(SOMEONE_ELSE_BOOKING, mHomActivityModel.isSomeOneElseBooking());
                        bundle.putString(FAV_DRIVERS, mHomActivityModel.getFavoriteDriverId());
                        bundle.putInt(BOOKING_TYPE, mHomActivityModel.getBookingType());
                        homeActivityView.openRentalScreen(bundle);
                    }
                }
                break;
        }
    }

    @Override
    public void handleHomeBackgroundState() {
        Utility.printLog(TAG + " handleHomeBackgroundState ");
        mHomActivityModel.setToAnimate(false);
        disposeObservables();
        stopTimer();
    }

    @Override
    public void calculateMinDate() {
        String timeZoneString = TimezoneMapper.latLngToTimezoneString(Double.parseDouble(preferenceHelperDataSource.getCurrLatitude()),
                Double.parseDouble(preferenceHelperDataSource.getCurrLongitude()));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        Long currentTimeInSec = (long) preferenceHelperDataSource.getCurrentTimeStamp();
        Utility.printLog(TAG + " currnt time value " + currentTimeInSec);
        Long bufferTime = preferenceHelperDataSource.getLaterBookingBufferTime();
        Utility.printLog(TAG + " current time " + currentTimeInSec + " buffer time " +
                bufferTime);

        long addedTime = currentTimeInSec + bufferTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(addedTime * 1000L);
        calendar.setTimeZone(timeZone);
        Utility.printLog(TAG + " date " + calendar.get(Calendar.DAY_OF_MONTH));
        homeActivityView.setMinDateToPicker(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    @Override
    public void handleCurrLocationButtonClick(boolean confirmScreen) {
        if (mHomActivityModel.getCurrentLatitude() != 0.0 && mHomActivityModel.getCurrentLongitude() != 0.0) {
            LatLng currentLatLong = new LatLng(mHomActivityModel.getCurrentLatitude(),
                    mHomActivityModel.getCurrentLongitude());
            homeActivityView.moveGoogleMapToLocation(mHomActivityModel.getCurrentLatitude(), mHomActivityModel.getCurrentLongitude());

            if (confirmScreen)
                homeActivityView.addLatLongBounds(false);
            else
                verifyAndUpdateNewLocation(currentLatLong);

            if (!confirmScreen) {
                zoneId = "";
                selectedPickId = "";
                triggerVehicleDetails(true);
            }
        }
    }

    /**
     * <h2>getVehicleDetails</h2>
     * <p>
     * This method is used for calling get Drivers API.
     * </p>
     */
    private void getVehicleDetails() {
        if (networkStateHolder.isConnected()) {
            Observable<Response<ResponseBody>> request =
                    networkService.getDrivers(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), preferenceHelperDataSource.getCurrLatitude(),
                            preferenceHelperDataSource.getCurrLongitude(), preferenceHelperDataSource.getMqttTopic(),
                            RESPONSE_IN_MQTT, preferenceHelperDataSource.getBusinessType());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result) {
                            switch (result.code()) {
                                case 200:
                                    break;

                                case 401:
                                    Toast.makeText(mContext, DataParser.fetchErrorMessage(result), Toast.LENGTH_LONG).show();
                                    ExpireSession.refreshApplication(mContext, mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                default:
                                    Toast.makeText(mContext, result.code() + DataParser.fetchErrorMessage(result), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            Utility.printLog(TAG + " getVehicleDetails onAddCardError " + errorMsg);
                        }

                        @Override
                        public void onComplete() {
                            Utility.printLog(TAG + " getVehicleDetails onComplete ");
                        }
                    });
        }
    }

    @Override
    public void checkForOutstandingAmount() {
        if (mHomActivityModel.getDriverPreference().equals("") && mHomActivityModel.isTowingEnabled()) {
            homeActivityView.showToast(mContext.getString(R.string.add_services));
            return;
        } else if (driverPreferenceModel.getSelectedDriverPref().contains("3") && preferenceHelperDataSource.getDropAddress().equals(""))//3 for towing
        {
            homeActivityView.showToast(mContext.getString(R.string.add_address_to_proceed));
            return;
        }

        if (mHomActivityModel.getSelectedProfile() == null) {
            if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                homeActivityView.showHardLimitAlert();
            else {
                if (networkStateHolder.isConnected()) {
                    homeActivityView.showProgressDialog();
                    Observable<Response<ResponseBody>> request =
                            networkService.getOutstandingBalance(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                                    preferenceHelperDataSource.getLanguageSettings().getCode());

                    request.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Response<ResponseBody>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    compositeDisposable.add(d);
                                }

                                @Override
                                public void onNext(Response<ResponseBody> result) {
                                    Utility.printLog(TAG + " checkForOutstandingAmount onNext " + result.code());
                                    switch (result.code()) {
                                        case 200:
                                            String dataObjectString = DataParser.fetchDataObjectString(result);
                                            try {
                                                JSONObject dataObject = new JSONObject(dataObjectString);
                                                boolean isLastDueAvailable = dataObject.getBoolean("isLastDueAvailable");

                                                if (isLastDueAvailable) {
                                                    String lastDueMsg = dataObject.getString("lastDueMsg");
                                                    String businessName = dataObject.getString("businessName");
                                                    String bookingDate = dataObject.getString("bookingDate");
                                                    String pickupAddress = dataObject.getString("pickupAddress");
                                                    homeActivityView.showOutstandingDialog(lastDueMsg, businessName, bookingDate, pickupAddress);
                                                } else
                                                    homeActivityView.confirmToBook();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;

                                        case 401:
                                            ExpireSession.refreshApplication(mContext, mqttManager,
                                                    preferenceHelperDataSource, addressDataSource);
                                            break;

                                        case 502:
                                            homeActivityView.showToast(mContext.getString(R.string.bad_gateway));
                                            break;

                                        default:
                                            homeActivityView.showToast(DataParser.fetchErrorMessage(result));
                                            break;
                                    }
                                }

                                @Override
                                public void onError(Throwable errorMsg) {
                                    Utility.printLog(TAG + " checkForOutstandingAmount onError " + errorMsg);
                                    homeActivityView.dismissProgressDialog();
                                    homeActivityView.showToast(mContext.getString(R.string.network_problem));
                                }

                                @Override
                                public void onComplete() {
                                    homeActivityView.dismissProgressDialog();
                                    Utility.printLog(TAG + " checkForOutstandingAmount onComplete ");
                                }
                            });
                } else
                    homeActivityView.showToast(mContext.getString(R.string.network_problem));
            }
        } else {
            String instituteId = mHomActivityModel.getSelectedProfile().getUserId();
            if (instituteId != null && preferenceHelperDataSource.getDropAddress().equals(""))
                homeActivityView.showToast(mContext.getString(R.string.add_address_to_proceed));

            else
                openRequest();
        }
    }

    @Override
    public void validateFavAddress() {
        if (mHomActivityModel.isFavAddress())
            homeActivityView.showToast(mContext.getString(R.string.alreadySetAsFavAdrs));
        else
            checkForFavAddress(true, true);
    }

    @Override
    public void checkForVehicleTypes() {
        mHomActivityModel.getDriversMarkerIconUrls().clear();
        Utility.printLog(TAG + " selected vehicle ID " + mHomActivityModel.getSelectedVehicleId());
        //to make 1st vehicle type selected
        ArrayList<String> vehicleTypeIds = new ArrayList<>();
        for (int i = 0; i < mHomActivityModel.getVehicleTypesDetails().size(); i++)
            vehicleTypeIds.add(mHomActivityModel.getVehicleTypesDetails().get(i).getTypeId());
        if (!vehicleTypeIds.contains(mHomActivityModel.getSelectedVehicleId()))
            mHomActivityModel.setSelectedVehicleId(null);

        //to plot all the vehicle types
        for (int i = 0; i < mHomActivityModel.getVehicleTypesDetails().size(); i++) {
            boolean vehicleSelected;
            final VehicleTypesDetails vehicleItem = mHomActivityModel.getVehicleTypesDetails().get(i);
            final String vehicleId = vehicleItem.getTypeId();

            if (mHomActivityModel.getSelectedVehicleId() == null)
                mHomActivityModel.setSelectedVehicleId(vehicleItem.getTypeId());

            mHomActivityModel.getDriversMarkerIconUrls().put(vehicleId, vehicleItem.getVehicleMapIcon().replace(" ", "%20"));

            if (mHomActivityModel.getSelectedVehicleId().equals(vehicleId)) {
                mHomActivityModel.setSelectedVehicleName(vehicleItem.getTypeName());
                mHomActivityModel.setSelectedVehicleImage(vehicleItem.getVehicleImgOn());
                mHomActivityModel.setTowingEnabled(vehicleItem.isTowTruck());
                mHomActivityModel.setRentalEnabled(vehicleItem.isRental());
                switch (preferenceHelperDataSource.getBusinessType()) {
                    case 1:
                        if (!vehicleItem.isTowTruck())
                            mHomActivityModel.setPreferenceEnabled(vehicleItem.getDelivery().isPreferenceEnabled());
                        else
                            mHomActivityModel.setPreferenceEnabled(vehicleItem.getTowTruck().isPreferenceEnabled());
                        break;

                    case 2:
                        if (!vehicleItem.isTowTruck())
                            mHomActivityModel.setPreferenceEnabled(vehicleItem.getRide().isPreferenceEnabled());
                        else
                            mHomActivityModel.setPreferenceEnabled(vehicleItem.getTowTruck().isPreferenceEnabled());
                        break;
                }
                vehicleSelected = true;
            } else
                vehicleSelected = false;

            homeActivityView.setVehicleTypes(i, mHomActivityModel.getVehicleTypesDetails().size(), vehicleItem,
                    mHomActivityModel.getEtaOfEachType().get(vehicleId), vehicleSelected);
        }
    }

    @Override
    public void handleClickOfVehicleType(VehicleTypesDetails vehicleItem, String vehicleId) {
        String minFare, mileagePriceAfter, mileagePrice, timeFareAfter, timeFare, advanceFee;
        boolean isDropMandatory = false, isPreferenceEnabled = false;
        switch (preferenceHelperDataSource.getBusinessType()) {
            case 1: // courier
                if (!vehicleItem.isTowTruck()) {
                    minFare = vehicleItem.getDelivery().getMinimumFare();
                    mileagePriceAfter = vehicleItem.getDelivery().getMileagePriceAfter();
                    mileagePrice = vehicleItem.getDelivery().getMileagePrice();
                    timeFareAfter = vehicleItem.getDelivery().getTimeFareAfter();
                    timeFare = vehicleItem.getDelivery().getTimeFare();
                    isDropMandatory = vehicleItem.getDelivery().isDropLocationMandatory();
                    advanceFee = vehicleItem.getDelivery().getLaterBookingAdvanceFee();
                    isPreferenceEnabled = vehicleItem.getDelivery().isPreferenceEnabled();
                    if (maxCapacity != vehicleItem.getDelivery().getSeatingCapacity()) {
                        maxCapacity = vehicleItem.getDelivery().getSeatingCapacity();
                        homeActivityView.populateSeatsSpinner(vehicleItem.getDelivery().getSeatingCapacity());
                    }
                    homeActivityView.showSeatsForRide();
                    mHomActivityModel.setVehicleAdded(true);
                } else {
                    minFare = vehicleItem.getTowTruck().getMinimumFare();
                    mileagePriceAfter = vehicleItem.getTowTruck().getMileagePriceAfter();
                    mileagePrice = vehicleItem.getTowTruck().getMileagePrice();
                    timeFareAfter = vehicleItem.getTowTruck().getTimeFareAfter();
                    timeFare = vehicleItem.getTowTruck().getTimeFare();
                    isDropMandatory = vehicleItem.getTowTruck().isDropLocationMandatory();
                    advanceFee = vehicleItem.getTowTruck().getLaterBookingAdvanceFee();
                    isPreferenceEnabled = vehicleItem.getTowTruck().isPreferenceEnabled();
                    checkForDefaultVehicle();
                }

                mHomActivityModel.setDropLocationMandatory(isDropMandatory);
                if (preferenceHelperDataSource.getLoginType() != 0) {
                    mHomActivityModel.setDropLocationMandatory(true);
                }
                mHomActivityModel.setAdvanceFee(advanceFee);

                if (mHomActivityModel.getSelectedVehicleId().equals(vehicleId)) {
                    homeActivityView.showShipmentRateCardDialog(vehicleItem, minFare, mileagePriceAfter, mileagePrice,
                            timeFareAfter, timeFare);
                } else {
                    mHomActivityModel.setSelectedVehicleId(vehicleId);
                    mHomActivityModel.setPreferenceEnabled(isPreferenceEnabled);
                    mHomActivityModel.setSelectedVehicleName(vehicleItem.getTypeName());
                    mHomActivityModel.setSelectedVehicleImage(vehicleItem.getVehicleImgOn());
                    mHomActivityModel.setTowingEnabled(vehicleItem.isTowTruck());
                    mHomActivityModel.setRentalEnabled(vehicleItem.isRental());
                    mqttManager.handleVehiclesData(preferenceHelperDataSource.getVehicleDetailsResponse());
                }
                break;

            case 2: //ride
                if (!vehicleItem.isTowTruck()) {
                    isDropMandatory = vehicleItem.getRide().isDropLocationMandatory();
                    advanceFee = vehicleItem.getRide().getLaterBookingAdvanceFee();
                    homeActivityView.populateSeatsSpinner(vehicleItem.getRide().getSeatingCapacity());
                    isPreferenceEnabled = vehicleItem.getRide().isPreferenceEnabled();
                    if (maxCapacity != vehicleItem.getRide().getSeatingCapacity()) {
                        maxCapacity = vehicleItem.getRide().getSeatingCapacity();
                        homeActivityView.populateSeatsSpinner(vehicleItem.getRide().getSeatingCapacity());
                    }
                    homeActivityView.showSeatsForRide();
                    mHomActivityModel.setVehicleAdded(true);
                } else {
                    isDropMandatory = vehicleItem.getTowTruck().isDropLocationMandatory();
                    advanceFee = vehicleItem.getTowTruck().getLaterBookingAdvanceFee();
                    isPreferenceEnabled = vehicleItem.getTowTruck().isPreferenceEnabled();
                    checkForDefaultVehicle();
                }
                mHomActivityModel.setDropLocationMandatory(isDropMandatory);
                if (preferenceHelperDataSource.getLoginType() != 0) {
                    mHomActivityModel.setDropLocationMandatory(true);
                }
                mHomActivityModel.setAdvanceFee(advanceFee);

                if (mHomActivityModel.getSelectedVehicleId().equals(vehicleId)) {
                    if (!mHomActivityModel.getSelectedVehicleId().isEmpty()) {
                        homeActivityView.showRideRateCard();
                    }
                } else {
                    mHomActivityModel.setSelectedVehicleId(vehicleId);
                    mHomActivityModel.setPreferenceEnabled(isPreferenceEnabled);
                    mHomActivityModel.setSelectedVehicleName(vehicleItem.getTypeName());
                    mHomActivityModel.setSelectedVehicleImage(vehicleItem.getVehicleImgOn());
                    mHomActivityModel.setTowingEnabled(vehicleItem.isTowTruck());
                    mHomActivityModel.setRentalEnabled(vehicleItem.isRental());
                    mqttManager.handleVehiclesData(preferenceHelperDataSource.getVehicleDetailsResponse());
                }
                break;
        }
    }

    /**
     * <h2>checkForOnlineDrivers</h2>
     * This method is used to check for the online drivers for selected vehicle
     */
    private void checkForOnlineDrivers() {
        favDriversList.clear();
        favoriteDriverId = "";
        String driverMarkerImageUrl = mHomActivityModel.getDriversMarkerIconUrls().get(mHomActivityModel.getSelectedVehicleId());
        driverMarkerImageUrl = driverMarkerImageUrl.replace(" ", "%20");
        for (DriverDetailsModel driverDetailsModel : mHomActivityModel.getDriversListSelectedType()) {
            LatLng latLng = new LatLng(driverDetailsModel.getLatitude(), driverDetailsModel.getLongitude());
            Glide.with(mContext)
                    .asBitmap()
                    .load(driverMarkerImageUrl)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Utility.printLog(TAG + " onLoadFailed TEST bitmap of image failed ");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (driverDetailsModel.isFavouriteDriver()) {
                                if (favoriteDriverId.equals(""))
                                    favoriteDriverId = driverDetailsModel.getMasterId();
                                else
                                    favoriteDriverId = favoriteDriverId + "," + driverDetailsModel.getMasterId();

                                ContactDetails contactDetails = new ContactDetails(driverDetailsModel.getFullName());
                                contactDetails.setImgUrl(driverDetailsModel.getProfilePic());
                                contactDetails.setMasterId(driverDetailsModel.getMasterId());
                                favDriversList.add(contactDetails);

                                Bitmap background = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.home_star_icon);
                                bitmap = createSingleImageFromMultipleImages(bitmap, background);
                                Utility.printLog(TAG + " bitmap of custom image 1 " + bitmap.toString());
                                homeActivityView.plotOnlineDriverMarkers(driverDetailsModel.getMasterId(), latLng, mHomActivityModel.getDriverMarkerWidth(),
                                        mHomActivityModel.getDriverMarkerHeight(), bitmap, preferenceHelperDataSource.getCustomerApiInterval());
                            } else
                                homeActivityView.plotOnlineDriverMarkers(driverDetailsModel.getMasterId(), latLng, mHomActivityModel.getDriverMarkerWidth(),
                                        mHomActivityModel.getDriverMarkerHeight(), bitmap, preferenceHelperDataSource.getCustomerApiInterval());

                            homeActivityView.dismissProgressDialog();
                            Utility.printLog(TAG + " TEST bitmap of image download ");
                            return false;
                        }
                    }).submit();
        }
        if (favDriversList.size() > 0) {
            ArrayList<ContactDetails> tempContactDetails = new ArrayList<>();
            if (favDriversList.size() > 1)
                tempContactDetails.add(new ContactDetails(mContext.getString(R.string.any_fav_driver)));
            tempContactDetails.addAll(favDriversList);
            tempContactDetails.add(new ContactDetails(mContext.getString(R.string.all_drivers)));
            favDriversList.clear();
            favDriversList.addAll(tempContactDetails);
        }
        homeActivityView.notifyAdapterWithFavDrivers();
    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, (firstImage.getWidth() - secondImage.getWidth()) / 2, (firstImage.getWidth() - secondImage.getWidth()) / 2, null);
        return result;
    }

    @Override
    public void chooseAllFavDrivers(int bookingType) {
        mHomActivityModel.setFavoriteDriverId(favoriteDriverId);
        checkForBookingType(bookingType, true);
    }

    @Override
    public void chooseAFavDriver(String masterId, int bookingType) {
        mHomActivityModel.setFavoriteDriverId(masterId);
        checkForBookingType(bookingType, true);
    }

    @Override
    public void chooseAllDrivers(int bookingType) {
        mHomActivityModel.setFavoriteDriverId("");
        checkForBookingType(bookingType, true);
    }

    @Override
    public void findCurrentLocation() {
        triggerVehicleDetails(true);

        if (mHomActivityModel.getMapCenterLatitude() != 0.0 && mHomActivityModel.getMapCenterLongitude() != 0.0)
            homeActivityView.moveGoogleMapToLocation(mHomActivityModel.getMapCenterLatitude(), mHomActivityModel.getMapCenterLongitude());
    }

    @Override
    public void disposeObservables() {
        compositeDisposable.clear();
        locationProvider.stopLocationUpdates();
        addressProvider.stopAddressListener();
    }

    @Override
    public void onLocationServiceDisabled(Status status) {
        homeActivityView.promptUserWithLocationAlert(status);
    }

    @Override
    public void checkForFavAddress(boolean isToSetAsFavAddress, boolean isToAnimate) {
        if (isToSetAsFavAddress) {
            mHomActivityModel.setFavFieldShowing(true);
            homeActivityView.handleFavAddressUI();
        } else {
            mHomActivityModel.setFavFieldShowing(false);
            if (isToAnimate)
                homeActivityView.handleNonFavAddressUI();
        }
    }

    @Override
    public void handleGoogleMapIdle() {
        //to animate and show the view if map stops movement except first time and if the fav diaolg is opened
        if (mHomActivityModel.isToAnimate() && !mHomActivityModel.isFavFieldShowing()) {
            // Schedule a runnable to display UI elements after a delay
            mHideHandler.removeCallbacks(startAnimationThread);
            mHideHandler.postDelayed(hideAnimationThread, UI_ANIMATION_DELAY);
        }
    }

    @Override
    public void handleGoogleMapStartedMove(int reason) {
        //to make the variable true if user gestured on map then make it true and start the animation
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            selectedPickId = "";
            mHomActivityModel.setToAnimate(true);
        }

        //to animate and hide the view if map starts movement except first time
        if (mHomActivityModel.isToAnimate() && !mHomActivityModel.isFavFieldShowing()) {
            // Schedule a runnable to display UI elements after a delay
            mHideHandler.removeCallbacks(hideAnimationThread);
            mHideHandler.postDelayed(startAnimationThread, UI_ANIMATION_DELAY);
        }
    }

    @Override
    public void storeFirstTimeAnimation(boolean firstTime) {
        mHomActivityModel.setToAnimate(firstTime);
    }

    @Override
    public void setFavoriteType(int favoriteType) {
        mHomActivityModel.setFavoriteType(favoriteType);
    }

    @Override
    public void handleResultFromIntents(int requestCode, int resultCode, String latitude, String longitude, String address,
                                        Bundle bundle) {
        if (latitude != null && longitude != null && address != null) {
            double pickup_lat = Double.parseDouble(latitude);
            double pickup_lng = Double.parseDouble(longitude);
            switch (requestCode) {
                case PICK_ID:

                    refreshFavAddressList(true);
                    try {
                        storeFirstTimeAnimation(true);
                        if (preferenceHelperDataSource.isHotelExists())
                            homeActivityView.changePickAddressUI(preferenceHelperDataSource.getHotelName(),
                                    pickup_lat, pickup_lng);
                        else
                            homeActivityView.changePickAddressUI(address, pickup_lat, pickup_lng);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case DROP_ADDRESS_REQUEST:
                    Utility.printLog(TAG + " TESTING FARE onActivityResult dropLat: " + latitude + "  droplng: " +
                            longitude);
                    preferenceHelperDataSource.setDropLatitude(latitude);
                    preferenceHelperDataSource.setDropLongitude(longitude);
                    preferenceHelperDataSource.setDropAddress(address);
                    String confirmText = "";
                    if (mHomActivityModel.isTowingEnabled())
                        confirmText = mContext.getString(R.string.towing_request);
                    else
                        confirmText = mContext.getString(R.string.ride_request);
                    homeActivityView.changeDropAddressUI(address, pickup_lat, pickup_lng,
                            preferenceHelperDataSource.getPickUpLatitude(), preferenceHelperDataSource.getPickUpLongitude(), confirmText);

                    if (mHomActivityModel.getPaymentType() != 0 && mHomActivityModel.isVehicleAdded())
                        homeActivityView.enableBooking();
                    break;
            }
        }
    }

    @Override
    public void makeCardAsDefault() {
        mHomActivityModel.setWalletSelected(false);
        mHomActivityModel.setPaymentType(CARD);
        mHomActivityModel.setPayByWallet(DONT_PAY_BY_WALLET);
        CardDetails cardDetailsDataModel = preferenceHelperDataSource.getDefaultCardDetails();
        mHomActivityModel.setCardToken(cardDetailsDataModel.getId());
        mHomActivityModel.setCardLastDigits(cardDetailsDataModel.getLast4());
        mHomActivityModel.setCardBrand(cardDetailsDataModel.getBrand());
        homeActivityView.setSelectedCard(cardDetailsDataModel.getLast4(), cardDetailsDataModel.getBrand());
        if (mHomActivityModel.isVehicleAdded())
            homeActivityView.enableBooking();
        checkForPromoValid();
    }

    @Override
    public void setCashPaymentOption() {
        mHomActivityModel.setWalletSelected(false);
        mHomActivityModel.setPaymentType(CASH);
        mHomActivityModel.setPayByWallet(DONT_PAY_BY_WALLET);
        if (mHomActivityModel.isVehicleAdded())
            homeActivityView.enableBooking();
        checkForPromoValid();
    }

    @Override
    public void setWalletPaymentOption(String walletText) {
        Utility.printLog(TAG + " calling 3 " + mHomActivityModel.getPaymentType());
        if (preferenceHelperDataSource.isCashEnable() && preferenceHelperDataSource.isCardEnable()
                && preferenceHelperDataSource.getDefaultCardDetails() != null && preferenceHelperDataSource.getLoginType() == 0)
            homeActivityView.showWalletExcessAlert(mHomActivityModel.getPaymentType());
        else
            checkForWallet(false);
    }

    @Override
    public void chooseCashWithWallet(boolean toOpenBooking) {
        checkForWallet(toOpenBooking);
    }

    @Override
    public void chooseCardWithWallet(boolean toOpenBooking) {
        if (preferenceHelperDataSource.isCardEnable() &&
                preferenceHelperDataSource.getDefaultCardDetails() != null && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))) {
            if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                homeActivityView.showHardLimitAlert();
            else {
                mHomActivityModel.setWalletSelected(true);
                if (mHomActivityModel.isVehicleAdded())
                    homeActivityView.enableBooking();
                mHomActivityModel.setPaymentType(CARD);
                mHomActivityModel.setPayByWallet(PAY_BY_WALLET);
                checkForPromoValid();
                homeActivityView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(), preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                if (toOpenBooking)
                    openRequest();
            }
        } else if (preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2))) {
            if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                homeActivityView.showHardLimitAlert();
            else {
                mHomActivityModel.setWalletSelected(true);
                if (mHomActivityModel.isVehicleAdded())
                    homeActivityView.enableBooking();
                mHomActivityModel.setPaymentType(CASH);
                mHomActivityModel.setPayByWallet(PAY_BY_WALLET);
                checkForPromoValid();
                homeActivityView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(), preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                if (toOpenBooking)
                    openRequest();
            }
        } else {
            mHomActivityModel.setWalletSelected(false);
            homeActivityView.disableBooking(1);
            homeActivityView.showToast(mContext.getString(R.string.add_card_to_proceed));
        }
    }

    /**
     * <h2>checkforWallet</h2>
     * used to make the cash or card with wallet
     */
    private void checkForWallet(boolean toOpenBooking) {
        if (preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2))) {
            if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                homeActivityView.showHardLimitAlert();
            else {
                mHomActivityModel.setWalletSelected(true);
                if (mHomActivityModel.isVehicleAdded())
                    homeActivityView.enableBooking();
                mHomActivityModel.setPaymentType(CASH);
                mHomActivityModel.setPayByWallet(PAY_BY_WALLET);
                checkForPromoValid();
                homeActivityView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(), preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                if (toOpenBooking)
                    openRequest();
            }
        } else if (preferenceHelperDataSource.isCardEnable() &&
                preferenceHelperDataSource.getDefaultCardDetails() != null && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))) {
            if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                homeActivityView.showHardLimitAlert();
            else {
                mHomActivityModel.setWalletSelected(true);
                if (mHomActivityModel.isVehicleAdded())
                    homeActivityView.enableBooking();
                checkForPromoValid();
                mHomActivityModel.setPaymentType(CARD);
                mHomActivityModel.setPayByWallet(PAY_BY_WALLET);
                homeActivityView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(), preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                if (toOpenBooking)
                    openRequest();
            }
        } else {
            mHomActivityModel.setWalletSelected(false);
            homeActivityView.disableBooking(1);
            homeActivityView.showToast(mContext.getString(R.string.add_card_to_proceed));
        }
    }

    @Override
    public void setCardPaymentOption() {
        if (preferenceHelperDataSource.getDefaultCardDetails() != null && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))) {
            mHomActivityModel.setWalletSelected(false);
            mHomActivityModel.setPaymentType(CARD);
            mHomActivityModel.setPayByWallet(DONT_PAY_BY_WALLET);

            mHomActivityModel.setCardToken(preferenceHelperDataSource.getDefaultCardDetails().getId());
            mHomActivityModel.setCardLastDigits(preferenceHelperDataSource.getDefaultCardDetails().getLast4());
            mHomActivityModel.setCardBrand(preferenceHelperDataSource.getDefaultCardDetails().getBrand());
            homeActivityView.setSelectedCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                    preferenceHelperDataSource.getDefaultCardDetails().getBrand());
            if (mHomActivityModel.isVehicleAdded())
                homeActivityView.enableBooking();
            checkForPromoValid();
        }
    }

    /**
     * <h2>checkForPromoValid</h2>
     * used to check promo is valid
     */
    private void checkForPromoValid() {
        if (mHomActivityModel.getPromoCodeModel() != null) {
            if ((mHomActivityModel.getPromoCodeModel().getPromoPaymentMothod() != mHomActivityModel.getPaymentType()
                    && mHomActivityModel.getPromoCodeModel().getPromoPaymentMothod() != BOTH)
                    || (mHomActivityModel.getPayByWallet() == PAY_BY_WALLET && mHomActivityModel.getPromoCodeModel().getIsApplicableWithWallet() == DONT_PAY_BY_WALLET))
                clearPromo();
        }
    }

    /**
     * <h2>clearPromo</h2>
     * used to clear the promo code
     */
    private void clearPromo() {
        homeActivityView.showToast(mContext.getString(R.string.promo_invalid));
        mHomActivityModel.setPromoCodeModel(null);
        homeActivityView.setPromoCode(mContext.getString(R.string.promo_code));

        if (fareEstimateModel != null && listOfBreakDown != null) {
            ReceiptDetails updatesReceiptDetails = listOfBreakDown.get(listOfBreakDown.size() - 1);
            updatesReceiptDetails.setReceiptValue(fareEstimateModel.getFinalAmount());
            listOfBreakDown.set(listOfBreakDown.size() - 1, updatesReceiptDetails);
            if (fareEstimateModel.getPromoCodeData().getDiscountAmount() > 0)
                listOfBreakDown.remove(listOfBreakDown.size() - 2);

            homeActivityView.showFareEstimation(listOfBreakDown, utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(), fareEstimateModel.getFinalAmount()));
        }
    }

    @Override
    public void clearDropAddress() {
        zoneId = "";
        selectedPickId = "";
        homeActivityView.showSelectedDriverPref(mContext.getString(R.string.driver_preference));
        preferenceHelperDataSource.setDropLatitude("");
        preferenceHelperDataSource.setDropLongitude("");
        preferenceHelperDataSource.setDropAddress("");
        mHomActivityModel.setAmount("0");
        mHomActivityModel.setTimeFare("0");
        mHomActivityModel.setDistFare("0");
        mHomActivityModel.setDistance("0");
        mHomActivityModel.setDuration("0");
        mHomActivityModel.setEstimateId("0");
        mHomActivityModel.setSelectedProfile(null);
        mHomActivityModel.setPromoCodeModel(null);
        mHomActivityModel.setFavoriteDriverId("");
        mHomActivityModel.setDriverPreference("");
        driverPreferenceModel.getData().clear();
        driverPreferenceModel.getSelectedDriverPref().clear();
        tempPreferenceDataModels.clear();
        fareEstimateModel = null;
    }

    @Override
    public String getSavedPromo() {
        if (mHomActivityModel.getPromoCodeModel() != null)
            return mHomActivityModel.getPromoCodeModel().getCouponCode();
        else
            return null;
    }

    @Override
    public void getApproxFareEstimation() {
        String instituteUserId = null, dropLat, dropLong, dropAddress;
        if (mHomActivityModel.getSelectedProfile() != null)
            instituteUserId = mHomActivityModel.getSelectedProfile().getUserId();
        if (preferenceHelperDataSource.getDropAddress().equals("")) {
            dropLat = preferenceHelperDataSource.getPickUpLatitude();
            dropLong = preferenceHelperDataSource.getPickUpLongitude();
            dropAddress = preferenceHelperDataSource.getPickUpAddress();
        } else {
            dropLat = preferenceHelperDataSource.getDropLatitude();
            dropLong = preferenceHelperDataSource.getDropLongitude();
            dropAddress = preferenceHelperDataSource.getDropAddress();
        }
        if (networkStateHolder.isConnected()) {
            homeActivityView.showProgressDialog();
            String promoCode = null;
            if (mHomActivityModel.getPromoCodeModel() != null)
                promoCode = mHomActivityModel.getPromoCodeModel().getCouponCode();

            Observable<Response<ResponseBody>> request =
                    networkService.fareEstimation(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), mHomActivityModel.getSelectedVehicleId(),
                            preferenceHelperDataSource.getPickUpLatitude() + "," + preferenceHelperDataSource.getPickUpLongitude(),
                            dropLat + "," + dropLong, preferenceHelperDataSource.getBookingType(),
                            preferenceHelperDataSource.getPickUpAddress(), dropAddress, instituteUserId, promoCode,
                            mHomActivityModel.getPaymentType(), mHomActivityModel.getPayByWallet(),
                            mHomActivityModel.getDriverPreference());

            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result) {
                            Utility.printLog(TAG + " TESTING FARE onNext " + result.code());
                            switch (result.code()) {
                                case 401:
                                    homeActivityView.showToast(DataParser.fetchErrorMessage(result));
                                    ExpireSession.refreshApplication(mContext, mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    String dataObjectString = DataParser.fetchDataObjectString(result);
                                    fareEstimateModel = gson.fromJson(dataObjectString, FareEstimateModel.class);
                                    mHomActivityModel.setAmount(fareEstimateModel.getFinalAmount());
                                    mHomActivityModel.setTimeFare(fareEstimateModel.getTimeFee());
                                    mHomActivityModel.setDistFare(fareEstimateModel.getDistanceFee());
                                    mHomActivityModel.setDistance(fareEstimateModel.getDistance());
                                    mHomActivityModel.setDuration(fareEstimateModel.getDuration());
                                    mHomActivityModel.setEstimateId(fareEstimateModel.getEstimateId());
                                    handleFinalBreakDown();
                                    break;

                                case 502:
                                    homeActivityView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    homeActivityView.showAlertForOutZone(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            Utility.printLog(TAG + " getApproxFareEstimation onAddCardError " + errorMsg);
                            homeActivityView.dismissProgressDialog();
                            homeActivityView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            homeActivityView.dismissProgressDialog();
                            Utility.printLog(TAG + " getApproxFareEstimation onComplete ");
                        }
                    });
        } else
            homeActivityView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h2>handleFinalBreakDown</h2>
     * used to handle the final breakdown
     */
    @Override
    public void handleFinalBreakDown() {
        Utility.printLog("PromoCodeAppliedHome " + fareEstimateModel.isPromoCodeApplied());
        listOfBreakDown = new ArrayList<>();
        switch (fareEstimateModel.getIsMinFeeApplied()) {
            case 1:
                ReceiptDetails minFee = new ReceiptDetails();
                minFee.setReceiptText(mContext.getString(R.string.min_fare));
                minFee.setGrandTotal(false);
                minFee.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                        fareEstimateModel.getCurrencySymbol(), fareEstimateModel.getMinFee()));
                listOfBreakDown.add(minFee);
                break;

            default:
                if (!fareEstimateModel.isTowTruckBooking())
                    populateSubTotalValues(2);//ride
                else {
                    switch (fareEstimateModel.getTowTruckBookingService()) {
                        case 1: //only fixed
                            populateSubTotalValues(1);
                            break;

                        case 2: //only towing
                            populateSubTotalValues(2);
                            break;

                        case 3://both towing and fixed
                            populateSubTotalValues(3);
                            break;
                    }
                }
                break;
        }

        ArrayList<String> extraFeesIncludedBill = new ArrayList<>();
        if (fareEstimateModel != null && !fareEstimateModel.getExtraFees().isEmpty() || fareEstimateModel.isPromoCodeApplied()) {
            //for extra fees
            if (!fareEstimateModel.getExtraFees().isEmpty()) {
                ReceiptDetails subTotal = new ReceiptDetails();
                subTotal.setGrandTotal(true);
                subTotal.setReceiptText(mContext.getString(R.string.total_title));
                subTotal.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                        fareEstimateModel.getCurrencySymbol(), String.valueOf(fareEstimateModel.getSubTotal())));
                listOfBreakDown.add(subTotal);

                for (ExtraFeesModel extraFeesModel : fareEstimateModel.getExtraFees()) {
                    extraFeesIncludedBill.add(mContext.getString(R.string.advance_fee1) + " " +
                            utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                                    fareEstimateModel.getCurrencySymbol(), String.valueOf(extraFeesModel.getFee())) + " " +
                            extraFeesModel.getTitle() + ".");
                    ReceiptDetails receiptDetails6 = new ReceiptDetails();
                    receiptDetails6.setReceiptText(extraFeesModel.getTitle());
                    receiptDetails6.setGrandTotal(false);
                    receiptDetails6.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                            fareEstimateModel.getCurrencySymbol(), String.valueOf(extraFeesModel.getFee())));
                    listOfBreakDown.add(receiptDetails6);
                }
            }

            if (fareEstimateModel.isPromoCodeApplied()) {
                if (fareEstimateModel.getPromoCodeData().getDiscountAmount() > 0) {
                    if (fareEstimateModel.getExtraFees().isEmpty()) {
                        ReceiptDetails subTotal = new ReceiptDetails();
                        subTotal.setGrandTotal(true);
                        subTotal.setReceiptText(mContext.getString(R.string.total_title));
                        subTotal.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                                fareEstimateModel.getCurrencySymbol(), String.valueOf(fareEstimateModel.getSubTotal())));
                        listOfBreakDown.add(subTotal);
                    }

                    String discountPercent = "";
                    if (fareEstimateModel.getPromoCodeData().getDiscountType() == 2)
                        discountPercent = fareEstimateModel.getPromoCodeData().getDiscountValue() + "%";
                    ReceiptDetails discount = new ReceiptDetails();
                    discount.setGrandTotal(false);
                    switch (fareEstimateModel.getPromoCodeData().getDiscountType()) {
                        case 1:
                            discount.setReceiptText(mContext.getString(R.string.discount) + " (" +
                                    fareEstimateModel.getPromoCodeData().getCouponCode() + ")");
                            break;

                        case 2:
                            discount.setReceiptText(mContext.getString(R.string.discount) + " (" +
                                    fareEstimateModel.getPromoCodeData().getCouponCode() + ")" + "(" + discountPercent + ")");
                            break;
                    }
                    discount.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                            fareEstimateModel.getCurrencySymbol(), String.valueOf(fareEstimateModel.getPromoCodeData().getDiscountAmount())));
                    listOfBreakDown.add(discount);
                }
            }
        }

        String finalAmount = fareEstimateModel.getFinalAmount();
        if (fareEstimateModel.isPromoCodeApplied()) {
            if (fareEstimateModel.getPromoCodeData().getDiscountAmount() > 0)
                finalAmount = fareEstimateModel.getPromoCodeData().getFinalAmount() + "";
        }
        ReceiptDetails receiptDetails4 = new ReceiptDetails();
        receiptDetails4.setReceiptText(mContext.getString(R.string.grand_total));
        receiptDetails4.setGrandTotal(true);
        receiptDetails4.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                fareEstimateModel.getCurrencySymbol(), finalAmount));
        listOfBreakDown.add(receiptDetails4);

        homeActivityView.showFareEstimation(listOfBreakDown, utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                fareEstimateModel.getCurrencySymbol(), finalAmount));

        if (mHomActivityModel.getBookingType() == NOW_BOOKING_TYPE) {
            if (extraFeesIncludedBill.isEmpty())
                homeActivityView.hideAdvanceFee();
            else
                homeActivityView.showAdvanceFee(extraFeesIncludedBill);
        } else {
            if (fareEstimateModel.getLaterBookingAdvanceFee() != 0) {
                extraFeesIncludedBill.add(mContext.getString(R.string.advance_fee1) + " " +
                        String.valueOf(fareEstimateModel.getLaterBookingAdvanceFee()) + " " +
                        mContext.getString(R.string.advance_fee2));
            }
            if (!extraFeesIncludedBill.isEmpty())
                homeActivityView.showAdvanceFee(extraFeesIncludedBill);
        }

        if (fareEstimateModel.isSurgeApplied())
            homeActivityView.showSurgeBar(fareEstimateModel.getSurgePriceText());
        else
            homeActivityView.hideSurgeBar();
    }

    /**
     * <h2>populateSubTotalValues</h2>
     * used to populate the sub total breakdown
     *
     * @param type 1 for only fixed  , 2 for only towing & ride, 3 for both
     */
    private void populateSubTotalValues(int type) {
        if (type == 2 || type == 3) {
            ReceiptDetails receiptDetails = new ReceiptDetails();
            receiptDetails.setReceiptText(mContext.getString(R.string.baseFare));
            receiptDetails.setGrandTotal(false);
            receiptDetails.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(), fareEstimateModel.getBaseFare()));
            listOfBreakDown.add(receiptDetails);

            ReceiptDetails receiptDetails1 = new ReceiptDetails();
            receiptDetails1.setGrandTotal(false);
            if (fareEstimateModel.getFreeDistance() > 0)
                receiptDetails1.setReceiptText(mContext.getString(R.string.distance_fare) + "(" +
                        ActivityUtils.distanceAdjustment(fareEstimateModel.getDistanceMetrics(), fareEstimateModel.getDistance()) + ")" +
                        "\n" + "(" + mContext.getString(R.string.charges_applied) + " " + fareEstimateModel.getFreeDistance() + " " +
                        fareEstimateModel.getDistanceMetrics() + ")");
            else
                receiptDetails1.setReceiptText(mContext.getString(R.string.distance_fare) + "(" +
                        ActivityUtils.distanceAdjustment(fareEstimateModel.getDistanceMetrics(), fareEstimateModel.getDistance()));

            receiptDetails1.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(), fareEstimateModel.getDistanceFee()));
            listOfBreakDown.add(receiptDetails1);

            ReceiptDetails receiptDetails2 = new ReceiptDetails();
            receiptDetails2.setGrandTotal(false);

            if (fareEstimateModel.getFreeTime() > 0)
                receiptDetails2.setReceiptText(mContext.getString(R.string.time_fare) + "(" +
                        ActivityUtils.timeAdjustment(fareEstimateModel.getTimeMetrics(), fareEstimateModel.getDuration()) + ")" +
                        "\n" + "(" + mContext.getString(R.string.charges_applied) + " " + fareEstimateModel.getFreeTime() + " " +
                        fareEstimateModel.getTimeMetrics() + ")");
            else
                receiptDetails2.setReceiptText(mContext.getString(R.string.time_fare) +
                        "(" + ActivityUtils.timeAdjustment(fareEstimateModel.getTimeMetrics(), fareEstimateModel.getDuration()) + ")");

            receiptDetails2.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(), fareEstimateModel.getTimeFee()));
            listOfBreakDown.add(receiptDetails2);

            if (!(fareEstimateModel.getLaterBookingAdvanceFee() == 0 ||
                    mHomActivityModel.getBookingType() == NOW_BOOKING_TYPE)) {
                ReceiptDetails receiptDetails3 = new ReceiptDetails();
                receiptDetails3.setGrandTotal(false);
                receiptDetails3.setReceiptText(mContext.getString(R.string.advance_Fare));
                receiptDetails3.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                        fareEstimateModel.getCurrencySymbol(), String.valueOf(fareEstimateModel.getLaterBookingAdvanceFee())));
                listOfBreakDown.add(receiptDetails3);
            }
        }
        if (type == 1 || type == 3) {
            if (fareEstimateModel != null && !fareEstimateModel.getTowTruckServices().isEmpty()) {
                //for extra fees
                if (!fareEstimateModel.getTowTruckServices().isEmpty()) {
                    for (ExtraFeesModel serviceFeesModel : fareEstimateModel.getTowTruckServices()) {
                        ReceiptDetails receiptDetails7 = new ReceiptDetails();
                        receiptDetails7.setReceiptText(serviceFeesModel.getTitle());
                        receiptDetails7.setGrandTotal(false);
                        receiptDetails7.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                                fareEstimateModel.getCurrencySymbol(), String.valueOf(serviceFeesModel.getFee())));
                        listOfBreakDown.add(receiptDetails7);
                    }
                }
            }
        }
    }

    @Override
    public void getCorporateProfiles() {
        if (networkStateHolder.isConnected()) {
            homeActivityView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getCorporateProfiles(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(" onNext corporateProfileModel" + value.code());
                            switch (value.code()) {
                                case 200:
                                    try {
                                        if (mHomActivityModel.getSelectedProfile() == null) {
                                            ArrayList<CorporateProfileData> corporateProfilesModel = new ArrayList<>();
                                            CorporateProfileData corporateProfileData = new CorporateProfileData();
                                            corporateProfileData.setInstituteName(mContext.getString(R.string.personal));
                                            corporateProfileData.setSelected(true);
                                            corporateProfilesModel.add(corporateProfileData);
                                            String institutes = value.body().string();
                                            Utility.printLog(TAG + " corporate accounts " + institutes);
                                            CorporateProfileModel corporateProfileModel = gson.fromJson(institutes,
                                                    CorporateProfileModel.class);
                                            corporateProfilesModel.addAll(corporateProfileModel.getData());
                                            CorporateProfileData add = new CorporateProfileData();
                                            add.setInstituteName(mContext.getString(R.string.corporate_mail_add));
                                            add.setSelected(false);
                                            corporateProfilesModel.add(add);
                                            Utility.printLog(TAG + " corporateProfileModel size " + corporateProfileModel.getData().size());
                                            homeActivityView.populateProfiles(corporateProfilesModel);
                                        } else {
                                            ArrayList<CorporateProfileData> corporateProfilesModel = new ArrayList<>();
                                            CorporateProfileData corporateProfileData = new CorporateProfileData();
                                            corporateProfileData.setInstituteName(mContext.getString(R.string.personal));
                                            corporateProfileData.setSelected(false);
                                            corporateProfilesModel.add(corporateProfileData);
                                            CorporateProfileModel corporateProfileModel = gson.fromJson(value.body().string(),
                                                    CorporateProfileModel.class);

                                            for (int i = 0; i < corporateProfileModel.getData().size(); i++) {
                                                if (mHomActivityModel.getSelectedProfile().getUserId().equals(corporateProfileModel.getData().get(i).getUserId())) {
                                                    corporateProfileModel.getData().get(i).setSelected(true);
                                                    corporateProfilesModel.add(corporateProfileModel.getData().get(i));
                                                } else {
                                                    corporateProfileModel.getData().get(i).setSelected(false);
                                                    corporateProfilesModel.add(corporateProfileModel.getData().get(i));
                                                }
                                            }
                                            CorporateProfileData add = new CorporateProfileData();
                                            add.setInstituteName(mContext.getString(R.string.corporate_mail_add));
                                            add.setSelected(false);
                                            corporateProfilesModel.add(add);
                                            Utility.printLog(TAG + " corporateProfileModel size " + corporateProfileModel.getData().size());
                                            homeActivityView.populateProfiles(corporateProfilesModel);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext, mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 502:
                                    homeActivityView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    homeActivityView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            homeActivityView.dismissProgressDialog();
                            homeActivityView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            homeActivityView.dismissProgressDialog();
                        }
                    });
        } else
            homeActivityView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void fetchDriverPreferences() {
        if (networkStateHolder.isConnected()) {
            homeActivityView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getDriverPreferences(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),
                    preferenceHelperDataSource.getCityId(),
                    preferenceHelperDataSource.getVehicleID(),
                    preferenceHelperDataSource.getBusinessType());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(" onNext DriverPreferences " + value.code());
                            switch (value.code()) {
                                case 200:
                                    try {
                                        String response = value.body().string();
                                        Utility.printLog(" onNext DriverPreferences response " + response);
                                        Utility.printLog(" onNext DriverPreferences prevDriverPrefModel " + prevDriverPrefModel + "\n" +
                                                "to send " + mHomActivityModel.getDriverPreference());
                                        DriverPreferenceModel driverPreferenceModel = gson.fromJson(response, DriverPreferenceModel.class);
                                        if (tempPreferenceDataModels.isEmpty())
                                            tempPreferenceDataModels.addAll(driverPreferenceModel.getData());
                                        if (HomeFragmentPresenter.this.driverPreferenceModel.getData().size() > 0) {
                                            for (int i = 0; i < driverPreferenceModel.getData().size(); i++) {
                                                for (DriverPreferenceDataModel driverPreferenceDataModel : HomeFragmentPresenter.this.driverPreferenceModel.getData()) {
                                                    if (HomeFragmentPresenter.this.driverPreferenceModel.getData().get(i).getId().
                                                            equals(driverPreferenceDataModel.getId())) {
                                                        driverPreferenceModel.getData().get(i).setSelected(HomeFragmentPresenter.this.driverPreferenceModel.getData().get(i).isSelected());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        HomeFragmentPresenter.this.driverPreferenceModel.getData().clear();
                                        HomeFragmentPresenter.this.driverPreferenceModel.getData().addAll(driverPreferenceModel.getData());
                                        HomeFragmentPresenter.this.driverPreferenceModel.setCurrencySymbol(preferenceHelperDataSource.getWalletSettings().getCurrencySymbol());
                                        HomeFragmentPresenter.this.driverPreferenceModel.setCurrencyAbbr(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr());
                                        homeActivityView.showDriverPref();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext, mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 502:
                                    homeActivityView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    homeActivityView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            homeActivityView.dismissProgressDialog();
                            homeActivityView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            homeActivityView.dismissProgressDialog();
                        }
                    });
        } else
            homeActivityView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void setSelectedProfile(CorporateProfileData selectedProfile) {
        mHomActivityModel.setSelectedProfile(selectedProfile);
        if (selectedProfile.getUserId() != null) {
            homeActivityView.showInstituteWallet(utility.currencyAdjustment(selectedProfile.getCurrencyAbbr(),
                    selectedProfile.getCurrencySymbol(), selectedProfile.getUserWalletBalance() + ""));
            mHomActivityModel.setPayByWallet(DONT_PAY_BY_WALLET);
            homeActivityView.setSelectedProfile(selectedProfile.getInstituteName(),
                    mContext.getResources().getDrawable(R.drawable.briefcase_icon));
            Utility.printLog(TAG + " drop address 1 " + preferenceHelperDataSource.getDropAddress());
            if (!preferenceHelperDataSource.getDropAddress().equals(""))
                getApproxFareEstimation();
            else
                homeActivityView.openDropAddressScreen();
        } else {
            homeActivityView.enablePaymentOptions();
            Utility.printLog(TAG + " drop address 2 " + preferenceHelperDataSource.getDropAddress());
            mHomActivityModel.setSelectedProfile(null);
            homeActivityView.setSelectedProfile(selectedProfile.getInstituteName(),
                    mContext.getResources().getDrawable(R.drawable.confirmation_personal_icon));
            if (!preferenceHelperDataSource.getDropAddress().equals(""))
                getApproxFareEstimation();
            checkForDefaultPayment();
        }
    }

    @Override
    public void checkForBookingType(VehicleTypesDetails vehicleItem) {
        String advanceFee = null;
        boolean isDropLocationMandatory = false;
        int bookingType;
        switch (preferenceHelperDataSource.getBusinessType()) {
            case 1:
                if (!vehicleItem.isTowTruck()) {
                    isDropLocationMandatory = vehicleItem.getDelivery().isDropLocationMandatory();
                    advanceFee = vehicleItem.getDelivery().getLaterBookingAdvanceFee();
                    bookingType = vehicleItem.getDelivery().getBookingType();
                    if (maxCapacity != vehicleItem.getDelivery().getSeatingCapacity()) {
                        maxCapacity = vehicleItem.getDelivery().getSeatingCapacity();
                        homeActivityView.populateSeatsSpinner(vehicleItem.getDelivery().getSeatingCapacity());
                    }
                    homeActivityView.showSeatsForRide();
                    mHomActivityModel.setVehicleAdded(true);
                } else {
                    isDropLocationMandatory = vehicleItem.getTowTruck().isDropLocationMandatory();
                    advanceFee = vehicleItem.getTowTruck().getLaterBookingAdvanceFee();
                    bookingType = vehicleItem.getTowTruck().getBookingType();
                    checkForDefaultVehicle();
                }
                mHomActivityModel.setDropLocationMandatory(isDropLocationMandatory);
                if (preferenceHelperDataSource.getLoginType() != 0) {
                    mHomActivityModel.setDropLocationMandatory(true);
                }
                mHomActivityModel.setAdvanceFee(advanceFee);

                switch (bookingType) {
                    //bookingType 0: both run now and run later
                    //bookingType 1: run now
                    //bookingType 2: run later
                    case 0:
                        homeActivityView.enableBothBookingType();
                        break;

                    case 1:
                        homeActivityView.enableOnlyNowBookingType();
                        break;

                    case 2:
                        homeActivityView.enableOnlyLaterBookingType();
                        break;

                    default:
                        break;
                }
                break;

            case 2:
                if (!vehicleItem.isTowTruck()) {
                    isDropLocationMandatory = vehicleItem.getRide().isDropLocationMandatory();
                    advanceFee = vehicleItem.getRide().getLaterBookingAdvanceFee();
                    bookingType = vehicleItem.getRide().getBookingType();
                    if (maxCapacity != vehicleItem.getRide().getSeatingCapacity()) {
                        maxCapacity = vehicleItem.getRide().getSeatingCapacity();
                        homeActivityView.populateSeatsSpinner(vehicleItem.getRide().getSeatingCapacity());
                    }
                    homeActivityView.showSeatsForRide();
                    mHomActivityModel.setVehicleAdded(true);
                } else {
                    isDropLocationMandatory = vehicleItem.getTowTruck().isDropLocationMandatory();
                    advanceFee = vehicleItem.getTowTruck().getLaterBookingAdvanceFee();
                    bookingType = vehicleItem.getTowTruck().getBookingType();
                    checkForDefaultVehicle();
                }
                mHomActivityModel.setDropLocationMandatory(isDropLocationMandatory);
                if (preferenceHelperDataSource.getLoginType() != 0) {
                    mHomActivityModel.setDropLocationMandatory(true);
                }
                mHomActivityModel.setAdvanceFee(advanceFee);

                switch (bookingType) {
                    //bookingType 0: both run now and run later
                    //bookingType 1: run now
                    //bookingType 2: run later
                    case 0:
                        homeActivityView.enableBothBookingType();
                        break;

                    case 1:
                        homeActivityView.enableOnlyNowBookingType();
                        break;

                    case 2:
                        homeActivityView.enableOnlyLaterBookingType();
                        break;
                }
                break;
        }

        if (mHomActivityModel.getPaymentType() != 0 && mHomActivityModel.isVehicleAdded())
            homeActivityView.enableBooking();
        else {
            if (mHomActivityModel.getPaymentType() == 0)
                homeActivityView.disableBooking(1);
            else if (!mHomActivityModel.isVehicleAdded())
                homeActivityView.disableBooking(2);
        }
    }

    @Override
    public void handleRequestBooking() {
        if (mHomActivityModel.isWalletSelected()) {
            if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                homeActivityView.showHardLimitAlert();
            else
                openRequest();
        } else {
            if (preferenceHelperDataSource.getWalletSettings().isWalletEnable() &&
                    preferenceHelperDataSource.getWalletSettings().getWalletBalance() >= preferenceHelperDataSource.getWalletSettings().getSoftLimit()
                    && preferenceHelperDataSource.getWalletSettings().getWalletBalance() > 0 &&
                    (preferenceHelperDataSource.getLoginType() == 0 ||
                            (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))) {
                homeActivityView.showWalletUseAlert(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(), preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""),
                        mHomActivityModel.getPaymentType());
            } else {
                if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getHardLimit()
                        && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                        (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                    homeActivityView.showHardLimitAlert();
                else
                    openRequest();
            }
        }
    }

    @Override
    public void checkForDefaultVehicle() {
        if (preferenceHelperDataSource.getDefaultVehicle() != null) {
            mHomActivityModel.setVehicleAdded(true);
            homeActivityView.showMyVehiclesForTowing(preferenceHelperDataSource.getDefaultVehicle().getMake() + " " +
                    preferenceHelperDataSource.getDefaultVehicle().getModel() + " " + preferenceHelperDataSource.getDefaultVehicle().getColor());
        } else {
            mHomActivityModel.setVehicleAdded(false);
            homeActivityView.showMyVehiclesForTowing(mContext.getString(R.string.add_vehicle));
        }
    }

    @Override
    public void openRequest() {
        Utility.printLog(TAG + " booking details  " + mHomActivityModel.getAmount() + " " + mHomActivityModel.getTimeFare());
        Bundle bundle = new Bundle();
        bundle.putString(VEHICLE_ID, mHomActivityModel.getSelectedVehicleId());
        bundle.putInt(PAYMENT_TYPE, mHomActivityModel.getPaymentType());
        bundle.putInt(BOOKING_TYPE, mHomActivityModel.getBookingType());
        bundle.putString(PICK_ADDRESS, preferenceHelperDataSource.getPickUpAddress());
        bundle.putString(DROP_ADDRESS, preferenceHelperDataSource.getDropAddress());
        bundle.putString(PICK_LAT, preferenceHelperDataSource.getPickUpLatitude());
        bundle.putString(PICK_LONG, preferenceHelperDataSource.getPickUpLongitude());
        bundle.putString(AMOUNT, mHomActivityModel.getAmount());
        bundle.putString(TIME_FARE, mHomActivityModel.getTimeFare());
        bundle.putString(DISTANCE_FARE, mHomActivityModel.getDistFare());
        bundle.putString(DISTANCE, mHomActivityModel.getDistance());
        bundle.putString(DURATION, mHomActivityModel.getDuration());
        bundle.putString(ESTIMATE_ID, mHomActivityModel.getEstimateId());
        bundle.putString(VEHICLE_IMAGE, mHomActivityModel.getSelectedVehicleImage());
        bundle.putString(VEHICLE_NAME, mHomActivityModel.getSelectedVehicleName());
        bundle.putString(BOOKING_DATE, mHomActivityModel.getBookingDate());
        bundle.putString(PICK_ZONE_ID, mHomActivityModel.getAreaZoneId());
        bundle.putString(PICK_ZONE_TITLE, mHomActivityModel.getAreaZoneTitle());
        bundle.putString(PICK_GATE_ID, mHomActivityModel.getAreaPickupId());
        bundle.putString(PICK_GATE_TITLE, mHomActivityModel.getAreaPickupTitle());
        bundle.putString(SOMEONE_NAME, mHomActivityModel.getCustomerName());
        bundle.putString(SOMEONE_NUMBER, mHomActivityModel.getCustomerNumber());
        bundle.putString(VEHICLE_CAPACITY, mHomActivityModel.getVehicleCapacity());
        bundle.putInt(SOMEONE_ELSE_BOOKING, mHomActivityModel.isSomeOneElseBooking());
        bundle.putInt(PAYMENT_OPTION, mHomActivityModel.getPayByWallet());
        bundle.putString(FAV_DRIVERS, mHomActivityModel.getFavoriteDriverId());
        bundle.putString(DRIVER_PREF, mHomActivityModel.getDriverPreference());
        if (mHomActivityModel.getPromoCodeModel() != null)
            bundle.putString(PROMO_CODE, mHomActivityModel.getPromoCodeModel().getCouponCode());
        String instituteId = null;
        if (mHomActivityModel.getSelectedProfile() != null)
            instituteId = mHomActivityModel.getSelectedProfile().getUserId();
        bundle.putString(INSTITUTE_ID, instituteId);
        bundle.putBoolean(IS_TOWING_ENABLE, mHomActivityModel.isTowingEnabled());
        bundle.putInt(IS_PARTNER_TYPE, mHomActivityModel.isPartnerUser());
        bundle.putInt(HOTEL_USER_TYPE, mHomActivityModel.getHotelUserType());
        bundle.putString(GUEST_NAME, mHomActivityModel.getGuestName());
        bundle.putString(GUEST_COUNRY_CODE, mHomActivityModel.getGuestCountryCode());
        bundle.putString(GUEST_NUMBER, mHomActivityModel.getGuestNumber());
        bundle.putString(GUEST_ROOM_NUMBER, mHomActivityModel.getGuestRoomNumber());
        bundle.putString(HOTEL_USER_ID, mHomActivityModel.getHotelUserId());

        switch (mHomActivityModel.getPaymentType()) {
            case CARD:
                bundle.putString(CARD_TOKEN, mHomActivityModel.getCardToken());
                bundle.putString(LAST_DIGITS, mHomActivityModel.getCardLastDigits());
                bundle.putString(CARD_BRAND, mHomActivityModel.getCardBrand());
                break;

            case CASH:
                bundle.putString(CARD_TOKEN, null);
                bundle.putString(LAST_DIGITS, null);
                bundle.putString(CARD_BRAND, null);
                break;
        }
        homeActivityView.openRequestingScreenNormal(bundle);
    }

    @Override
    public void checkForPaymentOptions() {
        if (!preferenceHelperDataSource.isCashEnable())
            homeActivityView.disableCashOption();
        else {
            switch (preferenceHelperDataSource.getLoginType()) {
                case 1: //hotel
                    switch (preferenceHelperDataSource.getHotelDataModel().getHotelUserType()) {
                        case 1: //travel desk
                            //homeActivityView.disableCashOption();
                            break;
                    }
                    break;
            }
        }

        if (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)) {
            if (!preferenceHelperDataSource.isCardEnable())
                homeActivityView.disableCardOption(true);
            else if (preferenceHelperDataSource.getDefaultCardDetails() == null)
                homeActivityView.disableCardOption(false);

            if (!preferenceHelperDataSource.getWalletSettings().isWalletEnable()) {
                homeActivityView.disableWalletOption();
                mHomActivityModel.setWalletSelected(false);
            } else {
                if (preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getSoftLimit()
                        || preferenceHelperDataSource.getWalletSettings().getWalletBalance() <= 0) {
                    homeActivityView.hideWalletOption();
                    mHomActivityModel.setWalletSelected(false);
                } else {
                    homeActivityView.enableWalletOption(utility.currencyAdjustment
                            (preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                                    preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),
                                    preferenceHelperDataSource.getWalletSettings().getWalletBalance() + ""));
                }
            }

            if (preferenceHelperDataSource.getDefaultCardDetails() != null) {
                homeActivityView.showDefaultCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                        preferenceHelperDataSource.getDefaultCardDetails().getBrand(),
                        mHomActivityModel.getPaymentType(), mHomActivityModel.isWalletSelected());
            } else
                homeActivityView.showDefaultCard(null, null, mHomActivityModel.getPaymentType(),
                        mHomActivityModel.isWalletSelected());
        } else if (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2) {
            homeActivityView.disableCardOption(true);
            homeActivityView.disableWalletOption();
            mHomActivityModel.setWalletSelected(false);
        }
    }

    @Override
    public void checkForDefaultPayment() {
        Utility.printLog(TAG + " CARD checkForDefaultPayment ");
        if (mHomActivityModel.getSelectedProfile() == null) {
            switch (preferenceHelperDataSource.getDefaultPaymentMethod()) {
                case 1:
                    Utility.printLog(TAG + " checkForDefaultPayment 1 ");
                    checkForCardDefault();
                    break;

                case 2:
                    if (preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                            (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)))
                        homeActivityView.setCashPaymentOption();
                    else
                        checkForCardDefault();
                    break;
            }
        }

        if (mHomActivityModel.getPromoCodeModel() != null)
            homeActivityView.setPromoCode(mHomActivityModel.getPromoCodeModel().getCouponCode());
        else
            homeActivityView.setPromoCode(mContext.getString(R.string.promo_code));
    }

    /**
     * <h2>checkForCardDefault</h2>
     * used to check for card payment default
     */
    private void checkForCardDefault() {
        if (preferenceHelperDataSource.isCardEnable()) {
            if (preferenceHelperDataSource.getDefaultCardDetails() != null && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))) {
                Utility.printLog(TAG + " checkForDefaultPayment not null ");
                mHomActivityModel.setWalletSelected(false);
                mHomActivityModel.setPaymentType(CARD);
                mHomActivityModel.setPayByWallet(DONT_PAY_BY_WALLET);
                mHomActivityModel.setCardToken(preferenceHelperDataSource.getDefaultCardDetails().getId());
                mHomActivityModel.setCardLastDigits(preferenceHelperDataSource.getDefaultCardDetails().getLast4());
                mHomActivityModel.setCardBrand(preferenceHelperDataSource.getDefaultCardDetails().getBrand());
                homeActivityView.setSelectedCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                        preferenceHelperDataSource.getDefaultCardDetails().getBrand());
                if (mHomActivityModel.isVehicleAdded())
                    homeActivityView.enableBooking();
            } else if (preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2))) {
                mHomActivityModel.setWalletSelected(false);
                Utility.printLog(TAG + " checkForDefaultPayment null ");
                homeActivityView.setCashPaymentOption();
            } else
                homeActivityView.disableBooking(1);
        } else if (preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2))) {
            mHomActivityModel.setWalletSelected(false);
            Utility.printLog(TAG + " checkForDefaultPayment null ");
            homeActivityView.setCashPaymentOption();
        } else
            homeActivityView.disableBooking(1);
    }

    @Override
    public void checkSomeOneBookingEligible(int bookingType, boolean change) {
        if (!preferenceHelperDataSource.getPickUpLatitude().equals("") &&
                !preferenceHelperDataSource.getPickUpLongitude().equals("")) {
            Location startPoint = new Location("START");
            startPoint.setLatitude(mHomActivityModel.getCurrentLatitude());
            startPoint.setLongitude(mHomActivityModel.getCurrentLongitude());

            Location endPoint = new Location("END");
            endPoint.setLatitude(mHomActivityModel.getMapCenterLatitude());
            endPoint.setLongitude(mHomActivityModel.getMapCenterLongitude());

            Utility.printLog(TAG + " difference buffer " + preferenceHelperDataSource.getSomeoneBookingRange());
            if (startPoint.distanceTo(endPoint) > preferenceHelperDataSource.getSomeoneBookingRange() &&
                    preferenceHelperDataSource.getLoginType() == 0) {
                Utility.printLog(TAG + " difference " + startPoint.distanceTo(endPoint));
                homeActivityView.showSomeOneBookingLayout(bookingType);
            } else {
                mHomActivityModel.setCustomerName("");
                mHomActivityModel.setCustomerNumber("");
                mHomActivityModel.setSomeOneElseBooking(0);
                if (favDriversList.size() > 1 && preferenceHelperDataSource.isFavDriverEnable())
                    homeActivityView.showFavDriversDialog(bookingType);
                else
                    checkForBookingType(bookingType, change);
            }
        } else {
            mHomActivityModel.setCustomerName("");
            mHomActivityModel.setCustomerNumber("");
            mHomActivityModel.setSomeOneElseBooking(0);
            if (favDriversList.size() > 1 && preferenceHelperDataSource.isFavDriverEnable())
                homeActivityView.showFavDriversDialog(bookingType);
            else
                checkForBookingType(bookingType, change);
        }
    }

    @Override
    public void checkForBookingType(int bookingType, boolean change) {
        switch (bookingType) {
            case NOW_BOOKING_TYPE:
                handleClickEventForBooking(NOW_BOOKING_TYPE, "", "", change);
                break;

            case LATER_BOOKING_TYPE:
                homeActivityView.showLaterBookingTimer(change);
                break;
        }
    }

    @Override
    public void setSomeOneDetails(String name, String phNumber, int bookingType) {
        mHomActivityModel.setCustomerName(name);
        mHomActivityModel.setCustomerNumber(phNumber);
        if (name.equals("") && phNumber.equals(""))
            mHomActivityModel.setSomeOneElseBooking(0);
        else
            mHomActivityModel.setSomeOneElseBooking(1);
        if (favDriversList.size() > 1 && preferenceHelperDataSource.isFavDriverEnable())
            homeActivityView.showFavDriversDialog(bookingType);
        else
            checkForBookingType(bookingType, true);
    }

    @Override
    public void getUserDetails() {
        homeActivityView.populateUserDetails(preferenceHelperDataSource.getMobileNo(),
                preferenceHelperDataSource.getUsername(), preferenceHelperDataSource.getImageUrl());
    }

    @Override
    public void validateMobileNumber(String number, String name, ArrayList<String> listOfContacts) {
        Utility.printLog(TAG + " is number valid " + Validation.isValidMobile(number));
        if (Validation.isValidMobile(number)) {
            if (!listOfContacts.contains(number))
                homeActivityView.addContact(name, number);
            else
                homeActivityView.showAlertForMultipleNumber();
        } else
            homeActivityView.showAlertForInvalidNumber();
    }

    @Override
    public void setMapReady(boolean ready) {
        mHomActivityModel.setMapReady(ready);
    }

    @Override
    public void handleCapacityChose(String capacity) {
        mHomActivityModel.setVehicleCapacity(capacity);
    }

    @Override
    public void rotateKey() {
        plotPath();
    }

    @Override
    public void plotPathRoute(LatLng origin, LatLng dest) {
        pathPlotOrigin = origin;
        pathPlotDest = dest;
        plotPath();
    }

    /**
     * <h2>plotPath</h2>
     * used to plot the path
     */
    private void plotPath() {
        int color = ContextCompat.getColor(mContext, R.color.vehicle_unselect_color);
        DownloadPathUrl downloadPathUrl = new DownloadPathUrl(rxRoutePathObserver, color,
                preferenceHelperDataSource, this);
        String url = GetGoogleDirectionsUrl.getDirectionsUrl(pathPlotOrigin, pathPlotDest,
                preferenceHelperDataSource.getGoogleServerKey());
        downloadPathUrl.execute(url);
    }
}
