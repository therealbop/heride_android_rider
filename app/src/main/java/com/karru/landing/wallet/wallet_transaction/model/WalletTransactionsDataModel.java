package com.karru.landing.wallet.wallet_transaction.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @since 19/09/17.
 */

public class WalletTransactionsDataModel implements Serializable
{
    /* "data":{
     "debitArr":[],
     "creditArr":[],
     "creditDebitArr":[]}*/
    @SerializedName("debitTransctions")
    @Expose
    private ArrayList<CreditDebitTransactionsModel> debitTransctions;
    @SerializedName("creditTransctions")
    @Expose
    private ArrayList<CreditDebitTransactionsModel> creditTransctions;
    @SerializedName("creditDebitTransctions")
    @Expose
    private ArrayList<CreditDebitTransactionsModel> creditDebitTransctions;

    public ArrayList<CreditDebitTransactionsModel> getDebitArr() {
        return debitTransctions;
    }
    public ArrayList<CreditDebitTransactionsModel> getCreditArr() {
        return creditTransctions;
    }
    public ArrayList<CreditDebitTransactionsModel> getCreditDebitArr() { return creditDebitTransctions; }

    @Override
    public String toString() {
        return "WalletTransactionsDataModel{" +
                "debitArr=" + debitTransctions +
                ", creditArr=" + creditTransctions +
                ", creditDebitArr=" + creditDebitTransctions +
                '}';
    }
}
