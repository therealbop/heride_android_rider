package com.karru.landing.history;

import android.content.Context;

import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.history.model.HistoryDataModel;
import com.karru.landing.history.model.HistoryModel;
import com.karru.landing.history.view.BookingHistoryActivity;
import com.karru.landing.home.model.BookingDetailsDataModel;
import com.karru.landing.home.model.DriverCancellationModel;
import com.karru.landing.home.model.InvoiceDetailsModel;
import com.karru.managers.booking.RxDriverCancelledObserver;
import com.karru.managers.booking.RxInvoiceDetailsObserver;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.util.ArrayList;

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
import static com.karru.utility.Constants.HISTORY;

/**
 * <h1>BookingHistoryPresenter</h1>
 * usd to call API
 * @author 3Embed
 * @since on 2/22/2018.
 */
public class BookingHistoryPresenter implements BookingHistoryContract.Presenter
{
    private static final String TAG = "BookingHistoryPresenter";
    private Disposable disposable;
    private HistoryModel historyModel;
    private RxLiveBookingDetailsObserver rxBookingDetailsObserver;
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxInvoiceDetailsObserver rxInvoiceDetailsObserver;

    @Inject Context mContext;
    @Inject BookingHistoryActivity mActivity;
    @Inject Gson gson;
    @Inject com.karru.util.Utility utility;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject NetworkService networkService;
    @Inject @Named(HISTORY) CompositeDisposable compositeDisposable;
    @Inject BookingHistoryContract.View historyView;
    @Inject SQLiteDataSource dataSource;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;
    @Inject RxNetworkObserver rxNetworkObserver;

