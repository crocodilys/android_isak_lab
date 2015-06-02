package com.example.matveinazaruk.myapplication;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

/**
 * Created by matveinazaruk on 12/21/14.
 */
public class GameActivity extends Activity {

    private static final float BALL_SPEED_MULTIPLIER = 5;

    private GameSurface gameSurface;
    private SensorManager sensorManager;
    private Sensor sensorGravity;

    private MediaPlayer mp;

    private String playerName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerName = getIntent().getExtras().getString(MainActivity.ARG_USER_NAME);

        setContentView(R.layout.activity_game);
        gameSurface = (GameSurface)findViewById(R.id.gameSurfaceView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sensorManager.registerListener(listener, sensorGravity,
                SensorManager.SENSOR_DELAY_NORMAL);

        mp = MediaPlayer.create(this, R.raw.music);

        mp.setVolume(0, 0.05f);
        mp.setLooping(false);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurface.onResume();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurface.onPause();
        sensorManager.unregisterListener(listener);
        mp.stop();
    }


    public void onExit(View view) {
        onBackPressed();
        (new ResultsDB(this)).addResult(playerName, 1);
    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {

                case Sensor.TYPE_GRAVITY:
                    gameSurface.setBall_dx((-event.values[0] * BALL_SPEED_MULTIPLIER));
                    gameSurface.setBall_dy((event.values[1] * BALL_SPEED_MULTIPLIER));
                    break;
            }

        }

    };

}

