package com.example.ehu.recodevoiceinbackgroud;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.protobuf.ByteString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by kaikoro on 2017/11/13.
 */

public class RecordVoiceService extends Service {
    //録音クラスの宣言
    AudioRecord audioRecord;
    //録音用のファイルパス
    static final String filePath = Environment.getExternalStorageDirectory() + "/test1.3gp";
    //録音フラグ。録音を開始するなら、true。停止するなら、false。ボタンで切り替える
    boolean bIsRecording = false;
    int bufferSize = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("RecordVoiceService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecordVoice();
        Log.d("RecordVoiceService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    //録音開始
    private void startRecordVoice() {
        initAudioRecord();
        startAudioRecorder();
    }


    public void getVoice() {

    }

    //録音停止
    private void stopRecordVoice() {
        //録音フラグを停止にする
        bIsRecording = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("RecordVoiceService", "onDestroy");
        stopRecordVoice();
    }

    //初期化
    public void initAudioRecord() {
        //wav形式
        int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
        int rate = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

        audioRecord = new AudioRecord(audioSource, rate, channelConfig, audioFormat, bufferSize);
    }

    //録音開始、停止が押されるまで録音する
    private void startAudioRecorder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                short buf[] = new short[bufferSize];
                //stopRecordVoice()が呼ばれるまでbufに書き込む
                while (bIsRecording) {
                    audioRecord.read(buf, 0, buf.length);
                    Log.d("audiorecord", String.valueOf(buf.length));
                }
                // 録音停止
                audioRecord.stop();
                audioRecord.release();
            }
        }).start();
    }

}
