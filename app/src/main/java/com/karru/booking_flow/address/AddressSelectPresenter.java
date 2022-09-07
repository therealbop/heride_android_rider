package com.karru.booking_flow.address;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.booking_flow.address.model.FavAddressModel;
import com.karru.booking_flow.address.view.AddressSelectionActivity;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.booking_flow.address.model.AddressDataModel;
import com.karru.booking_flow.address.model.DropLocationGoogleModel;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.booking_flow.address.model.PlaceAutoCompleteModel;
import com.karru.utility.Constants;
import com.karru.utility.Utility;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.ADDRESS_SELECTION;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.DROP_ADDRESS_REQUEST;
import static com.karru.utility.Constants.DROP_LAT;
import static com.karru.utility.Constants.DROP_LONG;
import static com.karru.utility.Constants.FAV_TYPE_LIST;
import static com.karru.utility.Constants.RECENT_TYPE_LIST;
import static com.karru.utility.Constants.SPECIAL_TYPE;

/**
 * <h1>AddressSelectPresenter</h1>
 * This class is used to call the API
 * @author 3Embed
 * @since on 20-01-2018.
 */
public class AddressSelectPresenter implements AddressSelectContract.Presenter
{
    private static final String TAG = "AddressSelectPresenter";

    @Inject Context mContext;
    @Inject AddressSelectionActivity mActivity;
    @Inject Gson gson;
    @Inject NetworkService networkService;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject @Named(ADDRESS_SELECTION) CompositeDisposable compositeDisposable;
    @Inject AddressSelectContract.View addressSelectView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject AddressSelectPresenter() { }
    private ArrayList<AddressDataModel> recentAddressList = new ArrayList<>();
    private ArrayList<FavAddressDataModel> favAddressList = new ArrayList<>();
    private ArrayList<AddressDataModel> specialAddressList = new ArrayList<>();


    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void deleteFavAddressAPI(final String id, final int index)
    {
        if(networkStateHolder.isConnected())
        {
            addressSelectView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.deleteFavAddress(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),id);
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
                            Utility.printLog(TAG + " deleteFavAddressAPI onNext " + result.code());
                            switch (result.code())
                            {
                                case 200:
                                    Utility.printLog(TAG+ "deleteFavAddressAPI result: "+result.code());
                                    addressDataSource.deleteFavAddress(id);
                                    addressSelectView.deleteAddressNotify(index);
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " deleteFavAddressAPI onAddCardError " + errorMsg);
                        }
                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " deleteFavAddressAPI onComplete ");
                            addressSelectView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void fetchSpecialAddress(String s)
    {
        if(networkStateHolder.isConnected())
        {
            addressSelectView.showProgressDialog();
            Observable<Response<ResponseBody>> request =
                    networkService.fetchHotelFavAddress(((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),s);
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
                            Utility.printLog(TAG + " fetchSpecialAddress onNext " + result.code());
                            switch (result.code())
                            {
                                case 200:
                                    try
                                    {
                                        String response = result.body().string();
                                        Utility.printLog(TAG + " fetchSpecialAddress response " + response);
                                        FavAddressModel favAddressModel = gson.fromJson(response,FavAddressModel.class);
                                        specialAddressList.clear();
                                        for(FavAddressDataModel favAddressDataModel:favAddressModel.getData())
                                        {
                                            AddressDataModel addressDataModel = new AddressDataModel();
                                            addressDataModel.setAddress(favAddressDataModel.getAddress());
                                            addressDataModel.setLat(favAddressDataModel.getLatitude()+"");
                                            addressDataModel.setLng(favAddressDataModel.getLongitude()+"");
                                            addressDataModel.setType(SPECIAL_TYPE);
                                            specialAddressList.add(addressDataModel);
                                        }
                                        addressSelectView.replaceDataNotifyAdapter(recentAddressList, favAddressList,specialAddressList);
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable errorMsg)
                        {
                            Utility.printLog(TAG + " fetchSpecialAddress errorMsg " + errorMsg);
                            addressSelectView.dismissProgressDialog();
                        }
                        @Override
                        public void onComplete()
                        {
                            Utility.printLog(TAG + " fetchSpecialAddress onComplete ");
                            addressSelectView.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void initializeVariables(String comingFrom)
    {
        switch (comingFrom)
        {
            case "pick":
                addressSelectView.setTitleForPickAddress();
                break;

            case "signup":
                addressSelectView.setTitleForBusinessAddress();
                break;

            default:
                addressSelectView.setTitleForDropAddress();
                break;
        }
        recentAddressList = addressDataSource.extractAllNonFavAddresses();
        favAddressList = addressDataSource.getFavAddresses();
        Collections.reverse(recentAddressList);
        Collections.reverse(favAddressList);
        addressSelectView.replaceDataNotifyAdapter(recentAddressList, favAddressList,specialAddressList);
    }

    @Override
    public void rotateNextKey()
    {
        Utility.printLog(TAG+" ETA TEST Distance matrix key exceeded ");
        //if the stored key is exceeded then rotate to next key
        List<String> googleServerKeys=preferenceHelperDataSource.getGoogleServerKeys();
        if(googleServerKeys.size()>0)
        {
            Utility.printLog(TAG+ " ETA TEST Distance matrix keys size before remove "+googleServerKeys.size());
            googleServerKeys.remove(0);
            if(googleServerKeys.size()>0)
            {
                Utility.printLog(TAG+" ETA TEST Distance matrix keys next key "+googleServerKeys.get(0));
                //store next key in shared pref
                preferenceHelperDataSource.setGoogleServerKey(googleServerKeys.get(0));
                //if the stored key is exceeded then rotate to next and call eta API
                addressSelectView.filterAddress();
            }
            //to store the google keys array by removing exceeded key from list
            preferenceHelperDataSource.setGoogleServerKeys(googleServerKeys);
        }
    }

    @Override
    public void storeAndHandleAddressSelect(int keyId,AddressDataModel addressDataModel,String key)
    {
        switch (keyId)
        {
            case DROP_ADDRESS_REQUEST:
            case Constants.DROP_ID:
                preferenceHelperDataSource.setDropLatitude(addressDataModel.getLat());
                preferenceHelperDataSource.setDropLongitude(addressDataModel.getLng());
                preferenceHelperDataSource.setDropAddress(addressDataModel.getAddress());
                break;

            default:
                preferenceHelperDataSource.setPickUpLatitude(addressDataModel.getLat());
                preferenceHelperDataSource.setPickUpLongitude(addressDataModel.getLng());
                preferenceHelperDataSource.setPickUpAddress(addressDataModel.getAddress());
                break;
        }

        switch (key)
        {
            case "startActivityForResult":
                addressSelectView.openShipmentScreen(addressDataModel.getLat() , addressDataModel.getLng() ,
                        addressDataModel.getAddress());
                break;

            case "startActivityForResultHOME":
                addressSelectView.notifyPickAddressChangeUI(addressDataModel.getLat() , addressDataModel.getLng() ,
                        addressDataModel.getAddress());
                break;

            case "startActivityForResultAddr":
                addressSelectView.notifySignUpBusinessAddressUI(addressDataModel.getLat() , addressDataModel.getLng() ,
                        addressDataModel.getAddress());
                break;

            default:
                addressSelectView.notifyLaterBookingClick();
                break;
        }
    }

    @Override
    public void handleAutoSuggestAddressClick(final ArrayList<PlaceAutoCompleteModel> mAddressList, int position,
                                              ArrayList<AddressDataModel> recentAddressList, int keyId, String key)
    {
        if(mAddressList!=null)
        {
            try {
                final String ref_key = String.valueOf(mAddressList.get(position).getRef_key());
                final String url = getPlaceDetailsUrl(ref_key);
                Thread thread = new Thread(() ->
                {
                    boolean insertFlag = true;
                    AddressDataModel addressDataModel = getPlaceData(url,mAddressList,position,recentAddressList,keyId,key);
                    if(addressDataModel != null)
                    {
                        addressDataModel.setAddress(mAddressList.get(position).getAddress());
                        for (int pos = 0; pos < recentAddressList.size(); pos++)
                        {
                            if (addressDataModel.getAddress().equals(recentAddressList.get(pos).getAddress()) && addressDataModel.getLat().equals(recentAddressList.get(pos).getLat())
                                    && addressDataModel.getLng().equals(recentAddressList.get(pos).getLng()))
                            {
                                insertFlag = false;
                                break;
                            }
                        }
                        long autoGeneratedId = -1;
                        if (insertFlag)
                            autoGeneratedId = addressDataSource.insertNonFavAddressData(addressDataModel.getAddress(), addressDataModel.getLat(), addressDataModel.getLng());
                        addressDataModel.setName("");
                        addressDataModel.set_id(String.valueOf(autoGeneratedId));
                        addressDataModel.setAddressId((int)autoGeneratedId);
                        addressDataModel.setIsItAFavAdrs(false);

                        addressSelectView.hideKeyboard();
                        storeAndHandleAddressSelect(keyId,addressDataModel,key);
                    }
                });
                thread.start();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServerKey()
    {
        return preferenceHelperDataSource.getGoogleServerKey();
    }

    /**
     * <h2>AddressDataModel</h2>
     * <p>
     * This method is providing the LAT-LONG based on the URL, we got from getPlaceDetailsUrl().
     * </p>
     * @param inputURL represent a URL.
     * @return returns AddressDataModel object based on the filter
     */
    private AddressDataModel getPlaceData(String inputURL,final ArrayList<PlaceAutoCompleteModel> mAddressList, int position,
                                          ArrayList<AddressDataModel> recentAddressList, int keyId, String key)
    {
        AddressDataModel addressDataModel = new AddressDataModel();
        Utility.printLog("value of url: activity: "+inputURL);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(inputURL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Utility.printLog(TAG+" ,address:11: "+jsonResults);
            Gson gson = new Gson();
            DropLocationGoogleModel dropLocationGoogleModel = gson.fromJson(jsonResults.toString(), DropLocationGoogleModel.class);
            if(dropLocationGoogleModel.getStatus().equals("OK"))
            {
                String lat = dropLocationGoogleModel.getResult().getGeometry().getLocation().getLat();
                String lng = dropLocationGoogleModel.getResult().getGeometry().getLocation().getLng();
                addressDataModel.setLat(lat);
                addressDataModel.setLng(lng);
                return addressDataModel;
            }
            else
            {
                Utility.printLog(TAG+" ETA TEST Distance matrix key exceeded ");
                //if the stored key is exceeded then rotate to next key
                List<String> googleServerKeys=preferenceHelperDataSource.getGoogleServerKeys();
                if(googleServerKeys.size()>0)
                {
                    Utility.printLog(TAG+ " ETA TEST Distance matrix keys size before remove "+googleServerKeys.size());
                    googleServerKeys.remove(0);
                    if(googleServerKeys.size()>0)
                    {
                        Utility.printLog(TAG+" ETA TEST Distance matrix keys next key "+googleServerKeys.get(0));
                        //store next key in shared pref
                        preferenceHelperDataSource.setGoogleServerKey(googleServerKeys.get(0));
                        //if the stored key is exceeded then rotate to next and call eta API
                        handleAutoSuggestAddressClick( mAddressList,  position, recentAddressList,  keyId,  key);
                    }
                    //to store the google keys array by removing exceeded key from list
                    preferenceHelperDataSource.setGoogleServerKeys(googleServerKeys);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Utility.printLog(TAG+ " Error API "+ e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * <h>getPlaceDetailsUrl</h>
     * <p>
     * Creating the google API request list for getting the lat-long based on our requested address.
     * </p>
     */
    private String getPlaceDetailsUrl(String ref)
    {
        String key = "key="+preferenceHelperDataSource.getGoogleServerKey();
        String reference = "reference="+ref;                // reference of place
        String sensor = "sensor=false";                     // Sensor enabled
        String parameters = reference+"&"+sensor+"&"+key;   // Building the parameters to the web service
        String output = "json";                             // Output format
        return "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;
    }

    @Override
    public void toggleFavAddressField(boolean showFavAddressList) {
        if(showFavAddressList)
            addressSelectView.showFavAddressListUI();
        else
        {
            if(preferenceHelperDataSource.getLoginType() == 0) //normal login type
                addressSelectView.hideFavAddressListUI(GONE);
            else
                addressSelectView.hideFavAddressListUI(VISIBLE);
        }
    }

    @Override
    public void checkForLoginType(String s) {
        if(preferenceHelperDataSource.getLoginType() ==1) //if hotel login
            fetchSpecialAddress(s);
        else
            addressSelectView.hideSpecialAddress();
    }

    @Override
    public void handleClickOfAddress(int listType,ArrayList<AddressDataModel> recentAddressList,
                                     ArrayList <FavAddressDataModel> favAddressList,
                                     ArrayList<AddressDataModel> specialAddressList,int position,
                                     int keyId,String key)
    {
        AddressDataModel addressDataModel = new AddressDataModel();
        switch (listType)
        {
            case RECENT_TYPE_LIST:
                Utility.printLog(TAG+ "onRVItemViewClicked() rlAdrsCell listTypeRecent: "+listType);
                addressDataModel = recentAddressList.get(position);
                addressDataModel.setIsToAddAsFav(false);
                addressDataModel.setIsItAFavAdrs(false);
                addressDataModel.setName("");
                addressSelectView.hideKeyboard();
                storeAndHandleAddressSelect(keyId,addressDataModel,key);
                break;

            case SPECIAL_TYPE:
                Utility.printLog(TAG+ "onRVItemViewClicked() rlAdrsCell listTypeRecent: "+listType);
                addressDataModel = specialAddressList.get(position);
                addressDataModel.setIsToAddAsFav(false);
                addressDataModel.setIsItAFavAdrs(false);
                addressDataModel.setName("");
                addressSelectView.hideKeyboard();
                storeAndHandleAddressSelect(keyId,addressDataModel,key);
                break;

            case FAV_TYPE_LIST:
                Utility.printLog(TAG+ "onRVItemViewClicked() rlAdrsCell listTypeFav: "+listType+"address "+
                        favAddressList.get(position));
                FavAddressDataModel favAddressDataModel = favAddressList.get(position);
                addressDataModel.setIsToAddAsFav(false);
                addressDataModel.setIsItAFavAdrs(true);
                addressDataModel.setName("");
                addressDataModel.set_id(favAddressDataModel.getAddressId());
                addressDataModel.setAddress(favAddressDataModel.getAddress());
                addressDataModel.setLat(String.valueOf(favAddressDataModel.getLatitude()));
                addressDataModel.setLng(String.valueOf(favAddressDataModel.getLongitude()));
                addressSelectView.hideKeyboard();
                storeAndHandleAddressSelect(keyId,addressDataModel,key);
                break;
        }
    }

    @Override
    public void handleResultData(int requestCode, int resultCode) {
        switch (requestCode)
        {
            case DROP_ADDRESS_REQUEST:
                if(resultCode == RESULT_OK)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString(DROP_LAT, preferenceHelperDataSource.getDropLatitude());
                    bundle.putString(DROP_LONG, preferenceHelperDataSource.getDropLongitude());
                    bundle.putString(DROP_ADDRESS, preferenceHelperDataSource.getDropAddress());
                    addressSelectView.sendAddressBack(bundle);
                }
                break;
        }
    }
}
