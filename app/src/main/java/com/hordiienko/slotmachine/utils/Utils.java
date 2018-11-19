package com.hordiienko.slotmachine.utils;

import com.hordiienko.slotmachine.game.Slot;

import java.util.List;

public class Utils {
    public static Slot getSlotByRank(List<Slot> slots, int rank) {
        for (Slot slot : slots) {
            if (slot.getRank() == rank) {
                return slot;
            }
        }

        // dummy
        return slots.get(0);
    }
}
