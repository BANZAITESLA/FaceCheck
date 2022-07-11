package com.disu.facecheck;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 10/07/2022 | 10119239 | DEA INESIA SRI UTAMI | IF6
 */

public interface API {
    @Multipart
    @POST("/predict/face")
    Call<List<Gambar>> uploadGambar(@Part MultipartBody.Part file);
}
