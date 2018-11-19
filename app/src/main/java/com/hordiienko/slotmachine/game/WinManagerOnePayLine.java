package com.hordiienko.slotmachine.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import com.hordiienko.slotmachine.Constants;
import com.hordiienko.slotmachine.utils.ColorAnimator;
import com.hordiienko.slotmachine.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class WinManagerOnePayLine extends WinManager {
    private int bet;
    private int[] ranks;
    private Path winLine;
    private Paint winLinePaint;
    private ColorAnimator colorAnimator;

    public WinManagerOnePayLine(int[] grid, int slotCount) {
        super(grid, slotCount);
        ranks = new int[getReelCount()];
        winLine = new Path();

        winLinePaint = new Paint();
        winLinePaint.setColor(Color.RED);
        winLinePaint.setStyle(Paint.Style.STROKE);
        winLinePaint.setAntiAlias(true);
        winLinePaint.setStrokeWidth(Constants.GAME_DRAW_WIN_LINE_STROKE_WIDTH * Constants.density);
        winLinePaint.setPathEffect(new CornerPathEffect(Constants.GAME_DRAW_WIN_LINE_CORNER_RADIUS));

        colorAnimator = new ColorAnimator(Color.RED, Color.WHITE, 0.3f);
    }

    @Override
    public int[][] startNewGame(int bet) {
        this.bet = bet;

        // stop drawWin
        setWin(false);

        ranks[0] = RandomGenerator.getRandomIntInclusive(1, getSlotCount());

        // 1% chance
        int rankChance = RandomGenerator.getIntChance(1, getSlotCount(), ranks[0], 1);

        // check if all ranks win
        if (rankChance == ranks[0]) {
            for (int i = 1; i < ranks.length; i++) {
                ranks[i] = ranks[0];
            }
        } else {
            for (int i = 1; i < ranks.length; i++) {
                ranks[i] = RandomGenerator.getRandomIntInclusive(1, getSlotCount());
            }
        }

//        ranks[0] = 1;
//        ranks[1] = 2;
//        ranks[2] = 2;
//        ranks[3] = 1;
//        ranks[4] = 1;

        return new int[][]{ranks};
    }

    @Override
    public void finishGame() {
        makeWinLine();

        setPrize(calcPrize());
        setWin(true);
    }

    @Override
    public void drawWin(Canvas canvas, float delta) {
        super.drawWin(canvas, delta);

        winLinePaint.setColor(colorAnimator.nextColor());

        canvas.drawPath(winLine, winLinePaint);
    }

    private int calcPrize() {
        int[] count = calcWinRanks();

        int prize = 0;

        // calc prize
        for (int i = 1; i < count.length; i++) {
            if (count[i] > 1) {
                prize += bet * Constants.SLOTS[i - 1].getCoefficient() * (count[i] * 0.5f);
            }
        }

        return prize;
    }

    private int[] calcWinRanks() {
        int[] count = new int[getSlotCount() + 1];

        for (int i = 1; i < count.length; i++) {
            for (int rank : ranks) {
                if (rank == i) {
                    count[i]++;
                }
            }
        }

        return count;
    }

    private void makeWinLine() {
        colorAnimator.reset();

        winLine.reset();

        // check all ranks equals
        boolean allWin = true;
        for (int i = 1; i < ranks.length; i++) {
            if (ranks[i] != ranks[i - 1]) {
                allWin = false;
                break;
            }
        }

        List<Reel> reels = getReels();

        if (allWin) {
            Reel first = reels.get(0);
            Reel last = reels.get(reels.size() - 1);

            winLine.moveTo(first.getX(), first.getHeight() / 2);
            winLine.lineTo(last.getX() + last.getWidth(), last.getHeight() / 2);

            return;
        }

        List<List<Integer>> indexes = new ArrayList<>();

        // fill indexes
        for (int i = 0; i <= getSlotCount(); i++) {
            indexes.add(new ArrayList<>());

            if (i == 0) {
                continue;
            }

            for (int j = 0; j < reels.size(); j++) {
                if (ranks[j] == i) {
                    indexes.get(i).add(j);
                }
            }
        }

        // loop indexes
        for (int i = 1; i <= getSlotCount(); i++) {
            List<Integer> list = indexes.get(i);

            if (list.size() < 2) {
                continue;
            }

            for (int j = 0; j < list.size(); j++) {
                int reelIndex = list.get(j);
                float reelWidth = reels.get(reelIndex).getWidth();
                float slotMargin = Constants.GAME_SLOT_MARGIN * Constants.density;

                if (j == 0) {
                    winLine.moveTo(reels.get(reelIndex).getX() + slotMargin, reels.get(reelIndex).getHeight() / 2);
                    winLine.lineTo(reels.get(reelIndex).getX() + reelWidth - slotMargin, reels.get(reelIndex).getHeight() / 2);
                } else {
                    int reelPreviousIndex = list.get(j - 1);

                    if (reelIndex - 1 != reelPreviousIndex) {
                        // jump
                        float slotHeight = reels.get(reelIndex).getSlotHeight();

                        float jumpX = reels.get(reelPreviousIndex).getX() + (reels.get(reelIndex).getX() + reelWidth - reels.get(reelPreviousIndex).getX()) / 2;
                        float jumpY = (reels.get(reelIndex).getHeight() / 2) - (slotHeight / 2);

                        winLine.lineTo(jumpX, jumpY);
                    }

                    winLine.lineTo(reels.get(reelIndex).getX() + slotMargin, reels.get(reelIndex).getHeight() / 2);
                    winLine.lineTo(reels.get(reelIndex).getX() + reelWidth - slotMargin, reels.get(reelIndex).getHeight() / 2);
                }
            }
        }

    }
}
