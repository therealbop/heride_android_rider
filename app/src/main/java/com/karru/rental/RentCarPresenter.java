package com.karru.rental;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.corporate.CorporateProfileModel;
import com.karru.landing.home.model.DriverPreferenceDataModel;
import com.karru.landing.home.model.DriverPreferenceModel;
import com.karru.landing.home.model.eta_model.ETADataModel;
import com.karru.landing.home.model.fare_estimate_model.ExtraFeesModel;
import com.karru.landing.home.model.fare_estimate_model.FareEstimateModel;
import com.karru.landing.payment.model.CardDetails;
import com.karru.managers.RxConfirmPickNotifier;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.rental.model.CarDataModel;
import com.karru.rental.model.CarTypeDataModel;
import com.karru.rental.model.DataModel;
import com.karru.rental.model.RentalModel;
import com.karru.rental.view.RentalActivity;
import com.karru.util.ActivityUtils;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;
import com.heride.rider.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
import static com.karru.utility.Constants.BOOKING_TYPE;
import static com.karru.utility.Constants.BOTH;
import static com.karru.utility.Constants.CARD;
import static com.karru.utility.Constants.CARD_BRAND;
import static com.karru.utility.Constants.CARD_TOKEN;
import static com.karru.utility.Constants.CASH;
import static com.karru.utility.Constants.DISTANCE;
import static com.karru.utility.Constants.DISTANCE_FARE;
import static com.karru.utility.Constants.DONT_PAY_BY_WALLET;
import static com.karru.utility.Constants.DRIVER_PREF;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DURATION;
import static com.karru.utility.Constants.ESTIMATE_ID;
import static com.karru.utility.Constants.FAV_DRIVERS;
import static com.karru.utility.Constants.GUEST_COUNRY_CODE;
import static com.karru.utility.Constants.GUEST_NAME;
import static com.karru.utility.Constants.GUEST_NUMBER;
import static com.karru.utility.Constants.GUEST_ROOM_NUMBER;
import static com.karru.utility.Constants.HOTEL_USER_ID;
import static com.karru.utility.Constants.HOTEL_USER_TYPE;
import static com.karru.utility.Constants.INSTITUTE_ID;
import static com.karru.utility.Constants.IS_PARTNER_TYPE;
import static com.karru.utility.Constants.IS_TOWING_ENABLE;
import static com.karru.utility.Constants.LAST_DIGITS;
import static com.karru.utility.Constants.NOW_BOOKING_TYPE;
import static com.karru.utility.Constants.PACKAGE_ID;
import static com.karru.utility.Constants.PAYMENT_OPTION;
import static com.karru.utility.Constants.PAYMENT_TYPE;
import static com.karru.utility.Constants.PAY_BY_WALLET;
import static com.karru.utility.Constants.PICK_ADDRESS;
import static com.karru.utility.Constants.PICK_GATE_ID;
import static com.karru.utility.Constants.PICK_GATE_TITLE;
import static com.karru.utility.Constants.PICK_LAT;
import static com.karru.utility.Constants.PICK_LONG;
import static com.karru.utility.Constants.PICK_ZONE_ID;
import static com.karru.utility.Constants.PICK_ZONE_TITLE;
import static com.karru.utility.Constants.PROMO_CODE;
import static com.karru.utility.Constants.SOMEONE_ELSE_BOOKING;
import static com.karru.utility.Constants.SOMEONE_NAME;
import static com.karru.utility.Constants.SOMEONE_NUMBER;
import static com.karru.utility.Constants.TIME_FARE;
import static com.karru.utility.Constants.VEHICLE_CAPACITY;
import static com.karru.utility.Constants.VEHICLE_ID;
import static com.karru.utility.Constants.VEHICLE_IMAGE;
import static com.karru.utility.Constants.VEHICLE_NAME;

/**
 * <h1>RentCarPresenter</h1>
 * This method is used to add the link between rentalView and model and call the API
 */

public class RentCarPresenter implements RentCarContract.Presenter {

    private static final String TAG = "RentCarPresenter";
    private CompositeDisposable compositeDisposable;
    private Disposable etaDisposable,rxDispose;
    private ArrayList<ReceiptDetails> listOfBreakDown;
    private FareEstimateModel fareEstimateModel;

    @Inject List<DataModel> rentalPackages;
    @Inject List<CarDataModel> carDataModel;
    @Inject Context mContext;
    @Inject RentalActivity mActivity;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Gson gson;
    @Inject MQTTManager mqttManager;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject RentCarContract.View rentalView;
    @Inject com.karru.util.Utility utility;
    @Inject RentalModel rentalModel;
    @Inject ArrayList<DriverPreferenceDataModel> tempPreferenceDataModels;
    @Inject DriverPreferenceModel driverPreferenceModel;

    private String nearestDriversLatLng = "";
    private  ArrayList<String> prevDriverPrefModel = new ArrayList<>();
    private RxConfirmPickNotifier rxConfirmPickNotifier;

