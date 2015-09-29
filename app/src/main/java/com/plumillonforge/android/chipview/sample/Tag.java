package com.plumillonforge.android.chipview.sample;

import android.os.Parcel;

import com.plumillonforge.android.chipview.Chip;

/**
 * Created by Plumillon Forge on 25/09/15.
 */
public class Tag extends Chip {
    private String mName;
    private int mType = 0;

    public Tag(String name, int type) {
        this(name);
        mType = type;
    }

    public Tag(String name) {
        mName = name;
    }

    @Override
    public String getText() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int getLayoutRes() {
        switch (mType) {
            default:
            case 2:
            case 4:
                return 0;

            case 1:
            case 5:
                return R.layout.chip_double_close;

            case 3:
                return R.layout.chip_close;
        }
    }

    @Override
    public int getBackgroundColorRes() {
        switch (mType) {
            default:
                return 0;

            case 1:
            case 4:
                return R.color.blue;

            case 2:
            case 5:
                return R.color.purple;

            case 3:
                return R.color.teal;
        }
    }
}
