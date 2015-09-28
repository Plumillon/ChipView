# ChipTextView
ChipTextView enables you to easily create [Chip](http://www.google.fr/design/spec/components/chips.html) like list on a TextView with optional click listener on each on chip.

ChipTextView is highly customizable to the point you can control every Chip layout and background color individually.

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

### Click listener
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

#### Setting android:textSize
Each `Chip` will reflect the `android:textSize` ChipTextView property

#### Changing the background color
You can change all the `Chip` background in one line with differents methods :
```
chipTextView.setBackgroundColorRes(R.color.green);
chipTextView.setBackgroundColorHex("#4CAF50");
```
Or remove it completely with `setHasDefaultBackground`

#### Changing the Chip layout
If you want your own layout for all `Chip`, you can specify it :
```
chipTextView.setChipLayoutRes(R.layout.chip_close);
```
**A `TextView` with `android:id="@android:id/text1"` is mandatory in the layout**

The background is set on the layout root `View` by default, if you need to place the background on a specific `View` on the layout, please provide a `android:id="@android:id/content"`.
<br />
<img src="https://raw.githubusercontent.com/Plumillon/ChipTextView/master/readme/control.png" height="60px" />

#### Controlling layout and  background color individually
If you need to customize your `Chip` individually, you can do so by overriding the `Chip` `getLayoutRes()` and `getBackgroundColorRes()` methods :
```
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
```
The `Chip` falls back to default behaviour if you return 0.
<br />
<img src="https://raw.githubusercontent.com/Plumillon/ChipTextView/master/readme/custom.png" height="60px" />

## Why ?
ChipTextView is a personal need for one of my project, I decided to develop and distribute it because I couldn't find anything which matched what I was seeking.

### How this work
ChipTextView simply generate en display a `Bitmap` for each `Chip` via an `ImageSpan`. Since `ImageSpan` can't handle click event, there are also a overlying `ClickSpan` for each `Chip`.

### Improve it !
Each suggestion and correction is welcome, do not hesitate !

## Licensing
ChipTextView is published with [Apache Licence](http://www.apache.org/licenses/LICENSE-2.0)

## How tenacious
You read it to the extra end ! Congratulations, here is a [potato](https://raw.githubusercontent.com/Plumillon/ChipTextView/master/readme/potato.jpg) :)
