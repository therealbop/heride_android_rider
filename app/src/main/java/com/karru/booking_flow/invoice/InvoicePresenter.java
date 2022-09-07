package com.karru.booking_flow.invoice;

import android.content.Context;
import android.os.Bundle;

import com.karru.api.NetworkService;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.booking_flow.invoice.view.InvoiceActivity;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.home.model.fare_estimate_model.ExtraFeesModel;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.booking_flow.invoice.model.DriverFeedbackDataModel;
import com.karru.booking_flow.invoice.model.InvoiceModel;
import com.karru.landing.home.model.InvoiceDetailsModel;
import com.karru.util.DataParser;
import com.karru.util.DateFormatter;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
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
import static com.karru.utility.Constants.FROM_LIVE_TRACKING;
import static com.karru.utility.Constants.INVOICE;
import static com.karru.utility.Constants.INVOICE_DATA;

/**
 * <h>InvoicePresenter</h>
 * <p>
 *     Class that work as a Presenter for ReceiptActivity
 * </p>
 * @since 23/08/17.
 */

public class InvoicePresenter implements InvoiceContract.Presenter
{
    private static final String TAG = "InvoicePresenter";

    @Inject Gson gson;
    @Inject Context mContext;
    @Inject InvoiceActivity mActivity;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject InvoiceContract.View invoiceView;
    @Inject @Named(INVOICE) CompositeDisposable compositeDisposable;
    @Inject DateFormatter dateFormatter;
    @Inject SQLiteDataSource addressDataSource;
    @Inject InvoiceModel invoiceModel;
    @Inject com.karru.util.Utility utility;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;

    private boolean isCardEnable;

