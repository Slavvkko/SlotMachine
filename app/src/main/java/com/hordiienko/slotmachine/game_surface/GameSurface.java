package com.hordiienko.slotmachine.game_surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hordiienko.slotmachine.game.GameLogic;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private static final float DELTA_MULTIPLIER = 100.0f;

    private GameThread gameThread;
    private GameLogic gameLogic;

    private boolean surfaceCreated;
    private long oldTime;

    public GameSurface(Context context) {
        super(context, null);
    }

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    public void start() {
        if (surfaceCreated && gameThread == null) {
            oldTime = System.currentTimeMillis();

            gameThread = new GameThread(this, getHolder());
            gameThread.setRunning(true);
            gameThread.start();
        }
    }

    public void stop() {
        if (gameThread != null) {
            gameThread.setRunning(false);

            boolean retry = true;

            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                    gameThread = null;
                } catch (InterruptedException e) {
                    // try again
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceCreated = true;
        gameLogic.onGameSurfaceCreated();

        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceCreated = false;
        stop();
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void drawFrame(Canvas canvas) {
        long now = System.currentTimeMillis();
        long elapsed = now - oldTime;

        oldTime = now;

        // draw transparent background
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);

        if (gameLogic.isDrawing()) {
            gameLogic.drawFrame(canvas, elapsed / DELTA_MULTIPLIER);
        }
    }
}
