/*
   Copyright 2015 Flavien Norindr (aka Plumillon Forge)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.plumillonforge.android.chipview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Plumillon Forge on 17/09/15.
 */
public class ChipView extends ViewGroup implements Observer {
    private ChipViewAdapter mAdapter;
    private OnChipClickListener mListener;

    // Data
    private List<Integer> mLineHeightList;

    public ChipView(Context context) {
        super(context);
        init(context, null);
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLineHeightList = new ArrayList<>();
        setAdapter(new ChipViewAdapter(context, attrs) {
            @Override
            public int getLayoutRes(int position) {
                return 0;
            }

            @Override
            public int getBackgroundRes(int position) {
                return 0;
            }

            @Override
            public int getBackgroundColor(int position) {
                return 0;
            }

            @Override
            public int getBackgroundColorSelected(int position) {
                return 0;
            }

            @Override
            public void onLayout(View view, int position) {

            }
        });
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mLineHeightList.clear();
        int width = getMeasuredWidth();
        int height = getPaddingTop() + getPaddingBottom();
        int lineHeight = 0;
        int lineWidth = getPaddingLeft();
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            boolean lastChild = (i == childCount - 1);

            if (childView.getVisibility() == GONE) {
                if (lastChild)
                    mLineHeightList.add(lineHeight);

                continue;
            }

            int childWidth = (childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
            int childHeight = (childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
            lineHeight = Math.max(lineHeight, childHeight);

            if (childWidth > width)
                width = childWidth;

            if (lineWidth + childWidth + getPaddingRight() > width) {
                mLineHeightList.add(lineHeight);
                lineWidth = getPaddingLeft() + childWidth;
            } else
                lineWidth += childWidth;

            if (lastChild)
                mLineHeightList.add(lineHeight);
        }

        for (Integer h : mLineHeightList)
            height += h;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mAdapter != null) {
            int width = getMeasuredWidth();
            int lineWidth = getPaddingLeft();
            int childCount = getChildCount();
            int j = 0;
            int lineHeight = (mLineHeightList.size() > 0 ? mLineHeightList.get(j) : 0);
            int childY = getPaddingTop();

            for (int i = 0; i < childCount; i++) {
                final Chip chip = mAdapter.getChipList().get(i);
                View childView = getChildAt(i);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();

                if (childView.getVisibility() == View.GONE)
                    continue;

                int childWidth = (childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                int childHeight = (childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);

                if (childWidth > width)
                    width = childWidth;

                if (lineWidth + childWidth + getPaddingRight() > width) {
                    childY += lineHeight;
                    j++;
                    lineHeight = mLineHeightList.get(j);
                    lineWidth = getPaddingLeft() + childWidth;
                } else
                    lineWidth += childWidth;

                int childX = lineWidth - childWidth;

                childView.layout((childX + layoutParams.leftMargin), (childY + layoutParams.topMargin), (lineWidth - layoutParams.rightMargin), (childY + childHeight - layoutParams.bottomMargin));

                if (mListener != null) {
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onChipClick(chip);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void refresh() {
        if (mAdapter != null) {
            removeAllViews();

            for (int i = 0; i < mAdapter.count(); i++) {
                View view = mAdapter.getView(this, i);

                if (view != null) {
                    if (mListener != null) {
                        view.setClickable(true);
                        view.setFocusable(true);
                    }

                    addView(view);
                }
            }

            invalidate();
        }
    }

    /**
     * Helper to convert dp to px
     *
     * @param dp
     * @return float
     */
    public static float dpToPx(float dp) {
        float px = dp * (Resources.getSystem().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * Helper to convert px to sp
     *
     * @param px
     * @return float
     */
    public static float pxToSp(float px) {
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (px / scaledDensity);
    }

    /**
     * Wrapper to add a Chip
     *
     * @param chip
     */
    public void add(Chip chip) {
        mAdapter.add(chip);
    }

    /**
     * Wrapper to remove a Chip
     *
     * @param chip
     */
    public void remove(Chip chip) {
        mAdapter.remove(chip);
    }

    /**
     * How many Chip do we have
     *
     * @return int
     */
    public int count() {
        return mAdapter.count();
    }

    public List<Chip> getChipList() {
        return mAdapter.getChipList();
    }

    public void setChipList(List<Chip> chipList) {
        mAdapter.setChipList(chipList);
    }

    public ChipViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ChipViewAdapter adapter) {
        mAdapter = adapter;
        mAdapter.deleteObservers();
        mAdapter.addObserver(this);
        refresh();
    }

    /**
     * Set overall Chip background color by res id
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getBackgroundRes
     *
     * @param backgroundRes
     */
    public void setChipBackgroundRes(@DrawableRes int backgroundRes) {
        mAdapter.setChipBackgroundRes(backgroundRes);
    }

    public int getChipLayoutRes() {
        return mAdapter.getChipLayoutRes();
    }

    /**
     * Set overall Chip layout by res id
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getLayoutRes
     */
    public void setChipLayoutRes(@LayoutRes int chipRes) {
        mAdapter.setChipLayoutRes(chipRes);
    }

    /**
     * Set Chip onClick listener
     *
     * @param listener
     */
    public void setOnChipClickListener(OnChipClickListener listener) {
        mListener = listener;
    }

    public boolean isToleratingDuplicate() {
        return mAdapter.isToleratingDuplicate();
    }

    /**
     * Set whether or not ChipTextView tolerate duplicate Chip
     *
     * @param toleratingDuplicate
     */
    public void setToleratingDuplicate(boolean toleratingDuplicate) {
        mAdapter.setToleratingDuplicate(toleratingDuplicate);
    }

    public boolean hasBackground() {
        return mAdapter.hasBackground();
    }

    public void setHasBackground(boolean hasBackground) {
        mAdapter.setHasBackground(hasBackground);
    }

    public int getChipSpacing() {
        return mAdapter.getChipSpacing();
    }

    public void setChipSpacing(int chipSpacing) {
        mAdapter.setChipSpacing(chipSpacing);
    }

    public int getLineSpacing() {
        return mAdapter.getLineSpacing();
    }

    public void setLineSpacing(int lineSpacing) {
        mAdapter.setLineSpacing(lineSpacing);
    }

    public int getChipPadding() {
        return mAdapter.getChipPadding();
    }

    public void setChipPadding(int chipPadding) {
        mAdapter.setChipPadding(chipPadding);
    }

    public int getChipSidePadding() {
        return mAdapter.getChipSidePadding();
    }

    public void setChipSidePadding(int chipSidePadding) {
        mAdapter.setChipSidePadding(chipSidePadding);
    }

    public int getChipCornerRadius() {
        return mAdapter.getChipCornerRadius();
    }

    public void setChipCornerRadius(int chipCornerRadius) {
        mAdapter.setChipCornerRadius(chipCornerRadius);
    }

    public int getChipBackgroundColor() {
        return mAdapter.getChipBackgroundColor();
    }

    public void setChipBackgroundColor(@ColorInt int chipBackgroundColor) {
        mAdapter.setChipBackgroundColor(chipBackgroundColor);
    }

    public int getChipBackgroundColorSelected() {
        return mAdapter.getChipBackgroundColorSelected();
    }

    public void setChipBackgroundColorSelected(@ColorInt int chipBackgroundColorSelected) {
        mAdapter.setChipBackgroundColorSelected(chipBackgroundColorSelected);
    }

    public int getChipTextSize() {
        return mAdapter.getChipTextSize();
    }

    public void setChipTextSize(int chipTextSize) {
        mAdapter.setChipTextSize(chipTextSize);
    }

    @Override
    public void update(Observable observable, Object data) {
        refresh();
    }
}
