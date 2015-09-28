package com.plumillonforge.android.chiptextview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plumillon Forge on 17/09/15.
 */
public class ChipTextView extends TextView {
    // Chips need to have a separator, otherwise the ClickableSpan failed to get the good clicked span
    private static final String DEFAULT_SPACER = "\u0020";
    private static final String DEFAULT_BACKGROUND = "#CCCCCC";
    private static final int DEFAULT_SIDE_PADDING = 20;
    private static final int DEFAULT_PADDING = 6;
    private List<Chip> mChipList;
    private String mSpacer = DEFAULT_SPACER;
    private int mChipRes;
    private boolean mToleratingDuplicate = false;
    private int mBackgroundColor;
    private OnChipClickListener mListener;
    private boolean mHasDefaultBackground = true;

    public ChipTextView(Context context) {
        super(context);
        init();
    }

    public ChipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChipTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mChipList = new ArrayList<>();
        mBackgroundColor = Color.parseColor(DEFAULT_BACKGROUND);
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);
        setFocusable(false);
        setIncludeFontPadding(false);
    }

    /**
     * Draw the ChipTextView
     * Every Chip is a ImageSpan with a ClickSpan overlay to handle click
     */
    public void refresh() {
        SpannableStringBuilder sb = new SpannableStringBuilder();

        for (final Chip chip : mChipList) {
            String text = chip.getText() + mSpacer;
            Bitmap bitmap = getChipBitmap(chip);

            if (bitmap != null) {
                BitmapDrawable bd = new BitmapDrawable(bitmap);
                bd.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());

                ClickableSpan clickSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null)
                            mListener.onChipClick(chip);
                    }
                };

                sb.append(text);
                sb.setSpan(new ImageSpan(bd, ImageSpan.ALIGN_BASELINE), sb.length() - text.length(), sb.length() - mSpacer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(clickSpan, sb.length() - text.length(), sb.length() - mSpacer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        setText(sb);
    }

    /**
     * Return the Chip as a bitmap
     *
     * @param chip
     * @return Bitmap
     */
    private Bitmap getChipBitmap(Chip chip) {
        View view = null;
        Bitmap viewBitmap = null;
        int chipLayoutRes = (chip.getLayoutRes() != 0 ? chip.getLayoutRes() : getChipLayoutRes());
        int backgroundColor = (chip.getBackgroundColorRes() != 0 ? getResources().getColor(chip.getBackgroundColorRes()) : getBackgroundColor());

        if (chipLayoutRes == 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            view = new LinearLayout(getContext());
            view.setLayoutParams(layoutParams);
            ((LinearLayout) view).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout) view).setGravity(Gravity.CENTER_VERTICAL);
            view.setPadding(DEFAULT_SIDE_PADDING, DEFAULT_PADDING, DEFAULT_SIDE_PADDING, DEFAULT_PADDING);

            TextView text = new TextView(getContext());
            text.setId(android.R.id.text1);
            text.setTextSize(pxToSp(getTextSize()));
            text.setTextColor(getTextColors());
            ((LinearLayout) view).addView(text);
        } else
            view = inflate(getContext(), chipLayoutRes, null);

        if (view != null) {
            TextView text = (TextView) view.findViewById(android.R.id.text1);
            View content = view.findViewById(android.R.id.content);

            if (text != null) {
                text.setText(chip.getText());
                text.setGravity(Gravity.CENTER);
            }

            if (mHasDefaultBackground) {
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.RECTANGLE);
                background.setCornerRadius(100);
                background.setColor(backgroundColor);

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
                    if (content != null)
                        content.setBackgroundDrawable(background);
                    else
                        view.setBackgroundDrawable(background);
                else if (content != null)
                    content.setBackground(background);
                else
                    view.setBackground(background);
            }

            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(spec, spec);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.translate(-view.getScrollX(), -view.getScrollY());
            view.draw(c);
            view.setDrawingCacheEnabled(true);
            Bitmap cacheBmp = view.getDrawingCache();
            viewBitmap = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
            view.destroyDrawingCache();
        }

        return viewBitmap;
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
     * How many chip_close do we have
     *
     * @return int
     */
    public int count() {
        return mChipList.size();
    }

    public String getSpacer() {
        return mSpacer;
    }

    public void setSpacer(String mSpacer) {
        this.mSpacer = mSpacer;
    }

    public List<Chip> getChipList() {
        return mChipList;
    }

    public void setChipList(List<Chip> chipList) {
        mChipList = chipList;
        refresh();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Set overall chip_close background color by res id
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getBackgroundColorRes
     *
     * @param colorRes
     */
    public void setBackgroundColorRes(@ColorRes int colorRes) {
        mBackgroundColor = getResources().getColor(colorRes);
    }

    /**
     * Set overall chip_close background color by hex code
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getBackgroundColorRes
     *
     * @param colorHex
     */
    public void setBackgroundColorHex(String colorHex) {
        mBackgroundColor = Color.parseColor(colorHex);
    }

    public int getChipLayoutRes() {
        return mChipRes;
    }

    /**
     * Set overall chip_close layout by res id
     * Can be fine tuned by overriding @see com.scanners.android.bao.view.ChipTextView.Chip#getLayoutRes
     */
    public void setChipLayoutRes(@LayoutRes int chipRes) {
        mChipRes = chipRes;
    }

    /**
     * Set chip_close onClick listener
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
     * Set whether or not ChipTextView tolerate duplicate chip_close
     *
     * @param toleratingDuplicate
     */
    public void setToleratingDuplicate(boolean toleratingDuplicate) {
        mToleratingDuplicate = toleratingDuplicate;
    }

    public boolean hasDefaultBackground() {
        return mHasDefaultBackground;
    }

    public void setHasDefaultBackground(boolean hasDefaultBackground) {
        mHasDefaultBackground = hasDefaultBackground;
    }
}
