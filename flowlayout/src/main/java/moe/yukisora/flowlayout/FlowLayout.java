package moe.yukisora.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    public static final int DEFAULT_ITEM_SPACING = 0;
    public static final int DEFAULT_ROW_SPACING = 0;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_JUSTIFY = 3;
    private List<Integer> itemCounts;
    private List<Integer> rowTops;
    private List<Integer> rowWidths;
    private int itemSpacing;
    private int rowSpacing;
    private int align;
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        itemSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_fl_item_spacing, DEFAULT_ITEM_SPACING);
        rowSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_fl_row_spacing, DEFAULT_ROW_SPACING);
        align = typedArray.getInt(R.styleable.FlowLayout_fl_align, ALIGN_LEFT);

        itemCounts = new ArrayList<>();
        rowTops = new ArrayList<>();
        rowWidths = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        itemCounts.clear();
        rowTops.clear();
        rowWidths.clear();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int maxWidth = 0;
        int rowWidth = 0;
        int maxHeight = paddingTop;
        int rowHeight = 0;
        rowTops.add(maxHeight);
        int totalCount = getChildCount();
        int itemCount = 0;
        for (int i = 0; i < totalCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            // margin
            int marginLeft = 0;
            int marginTop = 0;
            int marginRight = 0;
            int marginBottom = 0;
            LayoutParams layoutParams = child.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                MarginLayoutParams marginLayoutParams = (MarginLayoutParams)layoutParams;
                marginLeft = marginLayoutParams.leftMargin;
                marginTop = marginLayoutParams.topMargin;
                marginRight = marginLayoutParams.rightMargin;
                marginBottom = marginLayoutParams.bottomMargin;
            }
            else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            // wrap
            int childWidth = marginLeft + child.getMeasuredWidth() + marginRight;
            int childHeight = marginTop + child.getMeasuredHeight() + marginBottom;
            if (paddingLeft + rowWidth + (itemCount > 0 ? itemSpacing : 0) + childWidth + paddingRight > layoutWidth) {
                maxWidth = Math.max(maxWidth, rowWidth);
                maxHeight += rowHeight + rowSpacing;
                itemCounts.add(itemCount);
                rowWidths.add(rowWidth);

                rowWidth = 0;
                rowHeight = 0;
                rowTops.add(maxHeight);
                itemCount = 0;
            }

            rowWidth += (itemCount > 0 ? itemSpacing : 0) + childWidth;
            rowHeight = Math.max(rowHeight, childHeight);
            itemCount++;
        }
        maxWidth = Math.max(maxWidth, rowWidth);
        maxHeight += rowHeight + paddingBottom;
        itemCounts.add(itemCount);
        rowWidths.add(rowWidth);

        layoutWidth = widthMode == MeasureSpec.EXACTLY ? layoutWidth : maxWidth;
        layoutHeight = heightMode == MeasureSpec.EXACTLY ? layoutHeight : maxHeight;
        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutWidth = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int rowCount = itemCounts.size();
        int viewCount = 0;
        for (int i = 0; i < rowCount; i++) {
            int itemCount = itemCounts.get(i);
            int rowTop = rowTops.get(i);
            int rowWidth = rowWidths.get(i);
            int currentRowWidth = 0;

            // align
            int leftSpacing = 0;
            if (align == ALIGN_RIGHT) {
                leftSpacing = layoutWidth - rowWidth;
            }
            else if (align == ALIGN_CENTER) {
                leftSpacing = (layoutWidth - rowWidth) / 2;
            }
            int intervalSpacing = 0;
            if (align == ALIGN_JUSTIFY && itemCount != 1) {
                intervalSpacing = (layoutWidth - rowWidth) / (itemCount - 1);
            }

            for (int j = 0; j < itemCount; j++, viewCount++) {
                View child = getChildAt(viewCount);

                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                // margin
                int marginLeft = 0;
                int marginTop = 0;
                int marginRight = 0;
                LayoutParams layoutParams = child.getLayoutParams();
                if (layoutParams instanceof MarginLayoutParams) {
                    MarginLayoutParams marginLayoutParams = (MarginLayoutParams)layoutParams;
                    marginLeft = marginLayoutParams.leftMargin;
                    marginTop = marginLayoutParams.topMargin;
                    marginRight = marginLayoutParams.rightMargin;
                }

                // measure location
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int left = paddingLeft + leftSpacing + currentRowWidth + (j > 0 ? itemSpacing + intervalSpacing : 0) + marginLeft;
                int top = rowTop + marginTop;
                child.layout(left, top, left + width, top + height);

                currentRowWidth += (j > 0 ? itemSpacing + intervalSpacing : 0) + marginLeft + width + marginRight;
            }
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public int getItemSpacing() {
        return itemSpacing;
    }

    public void setItemSpacing(int itemSpacing) {
        this.itemSpacing = itemSpacing;
        requestLayout();
    }

    public int getRowSpacing() {
        return rowSpacing;
    }

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
        requestLayout();
    }

    @Align
    public int getAlign() {
        return align;
    }

    public void setAlign(@Align int align) {
        this.align = align;
        requestLayout();
    }

    @IntDef({ALIGN_LEFT, ALIGN_RIGHT, ALIGN_CENTER, ALIGN_JUSTIFY})
    @Retention(RetentionPolicy.SOURCE)
    @interface Align {
    }
}
