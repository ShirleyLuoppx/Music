package com.ppx.music.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ppx.music.utils.LogUtils;

import java.io.IOException;

/**
 * 豆包生成的 MediaPlayer的 服务类
 */
public class MusicPlayerService extends Service  {

    private static final String TAG = "MusicPlayerService";
    private IBinder binder = new MusicPlayerBinder();
    private MusicController musicController;

    @Override
    public void onCreate() {
        super.onCreate();
        musicController = MusicController.Companion.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("PLAY")) {
                String url = intent.getStringExtra("url");
                if (url != null) {
                    musicController.playMusic(url);
                }
            } else if (intent.getAction().equals("PAUSE")) {
                musicController.pauseMusic();
            } else if (intent.getAction().equals("RESUME")) {
                musicController.resumeMusic();
            } else if (intent.getAction().equals("STOP")) {
                musicController.stopMusic();
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() service is destroyed and player is stopped!!!");
        musicController.stopMusic();
    }

    public class MusicPlayerBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
}
