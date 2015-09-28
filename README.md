# ChipTextView
ChipTextView enables you to easily create [Chip](http://www.google.fr/design/spec/components/chips.html) like list on a TextView with optional click listener on each on chip.

ChipTextView is highly customizable to the point you can control every Chip layout and background color.

## How to use
### Default mode
Just add ChipTextView to your layout (or programmatically) :
```
<com.plumillonforge.android.chiptextview.ChipTextView
        android:id="@+id/text_chip"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

Then prepare your data, each item on the ChipTextView must implements the `Chip` interface, just to know what String to display (via the `getText()` method) :
```
public class Tag extends Chip {
    private String mName;

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
}
```
Now you're free to go by adding `Chip` to your `ChipTextView` :
```
List<Chip> chipList = new ArrayList<>();
chipList.add(new Tag("Lorem"));
chipList.add(new Tag("Ipsum dolor"));
chipList.add(new Tag("Sit amet"));
chipList.add(new Tag("Consectetur"));
chipList.add(new Tag("adipiscing elit"));
ChipTextView chipDefault = (ChipTextView) findViewById(R.id.text_chip);
chipDefault.setChipList(chipList);
```
ChipTextView will be displayed with default settings :
<br />
<img src="https://raw.githubusercontent.com/Plumillon/ChipTextView/master/readme/default.png" height="60px" />

## Click listener
If you want to register a listener when a `Chip` is clicked, implement `OnChipClickListener` :
```
chipDefault.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                // Action here !
            }
        });
```

### More layout control
If the default layout and backgroud color doesn't match your needs, you can override it in differents ways.


#### How this works ?
The library is a TextView wrapper 
