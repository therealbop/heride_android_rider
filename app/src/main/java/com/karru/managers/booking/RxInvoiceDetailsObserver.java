package com.karru.managers.booking;

import com.karru.landing.home.model.InvoiceDetailsModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxInvoiceDetailsObserver</h1>
 * This class is used to publish the invoice details for the booking
 * @author 3Embed
 * @since on 02-02-2018.
 */
public class RxInvoiceDetailsObserver extends Observable<InvoiceDetailsModel>
{
    private ArrayList<Observer<? super InvoiceDetailsModel>> observerArrayList = new ArrayList<>();
    private static RxInvoiceDetailsObserver instance;

    public RxInvoiceDetailsObserver()
    {
        instance = this ;
    }

    //returns singleton object
    public static RxInvoiceDetailsObserver getInstance()
    {
        // Return the instance
        return instance;
    }

    @Override
    protected void subscribeActual(Observer<? super InvoiceDetailsModel> observer)
    {
        observerArrayList.add(observer);
    }

    /**
     * <h2>publishInvoiceDetails</h2>
     * This method is used to push the invoice details
     * @param invoiceDetailsModel invoice details to be pushed
     */
    public void publishInvoiceDetails(InvoiceDetailsModel invoiceDetailsModel)
    {
        for(Observer<? super InvoiceDetailsModel> observer : observerArrayList)
            observer.onNext(invoiceDetailsModel);
    }
}
