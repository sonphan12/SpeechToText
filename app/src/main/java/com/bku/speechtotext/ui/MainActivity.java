package com.bku.speechtotext.ui;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.bku.speechtotext.R;
import com.bku.speechtotext.data.model.RecognitionAudio;
import com.bku.speechtotext.data.model.RecognitionBody;
import com.bku.speechtotext.data.model.RecognitionConfig;
import com.bku.speechtotext.data.model.SpeechRecognitionAlternative;
import com.bku.speechtotext.data.model.SpeechRecognitionResult;
import com.bku.speechtotext.data.retrofit.GoogleCloudService;
import com.bku.speechtotext.data.retrofit.NetworkHelper;
import com.bku.speechtotext.utils.AudioEncoder;
import com.bku.speechtotext.utils.PermissionUtil;
import com.emrekose.recordbutton.OnRecordListener;
import com.emrekose.recordbutton.RecordButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "SpeechToText.3gp";
    private static String TAG = "MainActivity";
    @BindView(R.id.btnRecord)
    RecordButton btnRecord;
    @BindView(R.id.txtResult)
    TextView txtResult;

    private MediaRecorder mRecorder;
    private String audioPath;
    private boolean recordStarted = false;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkAndRequestPermission();

        btnRecord.setRecordListener(new OnRecordListener() {
            @Override
            public void onRecord() {
                if (!recordStarted) {
                    startRecord();
                    recordStarted = true;
                }
            }

            @Override
            public void onRecordCancel() {
                onRecordFinish();
            }

            @Override
            public void onRecordFinish() {
                finishRecord();
                recordStarted = false;
            }
        });
    }

    private void checkAndRequestPermission() {
        if (!PermissionUtil.checkSelfPermission(this)) {
            PermissionUtil.requestPermission(this);
        }
    }

    private void startRecord() {
        try {
            initRecorder();
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void finishRecord() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            voiceRecognize();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void voiceRecognize() {
        Disposable d = buildRecognitionBody()
                .flatMap(recognitionBody ->
                        NetworkHelper.getRetrofit().create(GoogleCloudService.class)
                        .recognize(NetworkHelper.CLOUD_API_KEY, recognitionBody))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showResult, e -> Log.w(TAG, e));
        mSubscription.add(d);
    }

    Observable<RecognitionBody> buildRecognitionBody() {
        return Observable.create(emitter -> {
            RecognitionAudio recognitionAudio = new RecognitionAudio();
            recognitionAudio.setContent(AudioEncoder.encodeBase64(audioPath));

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEnableAutomaticPunctuation(true)
                    .setEncoding("LINEAR16")
                    .setLanguage("vi-VN")
                    .setModel("default")
                    .build();

            RecognitionBody recognitionBody = new RecognitionBody();
            recognitionBody.setAudio(recognitionAudio);
            recognitionBody.setConfig(recognitionConfig);
            emitter.onNext(recognitionBody);
            emitter.onComplete();
        });
    }

    private void showResult(SpeechRecognitionResult recognitionResult) {
        SpeechRecognitionAlternative[] alternatives = recognitionResult.getAlternatives();
        if (alternatives.length <= 0) {
            return;
        }
        txtResult.setText(alternatives[0].getTranscript());
    }

    private void initRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        audioPath = Environment.getExternalStorageDirectory().getPath() + "/" + FILE_NAME;

        mRecorder.setOutputFile(audioPath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != PermissionUtil.ALL_PERMISSION_CODE) {
            return;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }
}



