package com.karru.landing.home.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * <h1>OutstandingBalanceBottomSheet</h1>
 * used to show the last dues bottom sheet dialog
 * @author 3Embed
 * @since on 2/27/2018.
 */
@SuppressLint("ValidFragment")
public class PromoCodeBottomSheet extends BottomSheetDialogFragment
{
    private static final String TAG = "OutstandingBalanceBottomSheet";

    @BindView(R.id.tv_promo_code_cancel) TextView tv_promo_code_cancel;
    @BindView(R.id.tv_promo_code_title) TextView tv_promo_code_title;
    @BindView(R.id.tv_promo_code_apply) TextView tv_promo_code_apply;
    @BindView(R.id.et_promo_code) EditText et_promo_code;
    @BindView(R.id.til_promo_code) TextInputLayout til_promo_code;
    @BindView(R.id.pb_promo_code_progress) ProgressBar pb_promo_code_progress;

    private AppTypeface appTypeface;
    private  HomeFragmentContract.View homeView;
    private HomeFragmentContract.Presenter presenter;

    public PromoCodeBottomSheet(AppTypeface appTypeface,HomeFragmentContract.Presenter presenter)
    {
        this.appTypeface = appTypeface;
        this.presenter = presenter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        Utility.printLog(TAG+" setupDialog ");
        View contentView = View.inflate(getContext(), R.layout.dialog_promocode, null);
        dialog.setContentView(contentView);
        initialize(contentView);
        if(presenter.getSavedPromo()!=null)
            et_promo_code.setText(presenter.getSavedPromo());
        setTypeface();
    }

    @OnClick({R.id.tv_promo_code_apply,R.id.tv_promo_code_cancel})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_promo_code_apply:
                pb_promo_code_progress.setVisibility(View.VISIBLE);
                tv_promo_code_apply.setVisibility(View.INVISIBLE);
                presenter.validatePromoCode(et_promo_code.getText().toString());
                break;

            case R.id.tv_promo_code_cancel:
                getDialog().dismiss();
                break;
        }
    }

    /**
     * <h2>invalidPromo</h2>
     * used to show invalid promo code
     * @param error error text to be shown
     */
    public void invalidPromo(String error,HomeFragmentContract.View homeView)
    {
        this.homeView = homeView;
        til_promo_code.setErrorEnabled(true);
        til_promo_code.setError(error);
        pb_promo_code_progress.setVisibility(View.GONE);
        tv_promo_code_apply.setVisibility(View.VISIBLE);
    }

    /**
     * <h2>validPromo</h2>
     * used to show valid promo code
     */
    public void validPromo(HomeFragmentContract.View homeView)
    {
        this.homeView = homeView;
        til_promo_code.setErrorEnabled(false);
        til_promo_code.setError(null);
        pb_promo_code_progress.setVisibility(View.GONE);
        tv_promo_code_apply.setVisibility(View.VISIBLE);
        getDialog().dismiss();
    }

    /**
     * <h2>setTypeface</h2>
     * Used to set the typeface
     */
    private void setTypeface()
    {
        tv_promo_code_cancel.setTypeface(appTypeface.getPro_narMedium());
        tv_promo_code_title.setTypeface(appTypeface.getPro_narMedium());
        tv_promo_code_apply.setTypeface(appTypeface.getPro_narMedium());
        et_promo_code.setTypeface(appTypeface.getPro_News());
    }

    @OnTextChanged(R.id.et_promo_code)
    public void onTextChanged(Editable editable)
    {
        til_promo_code.setErrorEnabled(false);
        til_promo_code.setError(null);
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize the variables
     */
    @SuppressLint("SetTextI18n")
    private void initialize(View contentView)
    {
        Utility.printLog(TAG+" initialize bottomsheet ");
        ButterKnife.bind(this,contentView);
        // Request focus and show soft keyboard automatically
        et_promo_code.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}


