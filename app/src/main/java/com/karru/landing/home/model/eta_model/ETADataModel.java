package com.karru.landing.home.model.eta_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Akbar on 26/11/16.
 */

public class ETADataModel implements Serializable
{
    /*    "destination_addresses":[],
    "origin_addresses":[],
    ""rows":[

    {
        "elements":[]
    }

],,
    "status":"OK"*/
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rows")
    @Expose
    private ArrayList<ElementsDetailsForEtaModel> rows;

    public String getStatus() {
        return status;
    }

    public ArrayList<ElementsDetailsForEtaModel> getRows() {
        return rows;
    }
}
