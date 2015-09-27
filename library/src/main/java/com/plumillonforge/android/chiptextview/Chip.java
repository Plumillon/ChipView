package com.plumillonforge.android.chiptextview;

import android.os.Parcelable;

/**
 * Created by Plumillon Forge on 17/09/15.
 */
public abstract class Chip implements Parcelable {
    /**
     * Return the chip_close text
     *
     * @return String
     */
    public abstract String getText();

    /**
     * Return the chip_close layout res id
     * Override it if you want to have different logic depending of chip_close
     *
     * @return int
     */
    public int getLayoutRes() {
        return 0;
    }

    /**
     * Return the chip_close color res id
     * Override it if you want to have different logic depending of chip_close
     *
     * @return int
     */
    public int getBackgroundColorRes() {
        return 0;
    }
}
