package com.hordiienko.slotmachine;

public class Constants {
    public static float density;

    public static final int[][] GAME_GRIDS = {
            {5, 3},
            {3, 3}
    };

    public static final int GAME_GRID_5X3 = 0;
    public static final int GAME_GRID_3X3 = 1;

    public static final int GAME_SLOT_COUNT = 8;
    public static final int GAME_SPEED = 300;
    public static final int GAME_SPIN_TIME = 1000; // milliseconds
    public static final int GAME_DRAW_WIN_TIME = 3000; // milliseconds

    public static final int GAME_DRAW_WIN_LINE_STROKE_WIDTH = 5; // dp
    public static final int GAME_DRAW_WIN_LINE_CORNER_RADIUS = 30;
    public static final int GAME_SLOT_MARGIN = 10; // dp

    public static final int GAME_DEFAULT_BET = 1;
    public static final int GAME_DEFAULT_BALANCE = 100;
    public static final int GAME_DEFAULT_GRID = GAME_GRID_5X3;

    public static final String[] SLOT_NAMES = {
            "charry",
            "lemon",
            "watermelon",
            "diamond",
            "seven",
            "bar",
            "bar 2",
            "bar 3",
    };

    public static final SlotInfo[] SLOTS;

    static {
        SLOTS = new SlotInfo[] {
                new SlotInfo(R.drawable.slot_charry, R.drawable.diffuse_slot_charry, 1, 1.0f),
                new SlotInfo(R.drawable.slot_lemon, R.drawable.diffuse_slot_lemon, 2, 1.5f),
                new SlotInfo(R.drawable.slot_watermelon, R.drawable.diffuse_slot_watermelon, 3, 1.7f),
                new SlotInfo(R.drawable.slot_diamond, R.drawable.diffuse_slot_diamond, 4, 3.0f),
                new SlotInfo(R.drawable.slot_seven, R.drawable.diffuse_slot_seven, 5, 7.0f),
                new SlotInfo(R.drawable.slot_bar, R.drawable.diffuse_slot_bar, 6, 8.0f),
                new SlotInfo(R.drawable.slot_bar_double, R.drawable.diffuse_slot_bar_double, 7, 9.0f),
                new SlotInfo(R.drawable.slot_bar_triple, R.drawable.diffuse_slot_bar_triple, 8, 20.0f)
        };
    }

    public static class SlotInfo {
        private int imageId;
        private int diffuseImageId;
        private int rank;
        private float coefficient;

        public SlotInfo(int imageId, int diffuseImageId, int rank, float coefficient) {
            this.imageId = imageId;
            this.diffuseImageId = diffuseImageId;
            this.rank = rank;
            this.coefficient = coefficient;
        }

        public int getImageId() {
            return imageId;
        }

        public int getDiffuseImageId() {
            return diffuseImageId;
        }

        public int getRank() {
            return rank;
        }

        public float getCoefficient() {
            return coefficient;
        }
    }
}
