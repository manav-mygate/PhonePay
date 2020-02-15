package com.example.phonepay.network;

import com.example.phonepay.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    private static String sAccessToken = null;

    public static void setAccessToken(String accessToken) {
        sAccessToken = accessToken;
    }

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.level(HttpLoggingInterceptor.Level.BASIC);
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder();
                if (sAccessToken != null && !sAccessToken.isEmpty()) {
                    requestBuilder = requestBuilder.header("Authorization", "Bearer " + sAccessToken);
                } /*else {
                    requestBuilder = requestBuilder.removeHeader("Authorization", "Bearer ");
                }*/
                requestBuilder.header("Content-Type", "application/json");
                requestBuilder.header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(interceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl("")//BuildConfig.WEB_URL+"/"
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        return retrofit;
    }

}