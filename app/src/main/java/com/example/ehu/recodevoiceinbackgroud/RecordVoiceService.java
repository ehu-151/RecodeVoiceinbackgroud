/*
* 端末のマイクから音をリアルタイムで周波数、dBFSを表示する
* バックグランドでの動作も可能
*
* 音取得にはAndroid標準の AudioRecord,
* 周波数、dBFSに計算は jtransforms ライブラリを利用
*
* サンプリング周波数 44100Hz
* 量子化ビット数 16
* モノラル
* */

package com.example.ehu.recodevoiceinbackgroud;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jtransforms.fft.DoubleFFT_1D;

/**
 * Created by kaikoro on 2017/11/13.
 */

public class RecordVoiceService extends Service {
    //録音クラスの宣言
    AudioRecord audioRecord;
    //録音フラグ。録音を開始するなら、true。停止するなら、false。ボタンで切り替える
    boolean bIsRecording = false;
    //AudioRecord用バッファ
    int bufferSize = 0;
    //FFT計算用バッファ
    final static int FFT_SIZE = 4096;
    //ｄBFSの計算式の元
    double dB_baseline = Math.pow(2, 15) * FFT_SIZE * Math.sqrt(2);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LifeCycle", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecordVoice();
        Log.d("LifeCycle", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    //録音開始
    private void startRecordVoice() {
        initAudioRecord();
        if (audioRecord.getState() == audioRecord.STATE_INITIALIZED) {  //初期化の確認
            Log.d("AudioRecord", "state" + audioRecord.getState());
            startAudioRecorder();
        } else {
            Log.w("AudioRecord", "state" + audioRecord.getState());
        }
    }


    public void getVoice() {

    }

    //録音停止
    private void stopRecordVoice() {
        //録音フラグを停止にする
        bIsRecording = false;
        Log.d("bIsRecording", String.valueOf(bIsRecording));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle", "onDestroy");
        stopRecordVoice();
        Log.d("RecordingState", String.valueOf(audioRecord.getRecordingState()));
    }

    //初期化
    public void initAudioRecord() {
        //wav形式
        int audioSource = MediaRecorder.AudioSource.MIC;
        int rate = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
        if (FFT_SIZE > bufferSize) bufferSize = FFT_SIZE;

        audioRecord = new AudioRecord(audioSource, rate, channelConfig, audioFormat, bufferSize * 2);
        Log.d("RecordingState", String.valueOf(audioRecord.getRecordingState()));
    }

    //録音開始する、停止が押されるまで録音する
    private void startAudioRecorder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bIsRecording = true;
                short buf[] = new short[bufferSize];
                audioRecord.startRecording();
                //stopRecordVoice()が呼ばれるまでbufに書き込む
                while (bIsRecording) {
                    //bufに音声データを格納
                    audioRecord.read(buf, 0, buf.length);
                    Log.d("RecordingState", String.valueOf(audioRecord.getRecordingState()));

                    //FFTで解析
                    DoubleFFT_1D fft = new DoubleFFT_1D(FFT_SIZE);
                    double[] FFTdata = new double[FFT_SIZE];
                    for (int i = 0; i < FFT_SIZE; i++) {
                        FFTdata[i] = (double) buf[i];
                    }
                    fft.realForward(FFTdata);
                    //dBFS計算
                    double[] dbfs = new double[FFT_SIZE / 2];
                    double max_db = -120d;
                    int max_i = 0;
                    for (int i = 0; i < FFT_SIZE; i += 2) {
                        dbfs[i / 2] = (int)
                                (20 * Math.log10(Math.sqrt(Math.pow(FFTdata[i], 2)
                                        + Math.pow(FFTdata[i + 1], 2)) / dB_baseline));
                        //最大値を計算
                        if (max_db < dbfs[i / 2]) {
                            max_db = dbfs[i / 2];
                            max_i = i / 2;
                        }
                    }
                    Log.d("db", "Hz" + (44100 / (double) FFT_SIZE) * max_i + "maxdb" + max_db);
                }
                // 録音停止
                audioRecord.stop();
                audioRecord.release();
            }
        }).start();
    }
}
