package com.reyworkspace.pbl_project.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static Retrofit retrofit;
    public static Retrofit getRetrofitInsatnce(){
        if(retrofit==null){
            retrofit= new Retrofit.Builder().baseUrl("https://api.exchangerate-api.com").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
