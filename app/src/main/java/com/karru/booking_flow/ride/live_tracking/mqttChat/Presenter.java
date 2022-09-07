package com.karru.booking_flow.ride.live_tracking.mqttChat;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.api.NetworkService;
import com.karru.booking_flow.ride.live_tracking.mqttChat.model.ChatData;
import com.karru.booking_flow.ride.live_tracking.mqttChat.model.ChatHistoryModel;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.home.model.DriverCancellationModel;
import com.karru.managers.booking.RxDriverCancelledObserver;
import com.karru.utility.Utility;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

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

import static com.karru.utility.Constants.CHAT;

/**
 * Created by DELL on 29-03-2018.
 */

public class Presenter implements ChattingContract.PresenterOperations
{
    private static final String TAG = "Presenter";
    private RxDriverCancelledObserver rxDriverCancelledObserver;

    @Inject
    public Presenter(RxDriverCancelledObserver rxDriverCancelledObserver)
    {
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
    }

    @Inject Context mContext;
    @Inject NetworkService messageService;
    @Inject ChattingContract.ViewOperations view;
    @Inject ChatDataObservable chatDataObservable;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject @Named(CHAT) CompositeDisposable compositeDisposable;

    private String bid,custName,custId,driverId;
    private Disposable disposableCancel;
    private String pageNo="0";

    private ArrayList<ChatData> chatDataArry=new ArrayList<>();

    @Override
    public void getData(Intent intent)
    {
        bid = intent.getStringExtra("BID");
        driverId = intent.getStringExtra("DRIVER_ID");
        custName = intent.getStringExtra("DRIVER_NAME");
        custId= preferenceHelperDataSource.getSid();
    }

    @Override
    public void setActionBar() {
        view.setActionBar(custName);
    }

    @Override
    public void initViews()
    {
        view.setViews(bid);
        view.setRecyclerView();
        initializeRxJava();
    }

    @Override
    public void subscribeDriverCancelDetails()
    {
        rxDriverCancelledObserver.subscribeOn(Schedulers.io());
        rxDriverCancelledObserver.observeOn(AndroidSchedulers.mainThread());
        disposableCancel = rxDriverCancelledObserver.subscribeWith(new DisposableObserver<DriverCancellationModel>()
        {
            @Override
            public void onNext(DriverCancellationModel driverCancellationModel)
            {
                Utility.printLog(TAG+" cancel driver details observed  "+driverCancellationModel.getStatusText());
                view.showDriverCancelDialog(driverCancellationModel.getStatusText());
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
                compositeDisposable.add(disposableCancel);
            }
        });
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    @Override
    public void getChattingData(int page) {

        view.showProgress();
        final Observable<Response<ResponseBody>> profile=messageService.chatHistory(preferenceHelperDataSource.getLanguageSettings().getCode(),
                ((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()), bid, String.valueOf(page));
        profile.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());

                                ChatHistoryModel chatHistoryResponse = new Gson().fromJson(jsonObject.toString(), ChatHistoryModel.class);
                                if(chatHistoryResponse.getData()!=null && chatHistoryResponse.getData().size()>0){
                                    chatDataArry.clear();
                                    for(int i=0;i<chatHistoryResponse.getData().size();i++){
                                        if(chatHistoryResponse.getData().get(i).getFromID().equals(custId)){
                                            chatHistoryResponse.getData().get(i).setCustProType(1);
                                        }else {
                                            chatHistoryResponse.getData().get(i).setCustProType(2);
                                        }
                                        chatDataArry.add(chatHistoryResponse.getData().get(i));
                                    }
                                    Collections.reverse(chatDataArry);
                                    view.updateData(chatDataArry);
                                }

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("chatHistory : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("chatHistory : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void message(String message) {
        if(!message.trim().isEmpty())
        {
            sendMessage(message);
        }
    }

    private void initializeRxJava()
    {
        Observer<ChatData> observer = new Observer<ChatData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ChatData chatData) {
                Utility.printLog(TAG + " chat data " + chatData.getType() + " pro type " +
                        chatData.getCustProType());
                chatData.setCustProType(2);
                chatDataArry.add(chatData);
                view.updateData(chatDataArry);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        chatDataObservable.subscribe(observer);
    }

    private void sendMessage(final String content){

        final long timeStamp=System.currentTimeMillis();

        if(view!=null)
            view.showProgress();
        Utility.printLog(TAG+" driverId "+driverId+" bid "+bid+" custId "+custId);
        Observable<Response<ResponseBody>> message=messageService.message(
                preferenceHelperDataSource.getLanguageSettings().getCode(),
                ((ApplicationClass)mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                "1",
                timeStamp+"", content, custId, bid, driverId);
        message.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());

                                ChatData chatData=new ChatData();
                                chatData.setTimestamp(timeStamp);
                                chatData.setType(1);
                                chatData.setTargetId(driverId);
                                chatData.setFromID(custId);
                                chatData.setContent(content);
                                chatData.setCustProType(1);

                                chatDataArry.add(chatData);
                                view.updateData(chatDataArry);

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }
                            Utility.printLog("messageApi : "+jsonObject.toString());
                        }catch (Exception e)
                        {
                            Utility.printLog("messageApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }
}
