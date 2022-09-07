package com.karru.booking_flow.ride.live_tracking;

import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.RxConfigCalled;
import com.karru.api.NetworkService;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.util.path_plot.DownloadPathUrl;
import com.karru.util.path_plot.GetGoogleDirectionsUrl;
import com.karru.util.path_plot.LatLongBounds;
import com.karru.util.path_plot.RotateKey;
import com.karru.util.path_plot.RxRoutePathObserver;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.booking.RxDriverCancelledObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.managers.booking.RxDriverDetailsObserver;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.landing.home.model.DriverCancellationModel;
import com.karru.landing.home.model.DriverDetailsModel;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import org.json.JSONArray;
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

import static android.app.Activity.RESULT_OK;
import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DROP_ADDRESS_REQUEST;
import static com.karru.utility.Constants.DROP_LAT;
import static com.karru.utility.Constants.DROP_LONG;
import static com.karru.utility.Constants.FROM_LIVE_TRACKING;
import static com.karru.utility.Constants.LIVE;

/**
 * <h1>LiveTrackingPresenter</h1>
 * This method is used to add the link between view and model and call the API
 * @author 3Embed
 * @since on 25-01-2018.
 */
public class LiveTrackingPresenter implements LiveTrackingContract.Presenter,RotateKey
{
    private static final String TAG = "LiveTrackingPresenter";

    @Inject Gson gson;
    @Inject Context mContext;
    @Inject LiveTrackingActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(LIVE) CompositeDisposable compositeDisposable;
    @Inject SQLiteDataSource addressDataSource;
    @Inject LiveTrackingContract.View liveTrackingView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private RxRoutePathObserver rxRoutePathObserver;
    private RxConfigCalled rxConfigCalled;
    private RxLiveBookingDetailsObserver rxBookingDetailsObserver;
    private RxDriverDetailsObserver rxDriverDetailsObserver;
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private int bookingStatus;
    private String driverTopic,bookingId,driverPhone,trackingUrl,driverProfilePic,driverId,driverName;
    private Disposable disposableBooking, disposableCancel,configDispose,pathPlotDisposable;
    private LatLng pathPlotOrigin, pathPlotDest, driverLatLong;

