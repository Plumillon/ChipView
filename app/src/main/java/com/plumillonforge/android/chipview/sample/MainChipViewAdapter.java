package com.plumillonforge.android.chipview.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipViewAdapter;

/**
 * Created by Plumillon Forge on 09/10/15.
 */
public class MainChipViewAdapter extends ChipViewAdapter {
    public MainChipViewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes(int position) {
        Tag tag = (Tag) getChip(position);

        switch (tag.getType()) {
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
    public int getBackgroundColor(int position) {
        Tag tag = (Tag) getChip(position);

        switch (tag.getType()) {
            default:
                return 0;

            case 1:
            case 4:
                return getColor(R.color.blue);

            case 2:
            case 5:
                return getColor(R.color.purple);

            case 3:
                return getColor(R.color.teal);
        }
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public int getBackgroundRes(int position) {
        return 0;
    }

    @Override
    public void onLayout(View view, int position) {
        Tag tag = (Tag) getChip(position);

        if (tag.getType() == 2)
            ((TextView) view.findViewById(android.R.id.text1)).setTextColor(getColor(R.color.blue));
    }
}
