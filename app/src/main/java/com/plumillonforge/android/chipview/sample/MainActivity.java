package com.plumillonforge.android.chipview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.plumillonforge.android.chipview.OnChipClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plumillon Forge on 25/09/15.
 */
public class MainActivity extends AppCompatActivity implements OnChipClickListener {
    private List<Chip> mTagList1;
    private List<Chip> mTagList2;

    private ChipView mTextChipDefault;
    private ChipView mTextChipAttrs;
    private ChipView mTextChipLayout;
    private ChipView mTextChipOverride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagList1 = new ArrayList<>();
        mTagList1.add(new Tag("Lorem"));
        mTagList1.add(new Tag("Ipsum dolor"));
        mTagList1.add(new Tag("Sit amet"));
        mTagList1.add(new Tag("Consectetur"));
        mTagList1.add(new Tag("adipiscing elit"));

        mTagList2 = new ArrayList<>();
        mTagList2.add(new Tag("Lorem", 1));
        mTagList2.add(new Tag("Ipsum dolor", 2));
        mTagList2.add(new Tag("Sit amet", 3));
        mTagList2.add(new Tag("Consectetur", 4));
        mTagList2.add(new Tag("adipiscing elit", 5));

        // Adapter
        ChipViewAdapter adapterLayout = new MainChipViewAdapter(this);
        ChipViewAdapter adapterOverride = new MainChipViewAdapter(this);

        // Default ChipTextView
        mTextChipDefault = (ChipView) findViewById(R.id.text_chip_default);
        mTextChipDefault.setChipList(mTagList1);

        // Attrs ChipTextView
        mTextChipAttrs = (ChipView) findViewById(R.id.text_chip_attrs);
        mTextChipAttrs.setChipList(mTagList1);
        mTextChipAttrs.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {

            }
        });

        // Custom layout and background colors
        mTextChipLayout = (ChipView) findViewById(R.id.text_chip_layout);
        mTextChipLayout.setAdapter(adapterLayout);
        mTextChipLayout.setChipLayoutRes(R.layout.chip_close);
        mTextChipLayout.setChipBackgroundColor(getResources().getColor(R.color.light_green));
        mTextChipLayout.setChipBackgroundColorSelected(getResources().getColor(R.color.green));
        mTextChipLayout.setChipList(mTagList1);
        mTextChipLayout.setOnChipClickListener(this);

        // Chip override
        mTextChipOverride = (ChipView) findViewById(R.id.text_chip_override);
        mTextChipOverride.setAdapter(adapterOverride);
        mTextChipOverride.setChipList(mTagList2);
    }

    @Override
    public void onChipClick(Chip chip) {
        mTextChipLayout.remove(chip);
    }
}
