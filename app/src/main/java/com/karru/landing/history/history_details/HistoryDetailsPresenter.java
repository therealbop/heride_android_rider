package com.karru.landing.history.history_details;

import android.content.Context;

import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.history.history_details.model.HelpDataModel;
import com.karru.landing.history.history_details.model.HelpModel;
import com.karru.landing.history.history_details.model.HistoryModel;
import com.karru.landing.history.history_details.view.HistoryDetailsActivity;
import com.karru.landing.home.model.fare_estimate_model.ExtraFeesModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.DataParser;
import com.karru.util.DateFormatter;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.FROM_HISTORY;
import static com.karru.utility.Constants.HISTORY_DETAILS;

/**
 * <h1>HistoryDetailsPresenter</h1>
 * THis class is used to link between view and model
 * @author 3Embed
 * @since on 2/23/2018.
 */
public class HistoryDetailsPresenter implements HistoryDetailsContract.Presenter
{
    private static final String TAG = "HistoryDetailsPresenter";
    private ArrayList<HelpDataModel> helpDataModel;
    private int tripStatus;

    @Inject Gson gson;
    @Inject com.karru.util.Utility utility;
    @Inject Context mContext;
    @Inject
    HistoryDetailsActivity mActivity;
    @Inject
    NetworkStateHolder networkStateHolder;
    @Inject
    NetworkService networkService;
    @Inject HistoryDetailsContract.View historyDetailsView;
    @Inject
    DateFormatter dateFormatter;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SQLiteDataSource addressDataSource;
    @Inject @Named(HISTORY_DETAILS) CompositeDisposable compositeDisposable;
    @Inject ArrayList<HelpDataModel> helpDataModels;

    @Inject
    HistoryDetailsPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void getBookingDetails(String bookingId)
    {
        if (networkStateHolder.isConnected())
        {
            historyDetailsView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.bookingDetails(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid())
                    , preferenceHelperDataSource.getLanguageSettings().getCode(), bookingId,FROM_HISTORY);
            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            switch (value.code())
                            {
                                case 200:
                                    Utility.printLog(TAG+" history details "+value.code());
                                    String dataObject = DataParser.fetchDataObjectString(value);
                                    HistoryModel historyModel = gson.fromJson(dataObject,HistoryModel.class);

                                    String currency = historyModel.getReceipt().getCurrencySymbol();
                                    int currencyAbbr = historyModel.getReceipt().getCurrencyAbbr();
                                    String appointDate =  dateFormatter.getDateInSpecificFormat(historyModel.getBookingDate());
                                    historyDetailsView.setTitleActionBar(appointDate,historyModel.getBookingId(),
                                            historyModel.getStatusText(),historyModel.getCancellationReason());
                                    historyDetailsView.setDriverDetails(historyModel.getDriver().getProfilePic(),
                                            historyModel.getDriver().getName(),historyModel.getDriver().getRating(),
                                            utility.currencyAdjustment(currencyAbbr,currency,historyModel.getAmount()),
                                            historyModel.getBusinessName());
                                    tripStatus = historyModel.getStatus();
                                    String currencySymbol = historyModel.getReceipt().getCurrencySymbol();
                                    int currencyAbbreviation = historyModel.getReceipt().getCurrencyAbbr();

                                    switch (historyModel.getStatus())
                                    {
                                        case 4:   //customer cancel
                                        case 5:   //driver cancel
                                            historyDetailsView.setCancelStatus();
                                            if(!historyModel.isCancelationFeesApplied())
                                                historyDetailsView.hideReceiptTab();
                                            else
                                            {
                                                ArrayList<ReceiptDetails> receiptDetailsList = new ArrayList<>();

                                                ReceiptDetails receiptDetails1= new ReceiptDetails();
                                                receiptDetails1.setReceiptText(mContext.getString(R.string.cancellationFee));
                                                receiptDetails1.setReceiptValue(utility.currencyAdjustment
                                                        (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getCancellationFee()+""));
                                                receiptDetails1.setSubTotal(false);
                                                receiptDetails1.setGrandTotal(false);
                                                receiptDetailsList.add(receiptDetails1);

                                                if(historyModel.getReceipt().getLastDue() != 0)
                                                {
                                                    ReceiptDetails lastDue= new ReceiptDetails();
                                                    lastDue.setReceiptText(mContext.getString(R.string.last_due));
                                                    lastDue.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getLastDue()+""));
                                                    lastDue.setSubTotal(false);
                                                    lastDue.setGrandTotal(false);
                                                    receiptDetailsList.add(lastDue);
                                                }

                                                ReceiptDetails receiptDetails7 = new ReceiptDetails();
                                                receiptDetails7.setReceiptText(mContext.getString(R.string.grand_total));
                                                receiptDetails7.setReceiptValue(utility.currencyAdjustment
                                                        (currencyAbbreviation,currencySymbol,historyModel.getAmount()+""));
                                                receiptDetails7.setSubTotal(false);
                                                receiptDetails7.setGrandTotal(true);
                                                receiptDetailsList.add(receiptDetails7);
                                                historyDetailsView.showReceiptDetails(receiptDetailsList);
                                            }
                                            break;

                                        default: //completed
                                            ArrayList<ReceiptDetails> receiptDetailsList = new ArrayList<>();

                                            switch (historyModel.getReceipt().getIsMinFeeApplied())
                                            {
                                                case 0:
                                                    ReceiptDetails receiptDetails= new ReceiptDetails();
                                                    receiptDetails.setReceiptText(mContext.getString(R.string.baseFare));
                                                    receiptDetails.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getBaseFee()+""));
                                                    receiptDetails.setSubTotal(false);
                                                    receiptDetails.setGrandTotal(false);
                                                    receiptDetailsList.add(receiptDetails);

