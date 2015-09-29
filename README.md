# ChipView
ChipView enables you to easily create [Chip](http://www.google.fr/design/spec/components/chips.html) list with optional click listener on each `Chip`.

ChipView is highly customizable to the point you can control every Chip layout and background colors (normal and selected) individually.

## How to use
### Default mode
Just add ChipView to your layout (or programmatically) :
```
<com.plumillonforge.android.chipview.ChipView
        android:id="@+id/chipview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

Then prepare your data, each item on the `ChipView` must implements the `Chip` interface, just to know what String to display (via the `getText()` method) :
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
Now you're free to go by adding `Chip` to your `ChipView` :
```
List<Chip> chipList = new ArrayList<>();
chipList.add(new Tag("Lorem"));
chipList.add(new Tag("Ipsum dolor"));
chipList.add(new Tag("Sit amet"));
chipList.add(new Tag("Consectetur"));
chipList.add(new Tag("adipiscing elit"));
ChipView chipDefault = (ChipView) findViewById(R.id.chipview);
chipDefault.setChipList(chipList);
```
ChipView will be displayed with default settings :
<br />
<img src="https://raw.githubusercontent.com/Plumillon/ChipView/master/readme/default.png" height="60px" />

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

#### Changing the background colors
You can change all the `Chip` background in one line :
```
chipView.setBackgroundColorRes(R.color.light_green);
```

If you got a click listener and want another color when clicked :
```
chipView.setChipBackgroundColorSelectedRes(R.color.green);
```

Or remove the background completely with `setHasBackground`

#### Changing the Chip spacing, line spacing and Chip padding
* You can control the space between `Chip` with `setChipSpacing` (default is 4dp)
* You can control the space between each line of `Chip` with `setChipLineSpacing` (default is 4dp)
* You can control the top and bottom `Chip` padding with `setChipPadding` (default is 2dp)
* You can control the left and right `Chip` padding with `setChipSidePadding` (default is 6dp)

#### Changing all the Chip layout
If you want your own layout for all `Chip`, you can specify it :
```
chipView.setChipLayoutRes(R.layout.chip_close);
```
**A `TextView` with `android:id="@android:id/text1"` is mandatory in the layout**

The background is set on the layout root `View` by default, if you need to place the background on a specific `View` on the layout, please provide a `android:id="@android:id/content"`.
<br />
<img src="https://raw.githubusercontent.com/Plumillon/ChipView/master/readme/control.png" height="60px" />

##### Chip spacing and line spacing rules
* If the layout doesn't got a right margin, we fall back to `ChipView` Chip spacing
* If the layout doesn't got a bottom margin, we fall back to `ChipView` Chip line spacing

#### Controlling layout and  background colors individually
If you need to customize your `Chip` individually, you can do so by overriding the `Chip` `getLayoutRes()`, `getBackgroundColorRes()` and `getBackgroundColorSelectedRes()` methods :
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
<img src="https://raw.githubusercontent.com/Plumillon/ChipView/master/readme/custom.png" height="60px" />
<br />
<br />
The `Chip` falls back to `ChipView` overall behaviour if you return 0.

## Why ?
ChipView is a personal need for one of my project, I decided to develop and distribute it because I couldn't find anything which matched what I was seeking.

### How does this work
ChipView extends `ViewGroup` and will contain each `Chip` as his child view.

### Improve it !
Each suggestion and correction is welcome, do not hesitate !

## Licensing
ChipView is published with [Apache Licence](http://www.apache.org/licenses/LICENSE-2.0)

## How tenacious
You read it to the extra end ! Congratulations, here is a [potato](https://raw.githubusercontent.com/Plumillon/ChipView/master/readme/potato.jpg) :)
