package com.bku.speechtotext.ui;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bku.speechtotext.R;
import com.bku.speechtotext.utils.PermissionUtil;
import com.emrekose.recordbutton.OnRecordListener;
import com.emrekose.recordbutton.RecordButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "SpeechToText.3gp";
    private static String TAG = "MainActivity";
    @BindView(R.id.btnRecord)
    RecordButton btnRecord;

    private MediaRecorder mRecorder;
    private String audioPath;
    private boolean recordStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkAndRequestPermission();

        initRecorder();
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
        audioPath = Environment.getExternalStorageDirectory().getPath() + "/" + FILE_NAME;

        mRecorder.setOutputFile(audioPath);
        try {
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
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void initRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
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



