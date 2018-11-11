package com.bku.speechtotext.data.retrofit;


import com.bku.speechtotext.data.model.RecognitionBody;
import com.bku.speechtotext.data.model.SpeechRecognitionResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GoogleCloudService {
    @POST("/v1p1beta1/speech:recognize")
    Observable<SpeechRecognitionResult> recognize(@Body RecognitionBody recognitionBody);
}