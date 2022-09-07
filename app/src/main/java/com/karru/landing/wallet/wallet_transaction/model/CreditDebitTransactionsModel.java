package com.karru.landing.wallet.wallet_transaction.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreditDebitTransactionsModel implements Serializable
{
    @SerializedName("txnType")
    @Expose
    private String txnType;
    @SerializedName("trigger")
    @Expose
    private String trigger;
    @SerializedName("openingBal")
    @Expose
    private String openingBal;
    @SerializedName("tripId")
    @Expose
    private String tripId;
    @SerializedName("txnId")
    @Expose
    private String txnId;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("txnDate")
    @Expose
    private String txnDate;
    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;

    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public String getTxnType ()
    {
        return txnType;
    }
    public String getTrigger ()
    {
        return trigger;
    }
    public String getTripId ()
    {
        return tripId;
    }
    public String getTxnId ()
    {
        return txnId;
    }
    public String getCurrency ()
    {
        return currencySymbol;
    }
    public void setCurrency (String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
    }
    public String getAmount ()
    {
        return amount;
    }
    public void setAmount (String amount)
    {
        this.amount = amount;
    }
    public String getTxnDate() {
        return txnDate;
    }
}
