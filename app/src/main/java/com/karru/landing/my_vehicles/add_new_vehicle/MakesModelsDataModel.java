package com.karru.landing.my_vehicles.add_new_vehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MakesModelsDataModel implements Serializable {
    /*"makeName":"alfa-romeo",
"makeId":"5b98af4cd821500bb0664fde"*/
    @Expose
    @SerializedName("makeName")
    private String makeName;

    @Expose
    @SerializedName("makeId")
    private String makeId;

    @Expose
    @SerializedName("modelName")
    private String modelName;

    @Expose
    @SerializedName("modelId")
    private String modelId;

    String getModelName() {
        return modelName;
    }

    void setModelName(String modelName) {
        this.modelName = modelName;
    }

    String getModelId() {
        return modelId;
    }

    void setModelId(String modelId) {
        this.modelId = modelId;
    }

    String getMakeName() {
        return makeName;
    }

    String getMakeId() {
        return makeId;
    }

    void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    void setMakeId(String makeId) {
        this.makeId = makeId;
    }
}
