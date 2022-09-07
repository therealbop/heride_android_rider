package com.karru.booking_flow.invoice;

import com.karru.dagger.ActivityScoped;
import com.karru.booking_flow.invoice.view.InvoiceActivity;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>InvoiceModule</h1>
 * This class is used to provide the classed required to invoice
 * @author 3Embed on 02-02-2018.
 */
@Module
public abstract class InvoiceModule
{
    @Binds
    @ActivityScoped
    abstract InvoiceContract.Presenter providePresenter(InvoicePresenter invoicePresenter);

    @Binds
    @ActivityScoped
    abstract InvoiceContract.View provideView(InvoiceActivity invoiceActivity);
}
