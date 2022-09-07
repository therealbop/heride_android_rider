package com.karru.booking_flow.ride.live_tracking.mqttChat;

import com.karru.booking_flow.ride.live_tracking.mqttChat.model.ChatData;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h>ChatDataObservable</h>
 * Created by Ali on 12/22/2017.
 */

public class ChatDataObservable extends Observable<ChatData>
{
    private Observer<?super ChatData> observer;

    @Override
    protected void subscribeActual(Observer<? super ChatData> observer)
    {
        this.observer = observer;
    }
    public void emitData(ChatData cData)
    {
        observer.onNext(cData);
        observer.onComplete();
    }
}