    @Inject
    BookingHistoryPresenter(RxLiveBookingDetailsObserver rxBookingDetailsObserver,
                            RxInvoiceDetailsObserver rxInvoiceDetailsObserver, RxDriverCancelledObserver rxDriverCancelledObserver)
    {
        this.rxBookingDetailsObserver = rxBookingDetailsObserver;
        this.rxInvoiceDetailsObserver = rxInvoiceDetailsObserver;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
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

                for(int i=0;i<historyModel.getAssigned().size();i++)
                {
                    if(bookingDetailsDataModel.getBookingId().equals(historyModel.getAssigned().get(i).getBookingId()))
                    {
                        switch (bookingDetailsDataModel.getStatus())
                        {
                            case 6:
                            case 7:
                            case 9:
                                historyModel.getAssigned().get(i).setStatus(bookingDetailsDataModel.getStatus());
                                historyModel.getAssigned().get(i).setStatusText(bookingDetailsDataModel.getStatusText());
                                historyView.notifyAssignedList(historyModel.getAssigned());
                                break;
                        }
                        break;
                    }
                }

                for(int unAssignedCount=0;unAssignedCount<historyModel.getUnassigned().size();unAssignedCount++)
                {
                    if(bookingDetailsDataModel.getBookingId().equals(historyModel.getUnassigned().get(unAssignedCount).getBookingId()))
                    {
                        switch (bookingDetailsDataModel.getStatus())
                        {
                            case 6:
                            case 7:
                            case 9:
                                historyModel.getUnassigned().get(unAssignedCount).setStatus(bookingDetailsDataModel.getStatus());
                                historyModel.getUnassigned().get(unAssignedCount).setStatusText(bookingDetailsDataModel.getStatusText());
                                historyModel.getUnassigned().get(unAssignedCount).setBookingDate(
                                        historyModel.getUnassigned().get(unAssignedCount).getBookingDate()+" "+
                                                historyModel.getUnassigned().get(unAssignedCount).getBookingTime());
                                ArrayList<HistoryDataModel> tempHistoryDataModels = new ArrayList<>();
                                tempHistoryDataModels.add(historyModel.getUnassigned().get(unAssignedCount));
                                tempHistoryDataModels.addAll(historyModel.getAssigned());
                                historyModel.getAssigned().clear();
                                historyModel.getAssigned().addAll(tempHistoryDataModels);
                                historyView.notifyAssignedList(historyModel.getAssigned());
                                historyModel.getUnassigned().remove(unAssignedCount);
                                historyView.notifyUnassignedList(historyModel.getUnassigned());
                                break;
                        }
                        break;
                    }
                }
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
     * <h2>subscribeForInvoiceDetails</h2>
     * This method is used to subscribe to the invoice details
     */
    private void subscribeForInvoiceDetails()
    {
        rxInvoiceDetailsObserver.subscribeOn(Schedulers.io());
        rxInvoiceDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        disposable = rxInvoiceDetailsObserver.subscribeWith( new DisposableObserver<InvoiceDetailsModel>()
        {
            @Override
            public void onNext(InvoiceDetailsModel invoiceDetailsModel)
            {
                Utility.printLog(TAG+" invoice details observed  ");
                for(int i=0;i<historyModel.getUnassigned().size();i++)
                {
                    if(invoiceDetailsModel.getBookingId().equals(historyModel.getUnassigned().get(i).getBookingId()))
                    {
                        historyModel.getUnassigned().get(i).setStatus(12);
                        historyModel.getUnassigned().get(i).setDropAddress(invoiceDetailsModel.getDropAddress());
                        historyModel.getUnassigned().get(i).setAmount(invoiceDetailsModel.getTotal()+"");
                        historyModel.getUnassigned().get(i).setCurrencyAbbr(invoiceDetailsModel.getCurrencyAbbr());
                        historyModel.getUnassigned().get(i).setCurrencySymbol(invoiceDetailsModel.getCurrencySymbol());
                        ArrayList<HistoryDataModel> tempList = new ArrayList<>();
                        tempList.add(historyModel.getUnassigned().get(i));
                        tempList.addAll(historyModel.getPast());
                        historyModel.getPast().clear();
                        historyModel.getPast().addAll(tempList);
                        historyView.notifyPastList(historyModel.getPast());
                        historyModel.getUnassigned().remove(i);
                        historyView.notifyUnassignedList(historyModel.getUnassigned());
                        break;
                    }
                }
                for(int i=0;i<historyModel.getAssigned().size();i++)
                {
                    if(invoiceDetailsModel.getBookingId().equals(historyModel.getAssigned().get(i).getBookingId()))
                    {
                        historyModel.getAssigned().get(i).setStatus(12);
                        historyModel.getAssigned().get(i).setDropAddress(invoiceDetailsModel.getDropAddress());
                        historyModel.getAssigned().get(i).setAmount(invoiceDetailsModel.getTotal()+"");
                        historyModel.getAssigned().get(i).setCurrencyAbbr(invoiceDetailsModel.getCurrencyAbbr());
                        historyModel.getAssigned().get(i).setCurrencySymbol(invoiceDetailsModel.getCurrencySymbol());
                        ArrayList<HistoryDataModel> tempList = new ArrayList<>();
                        tempList.add(historyModel.getAssigned().get(i));
                        tempList.addAll(historyModel.getPast());
                        historyModel.getPast().clear();
                        historyModel.getPast().addAll(tempList);
                        historyView.notifyPastList(historyModel.getPast());
                        historyModel.getAssigned().remove(i);
                        historyView.notifyAssignedList(historyModel.getAssigned());
                        break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Utility.printLog(TAG+" invoice details error  "+e);
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(disposable);
            }
        });
    }

    /**
     * <h2>subscribeDriverDetails</h2>
     * This method is used to subscribe to the driver details published
     */
    private void subscribeDriverCancelDetails()
    {
        rxDriverCancelledObserver.subscribeOn(Schedulers.io());
        rxDriverCancelledObserver.observeOn(AndroidSchedulers.mainThread());
        disposable = rxDriverCancelledObserver.subscribeWith( new DisposableObserver<DriverCancellationModel>()
        {
            @Override
            public void onNext(DriverCancellationModel driverCancellationModel)
            {
                Utility.printLog(TAG+" cancel driver details observed  "+driverCancellationModel.getStatusText());
                for(int i=0;i<historyModel.getAssigned().size();i++)
                {
                    if(driverCancellationModel.getBookingId().equals(historyModel.getAssigned().get(i).getBookingId()))
                    {
                        switch (driverCancellationModel.getStatus())
                        {
                            case 5:
                                historyModel.getAssigned().get(i).setStatus(driverCancellationModel.getStatus());
                                historyModel.getAssigned().get(i).setStatusText(driverCancellationModel.getStatusText());
                                historyModel.getAssigned().get(i).setAmount(driverCancellationModel.getAmount());
                                ArrayList<HistoryDataModel> tempHistoryDataModels = new ArrayList<>();
                                tempHistoryDataModels.add(historyModel.getAssigned().get(i));
                                tempHistoryDataModels.addAll(historyModel.getPast());
                                historyModel.getPast().clear();
                                historyModel.getPast().addAll(tempHistoryDataModels);
                                historyModel.getAssigned().remove(i);
                                historyView.notifyAssignedList(historyModel.getAssigned());
                                historyView.notifyPastList(historyModel.getPast());
                                break;
                        }
                        break;
                    }
                }
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
                compositeDisposable.add(disposable);
            }
        });
    }

    @Override
    public void subscribeObservables()
    {
        subscribeBookingDetails();
        subscribeDriverCancelDetails();
        subscribeForInvoiceDetails();
    }

    @Override
    public void getBookingHistory(boolean first)
    {
        if (networkStateHolder.isConnected())
        {
            historyView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getBookingHistory
                    (((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode());
            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(TAG+" booking history response "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    String dataObject = DataParser.fetchDataObjectString(value);
                                    historyModel = gson.fromJson(dataObject,HistoryModel.class);
                                    Utility.printLog(TAG+" list of history pastList in API "+
                                            historyModel.getUnassigned().size()+" "+
                                            historyModel.getAssigned().size()+" "+historyModel.getPast().size());
                                    for(int i =0;i<historyModel.getPast().size();i++)
                                    {
                                        if(historyModel.getPast().get(i).getStatus() == 13)//expired bookings removed from list
                                        {
                                            Utility.printLog(TAG+" list of history pastList id "+ " "+historyModel.getPast().get(i).getBookingId());
                                            historyModel.getPast().remove(i);
                                            i--;
                                            Utility.printLog(TAG+" list of history pastList id after "+ " "+historyModel.getPast().get(i).getBookingId());
                                        }
                                    }
                                    Utility.printLog(TAG+" list of history pastList in API after"+ " "+historyModel.getPast().size());
                                    if(first)
                                        historyView.notifyPagerAdapter(historyModel.getUnassigned(),
                                                historyModel.getAssigned(),historyModel.getPast());
                                    else
                                        historyView.notifyAllList(historyModel.getUnassigned(),
                                                historyModel.getAssigned(),historyModel.getPast());
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,dataSource);
                                    break;

                                case 502:
                                    historyView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    historyView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+" booking history "+ e.getMessage());
                            historyView.dismissProgressDialog();
                            historyView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            historyView.dismissProgressDialog();
                        }
                    });
        }
        else
            historyView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void disposeObservables() {
        compositeDisposable.clear();
    }
}
