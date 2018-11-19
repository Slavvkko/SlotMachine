package com.hordiienko.slotmachine.game;

import android.graphics.Canvas;
import android.os.Handler;

import com.hordiienko.slotmachine.Constants;
import com.hordiienko.slotmachine.game_surface.GameSurface;
import com.hordiienko.slotmachine.utils.SlotsLoader;

import java.util.ArrayList;
import java.util.List;

public class GameLogic implements Reel.ReelListener {
    private GameActivity activity;
    private GameSurface gameSurface;

    private Handler handler;

    private int[] grid;

    private boolean surfaceCreated;

    private List<Slot> slots;
    private List<Reel> reels;

    private int balance;
    private WinManager winManager;

    private boolean spin;
    private int reelsStopCounter;
    private long spinStartTime;

    private boolean drawing;

    private int surfaceWidth;
    private int surfaceHeight;

    public GameLogic(GameActivity activity, GameSurface gameSurface) {
        this.activity = activity;
        this.gameSurface = gameSurface;

        handler= new Handler();

        slots = SlotsLoader.loadSlots(activity);

        balance = Constants.GAME_DEFAULT_BALANCE;
        this.activity.updateBalance(balance);

        this.gameSurface.setGameLogic(this);

        spinStartTime = -1;
        drawing = false;
    }

    public void setGrid(int[] grid) {
        this.grid = grid;

        winManager = new WinManagerOnePayLine(this.grid, Constants.GAME_SLOT_COUNT);

        if (surfaceCreated) {
            initReels();
        }
    }

    public void onClickSpin() {
        if (spin) {
            return;
        }

        int bet = activity.getBet();

        if (balance < bet) {
            activity.showNoBalance();
            return;
        }

        balance -= bet;
        activity.updateBalance(balance);

        int[][] ranks = winManager.startNewGame(bet);
        reelsStopCounter = reels.size();

        for (int i = 0; i < reels.size(); i++) {
            Reel reel = reels.get(i);

            int[] reelRanks = new int[grid[1]];

            if (ranks.length == reelRanks.length) {
                for (int j = 0; j < reelRanks.length; j++) {
                    reelRanks[j] = ranks[j][i];
                }
            } else {
                reelRanks[grid[1] / 2] = ranks[0][i];
            }

            reel.startSpin(reelRanks);
        }

        spinStartTime = System.currentTimeMillis();

        spin = true;
    }

    public void onGameSurfaceCreated() {
        surfaceCreated = true;
        drawing = true;

        surfaceWidth = gameSurface.getWidth();
        surfaceHeight = gameSurface.getHeight();

        if (reels == null) {
            initReels();
        }
    }

    @Override
    public void onReelStop() {
        reelsStopCounter--;

        if (reelsStopCounter == 0) {
            handler.post(this::spinFinished);
        }
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void drawFrame(Canvas canvas, float delta) {
        for (Reel reel : reels) {
            reel.draw(canvas, delta);
        }

        long now = System.currentTimeMillis();

        if (spinStartTime != -1 && now - spinStartTime > Constants.GAME_SPIN_TIME) {
            stopSpin();
        }

        if (winManager.isWin()) {
            winManager.drawWin(canvas, delta);
        }
    }

    private void stopSpin() {
        spinStartTime = -1;

        for (int i = 0; i < reels.size(); i++) {
            Reel reel = reels.get(i);
            reel.stopSpin();
        }
    }

    // called from UI thread
    private void spinFinished() {
        winManager.finishGame();

        int prize = winManager.getPrize();

        if (prize > 0) {
            balance += prize;
            activity.updateBalance(balance);
        }

        spin = false;
    }

    private void initReels() {
        reels = makeReels();
        winManager.setReels(reels);
    }

    private List<Reel> makeReels() {
        List<Reel> list = new ArrayList<>();

        int reelWidth = surfaceWidth / grid[0];

        for (int i = 0; i < grid[0]; i++) {
            Reel reel = new Reel(
                    reelWidth * i,
                    0,
                    reelWidth,
                    surfaceHeight,
                    grid[1]
            );

            reel.setSlots(slots);
            reel.setReelListener(this);

            list.add(reel);
        }

        return list;
    }
}
