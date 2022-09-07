package com.karru.landing.home.model.eta_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Akbar on 26/11/16.
 */
public class ElementsDetailsForEtaModel implements Serializable
{
    /*    "elements":[
        {
            "distance":{
                "text":"48 m",
                "value":48
            },
            "duration":{
                "text":"1 min",
                "value":25
            },
            "status":"OK"
        }
    ]

}*/
    @SerializedName("elements")
    @Expose
    private ArrayList<ElementsForEtaModel> elements;

    public ArrayList<ElementsForEtaModel> getElements() {
        return elements;
    }
}
