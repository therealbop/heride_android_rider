package com.karru;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class RxConfigCalled extends Observable<Boolean>
{
    private Observer<?super Boolean> observer;

    @Override
    protected void subscribeActual(Observer<? super Boolean> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishStatusOfConfig(Boolean data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}

