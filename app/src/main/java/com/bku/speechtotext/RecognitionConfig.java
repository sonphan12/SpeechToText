package com.bku.speechtotext;

public class RecognitionConfig {
    private boolean mEnableAutomaticPunctuation;
    private String mEncoding;
    private String mLanguage;
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

    public String toJsonString() {
        return  "\"config\": {\n" +
                "\"enableAutomaticPunctuation\": " + mEnableAutomaticPunctuation + ",\n" +
                "\"encoding\": \"" + mEncoding + "\",\n" +
                "\"languageCode\": \"" + mLanguage + "\",\n" +
                "\"model\": \"" + mModel + "\"\n" +
                "}";
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
