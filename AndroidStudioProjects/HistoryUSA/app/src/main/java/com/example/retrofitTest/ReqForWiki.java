package com.example.retrofitTest;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReqForWiki {
    @GET("/wiki/{name}")
    Call<Response> call(@Path("name") String word);

}

