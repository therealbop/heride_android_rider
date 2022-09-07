package com.karru.landing.profile;

import android.net.Uri;

import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.splash.first.LanguagesList;
import com.karru.splash.first.LanguagesListModel;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.image_upload.MediaInterface;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.util.image_upload.AmazonUpload;
import com.karru.util.DataParser;
import com.karru.util.ExpireSession;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
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

import static com.karru.util.DataParser.stringToJsonAndPublish;
import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.LANGUAGE;
import static com.karru.utility.Constants.PROFILE;

/**
 * <h>ProfileModel</h>
 * <p> Profile Model which handle the service call Db </p>
 */
public class ProfileFragmentPresenter implements ProfileContract.Presenter
{
    private static final String TAG = "ProfileFragmentPresenter";
    @Inject Gson gson;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;
    @Inject NetworkService networkService;
    @Inject ProfileActivity mContext;
    @Inject SQLiteDataSource sqLiteDataSource;
    @Inject @Named(PROFILE) CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject AmazonUpload amazonImageUpload;
    @Inject ProfileContract.ProfileView profileView;
    @Inject @Named(LANGUAGE) ArrayList<LanguagesList> languagesLists = new ArrayList<>();

    private String amazonFileName;

    @Inject ProfileFragmentPresenter() {}

    @Override
    public void checkRTLConversion() {
        RtlConversion(mContext,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void changeLanguage(String langCode,String langName, int isRTL)
    {
        preferenceHelperDataSource.setLanguageSettings(new LanguagesList(langCode,langName,isRTL));
        profileView.setLanguage(langName,true);
    }



    @Override
    public void getLanguages()
    {
        Observable<Response<LanguagesListModel>> request = networkService.getLanguages();
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LanguagesListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<LanguagesListModel> result)
                    {
                        switch (result.code())
                        {
                            case 200:
                                LanguagesListModel languagesListModel = result.body();
                                languagesLists.clear();
                                languagesLists.addAll(languagesListModel.getData());
                                boolean isLanguage = false;
                                for(LanguagesList languagesList : languagesLists)
                                {
                                    if(preferenceHelperDataSource.getLanguageSettings().getCode().equals(languagesList.getCode()))
                                    {
                                        isLanguage = true;
                                        profileView.showLanguagesDialog(languagesLists.indexOf(languagesList));
                                        break;
                                    }
                                }
                                if(!isLanguage)
                                    profileView.showLanguagesDialog(-1);
                                break;

                            case 502:
                                profileView.onAPICallError(mContext.getString(R.string.bad_gateway));
                                break;

                            default:
                                try
                                {
                                    profileView.onAPICallError(DataParser.fetchErrorMessage1(result.errorBody().string()));
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                    @Override
                    public void onError(Throwable errorMsg)
                    {
                        Utility.printLog(TAG + " getLanguages onError " + errorMsg);
                        profileView.onAPICallError(mContext.getString(R.string.network_problem));
                    }
                    @Override
                    public void onComplete()
                    {
                        Utility.printLog(TAG + " getLanguages onComplete ");
                    }
                });
    }

    @Override
    public void updateProfilePic(String profilePicUrl)
    {
        if (networkStateHolder.isConnected())
        {
            profileView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.updateProfilePic
                    (((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                            preferenceHelperDataSource.getLanguageSettings().getCode(),profilePicUrl);
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
                            Utility.printLog(TAG+" onNext update profile response " + value.code());
                            switch (value.code())
                            {
                                case 200:
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,
                                            preferenceHelperDataSource, sqLiteDataSource);

                                case 502:
                                    profileView.onAPICallError(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    profileView.onAPICallError(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            profileView.dismissProgressDialog();
                            profileView.onAPICallError(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete()
                        {
                            profileView.dismissProgressDialog();
                        }
                    });
        }
        else
            profileView.onAPICallError(mContext.getString(R.string.network_problem));
    }

    @Override
    public void logoutAPI()
    {
        if (networkStateHolder.isConnected())
        {
            profileView.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.logOut(
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

                            String result;
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        result = value.body().string();
                                        Utility.printLog("logout showToast JSON DATA" + result);
                                        profileView.dismissProgressDialog();
                                        ExpireSession.refreshApplication(mContext,mqttManager,preferenceHelperDataSource, sqLiteDataSource);
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 401:
                                    ExpireSession.refreshApplication(mContext,mqttManager,
                                            preferenceHelperDataSource, sqLiteDataSource);

                                case 502:
                                    profileView.onAPICallError(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    profileView.onAPICallError(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            profileView.dismissProgressDialog();
                            profileView.onAPICallError(mContext.getString(R.string.network_problem));
                        }

                        @Override
                        public void onComplete() {
                            profileView.dismissProgressDialog();
                        }
                    });
        }
        else
            profileView.onAPICallError(mContext.getString(R.string.network_problem));
    }

    @Override
    public void uploadToAmazon(File image){
        Uri muri = Uri.fromFile(image);
        MediaInterface callbacksAmazon = new MediaInterface()
        {
            @Override
            public void onSuccessUpload(JSONObject message)
            {
                Utility.printLog(TAG+"image uploaded "+message.toString());
                String profilePicUrl= Constants.AMAZON_PROFILE_PATH+amazonFileName;
                profileView.onImageUploadSuccess(profilePicUrl);
            }
            @Override
            public void onUploadError(JSONObject message)
            {
                if(mContext!=null)
                    profileView.onImageUploadFailure();
            }
            @Override
            public void onSuccessDownload(String fileName, byte[] stream, JSONObject object) {
            }
            @Override
            public void onDownloadFailure(JSONObject object) {
            }
            @Override
            public void onSuccessPreview(String fileName, byte[] stream, JSONObject object) {
            }
        };
        amazonFileName = System.currentTimeMillis()+".jpg";
        JSONObject message = stringToJsonAndPublish(mContext.getString(R.string.app_name) +"/"+ amazonFileName, Uri.fromFile(image));
        amazonImageUpload.uploadMedia(mContext, muri, mContext.getString(R.string.AMAZON_PICTURE_BUCKET), Constants.AMAZON_PROFILE_FOLDER + amazonFileName, callbacksAmazon, message);
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.clear();
    }

    public String getImageUrl()
    {
        return preferenceHelperDataSource.getImageUrl();
    }

    public String getUserName()
    {
        return preferenceHelperDataSource.getUsername();
    }
    public String getUserEmail()
    {
        Utility.printLog("getUserEmail : "+ preferenceHelperDataSource.getUserEmail());
        return preferenceHelperDataSource.getUserEmail();
    }

    public String getMobileNumber()
    {
        return preferenceHelperDataSource.getMobileNo();
    }
    public String getPassword()
    {
        return preferenceHelperDataSource.getPassword();
    }

    public void setImageUrl(String url)
    {
        preferenceHelperDataSource.setImageUrl(url);
    }

    @Override
    public int getIsHotelBooking() {
        return preferenceHelperDataSource.getLoginType();
    }

    @Override
    public String getHotelName() {
        return preferenceHelperDataSource.getHotelName();
    }
}