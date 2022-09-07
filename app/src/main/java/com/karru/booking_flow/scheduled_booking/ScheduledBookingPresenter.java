package com.karru.booking_flow.scheduled_booking;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.util.DataParser;
import com.karru.util.DateFormatter;
import com.karru.util.ExpireSession;
import com.karru.util.TimezoneMapper;
import com.karru.utility.Utility;
import com.heride.rider.R;
import org.json.JSONArray;
import java.util.TimeZone;

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
import static com.karru.utility.Constants.BOOKING_DATE;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.BOOKING_TIME;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.FROM_LIVE_TRACKING;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.PICK_ADDRESS;
import static com.karru.utility.Constants.PICK_LAT;
import static com.karru.utility.Constants.PICK_LONG;
import static com.karru.utility.Constants.SCHEDULE;
import static com.karru.utility.Constants.VEHICLE_NAME;

/**
 * <h1>ScheduledBookingPresenter</h1>
 * This method is used to add the link between view and model and call the API
 * @author 3Embed
 * @since on 25-01-2018.
 */
class ScheduledBookingPresenter implements ScheduledBookingContract.Presenter
{
    private String bookingID;
    private Disposable disposable,networkDisposable;
    private RxLiveBookingDetailsObserver rxBookingDetailsObserver;
    private static final String TAG = "ScheduledBookingPresenter";

    @Inject Gson gson;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject Context mContext;
    @Inject ScheduledBookingActivity mActivity;
    @Inject MQTTManager mqttManager;
    @Inject ScheduledBookingContract.View view;
    @Inject NetworkService networkService;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(SCHEDULE) CompositeDisposable compositeDisposable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    ScheduledBookingPresenter(RxLiveBookingDetailsObserver rxBookingDetailsObserver)
    {
        this.rxBookingDetailsObserver = rxBookingDetailsObserver;
        subscribeBookingDetails();
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void extractData(Bundle bundle)
    {
        if(bundle!=null)
        {
            bookingID = bundle.getString(BOOKING_ID);
            String pickAddress = bundle.getString(PICK_ADDRESS);
            String dropAddress = bundle.getString(DROP_ADDRESS);
            String vehicleName = bundle.getString(VEHICLE_NAME);
            String bookingDate = bundle.getString(BOOKING_DATE);
            String bookingTime = bundle.getString(BOOKING_TIME);
            double pickupLatitude = bundle.getDouble(PICK_LAT);
            double pickupLongitude = bundle.getDouble(PICK_LONG);
            String timeZoneString =  TimezoneMapper.latLngToTimezoneString(Double.parseDouble(GMT_CURRENT_LAT),
                    Double.parseDouble(GMT_CURRENT_LNG));
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);

            view.setUpUI(pickupLatitude,pickupLongitude,bookingID,vehicleName,pickAddress,dropAddress,
                    bookingDate, DateFormatter.getDateWithTimeZone(bookingTime,timeZone));
        }
    }

    @Override
    public void bookingDetailsAPI()
    {
        if(bookingID!=null)
        {
            if (Utility.isNetworkAvailable(mContext))
            {
                Observable<Response<ResponseBody>> request = networkService.bookingDetails(((ApplicationClass)
                                mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(), bookingID,FROM_LIVE_TRACKING);
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
                                                    view.openLiveTrackingScreen(bookingID,preferenceHelperDataSource.getLoginType());
                                                    break;

                                                case 1:
                                                    break;

                                                default:
                                                    view.finishActivity(bookingID);
                                                    break;
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            Utility.printLog(TAG+" Exception bookingDetailsAPI "+e);
                                        }
                                        break;

                                    case 502:
                                        view.showToast(mContext.getString(R.string.bad_gateway));
                                        break;

                                    case 404:
                                        break;

                                    default:
                                        view.showToast(DataParser.fetchErrorMessage(result));
                                        view.finishActivity(bookingID);
                                        break;
                                }
                            }

                            @Override
                            public void onError(Throwable errorMsg)
                            {
                                Utility.printLog(TAG + " bookingDetailsAPI onError " + errorMsg);
                            }

                            @Override
                            public void onComplete()
                            {
                                Utility.printLog(TAG + " bookingDetailsAPI onComplete ");
                            }
                        });
            }
        }
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
                Utility.printLog(TAG+" booking details observed  "+bookingDetailsDataModel.getBookingId());
                if(bookingID.equals(bookingDetailsDataModel.getBookingId()))
                    view.openLiveTrackingScreen(bookingID, preferenceHelperDataSource.getLoginType());
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

    @Override
    public void cancelBookingAPI(String reason)
    {
        if (networkStateHolder.isConnected())
        {
            view.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.cancelBookingBeforeAccept(((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(), bookingID,reason);
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
                                    view.finishActivity(bookingID);
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            Utility.printLog(TAG + " cancelBookingAPI onAddCardError " + errorMsg);
                        }

                        @Override
                        public void onComplete() {
                            Utility.printLog(TAG + " cancelBookingAPI onComplete ");
                            view.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void getCancellationReasons()
    {
        if (networkStateHolder.isConnected())
        {
            view.showProgressDialog();
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
                                    view.populateCancelReasonsDialog(jsonArray);
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable errorMsg) {
                            Utility.printLog(TAG + " getCancellationReasons onAddCardError " + errorMsg);
                        }

                        @Override
                        public void onComplete() {
                            Utility.printLog(TAG + " getCancellationReasons onComplete ");
                            view.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }
}
