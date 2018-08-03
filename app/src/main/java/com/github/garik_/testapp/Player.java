package com.github.garik_.testapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;

class Player {
    public void play(Context context, String filepath) {
        Uri music = Uri.fromFile(new File(filepath));
        try {

            MediaPlayer mp = MediaPlayer.create(context, music);
            mp.setVolume(100, 100);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });

        } catch (Exception e) {
            Log.e(GarikApp.TAG, "Error default media");
        }
    }
}
