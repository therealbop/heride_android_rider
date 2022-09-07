package com.karru.help_center.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>ZendeskHistory</h>
 * Created by Ali on 12/29/2017.
 */
public class ZendeskHistory implements Serializable
{
    /*"data":{
"ticket_id":27,
"timeStamp":1514550475,
"subject":"errorGot",
"type":"open",
"priority":"high",
"events":[]
}*/

    @SerializedName("data")
    @Expose
    private ZendeskHistoryData data;

    public ZendeskHistoryData getData() {
        return data;
    }
}
