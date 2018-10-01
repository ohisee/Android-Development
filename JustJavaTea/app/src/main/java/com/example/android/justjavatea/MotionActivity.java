
package com.example.android.justjavatea;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * This is for motion activity.
 */
public class MotionActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private ShakeMotionListener mShakeMotionListener;

    /**
     * @param savedInstanceState state of something
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeMotionListener = new ShakeMotionListener();
        mShakeMotionListener.setOnShakeListener(new ShakeMotionListener.OnShakeListener() {
            @Override
            public void onShake() {
                handleShakeEvent();
            }
        });
        mSensorManager.registerListener(mShakeMotionListener,
                mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Activity pause
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeMotionListener);
        super.onPause();
    }

    /**
     * Activity resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeMotionListener,
                mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Handle shake event.
     */
    private void handleShakeEvent() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_motion);
        TextView textView = (TextView) findViewById(R.id.motion_indicator);
        // textView.setVisibility(View.INVISIBLE);
        textView.clearAnimation();
        textView.startAnimation(animation);
    }
}
