package com.hordiienko.slotmachine.game;

import android.graphics.Canvas;

import com.hordiienko.slotmachine.Constants;

import java.util.List;

public abstract class WinManager {
    private int reelCount;
    private int rowCount;
    private int slotCount;

    private List<Reel> reels;
    private int prize;
    private boolean win;

    private long drawWinStartTime;

    public WinManager(int[] grid, int slotCount) {
        this.reelCount = grid[0];
        this.rowCount = grid[1];
        this.slotCount = slotCount;
    }

    public abstract int[][] startNewGame(int bet);
    public abstract void finishGame();

    public void drawWin(Canvas canvas, float delta) {
        long now = System.currentTimeMillis();

        if (now - drawWinStartTime > Constants.GAME_DRAW_WIN_TIME) {
            setWin(false);
        }
    }

    protected int getReelCount() {
        return reelCount;
    }

    protected int getSlotCount() {
        return slotCount;
    }

    protected int getRowCount() {
        return rowCount;
    }

    public void setReels(List<Reel> reels) {
        this.reels = reels;
    }

    protected List<Reel> getReels() {
        return reels;
    }

    protected void setPrize(int prize) {
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }

    public boolean isWin() {
        return win;
    }

    protected void setWin(boolean win) {
        this.win = win;

        if (win) {
            drawWinStartTime = System.currentTimeMillis();
        }
    }
}
