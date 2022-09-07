package com.karru.help_center.zendesk_ticket_details;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.help_center.model.ZendeskHistory;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.ExpireSession;
import com.karru.util.Utility;
import com.heride.rider.R;

import org.json.JSONObject;
import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>HelpIndexPresenter</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexPresenter implements HelpIndexContract.presenter
{
    @Inject HelpIndexContract.HelpView helpView;
    @Inject NetworkService lspServices;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource addressDataSource;
    @Inject MQTTManager mqttManager;
    @Inject Context mActivity;
    private Gson gson;
    private String TAG = "HelpIndexPresenter";

    @Inject HelpIndexPresenter() {
        gson = new Gson();
    }

    @Override
    public void onPriorityImage(Context mContext, String priority, ImageView ivHelpCenterPriorityPre) {

        if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityUrgent)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext, R.color.green_continue));
        else if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityHigh)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext,R.color.livemblue3498));
        else if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityNormal)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext,R.color.red_login_dark));
        else if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityLow)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext,R.color.saffron));

    }

    @Override
    public void callApiToCommentOnTicket(String trim, String zenId)
    {
        Observable<Response<ResponseBody>>observable = lspServices.commentOnTicket(((ApplicationClass)mActivity.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                preferenceHelperDataSource.getLanguageSettings().getCode(), zenId,trim,preferenceHelperDataSource.getRequesterId());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        int code = responseBodyResponse.code();
                        switch (code)
                        {
                            case 200:
                                break;
                            case 498:
                                break;
                            case 440:
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void callApiToCreateTicket(final String trim, final String subject, final String priority)
    {
        com.karru.utility.Utility.printLog("RequesterId:-"+preferenceHelperDataSource.getRequesterId());
        Observable<Response<ResponseBody>>observable = lspServices.createTicket(((ApplicationClass)mActivity.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                preferenceHelperDataSource.getLanguageSettings().getCode(),subject,trim,"open",priority,"problem",preferenceHelperDataSource.getRequesterId());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse)
                    {
                        int code = responseBodyResponse.code();
                        com.karru.utility.Utility.printLog(TAG+" callApiToCreateTicket code :-"+code);
                        String response;
                        try {


                            switch (code)
                            {
                                case 200:
                                    response = responseBodyResponse.body().string();
                                    com.karru.utility.Utility.printLog(TAG+" callApiToCreateTicket response :-"+response);
                                    helpView.onZendeskTicketAdded(response);
                                    break;

                                case 498:
                                    com.karru.utility.Utility.printLog(TAG+" callApiToCreateTicket error :-"+responseBodyResponse.errorBody().string());
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;
                                case 440:
                                    com.karru.utility.Utility.printLog(TAG+" callApiToCreateTicket error :-"+responseBodyResponse.errorBody().string());
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;

                                default:
                                    com.karru.utility.Utility.printLog(TAG+" callApiToCreateTicket error :-"+responseBodyResponse.errorBody().string());
                                    break;
                            }
                        } catch (IOException e) {
                            com.karru.utility.Utility.printLog(TAG+" callApiToCreateTicket error :-"+e);
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void callApiToGetTicketInfo(String zenId)
    {
        Observable<Response<ResponseBody>> observable = lspServices.onToGetZendeskHistory(((ApplicationClass)mActivity.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                preferenceHelperDataSource.getLanguageSettings().getCode(), zenId);

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();
                        String response;
                        JSONObject jsonObject;
                        try
                        {

                            switch (code)
                            {
                                case 200:
                                    response = responseBodyResponse.body().string();
                                    Log.d("HELPINDEX", "onNextResponseINFOTICKET: "+response);
                                    ZendeskHistory zendeskHistory = gson.fromJson(response,ZendeskHistory.class);

                                    Date date = new Date(zendeskHistory.getData().getTimeStamp() * 1000L);
                                    String dateTime[] = Utility.getFormattedDate(date,preferenceHelperDataSource).split(",");
                                    String timeToSet =  dateTime[0]+" | "+dateTime[1];
                                    helpView.onTicketInfoSuccess(zendeskHistory.getData().getEvents(),timeToSet,
                                            zendeskHistory.getData().getSubject(),zendeskHistory.getData().getPriority(),zendeskHistory.getData().getType());
                                    helpView.hideLoading();
                                    break;
                                case 498:
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;
                                case 440:
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public boolean isNetworkAvailable() {
        return false;
    }

    @Override
    public void attachView(Object view) {

    }

    @Override
    public void detachView() {

    }
}
