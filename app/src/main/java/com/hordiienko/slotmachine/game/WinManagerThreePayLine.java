package com.hordiienko.slotmachine.game;

import android.graphics.Canvas;

import com.hordiienko.slotmachine.utils.RandomGenerator;

public class WinManagerThreePayLine extends WinManager {
    private int bet;
    private int[][] ranks;

    public WinManagerThreePayLine(int[] grid, int slotCount) {
        super(grid, slotCount);
        ranks = new int[getRowCount()][getReelCount()];
    }

    @Override
    public int[][] startNewGame(int bet) {
        this.bet = bet;

        // stop drawWin
        setWin(false);

        for (int row = 0; row < getRowCount(); row++) {
            for (int reel = 0; reel < getReelCount(); reel++) {
                ranks[row][reel] = RandomGenerator.getRandomIntInclusive(1, getSlotCount());
            }
        }

        return ranks;
    }

    @Override
    public void finishGame() {
        makeWinLine();

        setPrize(100);
        setWin(false);
    }

    @Override
    public void drawWin(Canvas canvas, float delta) {
        super.drawWin(canvas, delta);
    }

    private void makeWinLine() {

    }
}
