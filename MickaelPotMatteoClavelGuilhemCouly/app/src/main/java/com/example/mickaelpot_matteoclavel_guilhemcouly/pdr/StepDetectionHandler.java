package com.example.mickaelpot_matteoclavel_guilhemcouly.pdr;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Vector;

public class StepDetectionHandler implements SensorEventListener {

    private final SensorManager sensorManager;
    private ChangeListener listener;
    static Sensor linearAccelerationSensor;
    private final Vector<Float> averageCalc = new Vector<>();

    /**
     * Question 3.3
     * @param sensorManager
     */
    public StepDetectionHandler(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        start();
    }

    public void start() {
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        sensorManager.unregisterListener(this,linearAccelerationSensor);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /**
         * Question 3.4 et 3.5
         */
        if (sensorEvent.sensor.getType() == linearAccelerationSensor.getType()){
            float y = sensorEvent.values[1];
            float average = getAverage(y);
            if (average > 1.2) {
                if (listener != null) listener.onChange();
            }
        }
    }

    /**
     * Question 3.7
     */

    public float getAverage( float value ) {
        float tempValue = 0;
        averageCalc.add(value);
        if (averageCalc.size() >= 4) averageCalc.removeElementAt(0);
        for (float lastValue : averageCalc) tempValue += lastValue;
        return tempValue / averageCalc.size();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // When sensor precision changed
    }

    /**
     * Question 3.6
     */
    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}