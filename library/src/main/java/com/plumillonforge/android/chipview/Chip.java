package com.plumillonforge.android.chipview;

import android.os.Parcelable;

/**
 * Created by Plumillon Forge on 17/09/15.
 */
public abstract class Chip implements Parcelable {
    /**
     * Return the Chip text
     *
     * @return String
     */
    public abstract String getText();

    /**
     * Return the Chip layout res id
     * Override it if you want to have different logic depending of Chip
     *
     * @return int
     */
    public int getLayoutRes() {
        return 0;
    }

    /**
     * Return the Chip background res id
     * Override it if you want to have different logic depending of Chip
     *
     * @return int
     */
    public int getBackgroundRes() {
        return 0;
    }

    /**
     * Return the Chip background color res id
     * Override it if you want to have different logic depending of Chip
     *
     * @return int
     */
    public int getBackgroundColorRes() {
        return 0;
    }

    /**
     * Return the Chip color res id for selected state
     * Override it if you want to have different logic depending of Chip
     *
     * @return int
     */
    public int getBackgroundColorSelectedRes() {
        return 0;
    }
}
