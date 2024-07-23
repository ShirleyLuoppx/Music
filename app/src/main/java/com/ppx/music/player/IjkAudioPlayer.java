package com.ppx.music.player;

import android.media.AudioManager;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkAudioPlayer {

    private IjkMediaPlayer ijkMediaPlayer;

    public IjkMediaPlayer getPlayer() {
        return ijkMediaPlayer;
    }

    public void init() {
        if (ijkMediaPlayer == null) {
            ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 50L);

          /*  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"probesize",1024*10);*/
        }
        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setPathAndPrepare(String uri) {
        try {
            ijkMediaPlayer.setDataSource(uri);
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.start();
        }
    }

    public void pause() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.pause();
        }
    }

    public void stop() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
        }
    }

    /**
     * 耗时
     */
    public void reset() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.reset();
        }
    }

    public void release() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.reset();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }

}