package com.bku.speechtotext;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecognitionConfig {
    @SerializedName("enableAutomaticPunctuation")
    @Expose
    private boolean mEnableAutomaticPunctuation;
    @SerializedName("encoding")
    @Expose
    private String mEncoding;
    @SerializedName("languageCode")
    @Expose
    private String mLanguage;
    @SerializedName("model")
    @Expose
    private String mModel;

    private RecognitionConfig(Builder builder) {
        this.mEnableAutomaticPunctuation = builder.enableAutomaticPunctuation;
        this.mEncoding = builder.encoding;
        this.mLanguage = builder.language;
        this.mModel = builder.model;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    static class Builder {
        private boolean enableAutomaticPunctuation;
        private String encoding;
        private String language;
        private String model;

        public Builder setEnableAutomaticPunctuation(boolean enableAutomaticPunctuation) {
            this.enableAutomaticPunctuation = enableAutomaticPunctuation;
            return this;
        }

        public Builder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public RecognitionConfig build() {
            return new RecognitionConfig(this);
        }
    }
}
