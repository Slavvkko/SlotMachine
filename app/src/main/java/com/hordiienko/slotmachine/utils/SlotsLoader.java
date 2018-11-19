package com.hordiienko.slotmachine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hordiienko.slotmachine.Constants;
import com.hordiienko.slotmachine.game.Slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotsLoader {
    public static List<Slot> loadSlots(Context context) {
        List<Slot> slots = new ArrayList<>();

        Bitmap slotArray[] = SlotsLoader.loadSlotsImages(context, false);
        Bitmap diffuseSlotArray[] = SlotsLoader.loadSlotsImages(context, true);

        for (int i = 0; i < slotArray.length; i++) {
            slots.add(new Slot(
                    slotArray[i],
                    diffuseSlotArray[i],
                    Constants.SLOTS[i].getRank()
            ));
        }

        return Collections.unmodifiableList(slots);
    }

    private static Bitmap[] loadSlotsImages(Context context, boolean diffuse) {
        Bitmap[] bitmaps = new Bitmap[Constants.SLOTS.length];

        for (int i = 0; i < bitmaps.length; i++) {
            if (diffuse) {
                bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), Constants.SLOTS[i].getDiffuseImageId());
            } else {
                bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), Constants.SLOTS[i].getImageId());
            }
        }

        return bitmaps;
    }
}
