package com.example.mickaelpot_matteoclavel_guilhemcouly.pdr;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DeviceAttitudeHandler implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private DeviceAttitudeHandler.ChangeListener listener;

    private float[] accelero;
    private float[] geomagnetic;

    private float yaw;
    private float pitch;
    private float roll;

    // Calibration pour l'orientation du telephone quand il n'est pas à plat
    public static final float TWENTY_FIVE_DEGREE_IN_RADIAN = 0.436332313f;
    public static final float ONE_FIFTY_FIVE_DEGREE_IN_RADIAN = 2.7052603f;

    /**
     * Question 3.8
     */
    public DeviceAttitudeHandler(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        start();
    }

    /**
     * Question 3.9
     */

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Obtenir la valeur du Sensor Accelerometre
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelero = event.values.clone();
        // Obtenir la valeur du Sensor geomagnetique
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values.clone();
        //Obtenir l'orientation du telephone
        if (accelero != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, accelero, geomagnetic); // stocke dans R une matrice de vecteur
            if (success) {
                float orientationData[] = new float[3];
                float inclination = (float) Math.acos(R[8]);
                // Permet de modifier les axes afin d'obtenir les bonnes valeurs en fonction de l'inclinaison du telephone
                if (inclination < TWENTY_FIVE_DEGREE_IN_RADIAN
                        || inclination > ONE_FIFTY_FIVE_DEGREE_IN_RADIAN)
                {
                    SensorManager.getOrientation(R, orientationData);
                } else {
                    this.sensorManager.remapCoordinateSystem(R,SensorManager.AXIS_X, SensorManager.AXIS_Z,R); // réorganise la matrice de vecteur
                    SensorManager.getOrientation(R, orientationData);
                }
                yaw = orientationData[0];
                pitch = orientationData[1];
                roll = orientationData[2];
                if (listener != null) listener.onChange();
                //stop();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public double getYaw() {
        /**
         * Question 3.10 et 3.11
         */
        double degrees = Math.toDegrees(yaw);
        if(degrees < 0){
            degrees += 360;
        }
        return degrees;
    }

    public void start(){
        this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop(){
        this.sensorManager.unregisterListener(this);
    }

    public DeviceAttitudeHandler.ChangeListener getListener() {
        return listener;
    }

    public void setListener(DeviceAttitudeHandler.ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }

}
