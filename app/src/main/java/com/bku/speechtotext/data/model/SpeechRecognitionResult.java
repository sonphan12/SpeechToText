package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeechRecognitionResult {
    @SerializedName("alternatives")
    @Expose
    SpeechRecognitionAlternative[] alternatives;

    public SpeechRecognitionAlternative[] getAlternatives() {
        return alternatives;
    }
}
