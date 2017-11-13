package com.example.ehu.recodevoiceinbackgroud;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kaikoro on 2017/11/13.
 */

public class RecordVoiceService extends Service {
    //録音クラスの宣言
    MediaRecorder recorder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DatabaseUpdateChecker", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecordVoice();
        return super.onStartCommand(intent, flags, startId);
    }
    //録音開始
    private void startRecordVoice(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        //保存先
        String filePath = Environment.getExternalStorageDirectory() + "/audio.3gp";
        recorder.setOutputFile(filePath);

        //録音準備＆録音開始
        try {
            recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recorder.start();   //録音開始
    }

    //録音停止
    private void stopRecordVoice(){
        recorder.stop();
        recorder.reset();   //オブジェクトのリセット
        //release()前であればsetAudioSourceメソッドを呼び出すことで再利用可能
        recorder.release(); //Recorderオブジェクトの解放
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DatabaseUpdateChecker", "onDestroy");
        stopRecordVoice();
    }

}
