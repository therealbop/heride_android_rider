package com.karru.help_center.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>TicketOpen</h>
 * Created by Ali on 12/29/2017.
 */

public class TicketOpen implements Serializable
{

    /*"id":27,
"status":"open",
"timeStamp":1514550475,
"subject":"errorGot",
"type":"problem",
"priority":"high",
"description":"i got an error "*/

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("timeStamp")
    @Expose
    private long timeStamp;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("description")
    @Expose
    private String description;

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }
}
