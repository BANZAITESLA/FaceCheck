package com.disu.facecheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 10/07/2022 | 10119239 | DEA INESIA SRI UTAMI | IF6
 */

public class Gambar {
    @SerializedName("Years")
    @Expose
    private String years;
    @SerializedName("Gender")
    @Expose
    private String gender;

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
