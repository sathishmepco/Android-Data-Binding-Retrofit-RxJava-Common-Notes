package com.codificador.commonnotes.retrofit;

import com.codificador.commonnotes.CommonNote;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class APIClient {

    private static final String BASE_URL = "https://api.myjson.com/bins/";
    private static APIClient apiClient;
    private NoteService service;


    private APIClient(){
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(NoteService.class);
    }

    public static APIClient getApiClient() {
        if(apiClient == null)
            apiClient = new APIClient();
        return apiClient;
    }

    public Observable<List<CommonNote>> getAllNotes(){
        return service.getAllNotes();
    }

    public Observable<List<CommonNote>> updateAllNotes(List<CommonNote> notes){
        return service.updateAllNotes(notes);
    }
}