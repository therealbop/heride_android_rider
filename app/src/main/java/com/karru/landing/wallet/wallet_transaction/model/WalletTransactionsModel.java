package com.karru.landing.wallet.wallet_transaction.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @since 19/09/17.
 */

public class WalletTransactionsModel implements Serializable
{
    /*"errNum":200,
"errMsg":"Got The Details",
"errFlag":0,
"data":{
"debitArr":[],
"creditArr":[],
"creditDebitArr":[*/
    @SerializedName("data")
    @Expose
    private WalletTransactionsDataModel data;

    public WalletTransactionsDataModel getData() {
        return data;
    }
    public void setData(WalletTransactionsDataModel data) {
        this.data = data;
    }

    @Override
    public String toString() { return "WalletTransactionsModel{" + "data=" + data + '}'; }
}
