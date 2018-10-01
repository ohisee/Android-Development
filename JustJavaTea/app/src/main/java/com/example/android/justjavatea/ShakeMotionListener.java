package com.example.android.justjavatea;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * This is shake motion listener.
 */
public class ShakeMotionListener implements SensorEventListener {

    private long lastUpdate = 0;
    private float lastX, lastY, lastZ;
    private static final int SHAKE_THRESHOLD = 600;
    private static final long SHAKE_MOTION_PERIOD_MS = 100;

    private OnShakeListener mOnShakeListener;

    public interface OnShakeListener {
        public void onShake();
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        mOnShakeListener = onShakeListener;
    }

    /**
     * On sensor changed, detect shake and invoke handler.
     *
     * @param sensorEvent sensor event of something
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor aSensor = sensorEvent.sensor;
        if (Sensor.TYPE_ACCELEROMETER == aSensor.getType()) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > SHAKE_MOTION_PERIOD_MS) {
                long diffTime = currentTime - lastUpdate;
                lastUpdate = currentTime;
                float seed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
                if (seed > SHAKE_THRESHOLD) {
                    mOnShakeListener.onShake();
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    /**
     * @param sensor   sensor of something
     * @param accuracy accuracy of something
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
    }
}
