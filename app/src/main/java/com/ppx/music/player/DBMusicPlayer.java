package com.ppx.music.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class DBMusicPlayer extends Service implements IjkMediaPlayer.OnPreparedListener,
        IjkMediaPlayer.OnErrorListener, IjkMediaPlayer.OnCompletionListener {

    private static final String TAG = "MusicPlayerService";
    private IjkMediaPlayer ijkMediaPlayer;
    private IBinder binder = new MusicPlayerBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ijkMediaPlayer.setOnPreparedListener(this);
        ijkMediaPlayer.setOnErrorListener(this);
        ijkMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!= null && intent.getAction()!= null) {
            if (intent.getAction().equals("PLAY")) {
                String url = intent.getStringExtra("url");
                if (url!= null) {
                    playMusic(url);
                }
            } else if (intent.getAction().equals("PAUSE")) {
                pauseMusic();
            } else if (intent.getAction().equals("RESUME")) {
                resumeMusic();
            } else if (intent.getAction().equals("STOP")) {
                stopMusic();
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
        stopMusic();
    }

    private void playMusic(String url) {
        try {
            ijkMediaPlayer.reset();
            ijkMediaPlayer.setDataSource(url);
            ijkMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseMusic() {
        if (ijkMediaPlayer.isPlaying()) {
            ijkMediaPlayer.pause();
        }
    }

    private void resumeMusic() {
        if (!ijkMediaPlayer.isPlaying()) {
            ijkMediaPlayer.start();
        }
    }

    private void stopMusic() {
        if (ijkMediaPlayer.isPlaying()) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.reset();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        iMediaPlayer.start();
    }

    public class MusicPlayerBinder extends Binder {
        public DBMusicPlayer getService() {
            return DBMusicPlayer.this;
        }
    }
}
