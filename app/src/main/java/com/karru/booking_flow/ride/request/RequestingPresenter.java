package com.karru.booking_flow.ride.request;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.booking_flow.ride.request.model.RequestingModel;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.RxConfirmPickNotifier;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.karru.managers.location.AddressProviderFromLocation;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxAddressObserver;
import com.karru.managers.location.RxLocationObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.util.DataParser;
import com.karru.util.DateFormatter;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.AMOUNT;
import static com.karru.utility.Constants.BOOKING_DATE;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.BOOKING_TIME;
import static com.karru.utility.Constants.BOOKING_TYPE;
import static com.karru.utility.Constants.CARD_BRAND;
import static com.karru.utility.Constants.CARD_TOKEN;
import static com.karru.utility.Constants.DEVICE_TYPE;
import static com.karru.utility.Constants.DISTANCE;
import static com.karru.utility.Constants.DISTANCE_FARE;
import static com.karru.utility.Constants.DRIVER_PREF;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DURATION;
import static com.karru.utility.Constants.ESTIMATE_ID;
import static com.karru.utility.Constants.FAV_DRIVERS;
import static com.karru.utility.Constants.FROM_LIVE_TRACKING;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.GUEST_COUNRY_CODE;
import static com.karru.utility.Constants.GUEST_NAME;
import static com.karru.utility.Constants.GUEST_NUMBER;
import static com.karru.utility.Constants.GUEST_ROOM_NUMBER;
import static com.karru.utility.Constants.HOTEL_USER_ID;
import static com.karru.utility.Constants.HOTEL_USER_TYPE;
import static com.karru.utility.Constants.INSTITUTE_ID;
import static com.karru.utility.Constants.IS_PARTNER_TYPE;
import static com.karru.utility.Constants.IS_TOWING_ENABLE;
import static com.karru.utility.Constants.LATER_BOOKING_TYPE;
import static com.karru.utility.Constants.NOW_BOOKING_TYPE;
import static com.karru.utility.Constants.PACKAGE_ID;
import static com.karru.utility.Constants.PAYMENT_OPTION;
import static com.karru.utility.Constants.PAYMENT_TYPE;
import static com.karru.utility.Constants.PICK_ADDRESS;
import static com.karru.utility.Constants.PICK_GATE_ID;
import static com.karru.utility.Constants.PICK_GATE_TITLE;
import static com.karru.utility.Constants.PICK_LAT;
import static com.karru.utility.Constants.PICK_LONG;
import static com.karru.utility.Constants.PICK_ZONE_ID;
import static com.karru.utility.Constants.PICK_ZONE_TITLE;
import static com.karru.utility.Constants.PROMO_CODE;
import static com.karru.utility.Constants.REQUEST;
import static com.karru.utility.Constants.SOMEONE_ELSE_BOOKING;
import static com.karru.utility.Constants.SOMEONE_NAME;
import static com.karru.utility.Constants.SOMEONE_NUMBER;
import static com.karru.utility.Constants.TIME_FARE;
import static com.karru.utility.Constants.VEHICLE_CAPACITY;
import static com.karru.utility.Constants.VEHICLE_ID;
import static com.karru.utility.Constants.VEHICLE_IMAGE;
import static com.karru.utility.Constants.VEHICLE_NAME;

/**
 * <h1>RequestingPresenter</h1>
 * This class is used to call the API and link between view and model
 * @author 3Embed
 * @since on 24-01-2018.
 */
class RequestingPresenter implements RequestingContract.Presenter
{
    private static final String TAG = "RequestingPresenter";

    @Inject Context mContext;
    @Inject RequestingActivity mActivity;
    @Inject com.karru.util.Utility utility;
    @Inject NetworkService networkService;
    @Inject RequestingModel mRequestingModel;
    @Inject LocationProvider locationProvider;
    @Inject @Named(REQUEST) CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject DateFormatter dateFormatter;
    @Inject RequestingContract.View requestingView;
    @Inject AddressProviderFromLocation addressProvider;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;
    @Inject Gson gson;
    @Inject SQLiteDataSource sqLiteDataSource;

