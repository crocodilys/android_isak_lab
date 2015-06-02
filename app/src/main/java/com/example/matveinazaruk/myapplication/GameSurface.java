package com.example.matveinazaruk.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

class GameSurface extends SurfaceView implements Runnable {

    protected volatile float ball_dx;

    protected volatile float ball_dy;
    protected float ball_cur_x;
    protected float ball_cur_y;
    protected int ball_radius;
    protected int wall_x;
    protected int wall_width;
    protected int wall_height;
    Thread thread = null;


    SurfaceHolder surfaceHolder;
    volatile boolean running = false;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SoundPool soundPool;
    private int boomSoundId;
    private int scratchSoundId;

    private boolean alreadyPlayed = false;

    private Context mContext;

    Random random;
    public GameSurface(Context context) {
        this(context, null);
    }

    public GameSurface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public void onResume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPause() {
        boolean retry = true;
        running = false;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (running) {

            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);

                ball_cur_x += ball_dx;
                ball_cur_y += ball_dy;

                boolean playedThisTime = false;
                if (ball_cur_x + ball_radius >= canvas.getWidth()) {
                    ball_cur_x = canvas.getWidth() - ball_radius;
                    playBoom();
                    playedThisTime = true;
                }

                if (ball_cur_x - ball_radius <= 0 ) {
                    ball_cur_x = ball_radius;
                    playBoom();
                    playedThisTime = true;
                }

                if (ball_cur_y + ball_radius >= canvas.getHeight()) {
                    ball_cur_y = canvas.getHeight() - ball_radius;
                    playBoom();
                    playedThisTime = true;
                }

                if (ball_cur_y - ball_radius <= 0) {
                    ball_cur_y = ball_radius;
                    playBoom();
                    playedThisTime = true;
                }
                alreadyPlayed = playedThisTime;
                soundPool.play(scratchSoundId, 0.7f, 0.7f, 2, 0, 2f);

                int r = 255;
                int g = 0;
                int b = 0;
                paint.setColor(0xff000000 + (r << 16) + (g << 8) + b);
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                canvas.drawCircle(ball_cur_x, ball_cur_y, ball_radius, paint);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void init() {
        surfaceHolder = getHolder();
        random = new Random();

        ball_cur_x = 200;
        ball_cur_y = 200;
        ball_dx = 0;
        ball_dy = 0;
        ball_radius = 30;
        wall_x = 200;
        wall_height = 30;
        wall_width = 100;

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setMaxStreams(3);
        soundPool = spBuilder.build();

        boomSoundId = soundPool.load(mContext, R.raw.cartoonboing, 3);
        scratchSoundId = soundPool.load(mContext, R.raw.smalldrillonplate, 2);

    }

    public void setBall_dx(float ball_dx) {
        this.ball_dx = ball_dx;
    }

    public void setBall_dy(float ball_dy) {
        this.ball_dy = ball_dy;
    }

    public void playBoom() {
        if (!alreadyPlayed) {
            soundPool.play(boomSoundId, 0.8f, 0.8f, 3, 0, 1f);
            alreadyPlayed = true;
        }
    }

}
