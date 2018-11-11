package com.bku.speechtotext.data.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Welcome on 11/11/2018.
 */

public class Retro {

    private final String CLOUD_API_KEY = "AIzaSyCKYZN8rkHDb3FfcZFeCAqbyoGgFhcnv3w";

    private static String url = "https://speech.googleapis.com/";

    private static retrofit2.Retrofit retrofit = null;

    public static retrofit2.Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit2.Retrofit.Builder builder=new retrofit2.Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

            OkHttpClient.Builder httpClientBuilder=new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60,TimeUnit.SECONDS);

            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

            retrofit=builder.client(httpClientBuilder.build()).build();
        }
        return retrofit;
    }
}
