package com.karru.help_center;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.help_center.model.AllTicket;
import com.karru.help_center.model.OpenClose;
import com.karru.help_center.model.TicketClose;
import com.karru.help_center.model.TicketOpen;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.ExpireSession;
import com.karru.utility.Utility;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>ZendeskHelpPresenter</h>
 * Created by Ali on 2/26/2018.
 */

public class ZendeskHelpPresenter implements ZendeskHelpContract.Presenter
{

    /*http://45.77.190.140:9999/zendesk/user/akbar%40gmail.com*/

    @Inject ZendeskHelpContract.ZendeskView zendeskView;
    @Inject
    NetworkService lspServices;
    @Inject
    Gson gson;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    SQLiteDataSource addressDataSource;
    @Inject
    MQTTManager mqttManager;
    @Inject
    NetworkService networkService;
    @Inject Context mActivity;

    @Inject
    public ZendeskHelpPresenter()
    {
        gson = new Gson();
    }

    @Override
    public void onToGetZendeskTicket()
    {
        zendeskView.showLoading();
        Utility.printLog("Details"+((ApplicationClass)mActivity.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid())+"Language "+preferenceHelperDataSource.getLanguageSettings().getCode()+"EmalID:-"+preferenceHelperDataSource.getCustomerEmail());
        Observable<Response<ResponseBody>> observable = lspServices.onToGetZendeskTicket(((ApplicationClass)mActivity.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                preferenceHelperDataSource.getLanguageSettings().getCode(),preferenceHelperDataSource.getUserEmail());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();
                        String response;
                        //ErrorHandel errorHandel;
                        try{


                            switch (code)
                            {
                                case 200:
                                    response = Objects.requireNonNull(responseBodyResponse.body()).string();
                                    Log.d("TAG", "onNextTICKETSuccess: "+response);
                                    AllTicket allTicket = gson.fromJson(response,AllTicket.class);

                                    if(allTicket.getData().getClose().size()>0 || allTicket.getData().getOpen().size()>0)
                                    {
                                        if(allTicket.getData().getOpen().size()>0)
                                        {
                                            for(int i = 0;i<allTicket.getData().getOpen().size();i++)
                                            {
                                                TicketOpen ticketOpen = allTicket.getData().getOpen().get(i);
                                                OpenClose openClose = new OpenClose(ticketOpen.getId(),ticketOpen.getTimeStamp()
                                                        ,ticketOpen.getStatus(),ticketOpen.getSubject(),ticketOpen.getType(),
                                                        ticketOpen.getPriority(),ticketOpen.getDescription());

                                                if(i==0)
                                                    openClose.setFirst(true);
                                                // openCloses.add(openClose);
                                                zendeskView.onTicketStatus(openClose,allTicket.getData().getOpen().size(),true);
                                            }
                                        }

                                        if(allTicket.getData().getClose().size()>0)
                                        {
                                            for(int i = 0;i<allTicket.getData().getClose().size();i++)
                                            {
                                                TicketClose ticketClose = allTicket.getData().getClose().get(i);
                                                OpenClose openClose = new OpenClose(ticketClose.getId(),ticketClose.getTimeStamp()
                                                        ,ticketClose.getStatus(),ticketClose.getSubject(),ticketClose.getType(),ticketClose.getPriority()
                                                        ,ticketClose.getDescription());
                                                if(i==0)
                                                {
                                                    openClose.setFirst(true);
                                                }
                                                // openCloses.add(openClose);
                                               // zendeskView.onTicketStatus(openClose, allTicket.getData().getClose().size());
                                                zendeskView.onTicketStatus(openClose,allTicket.getData().getClose().size(),false);
                                            }
                                        }

                                        zendeskView.onNotifyData();
                                        zendeskView.hideLoading();
                                    }else
                                    {
                                        zendeskView.onEmptyTicket();
                                        zendeskView.hideLoading();
                                    }
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
