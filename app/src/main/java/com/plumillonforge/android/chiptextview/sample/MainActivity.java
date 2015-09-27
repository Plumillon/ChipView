package com.plumillonforge.android.chiptextview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.plumillonforge.android.chiptextview.Chip;
import com.plumillonforge.android.chiptextview.ChipTextView;
import com.plumillonforge.android.chiptextview.OnChipClickListener;
import com.plumillonforge.android.chiptextview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plumillon Forge on 25/09/15.
 */
public class MainActivity extends AppCompatActivity implements OnChipClickListener {
    private List<Chip> mTagList1;
    private List<Chip> mTagList2;

    private ChipTextView mTextChipDefault;
    private ChipTextView mTextChipLayout;
    private ChipTextView mTextChipOverride;

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

        // Default ChipTextView
        mTextChipDefault = (ChipTextView) findViewById(R.id.text_chip_default);
        mTextChipDefault.setChipList(mTagList1);

        mTextChipLayout = (ChipTextView) findViewById(R.id.text_chip_layout);
        mTextChipLayout.setChipLayoutRes(R.layout.chip_close);
        mTextChipLayout.setBackgroundColorRes(R.color.green);
        mTextChipLayout.setChipList(mTagList1);
        mTextChipLayout.setOnChipClickListener(this);

        mTextChipOverride = (ChipTextView) findViewById(R.id.text_chip_override);
        mTextChipOverride.setChipList(mTagList2);
    }

    @Override
    public void onChipClick(Chip chip) {
        mTextChipLayout.remove(chip);
    }
}