    private int progressStatus = 0;
    private String bookingId;
    private Disposable disposable;
    private Disposable locationDisposable,addressDisposable;
    private CountDownTimer countDownTimer;
    private RxLocationObserver rxLocationObserver;
    private RxAddressObserver rxAddressObserver;
    private RxLiveBookingDetailsObserver rxBookingDetailsObserver;
    private RxConfirmPickNotifier rxConfirmPickNotifier;
    private RequestBookingDetails requestBookingDetails;

    @Inject
    RequestingPresenter(RxLiveBookingDetailsObserver rxBookingDetailsObserver, RxLocationObserver rxLocationObserver,
                        RxAddressObserver rxAddressObserver, RxConfirmPickNotifier rxConfirmPickNotifier)
    {
        this.rxBookingDetailsObserver = rxBookingDetailsObserver;
        this.rxLocationObserver = rxLocationObserver;
        this.rxAddressObserver = rxAddressObserver;
        this.rxConfirmPickNotifier = rxConfirmPickNotifier;
        subscribeBookingDetails();
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void extractData(Bundle bundle,RequestBookingDetails requestBookingDetails)
    {
        this.requestBookingDetails = requestBookingDetails;
        if(requestBookingDetails == null)
        {
            mRequestingModel.setVehicleTypeId(bundle.getString(VEHICLE_ID));
            mRequestingModel.setPaymentType(bundle.getInt(PAYMENT_TYPE));
            mRequestingModel.setBookingType(bundle.getInt(BOOKING_TYPE));
            mRequestingModel.setPickupAddress(bundle.getString(PICK_ADDRESS));
            mRequestingModel.setPickupLatitude(bundle.getString(PICK_LAT));
            mRequestingModel.setPickupLongitude(bundle.getString(PICK_LONG));
            mRequestingModel.setAmount(bundle.getString(AMOUNT));
            mRequestingModel.setTimeFare(bundle.getString(TIME_FARE));
            mRequestingModel.setDistFare(bundle.getString(DISTANCE_FARE));
            mRequestingModel.setDistance(bundle.getString(DISTANCE));
            mRequestingModel.setDuration(bundle.getString(DURATION));
            mRequestingModel.setCardToken(bundle.getString(CARD_TOKEN));
            mRequestingModel.setCardType(bundle.getString(CARD_BRAND));
            mRequestingModel.setEstimateId(bundle.getString(ESTIMATE_ID));
            mRequestingModel.setSelectedVehicleImage(bundle.getString(VEHICLE_IMAGE));
            mRequestingModel.setSelectedVehicleName(bundle.getString(VEHICLE_NAME));
            mRequestingModel.setBookingDate(bundle.getString(BOOKING_DATE));
            mRequestingModel.setAreaZoneId(bundle.getString(PICK_ZONE_ID));
            mRequestingModel.setAreaZoneTitle(bundle.getString(PICK_ZONE_TITLE));
            mRequestingModel.setAreaPickupId(bundle.getString(PICK_GATE_ID));
            mRequestingModel.setAreaPickupTitle(bundle.getString(PICK_GATE_TITLE));
            mRequestingModel.setCustomerName(bundle.getString(SOMEONE_NAME));
            mRequestingModel.setCustomerNumber(bundle.getString(SOMEONE_NUMBER));
            mRequestingModel.setNumOfPassanger(bundle.getString(VEHICLE_CAPACITY));
            mRequestingModel.setSomeOneElseBooking(bundle.getInt(SOMEONE_ELSE_BOOKING));
            mRequestingModel.setDriverPreference( bundle.getString(DRIVER_PREF));
            mRequestingModel.setPayByWallet(bundle.getInt(PAYMENT_OPTION));
            mRequestingModel.setInstituteId(bundle.getString(INSTITUTE_ID));
            mRequestingModel.setFavoriteDriverId(bundle.getString(FAV_DRIVERS));
            mRequestingModel.setPromoCode(bundle.getString(PROMO_CODE));
            mRequestingModel.setTowingEnabled(bundle.getBoolean(IS_TOWING_ENABLE));
            mRequestingModel.setIsPartnerUser(bundle.getInt(IS_PARTNER_TYPE));
            mRequestingModel.setHotelUserType(bundle.getInt(HOTEL_USER_TYPE));
            mRequestingModel.setGuestName(bundle.getString(GUEST_NAME));
            mRequestingModel.setGuestCountryCode(bundle.getString(GUEST_COUNRY_CODE));
            mRequestingModel.setGuestNumber(bundle.getString(GUEST_NUMBER));
            mRequestingModel.setGuestRoomNumber(bundle.getString(GUEST_ROOM_NUMBER));
            mRequestingModel.setHotelUserId(bundle.getString(HOTEL_USER_ID));
            mRequestingModel.setPackageId(bundle.getString(PACKAGE_ID));

            String pickZoneTitle = bundle.getString(PICK_ZONE_TITLE);
            String pickGateTitle = bundle.getString(PICK_GATE_TITLE);
            if(Objects.requireNonNull(pickZoneTitle).isEmpty() && Objects.requireNonNull(pickGateTitle).isEmpty())
            {
                requestingView.setUIWithAddress(mRequestingModel.getPickupAddress(),bundle.getString(DROP_ADDRESS),
                        mRequestingModel.getSelectedVehicleImage(),mRequestingModel.getSelectedVehicleName());
            }
            else
            {
                requestingView.setUIWithAddress(mRequestingModel.getPickupAddress()+", "+pickZoneTitle+", "+
                                pickGateTitle,bundle.getString(DROP_ADDRESS),
                        mRequestingModel.getSelectedVehicleImage(),mRequestingModel.getSelectedVehicleName());
            }
        }
        else
        {
            requestingView.enableRequestingScreen();
            requestingView.setUIWithAddress(requestBookingDetails.getPickupAddress(),requestBookingDetails.getDropAddress(),
                    "",requestBookingDetails.getVehicleTypeName());
            int bookingPeakTIme = requestBookingDetails.getTotalTime();
            bookingId = requestBookingDetails.getBookingId();
            String vehicleTypeName = requestBookingDetails.getVehicleTypeName();
            triggerTimerForBooking(bookingPeakTIme+5,requestBookingDetails.getElapsedTime());
            requestingView.setBookingDetails(bookingId,vehicleTypeName);
        }
    }

    @Override
    public void fetchPickLocation() {
        requestingView.moveMapToCurrentLocation(new LatLng(Double.parseDouble(mRequestingModel.getPickupLatitude()),
                Double.parseDouble(mRequestingModel.getPickupLongitude())));
    }

    @Override
    public void startCurrLocation()
    {
        bookingDetailsAPI();
        locationProvider.startLocation(this);
    }

    @Override
    public void subscribeLocationChange()
    {
        rxLocationObserver.subscribeOn(Schedulers.io());
        rxLocationObserver.observeOn(AndroidSchedulers.mainThread());
        locationDisposable = rxLocationObserver.subscribeWith( new DisposableObserver<Location>()
        {
            @Override
            public void onNext(Location location)
            {
                GMT_CURRENT_LAT = location.getLatitude()+"";
                GMT_CURRENT_LNG = location.getLongitude()+"";
                Utility.printLog(TAG+" onNext location oberved  "+location.getLatitude()
                        +" "+location.getLongitude());
                preferenceHelperDataSource.setCurrLatitude(String.valueOf(location.getLatitude()));
                preferenceHelperDataSource.setCurrLongitude(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError location oberved  "+e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(locationDisposable);
            }
        });
    }

    @Override
    public void subscribeAddressChange()
    {
        rxAddressObserver.subscribeOn(Schedulers.io());
        rxAddressObserver.observeOn(AndroidSchedulers.mainThread());
        addressDisposable = rxAddressObserver.subscribeWith( new DisposableObserver<Address>()
        {
            @Override
            public void onNext(Address address)
            {
                Utility.printLog(TAG+" onNext address observed  "+address.toString());
                String fullAddress = DataParser.getStringAddress(address);
                Utility.printLog(TAG+ " onNext getAddressFromLocation fullAddress: " + fullAddress);
                preferenceHelperDataSource.setPickUpAddress(fullAddress);
                mRequestingModel.setPickupAddress(fullAddress);

                if(Objects.requireNonNull(mRequestingModel.getAreaZoneTitle()).isEmpty() &&
                        Objects.requireNonNull(mRequestingModel.getAreaPickupTitle()).isEmpty())
                    requestingView.setUIWithAddress(fullAddress);
                else
                    requestingView.setUIWithAddress(fullAddress+", "+ mRequestingModel.getAreaZoneTitle()+", "+
                            mRequestingModel.getAreaPickupTitle());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError address observed  "+e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(addressDisposable);
            }
        });
    }

    @Override
    public void bookingAPI()
    {
        if(networkStateHolder.isConnected())
        {
            rxConfirmPickNotifier.notifyConfirmClick(true);
            String userVehicleId = null;
            if(mRequestingModel.isTowingEnabled())
                userVehicleId = preferenceHelperDataSource.getDefaultVehicle().getId();

            Utility.printLog(TAG+" payment option "+mRequestingModel.getPaymentType()+" "+mRequestingModel.getPayByWallet());
            Observable<Response<ResponseBody>> request =
                    networkService.liveBooking(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),mRequestingModel.getVehicleTypeId(),mRequestingModel.getPaymentType(),
                            mRequestingModel.getPayByWallet(), mRequestingModel.getBookingType(), utility.getDeviceId(mContext), DEVICE_TYPE,
                            mRequestingModel.getBookingDate(), dateFormatter.getCurrTime(preferenceHelperDataSource), mRequestingModel.getAmount(),mRequestingModel.getTimeFare(),
                            mRequestingModel.getDistFare(), mRequestingModel.getDistance(),mRequestingModel.getDuration(),mRequestingModel.getPickupAddress(),
                            mRequestingModel.getPickupLongitude(),mRequestingModel.getPickupLatitude(),
                            preferenceHelperDataSource.getDropAddress(),preferenceHelperDataSource.getDropLongitude(),
                            preferenceHelperDataSource.getDropLatitude(),mRequestingModel.getCardToken(),
                            mRequestingModel.getCardType(),mRequestingModel.getEstimateId(),mRequestingModel.getAreaZoneId(),
                            mRequestingModel.getAreaZoneTitle(),mRequestingModel.getAreaPickupId(),mRequestingModel.getAreaPickupTitle(),
                            mRequestingModel.getCustomerName(),mRequestingModel.getCustomerNumber(),mRequestingModel.getNumOfPassanger(),
                            mRequestingModel.getDriverPreference(), mRequestingModel.isSomeOneElseBooking(),
                            mRequestingModel.getInstituteId(),mRequestingModel.getFavoriteDriverId(),2,
                            mRequestingModel.getPromoCode(),userVehicleId);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                        }

                        @Override
                        public void onNext(retrofit2.Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG + " bookingAPI onNext " + result.code());
                            preferenceHelperDataSource.setDefaultPaymentMethod(mRequestingModel.getPaymentType());
                            switch (result.code())
                            {
                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                            sqLiteDataSource);
                                    break;

                                case 200:
                                    String dataObject = DataParser.fetchDataObjectString(result);
                                    try
                                    {
                                        JSONObject object = new JSONObject(dataObject);
                                        int bookingPeakTIme = object.getInt("bookingRequestTimeRide");
                                        bookingId = object.getString("bookingId");
                                        String vehicleTypeName = object.getString("vehicleTypeName");
                                        triggerTimerForBooking(bookingPeakTIme+5,0);

                                        switch (mRequestingModel.getBookingType())
                                        {
                                            case NOW_BOOKING_TYPE:
                                                requestingView.setBookingDetails(bookingId,vehicleTypeName);
                                                break;

                                            case LATER_BOOKING_TYPE:
                                                String pickupAddress = object.getString("pickupAddress");
                                                String dropAddress = object.getString("dropAddress");
                                                String bookingDate = object.getString("bookingDate");
                                                String bookingTime = object.getString("bookingTime");
                                                String pickupLatitude = object.getString("pickupLatitude");
                                                String pickupLongitude = object.getString("pickupLongitude");
                                                Bundle bundle = new Bundle();
                                                bundle.putString(BOOKING_ID, bookingId);
                                                bundle.putString(PICK_ADDRESS,pickupAddress);
                                                bundle.putString(DROP_ADDRESS, dropAddress);
                                                bundle.putString(VEHICLE_NAME, vehicleTypeName);
                                                bundle.putString(BOOKING_DATE, bookingDate);
                                                bundle.putString(BOOKING_TIME,bookingTime);
                                                bundle.putDouble(PICK_LAT, Double.parseDouble(pickupLatitude));
                                                bundle.putDouble(PICK_LONG, Double.parseDouble(pickupLongitude));
                                                requestingView.showTick(mContext.getString(R.string.ride_scheduled));
                                                if(preferenceHelperDataSource.getLoginType() == 0)
                                                    new Handler().postDelayed(() ->
                                                            requestingView.openScheduledBookingScreen(bundle), 1000);
                                                else
                                                    new Handler().postDelayed(() ->
                                                            requestingView.openHotelScreen(), 1000);
                                                break;
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    requestingView.showToast(mContext.getString(R.string.bad_gateway));
                                    requestingView.finishActivity();
                                    break;

                                default:
                                    requestingView.showToast(DataParser.fetchErrorMessage(result));
                                    requestingView.finishActivity();
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " bookingAPI onError " + errorMsg);
                            requestingView.showToast(mContext.getString(R.string.network_problem));
                            requestingView.finishActivity();
                        }
                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " bookingAPI onComplete ");
                        }
                    });
        }
        else
        {
            requestingView.showToast(mContext.getString(R.string.network_problem));
            requestingView.finishActivity();
        }
    }

    @Override
    public void cancelBooking()
    {
        if(networkStateHolder.isConnected())
        {
            requestingView.showProgress();
            Observable<Response<ResponseBody>> request =
                    networkService.cancelBookingBeforeAccept(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),bookingId,"");
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(retrofit2.Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG + " cancelBooking onNext " + result.code());
                            switch (result.code())
                            {
                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                            sqLiteDataSource);
                                    break;

                                case 200:
                                    if(countDownTimer!=null)
                                    {
                                        countDownTimer.cancel();
                                        countDownTimer = null;
                                    }
                                    requestingView.finishActivity();
                                    break;

                                case 502:
                                    requestingView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                case 404:
                                    requestingView.showToast(DataParser.fetchErrorMessage(result));
                                    if(countDownTimer!=null)
                                    {
                                        countDownTimer.cancel();
                                        countDownTimer = null;
                                    }
                                    requestingView.finishActivity();
                                    break;

                                default:
                                    requestingView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " cancelBooking onError " + errorMsg);
                            requestingView.showToast(mContext.getString(R.string.network_problem));
                            requestingView.dismissProgress();
                        }
                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " cancelBooking onComplete ");
                            requestingView.dismissProgress();
                        }
                    });
        }
        else
            requestingView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h2>triggerTimerForBooking</h2>
     * This method is used to trigger the timer for booking
     * @param peakTime  peak time for booking
     */
    private void triggerTimerForBooking(int peakTime,int elapsedTime)
    {
        requestingView.setMaxBookingTime(peakTime);
        if(elapsedTime!=0)
        {
            progressStatus = peakTime-elapsedTime;
            peakTime = elapsedTime;
        }

        Utility.printLog("max time for seek "+peakTime+" "+elapsedTime);
        countDownTimer = new CountDownTimer(peakTime*1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                Utility.printLog("max time for response from tiggge seconds remaining: " +
                        millisUntilFinished / 1000);
                requestingView.setProgressTimer(progressStatus++);
            }
            public void onFinish()
            {
                progressStatus =0;
                requestingView.setProgressTimer(progressStatus);
                if(countDownTimer!=null)
                {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                if(requestBookingDetails != null)
                    requestingView.finishActivity();
                else
                    requestingView.showRetryUI();
            }
        }.start();
    }

    /**
     * <h2>subscribeBookingDetails</h2>
     * This method is used to subscribe to the booking details published
     */
    private void subscribeBookingDetails()
    {
        rxBookingDetailsObserver.subscribeOn(Schedulers.io());
        rxBookingDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        disposable = rxBookingDetailsObserver.subscribeWith( new DisposableObserver<BookingDetailsDataModel>()
        {
            @Override
            public void onNext(BookingDetailsDataModel bookingDetailsDataModel)
            {
                if(countDownTimer!=null)
                {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                requestingView.showTick(mContext.getString(R.string.ride_confirmed));
                Utility.printLog(TAG+" booking details observed  "+bookingDetailsDataModel.getBookingId());
                if(preferenceHelperDataSource.getLoginType() == 0)
                {
                    new Handler(Looper.getMainLooper()).postDelayed(() ->
                            requestingView.openLiveTrackingScreen(bookingDetailsDataModel.getBookingId(),preferenceHelperDataSource.getLoginType()), 1000);
                }
                else
                    new Handler(Looper.getMainLooper()).postDelayed(() ->
                            requestingView.openHotelScreen(), 1000);
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" booking details onAddCardError  "+e);
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(disposable);
            }
        });
    }

    /**
     * <h2>bookingDetailsAPI</h2>
     * used to get the booking details API
     */
    private void bookingDetailsAPI()
    {
        Utility.printLog(TAG+" promo "+mRequestingModel.getPromoCode());
        if(bookingId!=null)
        {
            if (Utility.isNetworkAvailable(mContext))
            {
                requestingView.showProgress();
                Observable<Response<ResponseBody>> request = networkService.bookingDetails(((ApplicationClass)
                                mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(), bookingId,FROM_LIVE_TRACKING);
                request.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(Response<ResponseBody> result)
                            {
                                Utility.printLog(TAG + " bookingDetailsAPI onNext " + result.code());
                                switch (result.code())
                                {
                                    case 401:
                                        ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, sqLiteDataSource);
                                        break;

                                    case 200:
                                        try
                                        {
                                            String dataObject = DataParser.fetchDataObjectString(result);
                                            BookingDetailsDataModel bookingDetailsDataModel = gson.fromJson(dataObject, BookingDetailsDataModel.class);
                                            switch (bookingDetailsDataModel.getStatus())
                                            {
                                                case 6:
                                                case 7:
                                                case 9:
                                                    requestingView.showTick(mContext.getString(R.string.ride_confirmed));
                                                    Utility.printLog(TAG+" booking details observed  "+bookingDetailsDataModel.getBookingId());
                                                    if(preferenceHelperDataSource.getLoginType() == 0)
                                                        new Handler().postDelayed(() ->
                                                                requestingView.openLiveTrackingScreen(bookingDetailsDataModel.getBookingId(), preferenceHelperDataSource.getLoginType()), 1000);
                                                    else
                                                        new Handler(Looper.getMainLooper()).postDelayed(() ->
                                                                requestingView.openHotelScreen(), 1000);
                                                    break;

                                                case 12:
                                                    requestingView.finishActivity();
                                                    break;
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            Utility.printLog(TAG+" Exception bookingDetailsAPI "+e);
                                        }
                                        break;

                                    case 502:
                                        requestingView.showToast(mContext.getString(R.string.bad_gateway));
                                        break;

                                    case 404:
                                        break;

                                    default:
                                        requestingView.showToast(DataParser.fetchErrorMessage(result));
                                        requestingView.finishActivity();
                                        break;
                                }
                            }

                            @Override
                            public void onError(Throwable errorMsg)
                            {
                                Utility.printLog(TAG + " bookingDetailsAPI onAddCardError " + errorMsg);
                                requestingView.dismissProgress();
                                requestingView.showToast(mContext.getString(R.string.network_problem));
                            }

                            @Override
                            public void onComplete()
                            {
                                Utility.printLog(TAG + " bookingDetailsAPI onComplete ");
                                requestingView.dismissProgress();
                            }
                        });
            }
            else
                requestingView.showToast(mContext.getString(R.string.network_problem));
        }
    }

    @Override
    public void handleBackgroundState()
    {
        compositeDisposable.clear();
        locationProvider.stopLocationUpdates();
        addressProvider.stopAddressListener();
    }

    @Override
    public void onLocationServiceDisabled(Status status)
    {
        requestingView.promptUserWithLocationAlert(status);
    }

    @Override
    public void getCurrentLocation() {
        requestingView.moveMapToCurrentLocation(new LatLng(Double.parseDouble(preferenceHelperDataSource.getCurrLatitude()),
                Double.parseDouble(preferenceHelperDataSource.getCurrLongitude())));
    }

    @Override
    public void getAddressFromLocation(LatLng latLng)
    {
        addressProvider.getAddressFromLocation(latLng.latitude,latLng.longitude,preferenceHelperDataSource.getGoogleServerKey());
    }
}
