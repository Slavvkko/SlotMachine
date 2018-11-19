package com.hordiienko.slotmachine.game;

import android.graphics.Canvas;

import com.hordiienko.slotmachine.Constants;
import com.hordiienko.slotmachine.utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Reel {
    public interface ReelListener {
        void onReelStop();
    }

    private float x;
    private float y;
    private float width;
    private float height;
    private int rows;

    private ReelListener reelListener;

    private List<Slot> slots;
    private final List<Slot> slotsToDraw;

    private float slotHeight;

    private boolean spin;
    private boolean stop;

    public Reel(int x, int y, int width, int height, int rows) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rows = rows;

        slotsToDraw = new LinkedList<>();
    }

    public float getX() {
        return x;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSlotHeight() {
        return slotHeight;
    }

    public void setReelListener(ReelListener reelListener) {
        this.reelListener = reelListener;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;

        // calculate slotHeight
        slotHeight = height / rows;

        // calculate scale for each slot based on their width
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).setContainerSize(width, slotHeight);
        }

        shuffle();
    }

    public void startSpin(int[] stopRanks) {
        fillSlotsToDraw(stopRanks);

        spin = true;
        stop = false;
    }

    public void stopSpin() {
        stop = true;
    }

    public void draw(Canvas canvas, float delta) {
        if (spin) {
            scroll(delta);
        }

        synchronized (slotsToDraw) {
            for (Slot slot : slotsToDraw) {
                slot.draw(canvas, spin);
            }
        }
    }

    private void scroll(float delta) {
        float additionY = Constants.GAME_SPEED * delta;
        float stopY = this.y;
        float slotY;

        if (stop) {
            slotY = slotsToDraw.get(0).getY();

            if (slotY < stopY) {
                if (slotY + additionY > stopY) {
                    additionY = Math.abs(stopY - slotY);
                    reelStop();
                }
            }
        }

        for (int i = 0; i < slotsToDraw.size(); i++) {
            Slot slot = slotsToDraw.get(i);

            slotY = slot.getY() + additionY;

            if (i == (rows) && slotY >= height) {
                resetSlotsXY(-(slotHeight * rows) + (slotY - height));
                break;
            } else {
                slot.setXY(this.x, slotY);
            }
        }
    }

    private void shuffle() {
        fillSlotsToDraw(null);
    }

    private void resetSlotsXY(float offset) {
        float startY = -(slots.size() * slotHeight) + (slotHeight * rows) + offset;

        for (int i = 0; i < slotsToDraw.size(); i++) {
            float y = startY + (slotHeight * i);

            slotsToDraw.get(i).setXY(this.x, y);
        }
    }

    private void fillSlotsToDraw(int[] firstRanks) {
        synchronized (slotsToDraw) {
            slotsToDraw.clear();

            int skip = 0;

            if (firstRanks != null) {
                for (int firstRank : firstRanks) {
                    if (firstRank != 0) {
                        slotsToDraw.add(new Slot(Utils.getSlotByRank(slots, firstRank)));
                    } else {
                        slotsToDraw.add(new Slot(slots.get(new Random().nextInt(slots.size()))));
                    }

                    skip++;
                }
            }

            for (int i = skip; i < slots.size() + rows; i++) {
                Slot slot;

                if (i < slots.size()) {
                    slot = slots.get(new Random().nextInt(slots.size()));
                } else {
                    slot = slotsToDraw.get(i - slots.size());
                }

                slotsToDraw.add(new Slot(slot));
            }
        }

        resetSlotsXY(0);
    }

    private void reelStop() {
        spin = false;

        if (reelListener != null) {
            reelListener.onReelStop();
        }
    }
}
