package com.hordiienko.slotmachine.game_surface;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean running;
    private GameSurface gameSurface;
    private final SurfaceHolder surfaceHolder;

    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder) {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        long startTime, now, waitTime;

        while (running) {
            startTime = System.currentTimeMillis();

            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                gameSurface.drawFrame(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            now = System.currentTimeMillis();
            waitTime = 33 - (now - startTime);

            if (waitTime > 0) {
                try {
                    sleep(waitTime);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
