package com.karru.booking_flow.invoice;

import android.content.Context;

import com.karru.dagger.ActivityScoped;
import com.karru.booking_flow.invoice.model.InvoiceModel;
import com.karru.booking_flow.invoice.view.FeedbackReasonsListAdapter;
import com.karru.booking_flow.invoice.view.InvoiceActivity;
import com.karru.booking_flow.invoice.view.InvoiceTipValueAdapter;
import com.karru.booking_flow.invoice.view.ReceiptDetailsAdapter;
import com.karru.booking_flow.invoice.view.ReceiptDetailsDialog;
import com.karru.network.NetworkReachableActivity;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.INVOICE;

/**
 * <h1>InvoiceUtilModule</h1>
 * This class is used to provide the classes to the dagger
 * @author 3Embed
 * @since on 05-02-2018.
 */
@Module
public class InvoiceUtilModule
{
    @Provides
    @ActivityScoped
    FeedbackReasonsListAdapter provideReceiptRatingsListAdapter(Context context,AppTypeface appTypeface)
    {
        return new FeedbackReasonsListAdapter(appTypeface);
    }

    @Provides
    @ActivityScoped
    InvoiceModel provideInvoiceModel()
    {
        return new InvoiceModel();
    }

    @Provides
    @ActivityScoped
    ReceiptDetailsAdapter receiptDetailsAdapter(Context context,AppTypeface appTypeface)
    {
        return new ReceiptDetailsAdapter(context,appTypeface);
    }

    @Provides
    @ActivityScoped
    ReceiptDetailsDialog provideReceiptDetailsDialog(InvoiceActivity context, AppTypeface appTypeface,
                                                     ReceiptDetailsAdapter receiptDetailsAdapter,
                                                     DateFormatter dateFormatter)
    {
        return new ReceiptDetailsDialog(context,appTypeface,receiptDetailsAdapter,dateFormatter);
    }

    @Provides
    @Named(INVOICE)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    InvoiceTipValueAdapter invoiceTipValueAdapter()
    {
        return new InvoiceTipValueAdapter();
    }
}
