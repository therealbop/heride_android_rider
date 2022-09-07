package com.karru.help_center.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ali on 12/29/2017.
 */

public class AllTicket implements Serializable
{
    /*{
        "data": {
        "open": [],
        "close": []
    }
    }*/

    @SerializedName("data")
    @Expose
    private AllTicketData data;

    public AllTicketData getData() {
        return data;
    }

    public class AllTicketData
    {
        /* "open": [],
        "close": []*/

        @SerializedName("open")
        @Expose
        public ArrayList<TicketOpen>open;
        @SerializedName("close")
        @Expose
        public ArrayList<TicketClose>close;

        public ArrayList<TicketOpen> getOpen() {
            return open;
        }

        public ArrayList<TicketClose> getClose() {
            return close;
        }
    }
}