    @Inject
    RentCarPresenter( RxConfirmPickNotifier rxConfirmPickNotifier) {
        this.rxConfirmPickNotifier = rxConfirmPickNotifier;
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void initializeCompositeDisposable() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void extractData(Bundle bundle) {
        rentalModel.setBookingDate(bundle.getString(BOOKING_DATE));
        rentalModel.setAreaZoneId(bundle.getString(PICK_ZONE_ID));
        rentalModel.setAreaZoneTitle(bundle.getString(PICK_ZONE_TITLE));
        rentalModel.setAreaPickupId(bundle.getString(PICK_GATE_ID));
        rentalModel.setAreaPickupTitle(bundle.getString(PICK_GATE_TITLE));
        rentalModel.setCustomerName(bundle.getString(SOMEONE_NAME));
        rentalModel.setCustomerNumber(bundle.getString(SOMEONE_NUMBER));
        rentalModel.setVehicleCapacity(bundle.getString(VEHICLE_CAPACITY));
        rentalModel.setIsSomeOneElseBooking(bundle.getInt(SOMEONE_ELSE_BOOKING));
        rentalModel.setFavoriteDriverId(bundle.getString(FAV_DRIVERS));
        rentalModel.setBookingType(bundle.getInt(BOOKING_TYPE));
        rentalView.setPickAddress(preferenceHelperDataSource.getPickUpAddress());
    }

    @Override
    public void updateSelectedVehicle(int abbr,String symbol, String offImage,String onImage, double cost,
                                      String vehicleId, String vehicleName,String eta) {
        preferenceHelperDataSource.setVehicleID(vehicleId);
        preferenceHelperDataSource.setVehicleName(vehicleName);
        preferenceHelperDataSource.setVehicleImage(onImage);
        String currency = utility.currencyAdjustment(abbr,symbol,String.valueOf(cost));
        rentalView.setCurrency(currency);
        if(eta.equals(mContext.getString(R.string.no_drivers)) && rentalModel.getBookingType() == NOW_BOOKING_TYPE )
            rentalView.disableBooking(0);
    }

    @Override
    public void makeCardAsDefault()
    {
        rentalModel.setWalletSelected(false);
        rentalModel.setPaymentType(CARD);
        rentalModel.setPayByWallet(DONT_PAY_BY_WALLET);
        CardDetails cardDetailsDataModel = preferenceHelperDataSource.getDefaultCardDetails();
        rentalModel.setCardToken(cardDetailsDataModel.getId());
        rentalModel.setCardLastDigits(cardDetailsDataModel.getLast4());
        rentalModel.setCardBrand(cardDetailsDataModel.getBrand());
        rentalView.setSelectedCard(cardDetailsDataModel.getLast4(),cardDetailsDataModel.getBrand());
        rentalView.enableBooking();
        checkForPromoValid();
    }

    @Override
    public void handleCapacityChose(String capacity)
    {
        rentalModel.setVehicleCapacity(capacity);
    }

    @Override
    public void getCorporateProfiles()
    {
        if (networkStateHolder.isConnected())
        {
            rentalView.showProgressDialog();
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
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(" onNext corporateProfileModel" + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        if(rentalModel.getSelectedProfile() == null)
                                        {
                                            ArrayList<CorporateProfileData> corporateProfilesModel = new ArrayList<>();
                                            CorporateProfileData corporateProfileData = new CorporateProfileData();
                                            corporateProfileData.setInstituteName(mContext.getString(R.string.personal));
                                            corporateProfileData.setSelected(true);
                                            corporateProfilesModel.add(corporateProfileData);
                                            String institutes = value.body().string();
                                            Utility.printLog(TAG+" corporate accounts "+institutes);
                                            CorporateProfileModel corporateProfileModel = gson.fromJson(institutes,
                                                    CorporateProfileModel.class);
                                            corporateProfilesModel.addAll(corporateProfileModel.getData());
                                            CorporateProfileData add = new CorporateProfileData();
                                            add.setInstituteName(mContext.getString(R.string.corporate_mail_add));
                                            add.setSelected(false);
                                            corporateProfilesModel.add(add);
                                            Utility.printLog(TAG+" corporateProfileModel size "+corporateProfileModel.getData().size());
                                            rentalView.populateProfiles(corporateProfilesModel);
                                        }
                                        else
                                        {
                                            ArrayList<CorporateProfileData> corporateProfilesModel = new ArrayList<>();
                                            CorporateProfileData corporateProfileData = new CorporateProfileData();
                                            corporateProfileData.setInstituteName(mContext.getString(R.string.personal));
                                            corporateProfileData.setSelected(false);
                                            corporateProfilesModel.add(corporateProfileData);
                                            CorporateProfileModel corporateProfileModel = gson.fromJson(value.body().string(),
                                                    CorporateProfileModel.class);

                                            for(int i=0; i <corporateProfileModel.getData().size();i++)
                                            {
                                                if(rentalModel.getSelectedProfile().getUserId().equals(corporateProfileModel.getData().get(i).getUserId()))
                                                {
                                                    corporateProfileModel.getData().get(i).setSelected(true);
                                                    corporateProfilesModel.add(corporateProfileModel.getData().get(i));
                                                }
                                                else
                                                {
                                                    corporateProfileModel.getData().get(i).setSelected(false);
                                                    corporateProfilesModel.add(corporateProfileModel.getData().get(i));
                                                }
                                            }
                                            CorporateProfileData add = new CorporateProfileData();
                                            add.setInstituteName(mContext.getString(R.string.corporate_mail_add));
                                            add.setSelected(false);
                                            corporateProfilesModel.add(add);
                                            Utility.printLog(TAG+" corporateProfileModel size "+corporateProfileModel.getData().size());
                                            rentalView.populateProfiles(corporateProfilesModel);
                                        }
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    rentalView.showMessage(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    rentalView.showMessage(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            rentalView.dismissProgressDialog();
                            rentalView.showMessage(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            rentalView.dismissProgressDialog();
                        }
                    });
        }
        else
            rentalView.showMessage(mContext.getString(R.string.network_problem));
    }

    @Override
    public void fetchDriverPreferences()
    {
        if (networkStateHolder.isConnected())
        {
            rentalView.showProgressDialog();
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
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(" onNext DriverPreferences " + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        String response = value.body().string();
                                        DriverPreferenceModel driverPreferenceModel = gson.fromJson(response,DriverPreferenceModel.class);
                                        if(tempPreferenceDataModels.isEmpty())
                                            tempPreferenceDataModels.addAll(driverPreferenceModel.getData());
                                        if(RentCarPresenter.this.driverPreferenceModel.getData().size()>0)
                                        {
                                            for(int i=0; i<driverPreferenceModel.getData().size();i++)
                                            {
                                                for(DriverPreferenceDataModel driverPreferenceDataModel:RentCarPresenter.this.driverPreferenceModel.getData())
                                                {
                                                    if(RentCarPresenter.this.driverPreferenceModel.getData().get(i).getId().
                                                            equals(driverPreferenceDataModel.getId()))
                                                    {
                                                        driverPreferenceModel.getData().get(i).setSelected(RentCarPresenter.this.driverPreferenceModel.getData().get(i).isSelected());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        RentCarPresenter.this.driverPreferenceModel.getData().clear();
                                        RentCarPresenter.this.driverPreferenceModel.getData().addAll(driverPreferenceModel.getData());
                                        RentCarPresenter.this.driverPreferenceModel.setCurrencySymbol(preferenceHelperDataSource.getWalletSettings().getCurrencySymbol());
                                        RentCarPresenter.this.driverPreferenceModel.setCurrencyAbbr(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr());
                                        rentalView.showDriverPref();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 502:
                                    rentalView.showMessage(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    rentalView.showMessage(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            rentalView.dismissProgressDialog();
                            rentalView.showMessage(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            rentalView.dismissProgressDialog();
                        }
                    });
        }
        else
            rentalView.showMessage(mContext.getString(R.string.network_problem));
    }

    @Override
    public void addDriverPreferences(boolean isDoneClicked)
    {
        if(isDoneClicked)
        {
            driverPreferenceModel.setData(tempPreferenceDataModels);
            ArrayList<String> latestDriverPrefModel = new ArrayList<>();
            StringBuilder preferenceSelected = new StringBuilder();
            boolean firstPos = false;
            for(DriverPreferenceDataModel driverPreferenceDataModel : driverPreferenceModel.getData())
            {
                if(driverPreferenceDataModel.isSelected())
                {
                    if(!firstPos)
                    {
                        firstPos = true;
                        preferenceSelected = new StringBuilder(driverPreferenceDataModel.getId());
                    }
                    else
                        preferenceSelected.append(",").append(driverPreferenceDataModel.getId());
                    latestDriverPrefModel.add(driverPreferenceDataModel.getId());
                }
            }

            if(!String.valueOf(preferenceSelected).equals(""))
            {
                rentalModel.setDriverPreference(String.valueOf(preferenceSelected));
                rentalView.showSelectedDriverPref(mContext.getString(R.string.driver_preference_set));
            }
            else
            {
                rentalModel.setDriverPreference("");
                rentalView.showSelectedDriverPref(mContext.getString(R.string.driver_preference));
            }

            prevDriverPrefModel = latestDriverPrefModel;
            driverPreferenceModel.setSelectedDriverPref(prevDriverPrefModel);
        }
    }

    @Override
    public void setSelectedProfile(CorporateProfileData selectedProfile)
    {
        rentalModel.setSelectedProfile(selectedProfile);
        if(selectedProfile.getUserId()!= null)
        {
            rentalView.showInstituteWallet(utility.currencyAdjustment(selectedProfile.getCurrencyAbbr(),
                    selectedProfile.getCurrencySymbol(),selectedProfile.getUserWalletBalance()+""));
            rentalModel.setPayByWallet(DONT_PAY_BY_WALLET);
            rentalView.setSelectedProfile(selectedProfile.getInstituteName(),
                    mContext.getResources().getDrawable(R.drawable.briefcase_icon));
            Utility.printLog(TAG+" drop address 1 "+preferenceHelperDataSource.getDropAddress());
        }
        else
        {
            rentalView.enablePaymentOptions();
            Utility.printLog(TAG+" drop address 2 "+preferenceHelperDataSource.getDropAddress());
            rentalModel.setSelectedProfile(null);
            rentalView.setSelectedProfile(selectedProfile.getInstituteName(),
                    mContext.getResources().getDrawable(R.drawable.confirmation_personal_icon));
            checkForDefaultPayment();
        }
    }

    @Override
    public void fetchRentalPackages() {
        if (networkStateHolder.isConnected()) {
            rentalView.showProgressDialog();
            Observable<Response<ResponseBody>> rentCarRequest = networkService.getPackages(((ApplicationClass)
                            mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode(), preferenceHelperDataSource.getPickUpLatitude(),
                    preferenceHelperDataSource.getPickUpLongitude());
            rentCarRequest.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> responseBodyResponse) {
                            switch (responseBodyResponse.code()) {
                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                    break;

                                case 200:
                                    try
                                    {
                                        String dataObject = DataParser.fetchDataObjectString(responseBodyResponse);
                                        RentCarDataModel rentCarDataModel = gson.fromJson(dataObject, RentCarDataModel.class);
                                        if (rentCarDataModel != null) {
                                            Utility.printLog(TAG+" Response rental cars : "+dataObject);
                                            List<DataModel> dataModel = rentCarDataModel.getData();
                                            if (dataModel != null)
                                            {
                                                rentalPackages.addAll(dataModel);
                                                rentalView.notifyPackages(rentCarDataModel.getFareDetailsRules());
                                            }
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        Utility.printLog(TAG+"Inside Catch Block");
                                    }
                                    break;

                                case 502:
                                    rentalView.showMessage(mContext.getString(R.string.bad_gateway));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            rentalView.dismissProgressDialog();
                            Utility.printLog(TAG+ " fetchRentalPackages onError " + e);
                        }

                        @Override
                        public void onComplete() {
                            rentalView.dismissProgressDialog();
                            Utility.printLog(TAG+" fetchRentalPackages onComplete ");
                        }
                    });
        }
    }

    @Override
    public void checkForDefaultPayment()
    {
        Utility.printLog(TAG+" CARD checkForDefaultPayment ");
        if(rentalModel.getSelectedProfile() == null)
        {
            switch (preferenceHelperDataSource.getDefaultPaymentMethod())
            {
                case 1:
                    Utility.printLog(TAG+" checkForDefaultPayment 1 ");
                    checkForCardDefault();
                    break;

                case 2:
                    if(preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                            (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)))
                        rentalView.setCashPaymentOption();
                    else
                        checkForCardDefault();
                    break;
            }
        }

        if(rentalModel.getPromoCodeModel() != null)
            rentalView.setPromoCode(rentalModel.getPromoCodeModel().getCouponCode());
        else
            rentalView.setPromoCode(mContext.getString(R.string.promo_code));
    }

    @Override
    public void setCardPaymentOption()
    {
        if(preferenceHelperDataSource.getDefaultCardDetails()!=null && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
        {
            rentalModel.setWalletSelected(false);
            rentalModel.setPaymentType(CARD);
            rentalModel.setPayByWallet(DONT_PAY_BY_WALLET);

            rentalModel.setCardToken(preferenceHelperDataSource.getDefaultCardDetails().getId());
            rentalModel.setCardLastDigits(preferenceHelperDataSource.getDefaultCardDetails().getLast4());
            rentalModel.setCardBrand(preferenceHelperDataSource.getDefaultCardDetails().getBrand());
            rentalView.setSelectedCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                    preferenceHelperDataSource.getDefaultCardDetails().getBrand());
            rentalView.enableBooking();
            checkForPromoValid();
        }
    }

    @Override
    public void setCashPaymentOption()
    {
        rentalModel.setWalletSelected(false);
        rentalModel.setPaymentType(CASH);
        rentalModel.setPayByWallet(DONT_PAY_BY_WALLET);
        rentalView.enableBooking();
        checkForPromoValid();
    }

    /**
     * <h2>checkForPromoValid</h2>
     * used to check promo is valid
     */
    private void checkForPromoValid()
    {
        if(rentalModel.getPromoCodeModel()!=null)
        {
            if((rentalModel.getPromoCodeModel().getPromoPaymentMothod() != rentalModel.getPaymentType()
                    && rentalModel.getPromoCodeModel().getPromoPaymentMothod() != BOTH)
                    || (rentalModel.getPayByWallet() == PAY_BY_WALLET && rentalModel.getPromoCodeModel().getIsApplicableWithWallet() == DONT_PAY_BY_WALLET))
                clearPromo();
        }
    }

    /**
     * <h2>clearPromo</h2>
     * used to clear the promo code
     */
    private void clearPromo()
    {
        fareEstimateModel = FareEstimateModel.getInstance();
        rentalView.showMessage(mContext.getString(R.string.promo_invalid));
        rentalModel.setPromoCodeModel(null);
        rentalView.setPromoCode(mContext.getString(R.string.promo_code));

        ReceiptDetails updatesReceiptDetails = listOfBreakDown.get(listOfBreakDown.size()-1);
        updatesReceiptDetails.setReceiptValue(fareEstimateModel.getFinalAmount());
        listOfBreakDown.set(listOfBreakDown.size()-1,updatesReceiptDetails);
    }

    /**
     * <h2>populateSubTotalValues</h2>
     * used to populate the sub total breakdown
     * @param type 1 for only fixed  , 2 for only towing & ride, 3 for both
     */
    private void populateSubTotalValues(int type)
    {
        if(type == 2 || type == 3)
        {
            ReceiptDetails receiptDetails= new ReceiptDetails();
            receiptDetails.setReceiptText(mContext.getString(R.string.baseFare));
            receiptDetails.setGrandTotal(false);
            receiptDetails.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(),fareEstimateModel.getBaseFare()));
            listOfBreakDown.add(receiptDetails);

            ReceiptDetails receiptDetails1= new ReceiptDetails();
            receiptDetails1.setGrandTotal(false);
            if(fareEstimateModel.getFreeDistance()>0)
                receiptDetails1.setReceiptText(mContext.getString(R.string.distance_fare)+"("+
                        ActivityUtils.distanceAdjustment(fareEstimateModel.getDistanceMetrics(),fareEstimateModel.getDistance())+")"+
                        "\n"+"("+mContext.getString(R.string.charges_applied)+" "+fareEstimateModel.getFreeDistance()+" "+
                        fareEstimateModel.getDistanceMetrics()+")");
            else
                receiptDetails1.setReceiptText(mContext.getString(R.string.distance_fare)+"("+
                        ActivityUtils.distanceAdjustment(fareEstimateModel.getDistanceMetrics(),fareEstimateModel.getDistance()));

            receiptDetails1.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(),fareEstimateModel.getDistanceFee()));
            listOfBreakDown.add(receiptDetails1);

            ReceiptDetails receiptDetails2= new ReceiptDetails();
            receiptDetails2.setGrandTotal(false);

            if(fareEstimateModel.getFreeTime()>0)
                receiptDetails2.setReceiptText(mContext.getString(R.string.time_fare)+"("+
                        ActivityUtils.timeAdjustment(fareEstimateModel.getTimeMetrics(),fareEstimateModel.getDuration())+")"+
                        "\n"+"("+mContext.getString(R.string.charges_applied)+" "+fareEstimateModel.getFreeTime()+" "+
                        fareEstimateModel.getTimeMetrics()+")");
            else
                receiptDetails2.setReceiptText(mContext.getString(R.string.time_fare)+
                        "("+ActivityUtils.timeAdjustment(fareEstimateModel.getTimeMetrics(),fareEstimateModel.getDuration())+")");

            receiptDetails2.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                    fareEstimateModel.getCurrencySymbol(),fareEstimateModel.getTimeFee()));
            listOfBreakDown.add(receiptDetails2);

            if(!(fareEstimateModel.getLaterBookingAdvanceFee()==0 ||
                    rentalModel.getBookingType()==NOW_BOOKING_TYPE))
            {
                ReceiptDetails receiptDetails3= new ReceiptDetails();
                receiptDetails3.setGrandTotal(false);
                receiptDetails3.setReceiptText(mContext.getString(R.string.advance_Fare));
                receiptDetails3.setReceiptValue(utility.currencyAdjustment(fareEstimateModel.getCurrencyAbbr(),
                        fareEstimateModel.getCurrencySymbol(), String.valueOf(fareEstimateModel.getLaterBookingAdvanceFee())));
                listOfBreakDown.add(receiptDetails3);
            }
        }
        if(type == 1 || type == 3)
        {
            if(fareEstimateModel != null && !fareEstimateModel.getTowTruckServices().isEmpty()) {
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

    /**
     * <h2>checkForCardDefault</h2>
     * used to check for card payment default
     */
    private void checkForCardDefault()
    {
        if(preferenceHelperDataSource.isCardEnable())
        {
            if(preferenceHelperDataSource.getDefaultCardDetails() != null && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
            {
                Utility.printLog(TAG+" checkForDefaultPayment not null ");
                rentalModel.setWalletSelected(false);
                rentalModel.setPaymentType(CARD);
                rentalModel.setPayByWallet(DONT_PAY_BY_WALLET);
                rentalModel.setCardToken(preferenceHelperDataSource.getDefaultCardDetails().getId());
                rentalModel.setCardLastDigits(preferenceHelperDataSource.getDefaultCardDetails().getLast4());
                rentalModel.setCardBrand(preferenceHelperDataSource.getDefaultCardDetails().getBrand());
                rentalView.setSelectedCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                        preferenceHelperDataSource.getDefaultCardDetails().getBrand());
                rentalView.enableBooking();
            }
            else if(preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)))
            {
                rentalModel.setWalletSelected(false);
                Utility.printLog(TAG+" checkForDefaultPayment null ");
                rentalView.setCashPaymentOption();
            }
            else
                rentalView.disableBooking(1);
        }
        else if(preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)))
        {
            rentalModel.setWalletSelected(false);
            Utility.printLog(TAG+" checkForDefaultPayment null ");
            rentalView.setCashPaymentOption();
        }
        else
            rentalView.disableBooking(1);
    }

    @Override
    public void fetchRentVehicleTypes(String bookingId) {
        if (bookingId != null) {
            if(networkStateHolder.isConnected())
            {
                rentalView.showProgressDialog();
                Observable<Response<ResponseBody>> request = networkService.getRentalVehicleTypes(((ApplicationClass)
                                mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                        preferenceHelperDataSource.getLanguageSettings().getCode(), preferenceHelperDataSource.getPickUpLatitude(),
                        preferenceHelperDataSource.getPickUpLongitude(), bookingId, rentalModel.getBookingType());
                request.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(Response<ResponseBody> responseBodyResponse) {

                                switch (responseBodyResponse.code()) {
                                    case 401:
                                        ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource,addressDataSource);
                                        break;

                                    case 200:
                                        try
                                        {
                                            String dataObject = responseBodyResponse.body().string();
                                            Utility.printLog(TAG+ "vehicle types rental: " + dataObject);
                                            CarTypeDataModel carTypeDataModel = gson.fromJson(dataObject, CarTypeDataModel.class);
                                            carDataModel.clear();
                                            carDataModel.addAll(carTypeDataModel.getData());
                                            rentalView.notifyRentalVehicleTypes();
                                            if(carDataModel.size()==0)
                                                rentalView.hideBookingViews();
                                            nearestDriversLatLng = "";
                                            for(CarDataModel carDataModel : carDataModel)
                                            {
                                                if(carDataModel.getDrivers().size()>0)
                                                {
                                                    nearestDriversLatLng = nearestDriversLatLng
                                                            + "|" + carDataModel.getDrivers().get(0).getLatitude() + ","
                                                            + carDataModel.getDrivers().get(0).getLongitude();
                                                }
                                            }
                                            if(carDataModel.size()>0)
                                                prepareToGetETA();
                                        }
                                        catch (Exception e)
                                        {
                                            Utility.printLog(TAG+" Exception bookingDetailsAPI "+e);
                                        }
                                        break;

                                    case 502:
                                        rentalView.showMessage(mContext.getString(R.string.bad_gateway));
                                        break;
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                rentalView.dismissProgressDialog();
                                Utility.printLog(TAG+ " fetchRentVehicleTypes onError " + e);
                            }

                            @Override
                            public void onComplete() {
                                rentalView.dismissProgressDialog();
                                Utility.printLog(TAG+ " fetchRentVehicleTypes onComplete ");
                            }
                        });
            }
        }
        else
            rentalView.showMessageForSelectingPackage();
    }

    /**
     *<h>prepareToGetETA</h>
     * method is used to calculate eta of all types for 1st driver
     */
    private void prepareToGetETA()
    {
        if(!nearestDriversLatLng.isEmpty() && Double.parseDouble(preferenceHelperDataSource.getPickUpLatitude()) != 0.0
                && Double.parseDouble(preferenceHelperDataSource.getPickUpLongitude()) != 0.0)
        {
            String[] params = new String[3];
            params[0] = preferenceHelperDataSource.getPickUpLatitude();
            params[1] = preferenceHelperDataSource.getPickUpLongitude();
            params[2] = nearestDriversLatLng;
            driversDistanceMatrixETAAPI(params);
        }
        else
        {
            for (CarDataModel carDataModel : carDataModel)
            {
                carDataModel.setEta(mContext.getString(R.string.no_drivers));
            }
            rentalView.notifyRentalVehicleTypes();
        }
    }

    /**
     * <h2>driversDistanceMatrixETAAPI</h2>
     * This class is used to get the ETA from google
     */
    private void driversDistanceMatrixETAAPI(String... params)
    {
        if (networkStateHolder.isConnected())
        {
            if(etaDisposable!=null)
                etaDisposable.dispose();

            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + params[0] + "," + params[1]
                    + "&" + "destinations=" + params[2] + "&mode=driving" + "&" + "key=" + preferenceHelperDataSource.getGoogleServerKey();
            Utility.printLog(TAG+ " ETA TEST  Distance matrix driversDistanceMatrixETAAPI() url: called " + url);
            Observable<Response<ResponseBody>> request = networkService.getETAMatrix(url);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            etaDisposable = d;
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> result)
                        {
                            Utility.printLog(TAG+ " ETA TEST current result code   " + result.code());
                            switch (result.code())
                            {
                                case 200:
                                    ETADataModel etaDataModel;
                                    try
                                    {
                                        etaDataModel = gson.fromJson(result.body().string(), ETADataModel.class);
                                        if (etaDataModel.getStatus().equals("OK") )
                                        {
                                            if(etaDataModel.getRows().get(0).getElements().get(0).getStatus().equals("OK"))
                                            {
                                                int etaIndexSelected = 0;
                                                for(int vehicleTypeIndex =0; vehicleTypeIndex< carDataModel.size(); vehicleTypeIndex++)
                                                {
                                                    if(carDataModel.get(vehicleTypeIndex).getDrivers().size()>0)
                                                    {
                                                        carDataModel.get(vehicleTypeIndex).setEta(etaDataModel.getRows().get(0).getElements().get(etaIndexSelected).getDuration().getText());
                                                        etaIndexSelected++;
                                                    }
                                                    else
                                                        carDataModel.get(vehicleTypeIndex).setEta(mContext.getString(R.string.no_drivers));
                                                }
                                                rentalView.notifyRentalVehicleTypes();
                                            }
                                        }
                                        else
                                        {
                                            Utility.printLog(TAG+" ETA TEST Distance matrix key exceeded ");
                                            //if the stored key is exceeded then rotate to next key
                                            List<String> googleServerKeys=preferenceHelperDataSource.getGoogleServerKeys();
                                            if(googleServerKeys.size()>0)
                                            {
                                                googleServerKeys.remove(0);
                                                if(googleServerKeys.size()>0)
                                                {
                                                    //store next key in shared pref
                                                    preferenceHelperDataSource.setGoogleServerKey(googleServerKeys.get(0));
                                                    //if the stored key is exceeded then rotate to next and call eta API
                                                    prepareToGetETA();
                                                }
                                                //to store the google keys array by removing exceeded key from list
                                                preferenceHelperDataSource.setGoogleServerKeys(googleServerKeys);
                                            }
                                        }
                                    } catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+" TEST ETA Distance matrix IOException "+e);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
        else
            rentalView.showMessage(mContext.getString(R.string.network_problem));
    }

    @Override
    public void updateSelectedPackage(String id) {
        rentalModel.setSelectedPackageId(id);
    }

    @Override
    public void setWalletPaymentOption(String walletText)
    {
        if (preferenceHelperDataSource.isCashEnable() && preferenceHelperDataSource.isCardEnable()
                && preferenceHelperDataSource.getDefaultCardDetails()!=null && preferenceHelperDataSource.getLoginType() == 0)
            rentalView.showWalletExcessAlert(rentalModel.getPaymentType());
        else
            checkForWallet(false);
    }

    @Override
    public void chooseCardWithWallet(boolean toOpenBooking)
    {
        if(preferenceHelperDataSource.isCardEnable() &&
                preferenceHelperDataSource.getDefaultCardDetails()!=null && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
        {
            if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                rentalView.showHardLimitAlert();
            else
            {
                rentalModel.setWalletSelected(true);
                rentalView.enableBooking();
                rentalModel.setPaymentType(CARD);
                rentalModel.setPayByWallet(PAY_BY_WALLET);
                checkForPromoValid();
                rentalView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),preferenceHelperDataSource.getWalletSettings().getWalletBalance()+""));
                if(toOpenBooking)
                    openRequest();
            }
        }

        else if(preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)))
        {
            if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                rentalView.showHardLimitAlert();
            else
            {
                rentalModel.setWalletSelected(true);
                rentalView.enableBooking();
                rentalModel.setPaymentType(CASH);
                rentalModel.setPayByWallet(PAY_BY_WALLET);
                checkForPromoValid();
                rentalView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),preferenceHelperDataSource.getWalletSettings().getWalletBalance()+""));
                if(toOpenBooking)
                    openRequest();
            }
        }

        else
        {
            rentalModel.setWalletSelected(false);
            rentalView.disableBooking(1);
            rentalView.showMessage(mContext.getString(R.string.add_card_to_proceed));
        }
    }

    @Override
    public void subscribeConfirmClick()
    {
        rxConfirmPickNotifier.subscribeOn(Schedulers.io());
        rxConfirmPickNotifier.observeOn(AndroidSchedulers.mainThread());
        rxDispose = rxConfirmPickNotifier.subscribeWith(new DisposableObserver<Boolean>()
        {
            @Override
            public void onNext(Boolean isClicked)
            {
                Utility.printLog(TAG+" onNext confirm clicked  "+isClicked);
                rentalView.finishRental();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Utility.printLog(TAG+" onAddCardError confirm clicked  "+e);
            }

            @Override
            public void onComplete() {
                compositeDisposable.add(rxDispose);
            }
        });
    }

    @Override
    public void openRequest()
    {
        Bundle bundle =new Bundle();
        bundle.putString(VEHICLE_ID, preferenceHelperDataSource.getVehicleID());
        bundle.putString(VEHICLE_IMAGE, preferenceHelperDataSource.getVehicleImage());
        bundle.putString(VEHICLE_NAME, preferenceHelperDataSource.getVehicleName());
        bundle.putInt(PAYMENT_TYPE, rentalModel.getPaymentType());
        bundle.putInt(BOOKING_TYPE, rentalModel.getBookingType());
        bundle.putString(PICK_ADDRESS, preferenceHelperDataSource.getPickUpAddress());
        bundle.putString(DROP_ADDRESS, preferenceHelperDataSource.getDropAddress());
        bundle.putString(PICK_LAT, preferenceHelperDataSource.getPickUpLatitude());
        bundle.putString(PICK_LONG, preferenceHelperDataSource.getPickUpLongitude());
        bundle.putString(AMOUNT, "0");
        bundle.putString(TIME_FARE, "0");
        bundle.putString(DISTANCE_FARE, "0");
        bundle.putString(DISTANCE, "0");
        bundle.putString(DURATION, "0");
        bundle.putString(ESTIMATE_ID, "0");
        bundle.putInt(PAYMENT_OPTION, rentalModel.getPayByWallet());
        bundle.putString(BOOKING_DATE, rentalModel.getBookingDate());
        bundle.putString(PICK_ZONE_ID, rentalModel.getAreaZoneId());
        bundle.putString(PICK_ZONE_TITLE, rentalModel.getAreaZoneTitle());
        bundle.putString(PICK_GATE_ID, rentalModel.getAreaPickupId());
        bundle.putString(PICK_GATE_TITLE, rentalModel.getAreaPickupTitle());
        bundle.putString(SOMEONE_NAME, rentalModel.getCustomerName());
        bundle.putString(SOMEONE_NUMBER, rentalModel.getCustomerNumber());
        bundle.putString(VEHICLE_CAPACITY, rentalModel.getVehicleCapacity());
        bundle.putInt(SOMEONE_ELSE_BOOKING, rentalModel.isSomeOneElseBooking());
        bundle.putString(FAV_DRIVERS, rentalModel.getFavoriteDriverId());
        bundle.putString(DRIVER_PREF, rentalModel.getDriverPreference() );
        if(rentalModel.getPromoCodeModel() != null)
            bundle.putString(PROMO_CODE, rentalModel.getPromoCodeModel().getCouponCode());
        String instituteId = null;
        if(rentalModel.getSelectedProfile()!=null)
            instituteId = rentalModel.getSelectedProfile().getUserId();
        bundle.putString(INSTITUTE_ID, instituteId);
        bundle.putBoolean(IS_TOWING_ENABLE, false);
        bundle.putInt(IS_PARTNER_TYPE, 0);//normal login
        bundle.putInt(HOTEL_USER_TYPE, 0); //normal
        bundle.putString(GUEST_NAME, "");
        bundle.putString(GUEST_COUNRY_CODE, "");
        bundle.putString(GUEST_NUMBER, "");
        bundle.putString(GUEST_ROOM_NUMBER, "");
        bundle.putString(HOTEL_USER_ID, "");
        bundle.putString(PACKAGE_ID, rentalModel.getSelectedPackageId());

        switch (rentalModel.getPaymentType())
        {
            case CARD:
                bundle.putString(CARD_TOKEN, rentalModel.getCardToken());
                bundle.putString(LAST_DIGITS, rentalModel.getCardLastDigits());
                bundle.putString(CARD_BRAND, rentalModel.getCardBrand());
                break;

            case CASH:
                bundle.putString(CARD_TOKEN, null);
                bundle.putString(LAST_DIGITS,null);
                bundle.putString(CARD_BRAND, null);
                break;
        }
        rentalView.openRequestingScreenNormal(bundle);
    }

    @Override
    public void checkForOutstandingAmount()
    {
        if(rentalModel.getSelectedProfile() == null)
        {
            if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                rentalView.showHardLimitAlert();
            else
            {
                if(networkStateHolder.isConnected())
                {
                    rentalView.showProgressDialog();
                    Observable<Response<ResponseBody>> request =
                            networkService.getOutstandingBalance(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                                    preferenceHelperDataSource.getLanguageSettings().getCode());

                    request.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Response<ResponseBody>>() {
                                @Override
                                public void onSubscribe(Disposable d)
                                {
                                    compositeDisposable.add(d);
                                }
                                @Override
                                public void onNext(Response<ResponseBody> result)
                                {
                                    Utility.printLog(TAG + " checkForOutstandingAmount onNext " + result.code());
                                    switch (result.code())
                                    {
                                        case 200:
                                            String dataObjectString = DataParser.fetchDataObjectString(result);
                                            try
                                            {
                                                JSONObject dataObject =new JSONObject(dataObjectString);
                                                boolean isLastDueAvailable = dataObject.getBoolean("isLastDueAvailable");

                                                if(isLastDueAvailable)
                                                {
                                                    String lastDueMsg = dataObject.getString("lastDueMsg");
                                                    String businessName = dataObject.getString("businessName");
                                                    String bookingDate = dataObject.getString("bookingDate");
                                                    String pickupAddress = dataObject.getString("pickupAddress");
                                                    rentalView.showOutstandingDialog(lastDueMsg,businessName,bookingDate,pickupAddress);
                                                }
                                                else
                                                    rentalView.confirmToBook();
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            break;

                                        case 401:
                                            ExpireSession.refreshApplication(mContext,mqttManager,
                                                    preferenceHelperDataSource,addressDataSource);
                                            break;

                                        case 502:
                                            rentalView.showMessage(mContext.getString(R.string.bad_gateway));
                                            break;

                                        default:
                                            rentalView.showMessage(DataParser.fetchErrorMessage(result));
                                            break;
                                    }
                                }
                                @Override
                                public void onError(Throwable errorMsg)
                                {
                                    Utility.printLog(TAG + " checkForOutstandingAmount onError " + errorMsg);
                                    rentalView.dismissProgressDialog();
                                    rentalView.showMessage(mContext.getString(R.string.network_problem));
                                }
                                @Override
                                public void onComplete()
                                {
                                    rentalView.dismissProgressDialog();
                                    Utility.printLog(TAG + " checkForOutstandingAmount onComplete ");
                                }
                            });
                }
                else
                    rentalView.showMessage(mContext.getString(R.string.network_problem));
            }
        }
        else
        {
            String instituteId = rentalModel.getSelectedProfile().getUserId();
            if(instituteId != null && preferenceHelperDataSource.getDropAddress().equals(""))
                rentalView.showMessage(mContext.getString(R.string.add_address_to_proceed));
            else
                openRequest();
        }
    }

    @Override
    public void handleRequestBooking()
    {
        if(rentalModel.isWalletSelected())
        {
            if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                rentalView.showHardLimitAlert();
            else
                openRequest();
        }
        else
        {
            if(preferenceHelperDataSource.getWalletSettings().isWalletEnable() &&
                    preferenceHelperDataSource.getWalletSettings().getWalletBalance() >= preferenceHelperDataSource.getWalletSettings().getSoftLimit()
                    && preferenceHelperDataSource.getWalletSettings().getWalletBalance()>0 &&
                    (preferenceHelperDataSource.getLoginType() == 0 ||
                            (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
            {
                rentalView.showWalletUseAlert(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),preferenceHelperDataSource.getWalletSettings().getWalletBalance()+""),
                        rentalModel.getPaymentType());
            }
            else
            {
                if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                        && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                        (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                    rentalView.showHardLimitAlert();
                else
                    openRequest();
            }
        }
    }

    @Override
    public void chooseCashWithWallet(boolean toOpenBooking)
    {
        checkForWallet(toOpenBooking);
    }

    /**
     * <h2>checkforWallet</h2>
     * used to make the cash or card with wallet
     */
    private void checkForWallet(boolean toOpenBooking)
    {
        if(preferenceHelperDataSource.isCashEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)))
        {
            if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                rentalView.showHardLimitAlert();
            else
            {
                rentalModel.setWalletSelected(true);
                rentalView.enableBooking();
                rentalModel.setPaymentType(CASH);
                rentalModel.setPayByWallet(PAY_BY_WALLET);
                checkForPromoValid();
                rentalView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),preferenceHelperDataSource.getWalletSettings().getWalletBalance()+""));
                if(toOpenBooking)
                    openRequest();
            }
        }
        else if(preferenceHelperDataSource.isCardEnable() &&
                preferenceHelperDataSource.getDefaultCardDetails()!=null && (preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
        {
            if(preferenceHelperDataSource.getWalletSettings().getWalletBalance()<preferenceHelperDataSource.getWalletSettings().getHardLimit()
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable() && (preferenceHelperDataSource.getLoginType() == 0 ||
                    (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)))
                rentalView.showHardLimitAlert();
            else
            {
                rentalModel.setWalletSelected(true);
                rentalView.enableBooking();
                checkForPromoValid();
                rentalModel.setPaymentType(CARD);
                rentalModel.setPayByWallet(PAY_BY_WALLET);
                rentalView.setWalletAmount(utility.currencyAdjustment(preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                        preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),preferenceHelperDataSource.getWalletSettings().getWalletBalance()+""));
                if(toOpenBooking)
                    openRequest();
            }
        }

        else
        {
            rentalModel.setWalletSelected(false);
            rentalView.disableBooking(1);
            rentalView.showMessage(mContext.getString(R.string.add_card_to_proceed));
        }
    }

    @Override
    public void checkForPaymentOptions()
    {
        if(!preferenceHelperDataSource.isCashEnable())
            rentalView.disableCashOption();
        else
        {
            switch (preferenceHelperDataSource.getLoginType())
            {
                case 1: //hotel
                    switch (preferenceHelperDataSource.getHotelDataModel().getHotelUserType())
                    {
                        case 1: //travel desk
                            rentalView.disableCashOption();
                            break;
                    }
                    break;
            }
        }

        if(preferenceHelperDataSource.getLoginType() == 0 ||
                (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))
        {
            if (!preferenceHelperDataSource.isCardEnable())
                rentalView.disableCardOption(true);
            else if(preferenceHelperDataSource.getDefaultCardDetails() == null)
                rentalView.disableCardOption(false);

            if(!preferenceHelperDataSource.getWalletSettings().isWalletEnable())
            {
                rentalView.disableWalletOption();
                rentalModel.setWalletSelected(false);
            }
            else
            {
                if(preferenceHelperDataSource.getWalletSettings().getWalletBalance() < preferenceHelperDataSource.getWalletSettings().getSoftLimit()
                        || preferenceHelperDataSource.getWalletSettings().getWalletBalance()<=0)
                {
                    rentalView.hideWalletOption();
                    rentalModel.setWalletSelected(false);
                }

                else
                {
                    rentalView.enableWalletOption(utility.currencyAdjustment
                            (preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr(),
                                    preferenceHelperDataSource.getWalletSettings().getCurrencySymbol(),
                                    preferenceHelperDataSource.getWalletSettings().getWalletBalance()+""));
                }
            }

            if(preferenceHelperDataSource.getDefaultCardDetails() != null)
            {
                rentalView.showDefaultCard(preferenceHelperDataSource.getDefaultCardDetails().getLast4(),
                        preferenceHelperDataSource.getDefaultCardDetails().getBrand(),
                        rentalModel.getPaymentType(),rentalModel.isWalletSelected());
            }
            else
                rentalView.showDefaultCard(null, null, rentalModel.getPaymentType(),
                        rentalModel.isWalletSelected());
        }
        else if(preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2 )
        {
            rentalView.disableCardOption(true);
            rentalView.disableWalletOption();
            rentalModel.setWalletSelected(false);
        }
    }
}
