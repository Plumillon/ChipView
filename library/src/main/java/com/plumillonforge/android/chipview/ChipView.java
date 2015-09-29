package com.plumillonforge.android.chipview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plumillonforge.android.chipview.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plumillon Forge on 17/09/15.
 */
public class ChipView extends ViewGroup {
    private int mChipSpacing;
    private int mLineSpacing;
    private int mChipPadding;
    private int mChipSidePadding;
    private List<Chip> mChipList;
    private int mChipRes;
    private boolean mToleratingDuplicate = false;
    private int mChipBackgroundColorRes;
    private int mChipBackgroundColorSelectedRes;
    private int mChipBackgroundRes;
    private OnChipClickListener mListener;
    private boolean mHasBackground = true;

    // Data
    private List<Integer> mLineHeightList;

    public ChipView(Context context) {
        super(context);
        init();
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mChipList = new ArrayList<>();
        mLineHeightList = new ArrayList<>();
        mChipSpacing = (int) getResources().getDimension(R.dimen.chip_spacing);
        mLineSpacing = (int) getResources().getDimension(R.dimen.chip_line_spacing);
        mChipPadding = (int) getResources().getDimension(R.dimen.chip_padding);
        mChipSidePadding = (int) getResources().getDimension(R.dimen.chip_side_padding);
        mChipBackgroundColorRes = R.color.chip_background;
        mChipBackgroundColorSelectedRes = R.color.chip_background_selected;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mLineHeightList.clear();
        int width = getMeasuredWidth();
        int height = getPaddingTop() + getPaddingBottom();
        int lineHeight = 0;
        int lineWidth = 0;
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

            if (lineWidth + childWidth > width) {
                mLineHeightList.add(lineHeight);
                lineWidth = childWidth;
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
        int width = getMeasuredWidth();
        int lineWidth = getPaddingLeft();
        int childCount = getChildCount();
        int j = 0;
        int lineHeight = (mLineHeightList.size() > 0 ? mLineHeightList.get(j) : 0);
        int childY = getPaddingTop();

        for (int i = 0; i < childCount; i++) {
            final Chip chip = mChipList.get(i);
            View childView = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            if (childView.getVisibility() == GONE)
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
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onChipClick(chip);
                    }
                });
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
        removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Chip chip : mChipList) {
            View view = null;
            int chipLayoutRes = (chip.getLayoutRes() != 0 ? chip.getLayoutRes() : getChipLayoutRes());
            Drawable chipBackground = generateBackgroundSelector(chip);

            if (chipLayoutRes == 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, mChipSpacing, mLineSpacing);
                view = new LinearLayout(getContext());
                view.setLayoutParams(layoutParams);
                ((LinearLayout) view).setOrientation(LinearLayout.HORIZONTAL);
                ((LinearLayout) view).setGravity(Gravity.CENTER_VERTICAL);
                view.setPadding(mChipSidePadding, mChipPadding, mChipSidePadding, mChipPadding);

                TextView text = new TextView(getContext());
                text.setId(android.R.id.text1);
                ((LinearLayout) view).addView(text);

                if (mListener != null) {
                    view.setClickable(true);
                    view.setFocusable(true);
                }
            } else {
                view = inflater.inflate(chipLayoutRes, this, false);
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, (layoutParams.rightMargin > 0 ? layoutParams.rightMargin : mChipSpacing), (layoutParams.bottomMargin > 0 ? layoutParams.bottomMargin : mLineSpacing));
            }

            if (view != null) {
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                View content = view.findViewById(android.R.id.content);

                if (text != null) {
                    text.setText(chip.getText());
                    text.setGravity(Gravity.CENTER);
                }

                if (mHasBackground) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        if (content != null)
                            content.setBackgroundDrawable(chipBackground);
                        else
                            view.setBackgroundDrawable(chipBackground);
                    } else {
                        if (content != null)
                            content.setBackground(chipBackground);
                        else
                            view.setBackground(chipBackground);
                    }
                }

                addView(view);
            }
        }

        invalidate();
    }

    private Drawable generateBackgroundSelector(Chip chip) {
        if (chip.getBackgroundRes() != 0)
            return getResources().getDrawable(chip.getBackgroundRes());
        else if (mChipBackgroundRes != 0) {
            return getResources().getDrawable(mChipBackgroundRes);
        }

        int backgroundColorRes = (chip.getBackgroundColorRes() != 0 ? chip.getBackgroundColorRes() : mChipBackgroundColorRes);
        int backgroundColorSelectedRes = (chip.getBackgroundColorSelectedRes() != 0 ? chip.getBackgroundColorSelectedRes() : mChipBackgroundColorSelectedRes);

        // Default state
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(100);
        background.setColor(getResources().getColor(backgroundColorRes));

        // Selected state
        GradientDrawable selectedBackground = selectedBackground = new GradientDrawable();
        selectedBackground.setShape(GradientDrawable.RECTANGLE);
        selectedBackground.setCornerRadius(100);
        selectedBackground.setColor(getResources().getColor(backgroundColorSelectedRes));


        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, selectedBackground);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, selectedBackground);
        stateListDrawable.addState(StateSet.WILD_CARD, background);

        return stateListDrawable;
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
        if (!mChipList.contains(chip) || mToleratingDuplicate) {
            mChipList.add(chip);
            refresh();
        }
    }

    /**
     * Wrapper to remove a Chip
     *
     * @param chip
     */
    public void remove(Chip chip) {
        mChipList.remove(chip);
        refresh();
    }

    /**
     * How many Chip do we have
     *
     * @return int
     */
    public int count() {
        return mChipList.size();
    }

    public List<Chip> getChipList() {
        return mChipList;
    }

    public void setChipList(List<Chip> chipList) {
        mChipList = chipList;
        refresh();
    }

    /**
     * Set overall Chip background color by res id
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getBackgroundRes
     *
     * @param backgroundRes
     */
    public void setChipBackgroundRes(@DrawableRes int backgroundRes) {
        mChipBackgroundRes = backgroundRes;
    }

    public int getChipLayoutRes() {
        return mChipRes;
    }

    /**
     * Set overall Chip layout by res id
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getLayoutRes
     */
    public void setChipLayoutRes(@LayoutRes int chipRes) {
        mChipRes = chipRes;
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
        return mToleratingDuplicate;
    }

    /**
     * Set whether or not ChipTextView tolerate duplicate Chip
     *
     * @param toleratingDuplicate
     */
    public void setToleratingDuplicate(boolean toleratingDuplicate) {
        mToleratingDuplicate = toleratingDuplicate;
    }

    public boolean hasBackground() {
        return mHasBackground;
    }

    public void setHasBackground(boolean hasBackground) {
        mHasBackground = hasBackground;
    }

    public int getChipSpacing() {
        return mChipSpacing;
    }

    public void setChipSpacing(int chipSpacing) {
        mChipSpacing = chipSpacing;
    }

    public int getLineSpacing() {
        return mLineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        mLineSpacing = lineSpacing;
    }
    
    public int getChipPadding() {
        return mChipPadding;
    }

    public void setChipPadding(int chipPadding) {
        mChipPadding = chipPadding;
    }

    public int getChipSidePadding() {
        return mChipSidePadding;
    }

    public void setChipSidePadding(int chipSidePadding) {
        mChipSidePadding = chipSidePadding;
    }

    public int getChipBackgroundColorRes() {
        return mChipBackgroundColorRes;
    }

    public void setChipBackgroundColorRes(int chipBackgroundColorRes) {
        mChipBackgroundColorRes = chipBackgroundColorRes;
    }

    public int getChipBackgroundColorSelectedRes() {
        return mChipBackgroundColorSelectedRes;
    }

    public void setChipBackgroundColorSelectedRes(int chipBackgroundColorSelectedRes) {
        mChipBackgroundColorSelectedRes = chipBackgroundColorSelectedRes;
    }
}
