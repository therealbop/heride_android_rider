package com.karru.booking_flow.invoice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>ReceiptDetails</h1>
 * This class is used to hold the receipt details
 * @author  3Embed
 * @since on 06-02-2018.
 */
public class ReceiptDetails implements Serializable
{
    @SerializedName("receiptText")
    @Expose
    private String receiptText;
    @SerializedName("receiptValue")
    @Expose
    private String receiptValue;
    @SerializedName("isSubTotal")
    @Expose
    private boolean isSubTotal;
    @SerializedName("isGrandTotal")
    @Expose
    private boolean isGrandTotal;

    public boolean isSubTotal() {
        return isSubTotal;
    }
    public boolean isGrandTotal() {
        return isGrandTotal;
    }
    public void setGrandTotal(boolean grandTotal) {
        isGrandTotal = grandTotal;
    }
    public void setSubTotal(boolean subTotal) {
        this.isSubTotal = subTotal;
    }
    public String getReceiptText() {
        return receiptText;
    }
    public void setReceiptText(String receiptText) {
        this.receiptText = receiptText;
    }
    public String getReceiptValue() {
        return receiptValue;
    }
    public void setReceiptValue(String receiptValue) {
        this.receiptValue = receiptValue;
    }
}
