package com.karru.landing.home.promo_code;

import com.karru.landing.home.model.promo_code_model.PromoCodeDataModel;

import java.util.ArrayList;

public interface PromoCodeContract {
    interface View
    {
        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();
        /**
         * <h2>dismissProgressDialog</h2>
         * This method is used to hide the progress dialog
         */
        void dismissProgressDialog();
        void showToast(String message);
        void setPromoCodeList(ArrayList<PromoCodeDataModel> promoCodeDataModel);
        void setPromoCodeValue(String promoCode);
        void invalidPromo(String error);
        void validPromo(String code);
        void fareEstimate(boolean fareEstimate);

    }

    interface Presenter {
        void getPromoCodeData();
        void validatePromoCode(String promoCode);

        void checkRTLConversion();
    }
}
