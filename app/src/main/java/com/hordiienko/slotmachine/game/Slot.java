package com.hordiienko.slotmachine.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.hordiienko.slotmachine.Constants;

public class Slot {
    private Bitmap slotImage;
    private Bitmap diffuseSlotImage;
    private int rank;

    private Matrix matrix;

    private float scale;
    private float xOffset;
    private float yOffset;

    private float x;
    private float y;

    public Slot(Bitmap slotImage, Bitmap diffuseSlotImage, int rank) {
        this.slotImage = slotImage;
        this.diffuseSlotImage = diffuseSlotImage;
        this.rank = rank;

        matrix = new Matrix();
    }

    public Slot(Slot copy) {
        this(copy.slotImage, copy.diffuseSlotImage, copy.rank);

        scale = copy.scale;
        xOffset = copy.xOffset;
        yOffset = copy.yOffset;
    }

    public int getRank() {
        return rank;
    }

    public void setContainerSize(float containerWidth, float containerHeight) {
        scale = Math.min(containerWidth, containerHeight) /
                Math.max(slotImage.getWidth() + (Constants.GAME_SLOT_MARGIN * Constants.density), slotImage.getHeight() + (Constants.GAME_SLOT_MARGIN * Constants.density));

        xOffset = (containerWidth - (slotImage.getWidth() * scale)) / 2;
        yOffset = (containerHeight - (slotImage.getHeight() * scale)) / 2;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void draw(Canvas canvas, boolean diffuse) {
        matrix.reset();
        matrix.postScale(scale, scale);
        matrix.postTranslate(x + xOffset, y + yOffset);

        canvas.drawBitmap(diffuse ? diffuseSlotImage : slotImage, matrix, null);
    }
}
