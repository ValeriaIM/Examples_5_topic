package com.example.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    private Bird bird;

    public GameSurfaceView(Context context) {
        super(context);
        init();
    }

    public GameSurfaceView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        getHolder().addCallback(this);
        if (this.bird == null) {
            this.bird = new Bird(getResources());
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(getHolder(), bird);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        // завершаем работу потока
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        bird.setXY((int)event.getX(), (int)event.getY());
        return true;
    }

    private class DrawThread extends Thread{
        private boolean runFlag = true;
        private SurfaceHolder surfaceHolder;
        private final Bird bird;

        public DrawThread(SurfaceHolder surfaceHolder, Bird bird){
            this.surfaceHolder = surfaceHolder;
            this.bird = bird;
        }

        public void setRunning(boolean run) {
            runFlag = run;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (runFlag) {
                try {
                    // получаем объект Canvas и выполняем отрисовку
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas != null) {
                        synchronized (surfaceHolder) {
                            // Очищаем канву, чтобы старых птиц не было
                            canvas.drawColor(Color.WHITE);
                            // Рисуем птицу
                            bird.draw(canvas);
                        }
                    }
                } finally {
                    // Освобождаем канву в любом случае
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    bird.move();
                }
            }
        }
    }
}
