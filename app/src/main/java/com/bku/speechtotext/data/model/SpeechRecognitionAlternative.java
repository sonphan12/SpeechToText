package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeechRecognitionAlternative {
    @SerializedName("transcript")
    @Expose
    String transcript;
    @SerializedName("confidence")
    @Expose
    int confidence;
    @SerializedName("words")
    @Expose
    WordInfo[] words;

    public String getTranscript() {
        return transcript;
    }

    public int getConfidence() {
        return confidence;
    }

    public WordInfo[] getWords() {
        return words;
    }
}
