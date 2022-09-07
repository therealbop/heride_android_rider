package com.karru.landing.history.history_details;

import android.content.Context;

import com.karru.dagger.ActivityScoped;
import com.karru.booking_flow.invoice.view.ReceiptDetailsAdapter;
import com.karru.landing.history.history_details.model.HelpDataModel;
import com.karru.landing.history.history_details.view.HistoryDetailsHelpAdapter;
import com.karru.util.AppTypeface;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.HISTORY_DETAILS;

/**
 * <h1>HistoryUtilModule</h1>
 * to provide models to dagger
 * @author 3Embed
 * @since on 2/23/2018.
 */
@Module
public class HistoryUtilModule
{
    @Provides
    @ActivityScoped
    @Named(HISTORY_DETAILS)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    ReceiptDetailsAdapter receiptDetailsAdapter(Context context, AppTypeface appTypeface)
    {
        return new ReceiptDetailsAdapter(context,appTypeface);
    }

    @Provides
    @ActivityScoped
    ArrayList<HelpDataModel> helpDataModelArrayList()
    {
       return new ArrayList<>();
    }

    @Provides
    @ActivityScoped
    HistoryDetailsHelpAdapter historyDetailsHelpAdapter(Context context, AppTypeface appTypeface, ArrayList<HelpDataModel> helpDataModels)
    {
        return new HistoryDetailsHelpAdapter(context,appTypeface,helpDataModels);
    }
}
