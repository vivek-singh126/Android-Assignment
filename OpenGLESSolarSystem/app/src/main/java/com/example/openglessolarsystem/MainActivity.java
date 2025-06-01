package com.example.openglessolarsystem;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView gLView;
    private MyGLRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gLView = new GLSurfaceView(this);

        ActivityManager activityManager =
        (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000;
        boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs3) {
            gLView.setEGLContextClientVersion(3);
            renderer = new MyGLRenderer(this);
            gLView.setRenderer(renderer);
        } else if (supportsEs2) {
            gLView.setEGLContextClientVersion(2);
            renderer = new MyGLRenderer(this);
            gLView.setRenderer(renderer);
        } else {
            // This device does not support OpenGL ES 2.0 or 3.0. Handle gracefully.
            return;
        }

        gLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(gLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gLView.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (renderer != null) {
            return renderer.handleTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}