    @Inject InvoicePresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    /**
     * <h2>getDriverFeedbackData</h2>
     * THis method is used to call the feedback API
     */
    private void getDriverFeedbackData()
    {
        if(networkStateHolder.isConnected())
        {
            invoiceView.showProgress();
            Observable<Response<ResponseBody>> request = networkService.driverFeedbackDetails(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());

            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+ " getDriverFeedbackData onNext: "+value.code());
                            switch (value.code())
                            {
                                case 401 :
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                            addressDataSource);
                                    break;

                                case 200:
                                    invoiceModel.setDriverFeedbackData(DataParser.fetchDataObjectString(value));
                                    DriverFeedbackDataModel driverFeedbackDataModel =gson.fromJson(invoiceModel.getDriverFeedbackData(),
                                            DriverFeedbackDataModel.class );
                                    if(driverFeedbackDataModel.getRating5()!=null)
                                        invoiceView.populateFeedbackList(driverFeedbackDataModel.getRating5());
                                    break;

                                case 502:
                                    invoiceView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    invoiceView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ "getDriverFeedbackData error: "+e.getMessage());
                            invoiceView.dismissProgress();
                            invoiceView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            invoiceView.dismissProgress();
                        }
                    });
        }
        else
            invoiceView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h2>getInvoiceDetailsData</h2>
     * This method is used to call the API to get the invoice details
     */
    private void getInvoiceDetailsData()
    {
        if(networkStateHolder.isConnected())
        {
            invoiceView.showProgress();
            Observable<Response<ResponseBody>> request = networkService.bookingDetails(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),invoiceModel.getInvoiceDetailsModel().getBookingId(),FROM_LIVE_TRACKING);

            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+ " getInvoiceDetailsData onNext: "+value.code());
                            switch (value.code())
                            {
                                case 401 :
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                            addressDataSource);
                                    break;

                                case 200:
                                    String dataObjectString = DataParser.fetchDataObjectString(value);
                                    try
                                    {
                                        JSONObject object=new JSONObject(dataObjectString);
                                        String invoiceObject = object.getString("invoice");
                                        String bookingId = object.getString("bookingId");

                                        invoiceModel.setInvoiceDetailsModel(gson.fromJson(invoiceObject, InvoiceDetailsModel.class));
                                        invoiceModel.setCurrency(invoiceModel.getInvoiceDetailsModel().getCurrencySymbol());
                                        invoiceModel.setCurrencyAbbreviation(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr());
                                        invoiceModel.getInvoiceDetailsModel().setBookingId(bookingId);
                                        populateUIWithData();
                                        checkForTipOption();
                                        getDriverFeedbackData();
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 502:
                                    invoiceView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    invoiceView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ "getInvoiceDetailsData error: "+e.getMessage());
                            invoiceView.dismissProgress();
                            invoiceView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            invoiceView.dismissProgress();
                        }
                    });
        }
        else
            invoiceView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h2>checkForTipOption</h2>
     * used to check for tip option
     */
    private void checkForTipOption()
    {
        if(invoiceModel.getInvoiceDetailsModel().getTip().isTipEnable() &&
                !invoiceModel.getInvoiceDetailsModel().getTip().getTipValues().isEmpty() &&
                isCardEnable)
        {
            invoiceView.enableTipOption(invoiceModel.getInvoiceDetailsModel().getTip().getTipValues(),
                    invoiceModel.getInvoiceDetailsModel().getTip().getTipType(),invoiceModel.getCurrency(),
                    invoiceModel.getCurrencyAbbreviation());
        }
    }

    @Override
    public void updateTip(double tip)
    {
        invoiceModel.setTipAdded(tip);
    }

    @Override
    public void updateReviewForDriver(float rating,String comment)
    {
        Utility.printLog(TAG+ "submitRatingApi selectedRatingReasons: "+ rating);
        if(networkStateHolder.isConnected())
        {
            invoiceView.showProgress();
            if(invoiceModel.getTipAdded() != 0)
            {
                Observable<Response<ResponseBody>> request = networkService.updateTip(
                        ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(),invoiceModel.getInvoiceDetailsModel().getBookingId(),
                        invoiceModel.getTipAdded());

                request.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<ResponseBody>>()
                        {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(Response<ResponseBody> value)
                            {
                                Utility.printLog(TAG+ " Test 1 update tip onNext: "+value.code());
                                switch (value.code())
                                {
                                    case 401 :
                                        invoiceView.dismissProgress();
                                        ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                                addressDataSource);
                                        break;

                                    case 200:
                                        updateReview(rating,comment);
                                        break;

                                    case 502:
                                        invoiceView.dismissProgress();
                                        invoiceView.showToast(mContext.getString(R.string.bad_gateway));
                                        break;

                                    default:
                                        invoiceView.dismissProgress();
                                        invoiceView.showToast(DataParser.fetchErrorMessage(value));
                                        break;
                                }

                            }
                            @Override
                            public void onError(Throwable e)
                            {
                                Utility.printLog(TAG+ "update tip error: "+e.getMessage());
                            }

                            @Override
                            public void onComplete()
                            {
                            }
                        });
            }
            else
                updateReview(rating,comment);
        }
        else
            invoiceView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void handleFavDriverClick()
    {
        if(networkStateHolder.isConnected())
        {
            invoiceView.showProgress();
            Observable<Response<ResponseBody>> request;

            if(invoiceModel.isDriverFavorite())
            {
                request = networkService.deleteDriverFromFav(
                        ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(), invoiceModel.getInvoiceDetailsModel().getDriverId());
            }
            else
            {
                request = networkService.addDriverToFav(
                        ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(), invoiceModel.getInvoiceDetailsModel().getDriverId());
            }
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+ " handleFavDriverClick onNext: "+value.code());
                            switch (value.code())
                            {
                                case 401 :
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                            addressDataSource);
                                    break;

                                case 502:
                                    invoiceView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                case 200:
                                    if(preferenceHelperDataSource.isFavDriverEnable())
                                    {
                                        if(invoiceModel.isDriverFavorite())
                                        {
                                            switch (invoiceModel.getRatingPoints())
                                            {
                                                case 4:
                                                case 5:
                                                    invoiceModel.setDriverFavorite(false);
                                                    invoiceView.enableAddFavDriver();
                                                    break;

                                                default:
                                                    invoiceModel.setDriverFavorite(false);
                                                    invoiceView.enableAddFavDriver();
                                                    invoiceView.hideFavDriverButton();
                                                    break;
                                            }
                                        }
                                        else
                                        {
                                            invoiceModel.setDriverFavorite(true);
                                            invoiceView.enableRemoveFavDriver();
                                        }
                                    }
                                    else
                                        invoiceView.hideFavDriverButton();
                                    break;

                                default:
                                    invoiceView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ " handleFavDriverClick: "+e.getMessage());
                            invoiceView.dismissProgress();
                            invoiceView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            invoiceView.dismissProgress();
                        }
                    });
        }
        else
            invoiceView.showToast(mContext.getString(R.string.network_problem));
    }

    /**
     * <h2>updateReview</h2>
     * used to update the review and rate
     */
    private void updateReview(float rating,String comment)
    {
        if(networkStateHolder.isConnected())
        {
            if(!comment.trim().isEmpty() && invoiceModel.getUserSelectedReasons()!=null )
                invoiceModel.getUserSelectedReasons().append(" ,").append(comment);
            if(invoiceModel.getUserSelectedReasons()==null)
                if(!comment.trim().isEmpty())
                {
                    StringBuilder stringBuilder = new StringBuilder(comment);
                    invoiceModel.setUserSelectedReasons(stringBuilder);
                }
            Utility.printLog(TAG+" user selected reasons "+invoiceModel.getUserSelectedReasons());

            Observable<Response<ResponseBody>> request = networkService.updateReview(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(),invoiceModel.getInvoiceDetailsModel().getBookingId(),rating,
                    invoiceModel.getUserSelectedReasons());

            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+ " Test 1 updateReviewForDriver onNext: "+value.code());
                            switch (value.code())
                            {
                                case 401 :
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,
                                            addressDataSource);
                                    break;

                                case 502:
                                    invoiceView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                case 200:
                                    invoiceView.finishActivity();
                                    break;

                                default:
                                    invoiceView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ "updateReviewForDriver error: "+e.getMessage());
                            invoiceView.dismissProgress();
                            invoiceView.showToast(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            invoiceView.dismissProgress();
                        }
                    });
        }
        else
            invoiceView.showToast(mContext.getString(R.string.network_problem));
    }

    @Override
    public void extractData(Bundle bundle)
    {
        invoiceModel.setInvoiceDetailsModel((InvoiceDetailsModel) bundle.getSerializable(INVOICE_DATA));
        if(invoiceModel.getInvoiceDetailsModel().isCallAPi())
            getInvoiceDetailsData();
        else
        {
            invoiceModel.setCurrency(invoiceModel.getInvoiceDetailsModel().getCurrencySymbol());
            invoiceModel.setCurrencyAbbreviation(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr());
            getDriverFeedbackData();
            populateUIWithData();
            checkForTipOption();
        }
    }

    /**
     * <h2>populateUIWithData</h2>
     * This method is used to populate the UI with the data
     */
    private void populateUIWithData()
    {
        String appntDate =  dateFormatter.getDateInSpecificFormat(invoiceModel.getInvoiceDetailsModel().getBookingDate());
        invoiceModel.setDriverId(invoiceModel.getInvoiceDetailsModel().getDriverId());
        invoiceModel.setDriverFavorite( invoiceModel.getInvoiceDetailsModel().isFavouriteDriver());

        if(preferenceHelperDataSource.isFavDriverEnable())
        {
            if(invoiceModel.isDriverFavorite())
                invoiceView.enableRemoveFavDriver();
            else
                invoiceView.enableAddFavDriver();
        }

        invoiceView.setUIForInvoice(invoiceModel.getInvoiceDetailsModel().getDriverName(),
                invoiceModel.getInvoiceDetailsModel().getMakeModel(), utility.currencyAdjustment(
                        invoiceModel.getCurrencyAbbreviation(),invoiceModel.getCurrency(),
                        invoiceModel.getInvoiceDetailsModel().getTotal()+""),
                invoiceModel.getInvoiceDetailsModel().getDistance(),invoiceModel.getInvoiceDetailsModel().getTime(),
                appntDate,invoiceModel.getInvoiceDetailsModel().getDriverProfilePic());

        checkForPaymentMethods();
        ArrayList<ReceiptDetails> receiptDetailsList = new ArrayList<>();

        switch (invoiceModel.getInvoiceDetailsModel().getIsMinFeeApplied())
        {
            case 0:
                if(!invoiceModel.getInvoiceDetailsModel().isTowTruckBooking())
                    populateSubTotalValues(receiptDetailsList,2);//ride
                else
                {
                    switch (invoiceModel.getInvoiceDetailsModel().getTowTruckBookingService())
                    {
                        case 1: //only fixed
                            populateSubTotalValues(receiptDetailsList,1);
                            break;

                        case 2: //only towing
                            populateSubTotalValues(receiptDetailsList,2);
                            break;

                        case 3://both towing and fixed
                            populateSubTotalValues(receiptDetailsList,3);
                            break;
                    }
                }
                break;

            case 1:
                ReceiptDetails minFare= new ReceiptDetails();
                minFare.setReceiptText(mContext.getString(R.string.min_fare_detail));
                minFare.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                        invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), invoiceModel.getInvoiceDetailsModel().getMinFee()+""));
                minFare.setSubTotal(false);
                minFare.setGrandTotal(false);
                receiptDetailsList.add(minFare);
                break;
        }

        ReceiptDetails subTotalFare= new ReceiptDetails();
        subTotalFare.setReceiptText(mContext.getString(R.string.total_title));
        subTotalFare.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), invoiceModel.getInvoiceDetailsModel().getSubTotal()+""));
        subTotalFare.setSubTotal(true);
        subTotalFare.setGrandTotal(false);
        receiptDetailsList.add(subTotalFare);

        if(!invoiceModel.getInvoiceDetailsModel().getExtraFees().isEmpty() || invoiceModel.getInvoiceDetailsModel().getDiscount() != 0
                || invoiceModel.getInvoiceDetailsModel().getLastDue() != 0 )
        {
            if(!invoiceModel.getInvoiceDetailsModel().getExtraFees().isEmpty())
            {
                for (ExtraFeesModel extraFeesModel : invoiceModel.getInvoiceDetailsModel().getExtraFees())
                {
                    ReceiptDetails receiptDetails8= new ReceiptDetails();
                    receiptDetails8.setReceiptText(extraFeesModel.getTitle());
                    receiptDetails8.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                            invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), String.valueOf(extraFeesModel.getFee())));
                    receiptDetails8.setSubTotal(false);
                    receiptDetails8.setGrandTotal(false);
                    receiptDetailsList.add(receiptDetails8);
                }
            }

            if(invoiceModel.getInvoiceDetailsModel().getDiscount() != 0)
            {
                ReceiptDetails receiptDetails6 = new ReceiptDetails();
                receiptDetails6.setReceiptText(mContext.getString(R.string.discount));
                receiptDetails6.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                        invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), invoiceModel.getInvoiceDetailsModel().getDiscount()+""));
                receiptDetails6.setSubTotal(false);
                receiptDetails6.setGrandTotal(false);
                receiptDetailsList.add(receiptDetails6);
            }

            if(invoiceModel.getInvoiceDetailsModel().getLastDue() != 0)
            {
                ReceiptDetails lastDue= new ReceiptDetails();
                lastDue.setReceiptText(mContext.getString(R.string.last_due));
                lastDue.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                        invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), invoiceModel.getInvoiceDetailsModel().getLastDue()+""));
                lastDue.setSubTotal(false);
                lastDue.setGrandTotal(false);
                receiptDetailsList.add(lastDue);
            }

            ReceiptDetails receiptDetails7 = new ReceiptDetails();
            receiptDetails7.setReceiptText(mContext.getString(R.string.grand_total));
            receiptDetails7.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                    invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), invoiceModel.getInvoiceDetailsModel().getTotal()+""));
            receiptDetails7.setSubTotal(false);
            receiptDetails7.setGrandTotal(true);
            receiptDetailsList.add(receiptDetails7);
        }

        invoiceView.setUIForReceipt(invoiceModel.getInvoiceDetailsModel().getPickupTime(),invoiceModel.getInvoiceDetailsModel().getPickupAddress(),
                invoiceModel.getInvoiceDetailsModel().getDropTime(),invoiceModel.getInvoiceDetailsModel().getDropAddress(),invoiceModel.getInvoiceDetailsModel().isRental(),invoiceModel.getInvoiceDetailsModel().getPackageTitle(),receiptDetailsList);
    }

    /**
     * <h2>populateSubTotalValues</h2>
     * used to populate the sub total breakdown
     * @param type 1 for only fixed  , 2 for only towing & ride, 3 for both
     */
    private void populateSubTotalValues(ArrayList<ReceiptDetails> receiptDetailsList,int type)
    {
        if(type == 2 || type == 3)
        {
            ReceiptDetails receiptDetails= new ReceiptDetails();
            receiptDetails.setReceiptText(mContext.getString(R.string.baseFare));
            receiptDetails.setReceiptValue(utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                    invoiceModel.getCurrency(), invoiceModel.getInvoiceDetailsModel().getBaseFee()+""));
            receiptDetails.setSubTotal(false);
            receiptDetails.setGrandTotal(false);
            receiptDetailsList.add(receiptDetails);

            ReceiptDetails receiptDetails1= new ReceiptDetails();
            receiptDetails1.setReceiptText(mContext.getString(R.string.distance_fare)+"("+invoiceModel.getInvoiceDetailsModel().getDistance()+")");
            receiptDetails1.setReceiptValue(utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                    invoiceModel.getCurrency(),
                    invoiceModel.getInvoiceDetailsModel().getDistanceFee()+""));
            receiptDetails1.setSubTotal(false);
            receiptDetails1.setGrandTotal(false);
            receiptDetailsList.add(receiptDetails1);

            ReceiptDetails receiptDetails2= new ReceiptDetails();
            receiptDetails2.setReceiptText(mContext.getString(R.string.time_fare)+"("+invoiceModel.getInvoiceDetailsModel().getTime()+")");
            receiptDetails2.setReceiptValue(utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                    invoiceModel.getCurrency(), invoiceModel.getInvoiceDetailsModel().getTimeFee()+""));
            receiptDetails2.setSubTotal(false);
            receiptDetails2.setGrandTotal(false);
            receiptDetailsList.add(receiptDetails2);

            if(invoiceModel.getInvoiceDetailsModel().getWaitingFee() != 0)
            {
                ReceiptDetails receiptDetails3= new ReceiptDetails();
                receiptDetails3.setReceiptText(mContext.getString(R.string.waiting_fare)+"("+invoiceModel.getInvoiceDetailsModel().getWaitingTime()+")");
                receiptDetails3.setReceiptValue(utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                        invoiceModel.getCurrency(), invoiceModel.getInvoiceDetailsModel().getWaitingFee()+""));
                receiptDetails3.setSubTotal(false);
                receiptDetails3.setGrandTotal(false);
                receiptDetailsList.add(receiptDetails3);
            }
        }
        if(type == 1 || type == 3)
        {
            if(invoiceModel != null && !invoiceModel.getInvoiceDetailsModel().getTowTruckServices().isEmpty()) {
                //for extra fees
                if (!invoiceModel.getInvoiceDetailsModel().getTowTruckServices().isEmpty()) {
                    for (ExtraFeesModel serviceFeesModel : invoiceModel.getInvoiceDetailsModel().getTowTruckServices()) {
                        ReceiptDetails receiptDetails7 = new ReceiptDetails();
                        receiptDetails7.setReceiptText(serviceFeesModel.getTitle());
                        receiptDetails7.setGrandTotal(false);
                        receiptDetails7.setReceiptValue(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                                invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), String.valueOf(serviceFeesModel.getFee())));
                        receiptDetailsList.add(receiptDetails7);
                    }
                }
            }
        }
    }

    /**
     * <h2>checkForPaymentMethods</h2>
     * used to check payment methods
     */
    private void checkForPaymentMethods()
    {
        if(invoiceModel.getInvoiceDetailsModel().getPaymentMethod().isCorporateBooking())
        {
            invoiceView.setPaymentCorporate(utility.currencyAdjustment(invoiceModel.getInvoiceDetailsModel().getCurrencyAbbr(),
                    invoiceModel.getInvoiceDetailsModel().getCurrencySymbol(), invoiceModel.getInvoiceDetailsModel().getTotal()+""));
        }
        else
        {
            if(invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getCashCollected()>0)
                invoiceView.setPaymentCash(utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                        invoiceModel.getCurrency(), invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getCashCollected()+""));

            if(invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getCardDeduct()>0)
            {
                isCardEnable = true;
                invoiceView.setPaymentCard(invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getCardLastDigits(),
                        invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getCardType(),
                        utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                                invoiceModel.getCurrency(), invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getCardDeduct()+""));
            }

            if(invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getWalletTransaction()>0)
                invoiceView.setPaymentWallet(utility.currencyAdjustment(invoiceModel.getCurrencyAbbreviation(),
                        invoiceModel.getCurrency(), invoiceModel.getInvoiceDetailsModel().getPaymentMethod().getWalletTransaction()+""));
        }
    }
    /**
     * <h2>onRatingChanged</h2>
     * <p>
     *     method to update the views on the change of rating
     * </p>
     * @param ratingValue : selected rating value
     */
    public void onRatingChanged(int ratingValue)
    {
        DriverFeedbackDataModel driverFeedbackDataModels = gson.fromJson(invoiceModel.getDriverFeedbackData(),
                DriverFeedbackDataModel.class);

        invoiceModel.setRatingPoints(ratingValue);
        invoiceModel.getReasonsList().clear();

        // extra work bcz server side sending obj not 2-D array\
        if(driverFeedbackDataModels!=null)
        {
            switch (invoiceModel.getRatingPoints())
            {
                case 1:
                    if(driverFeedbackDataModels.getRating1() != null)
                        invoiceModel.setReasonsList(driverFeedbackDataModels.getRating1());

                    invoiceView.hideFavDriverButton();
                    if(preferenceHelperDataSource.isFavDriverEnable())
                        if(invoiceModel.isDriverFavorite())
                            invoiceView.enableRemoveFavDriver();
                    break;

                case 2:
                    if(driverFeedbackDataModels.getRating2() != null)
                        invoiceModel.setReasonsList(driverFeedbackDataModels.getRating2());

                    invoiceView.hideFavDriverButton();
                    if(preferenceHelperDataSource.isFavDriverEnable())
                        if(invoiceModel.isDriverFavorite())
                            invoiceView.enableRemoveFavDriver();
                    break;

                case 3:
                    if(driverFeedbackDataModels.getRating3() != null)
                        invoiceModel.setReasonsList(driverFeedbackDataModels.getRating3());

                    invoiceView.hideFavDriverButton();
                    if(preferenceHelperDataSource.isFavDriverEnable())
                        if(invoiceModel.isDriverFavorite())
                            invoiceView.enableRemoveFavDriver();
                    break;

                case 4:
                    if(driverFeedbackDataModels.getRating4() != null)
                        invoiceModel.setReasonsList(driverFeedbackDataModels.getRating4());

                    if(preferenceHelperDataSource.isFavDriverEnable())
                    {
                        if(invoiceModel.isDriverFavorite())
                            invoiceView.enableRemoveFavDriver();
                        else
                            invoiceView.enableAddFavDriver();
                    }
                    else
                        invoiceView.hideFavDriverButton();
                    break;

                case 5:
                    if(driverFeedbackDataModels.getRating5() != null)
                        invoiceModel.setReasonsList(driverFeedbackDataModels.getRating5());

                    if(preferenceHelperDataSource.isFavDriverEnable())
                    {
                        if(invoiceModel.isDriverFavorite())
                            invoiceView.enableRemoveFavDriver();
                        else
                            invoiceView.enableAddFavDriver();
                    }
                    else
                        invoiceView.hideFavDriverButton();
                    break;
            }
            invoiceView.populateFeedbackList(invoiceModel.getReasonsList());
        }
    }

    @Override
    public void handleBackState()
    {
        compositeDisposable.clear();
    }

    @Override
    public void userSelectedReasonsList(StringBuilder userSelectedReasons)
    {
        invoiceModel.setUserSelectedReasons(userSelectedReasons);
        Utility.printLog(TAG+" userSelectedReasons "+userSelectedReasons);
    }
}
