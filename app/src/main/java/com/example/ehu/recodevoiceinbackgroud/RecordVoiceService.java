package com.example.ehu.recodevoiceinbackgroud;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kaikoro on 2017/11/13.
 */

public class RecordVoiceService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
