package com.bsyun.ido.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bsyun.ido.R;

public class WelcomeActivity extends AppCompatActivity {
    private VideoView videoview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        videoview = findViewById(R.id.videoview);
        initVideoview();
    }
    private void initVideoview(){
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/welcome"));
        videoview.setZOrderOnTop(true);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoview.start();
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("TAG", "onCompletion: ");
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
                mp.release();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
