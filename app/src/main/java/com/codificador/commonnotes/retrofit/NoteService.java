package com.codificador.commonnotes.retrofit;

import com.codificador.commonnotes.CommonNote;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import rx.Observable;

public interface NoteService {
    //retrieving notes from the cloud
    @GET("auevm")
    Observable<List<CommonNote>> getAllNotes();

    //uploading the notes to cloud
    @PUT("auevm")
    Observable<List<CommonNote>> updateAllNotes(@Body List<CommonNote> notes);
}