                                                    ReceiptDetails receiptDetails1= new ReceiptDetails();
                                                    receiptDetails1.setReceiptText(mContext.getString(R.string.distance_fare)+"("+historyModel.getReceipt().getDistance()+")");
                                                    receiptDetails1.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getDistanceFee()+""));
                                                    receiptDetails1.setSubTotal(false);
                                                    receiptDetails1.setGrandTotal(false);
                                                    receiptDetailsList.add(receiptDetails1);

                                                    ReceiptDetails receiptDetails2= new ReceiptDetails();
                                                    receiptDetails2.setReceiptText(mContext.getString(R.string.time_fare)+"("+historyModel.getReceipt().getTime()+")");
                                                    receiptDetails2.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getTimeFee()+""));
                                                    receiptDetails2.setSubTotal(false);
                                                    receiptDetails2.setGrandTotal(false);
                                                    receiptDetailsList.add(receiptDetails2);

                                                    if(historyModel.getReceipt().getWaitingFee() != 0)
                                                    {
                                                        ReceiptDetails receiptDetails3= new ReceiptDetails();
                                                        receiptDetails3.setReceiptText(mContext.getString(R.string.waiting_fare)+"("+historyModel.getReceipt().getWaitingTime()+")");
                                                        receiptDetails3.setReceiptValue(utility.currencyAdjustment
                                                                (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getWaitingFee()+""));
                                                        receiptDetails3.setSubTotal(false);
                                                        receiptDetails3.setGrandTotal(false);
                                                        receiptDetailsList.add(receiptDetails3);
                                                    }
                                                    break;

                                                case 1:
                                                    ReceiptDetails minFare= new ReceiptDetails();
                                                    minFare.setReceiptText(mContext.getString(R.string.min_fare_detail));
                                                    minFare.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getMinFee()+""));
                                                    minFare.setSubTotal(false);
                                                    minFare.setGrandTotal(false);
                                                    receiptDetailsList.add(minFare);
                                                    break;
                                            }

                                            ReceiptDetails subTotalFare= new ReceiptDetails();
                                            subTotalFare.setReceiptText(mContext.getString(R.string.total_title));
                                            subTotalFare.setReceiptValue(utility.currencyAdjustment
                                                    (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getSubTotal()+""));
                                            subTotalFare.setSubTotal(true);
                                            subTotalFare.setGrandTotal(false);
                                            receiptDetailsList.add(subTotalFare);

                                            if(historyModel.getReceipt().getDiscount() != 0 ||
                                                    historyModel.getReceipt().getLastDue() != 0 ||
                                                    !historyModel.getReceipt().getExtraFees().isEmpty() ||
                                                    historyModel.getReceipt().getTipAmount() != 0)
                                            {
                                                if(!historyModel.getReceipt().getExtraFees().isEmpty())
                                                {
                                                    for (ExtraFeesModel extraFeesModel : historyModel.getReceipt().getExtraFees())
                                                    {
                                                        ReceiptDetails receiptDetails8= new ReceiptDetails();
                                                        receiptDetails8.setReceiptText(extraFeesModel.getTitle());
                                                        receiptDetails8.setReceiptValue(utility.currencyAdjustment(historyModel.getReceipt().getCurrencyAbbr(),
                                                                historyModel.getReceipt().getCurrencySymbol(), extraFeesModel.getFee()+""));
                                                        receiptDetails8.setSubTotal(false);
                                                        receiptDetails8.setGrandTotal(false);
                                                        receiptDetailsList.add(receiptDetails8);
                                                    }
                                                }

                                                if(historyModel.getReceipt().getDiscount() != 0 )
                                                {
                                                    ReceiptDetails receiptDetails6 = new ReceiptDetails();
                                                    receiptDetails6.setReceiptText(mContext.getString(R.string.discount));
                                                    receiptDetails6.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getDiscount()+""));
                                                    receiptDetails6.setSubTotal(false);
                                                    receiptDetails6.setGrandTotal(false);
                                                    receiptDetailsList.add(receiptDetails6);
                                                }

                                                if(historyModel.getReceipt().getLastDue() != 0)
                                                {
                                                    ReceiptDetails lastDue= new ReceiptDetails();
                                                    lastDue.setReceiptText(mContext.getString(R.string.last_due));
                                                    lastDue.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getLastDue()+""));
                                                    lastDue.setSubTotal(false);
                                                    lastDue.setGrandTotal(false);
                                                    receiptDetailsList.add(lastDue);
                                                }

                                                if(historyModel.getReceipt().getTipAmount() != 0)
                                                {
                                                    ReceiptDetails tip= new ReceiptDetails();
                                                    tip.setReceiptText(mContext.getString(R.string.tip));
                                                    tip.setReceiptValue(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,historyModel.getReceipt().getTipAmount()+""));
                                                    tip.setSubTotal(false);
                                                    tip.setGrandTotal(false);
                                                    receiptDetailsList.add(tip);
                                                }

                                                ReceiptDetails receiptDetails7 = new ReceiptDetails();
                                                receiptDetails7.setReceiptText(mContext.getString(R.string.grand_total));
                                                receiptDetails7.setReceiptValue(utility.currencyAdjustment
                                                        (currencyAbbreviation,currencySymbol,historyModel.getAmount()+""));
                                                receiptDetails7.setSubTotal(false);
                                                receiptDetails7.setGrandTotal(true);
                                                receiptDetailsList.add(receiptDetails7);
                                            }

                                            double cashCollected = historyModel.getReceipt().getPaymentMethod().getCashCollected();
                                            double cardDeduct = historyModel.getReceipt().getPaymentMethod().getCardDeduct();
                                            double walletTransaction = historyModel.getReceipt().getPaymentMethod().getWalletTransaction();
                                            boolean isCorporateBooking = historyModel.getReceipt().getPaymentMethod().isCorporateBooking();

                                            if(isCorporateBooking)
                                            {
                                                historyDetailsView.setPaymentCorporate(utility.currencyAdjustment
                                                        (currencyAbbreviation,currencySymbol,historyModel.getAmount()+""));
                                            }
                                            else
                                            {
                                                if(cashCollected>0)
                                                    historyDetailsView.setPaymentCash(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,cashCollected+""));

                                                if(cardDeduct>0)
                                                    historyDetailsView.setPaymentCard(historyModel.getReceipt().getPaymentMethod().getCardLastDigits(),
                                                            historyModel.getReceipt().getPaymentMethod().getCardType(),utility.currencyAdjustment
                                                                    (currencyAbbreviation,currencySymbol,cardDeduct+""));

                                                if(walletTransaction>0)
                                                    historyDetailsView.setPaymentWallet(utility.currencyAdjustment
                                                            (currencyAbbreviation,currencySymbol,walletTransaction+""));
                                            }

                                            historyDetailsView.showReceiptDetails(receiptDetailsList);
                                            historyDetailsView.setCompletedStatus();
                                            break;
                                    }
                                    try
                                    {
                                        JSONObject dataObjectJson = new JSONObject(dataObject);
                                        JSONObject vehicleObject  = dataObjectJson.getJSONObject("vehicle");
                                        JSONObject distanceAndTimeObject  = dataObjectJson.getJSONObject("distanceAndTime");
                                        JSONObject address  = dataObjectJson.getJSONObject("address");

                                        String plateNo = vehicleObject.getString("plateNo");
                                        String makeModel = vehicleObject.getString("makeModel");
                                        String distance = distanceAndTimeObject.getString("distance");
                                        String distanceParameter = distanceAndTimeObject.getString("distanceParameter");
                                        String timeInMinute = distanceAndTimeObject.getString("timeInMinute");
                                        String timeInHour = distanceAndTimeObject.getString("timeInHour");

                                        String pickup = address.getString("pickup");
                                        String drop = address.getString("drop");

                                        historyDetailsView.setBookingDetails(plateNo,makeModel,distance,
                                                distanceParameter,timeInMinute,timeInHour,pickup,drop);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    historyDetailsView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    historyDetailsView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+" history details "+e);
                            historyDetailsView.dismissProgressDialog();
                            historyDetailsView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            historyDetailsView.dismissProgressDialog();
                        }
                    });
        }
        else
            historyDetailsView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void handleScreenDestroy() {
        compositeDisposable.clear();
    }

    @Override
    public void getHelpDetails() {
        if (networkStateHolder.isConnected())
        {
            int status;
            historyDetailsView.showProgressDialog();
            if(tripStatus == 4 || tripStatus == 5)
            {
                status = 1;
            }else {
                status= 2;
            }
            historyDetailsView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getHelpData(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),status);
            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            switch (value.code())
                            {
                                case 200:

                                    String dataObject = DataParser.fetchSuccessResponse(value);
                                    Utility.printLog(TAG+" help details "+dataObject);
                                    HelpModel helpModel = gson.fromJson(dataObject,HelpModel.class);
                                    helpDataModel = helpModel.getData();
                                    if(helpDataModel.size()>0)
                                    {
                                        helpDataModels.addAll(helpDataModel);
                                        historyDetailsView.setHelpDetailsList();
                                    }

                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    historyDetailsView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    historyDetailsView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+" history details "+e);
                            historyDetailsView.dismissProgressDialog();
                            historyDetailsView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            historyDetailsView.dismissProgressDialog();
                        }
                    });
        }
        else
            historyDetailsView.showToast(mContext.getString(R.string.network_problem));

    }
}
