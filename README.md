# FlowLayout
A FlowLayout for Android.

## Usage

```
<moe.yukisora.flowlayout.FlowLayout
    android:id="@+id/flowLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:text="Java"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
    
    <TextView
        android:text="C"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</moe.yukisora.flowlayout.FlowLayout>
```

## Attributes

### fl_align

|Value|Demo|
|-|-|
|left|<img src="images/align_left.png" alt="align_left" width="300px;"/>|
|right|<img src="images/align_right.png" alt="align_right" width="300px;"/>|
|center|<img src="images/align_center.png" alt="align_center" width="300px;"/>|
|justify|<img src="images/align_justify.png" alt="align_justify" width="300px;"/>|

### fl_last_row_align

|Value|Demo|
|-|-|
|left|<img src="images/last_row_left.png" alt="last_row_left" width="300px;"/>|
|right|<img src="images/last_row_right.png" alt="last_row_right" width="300px;"/>|
|center|<img src="images/last_row_center.png" alt="last_row_center" width="300px;"/>|
|justify|<img src="images/last_row_justify.png" alt="last_row_justify" width="300px;"/>|
|inherit|<img src="images/last_row_inherit.png" alt="last_row_inherit" width="300px;"/>|

### fl_vertical_align

|Value|Demo|
|-|-|
|top|<img src="images/vertical_align_top.png" alt="vertical_align_top" width="300px;"/>|
|middle|<img src="images/vertical_align_middle.png" alt="vertical_align_middle" width="300px;"/>|
|bottom|<img src="images/vertical_align_bottom.png" alt="vertical_align_bottom" width="300px;"/>|

### fl_direction

|Value|Demo|
|-|-|
|ltr|<img src="images/ltr.png" alt="ltr" width="300px;"/>|
|rtl|<img src="images/rtl.png" alt="rtl" width="300px;"/>|

### Attribute list

|Attribute|Value|
|-|-|
|fl_align|**`left`**/`right`/`center`/`justify`|
|fl_direction|**`ltr`**/`rtl`|
|fl_item_spacing|**`0dp`**/dimension|
|fl_last_row_align|`left`/`right`/`center`/`justify`/**`inherit`**|
|fl_max_items_per_row|**`undefined`**/int|
|fl_row_spacing|**`0dp`**/dimension|
|fl_vertical_align|**`top`**/`middle`/`bottom`|

## Licence

```
MIT License

Copyright (c) 2017 Yuki Sora

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