    @Inject
    LiveTrackingPresenter(RxLiveBookingDetailsObserver rxBookingDetailsObserver,
                          RxDriverDetailsObserver rxDriverDetailsObserver,RxConfigCalled rxConfigCalled,
                          RxDriverCancelledObserver rxDriverCancelledObserver,RxRoutePathObserver rxRoutePathObserver)
    {
        this.rxRoutePathObserver = rxRoutePathObserver;
        this.rxConfigCalled = rxConfigCalled;
        this.rxBookingDetailsObserver = rxBookingDetailsObserver;
        this.rxDriverDetailsObserver = rxDriverDetailsObserver;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void handleResume()
    {
        subscribeRoutePoints();
        subscribeBookingDetails();
        subscribeDriverDetails();
        subscribeDriverCancelDetails();
        subscribeConfigCalled();
        handleChatModule();
    }

    /**
     * Feature owner : Akbar
     * Date : 22-10-2018
     */
    private void handleChatModule()
    {
        if(preferenceHelperDataSource.isChatModuleEnable())
            liveTrackingView.enableChatModule();
        else
            liveTrackingView.disableChatModule();
    }

    /**
     * <h2>subscribeBookingDetails</h2>
     * This method is used to subscribe to the booking details published
     */
    private void subscribeBookingDetails()
    {
        rxBookingDetailsObserver.subscribeOn(Schedulers.io());
        rxBookingDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        disposableBooking = rxBookingDetailsObserver.subscribeWith(new DisposableObserver<BookingDetailsDataModel>()
        {
            @Override
            public void onNext(BookingDetailsDataModel bookingDetailsDataModel)
            {
                Utility.printLog(TAG+" booking details observed  "+bookingDetailsDataModel.getBookingId());
                if(bookingDetailsDataModel.getBookingId().equals(bookingId))
                    handleBookingDetails(bookingDetailsDataModel);
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
                compositeDisposable.add(disposableBooking);
            }
        });
    }

    /**
     * <h2>subscribeDriverDetails</h2>
     * This method is used to subscribe to the driver details published
     */
    private void subscribeDriverDetails()
    {
        Observer<DriverDetailsModel> observer = new Observer<DriverDetailsModel>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(DriverDetailsModel driverDetailsModel)
            {
                Utility.printLog(TAG+" driver details observed  "+driverDetailsModel.getLatitude());
                LatLng driverLocation = new LatLng(driverDetailsModel.getLatitude(),driverDetailsModel.getLongitude());
                liveTrackingView.moveCarIcon(driverLocation, Float.parseFloat(driverDetailsModel.getLocation_Heading()));

                switch (bookingStatus)
                {
                    case 6:
                        liveTrackingView.pickLatLongBounds(driverLocation);
                        break;

                    case 9:
                        liveTrackingView.destLatLongBounds(driverLocation);
                        break;

                    default:
                        liveTrackingView.hidePath(driverDetailsModel.getName());
                        break;
                }
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" driver details onAddCardError  "+e);
            }

            @Override
            public void onComplete()
            {}
        };
        rxDriverDetailsObserver.subscribeOn(Schedulers.io());
        rxDriverDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        rxDriverDetailsObserver.subscribe(observer);
    }

    /**
     * <h2>subscribeDriverDetails</h2>
     * This method is used to subscribe to the driver details published
     */
    private void subscribeDriverCancelDetails()
    {
        rxDriverCancelledObserver.subscribeOn(Schedulers.io());
        rxDriverCancelledObserver.observeOn(AndroidSchedulers.mainThread());
        disposableCancel = rxDriverCancelledObserver.subscribeWith(new DisposableObserver<DriverCancellationModel>()
        {
            @Override
            public void onNext(DriverCancellationModel driverCancellationModel)
            {
                Utility.printLog(TAG+" cancel driver details observed  "+driverCancellationModel.getStatusText());
                liveTrackingView.showDriverCancelDialog(driverCancellationModel.getStatusText());
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" cancel driver details onAddCardError  "+e);
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(disposableBooking);
            }
        });
    }

    @Override
    public void bookingDetailsAPI(String bookingId)
    {
        this.bookingId = bookingId;
        Utility.printLog(TAG+" bookingDetailsAPI ");
        if (Utility.isNetworkAvailable(mContext))
        {
            liveTrackingView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.bookingDetails(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
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
                                    ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    String dataObject = DataParser.fetchDataObjectString(result);
                                    BookingDetailsDataModel bookingDetailsDataModel = gson.fromJson(dataObject, BookingDetailsDataModel.class);
                                    switch (bookingDetailsDataModel.getStatus())
                                    {
                                        case 6:
                                        case 7:
                                        case 9:
                                            driverTopic = bookingDetailsDataModel.getDriver().getMqttTopic();
                                            if(mqttManager.isMQTTConnected())
                                                mqttManager.subscribeToTopic(driverTopic);
                                            handleBookingDetails(bookingDetailsDataModel);
                                            break;

                                        default:
                                            liveTrackingView.finishActivity();
                                            break;
                                    }
                                    break;

                                case 502:
                                    liveTrackingView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    liveTrackingView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " bookingDetailsAPI onAddCardError " + errorMsg);
                            liveTrackingView.dismissProgressDialog();
                            liveTrackingView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " bookingDetailsAPI onComplete ");
                            liveTrackingView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void confirmCancelBookingAPI()
    {
        Utility.printLog(TAG+" confirmCancelBookingAPI ");
        if (networkStateHolder.isConnected())
        {
            liveTrackingView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.cancelFeesAPI(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), bookingId);
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
                            Utility.printLog(TAG + " confirmCancelBookingAPI onNext " + result.code());
                            switch (result.code())
                            {
                                case 401:
                                    Toast.makeText(mContext, DataParser.fetchErrorMessage(result), Toast.LENGTH_LONG).show();
                                    ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    liveTrackingView.showPassengerCancelPopup(DataParser.fetchSuccessMessage(result));
                                    break;

                                case 502:
                                    liveTrackingView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    liveTrackingView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " confirmCancelBookingAPI onAddCardError " + errorMsg);
                            liveTrackingView.dismissProgressDialog();
                            liveTrackingView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " bookingDetailsAPI onComplete ");
                            liveTrackingView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void getCancellationReasons()
    {
        if (networkStateHolder.isConnected())
        {
            liveTrackingView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.cancelReasons(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode());
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
                            Utility.printLog(TAG + " getCancellationReasons onNext " + result.code());
                            switch (result.code())
                            {
                                case 401:
                                    Toast.makeText(mContext, DataParser.fetchErrorMessage(result), Toast.LENGTH_LONG).show();
                                    ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    JSONArray jsonArray = DataParser.fetchStringArrayFromData(result);
                                    liveTrackingView.populateCancelReasonsDialog(jsonArray);
                                    break;

                                case 502:
                                    liveTrackingView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    liveTrackingView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " getCancellationReasons onAddCardError " + errorMsg);
                            liveTrackingView.dismissProgressDialog();
                            liveTrackingView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " getCancellationReasons onComplete ");
                            liveTrackingView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void handleReturnIntent(int requestCode, int resultCode, Bundle bundle) {
        switch (requestCode)
        {
            case DROP_ADDRESS_REQUEST:
                if(resultCode == RESULT_OK)
                {
                    Utility.printLog(TAG+" drop address update "+bundle.getString(DROP_LAT)+" "
                            +bundle.getString(DROP_LONG)+" "+bundle.getString(DROP_ADDRESS));
                    preferenceHelperDataSource.setDropLatitude(bundle.getString(DROP_LAT));
                    preferenceHelperDataSource.setDropLongitude(bundle.getString(DROP_LONG));
                    preferenceHelperDataSource.setDropAddress(bundle.getString(DROP_ADDRESS));
                    updateDropAddressAPI();
                }
                break;
        }
    }

    /**
     * <h2>updateDropAddressAPI</h2>
     * used to update the drop address
     */
    private void updateDropAddressAPI()
    {
        Utility.printLog(TAG+" updateDropAddressAPI ");
        if (networkStateHolder.isConnected())
        {
            liveTrackingView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.updateDropAddress(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), bookingId,preferenceHelperDataSource.getDropLatitude(),
                            preferenceHelperDataSource.getDropLongitude(),preferenceHelperDataSource.getDropAddress());
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
                            Utility.printLog(TAG + " updateDropAddressAPI onNext " + result.code());
                            switch (result.code())
                            {
                                case 401:
                                    Toast.makeText(mContext, DataParser.fetchErrorMessage(result), Toast.LENGTH_LONG).show();
                                    ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    liveTrackingView.showAlertForAddressUpdate(DataParser.fetchSuccessMessage(result));
                                    break;

                                case 502:
                                    liveTrackingView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    liveTrackingView.showAlertForAddressUpdate(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            Utility.printLog(TAG + " updateDropAddressAPI onAddCardError " + errorMsg);
                            liveTrackingView.dismissProgressDialog();
                            liveTrackingView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " updateDropAddressAPI onComplete ");
                            liveTrackingView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void cancelBookingAPI(String reason)
    {
        if (networkStateHolder.isConnected())
        {
            liveTrackingView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.cancelBookingAPI(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), bookingId,reason);
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
                            Utility.printLog(TAG + " cancelBookingAPI onNext " + result.code());
                            switch (result.code())
                            {
                                case 401:
                                    Toast.makeText(mContext, DataParser.fetchErrorMessage(result), Toast.LENGTH_LONG).show();
                                    ExpireSession.refreshApplication(mContext,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                case 200:
                                    liveTrackingView.finishActivity();
                                    break;

                                case 502:
                                    liveTrackingView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    liveTrackingView.showToast(DataParser.fetchErrorMessage(result));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " cancelBookingAPI onAddCardError " + errorMsg);
                            liveTrackingView.dismissProgressDialog();
                            liveTrackingView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            Utility.printLog(TAG + " cancelBookingAPI onComplete ");
                            liveTrackingView.dismissProgressDialog();
                        }
                    });
        }
    }

    /**
     * <h2>handleBookingDetails</h2>
     * This method is used to handle the booking details
     * @param bookingDetailsDataModel booking details object
     */
    private void handleBookingDetails(BookingDetailsDataModel bookingDetailsDataModel)
    {
        bookingStatus = bookingDetailsDataModel.getStatus();
        driverPhone = bookingDetailsDataModel.getDriver().getPhone();
        trackingUrl = bookingDetailsDataModel.getTrackingUrl();
        driverProfilePic = bookingDetailsDataModel.getDriver().getProfilePic();
        driverId = bookingDetailsDataModel.getDriver().getMqttTopic();
        driverName = bookingDetailsDataModel.getDriver().getName();

        LatLng pickLatLong = new LatLng(bookingDetailsDataModel.getPickup().getLatitude(),
                bookingDetailsDataModel.getPickup().getLongitude());
        driverLatLong = new LatLng(bookingDetailsDataModel.getDriver().getLatitude(),
                bookingDetailsDataModel.getDriver().getLongitude());
        LatLng dropLatLong = new LatLng(bookingDetailsDataModel.getDrop().getLatitude(),
                bookingDetailsDataModel.getDrop().getLongitude());

        liveTrackingView.setBookingUI(bookingDetailsDataModel.getDriver().getAverageRating(), pickLatLong,dropLatLong, driverLatLong, bookingDetailsDataModel.isRental(), bookingDetailsDataModel.getDriver().getName(),
                bookingDetailsDataModel.getPickup().getAddress(), bookingDetailsDataModel.getDrop().getAddress(),
                bookingDetailsDataModel.getVehicle().getPlateNo(), bookingDetailsDataModel.getDriver().getProfilePic(),
                bookingDetailsDataModel.getVehicle().getTypeName(), bookingDetailsDataModel.getVehicle().getMapIcon(),
                bookingDetailsDataModel.getVehicle().getMakeModel(),bookingDetailsDataModel.getVehicle().getColour(),
                bookingDetailsDataModel.getVehicle().getVehicleImage(),bookingDetailsDataModel.getPackageTitle());

        switch (bookingDetailsDataModel.getStatus())
        {
            case 6:
                liveTrackingView.pickLatLongBounds(driverLatLong);
                break;

            case 9:
                if(preferenceHelperDataSource.isChatModuleEnable())
                    liveTrackingView.hideCancelOption(3);
                else
                    liveTrackingView.hideCancelOption(2);
                liveTrackingView.destLatLongBounds(driverLatLong);
                break;

            default:
                liveTrackingView.hidePath(bookingDetailsDataModel.getDriver().getName());
                break;
        }
    }

    /**
     * <h2>subscribeRoutePoints</h2>
     * This method is used to subscribe to the path points from origin to destination
     */
    private void subscribeRoutePoints()
    {
        rxRoutePathObserver.subscribeOn(Schedulers.io());
        rxRoutePathObserver.observeOn(AndroidSchedulers.mainThread());
        pathPlotDisposable = rxRoutePathObserver.subscribeWith( new DisposableObserver<LatLongBounds>()
        {
            @Override
            public void onNext(LatLongBounds latLongBounds)
            {
                Utility.printLog(TAG+"  path plot route "+latLongBounds.getNortheast());
                Utility.printLog(TAG+"  path plot route "+latLongBounds.getDuration());
                liveTrackingView.googlePathPlot(latLongBounds);
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" path plot route  "+e);
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(pathPlotDisposable);
            }
        });
    }

    /**
     * <h2>subscribeRoutePoints</h2>
     * This method is used to subscribe to the path points from origin to destination
     */
    private void subscribeConfigCalled()
    {
        rxConfigCalled.subscribeOn(Schedulers.io());
        rxConfigCalled.observeOn(AndroidSchedulers.mainThread());
        configDispose = rxConfigCalled.subscribeWith( new DisposableObserver<Boolean>()
        {
            @Override
            public void onNext(Boolean isConfigCalled)
            {
                Utility.printLog(TAG+" is subscribeConfigCalled  "+isConfigCalled);
                handleChatModule();
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" path plot route  "+e);
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(configDispose);
            }
        });
    }

    @Override
    public void plotPathRoute(LatLng origin, LatLng dest)
    {
        pathPlotOrigin = origin;
        pathPlotDest = dest;
        plotPath();
    }

    /**
     * <h2>plotPath</h2>
     * used to plot the path
     */
    private void plotPath()
    {
        int color = ContextCompat.getColor(mContext,R.color.vehicle_unselect_color);
        DownloadPathUrl downloadPathUrl = new DownloadPathUrl(rxRoutePathObserver,color,
                preferenceHelperDataSource,this);
        String url = GetGoogleDirectionsUrl.getDirectionsUrl(pathPlotOrigin,pathPlotDest,
                preferenceHelperDataSource.getGoogleServerKey());
        downloadPathUrl.execute(url);
    }

    @Override
    public void handleTrackingShare() {
        liveTrackingView.shareLink(trackingUrl);
    }

    @Override
    public void checkForLocationBounding() {
        switch (bookingStatus)
        {
            case 6:
                liveTrackingView.pickLatLongBounds(driverLatLong);
                break;

            case 9:
                liveTrackingView.destLatLongBounds(driverLatLong);
                break;

            default:
                liveTrackingView.hidePath(driverName);
                break;
        }
    }

    @Override
    public void handleBackState()
    {
        preferenceHelperDataSource.setDropLatitude("");
        preferenceHelperDataSource.setDropLongitude("");
        preferenceHelperDataSource.setDropAddress("");
        Utility.printLog(TAG+" handleBackState ");
        disposableCancel.dispose();
        disposableBooking.dispose();
        compositeDisposable.clear();
        mqttManager.unSubscribeToTopic(driverTopic);
    }

    @Override
    public void checkForChat()
    {
        liveTrackingView.openChatScreen(bookingId,driverId,driverName);
    }

    @Override
    public void handleDriverCall()
    {
        liveTrackingView.callDriver(driverPhone,preferenceHelperDataSource.isTWILIOCallEnable(),driverProfilePic);
    }

    @Override
    public void rotateKey()
    {
        plotPath();
    }
}
