package com.karru.landing.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>CardsDetailsModel</h1>
 * This class is used to hold the card details
 * @author embed
 * @since on 25/11/15.
 */
public class CardsDetailsModel implements Serializable
{
    @SerializedName("cards")
    @Expose
    private CardDetails[] cards;

    public CardDetails[] getCards() {
        return cards;
    }
}
