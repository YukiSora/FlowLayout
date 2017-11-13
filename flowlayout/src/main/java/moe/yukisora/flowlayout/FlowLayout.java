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
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_JUSTIFY = 3;
    public static final int ALIGN_INHERIT = 4;
    public static final int DEFAULT_ITEM_SPACING = 0;
    public static final int DEFAULT_ROW_SPACING = 0;
    public static final int DIRECTION_LTR = 0;
    public static final int DIRECTION_RTL = 1;
    public static final int VERTICAL_ALIGN_TOP = 0;
    public static final int VERTICAL_ALIGN_MIDDLE = 1;
    public static final int VERTICAL_ALIGN_BOTTOM = 2;
    private List<Integer> itemCounts;
    private List<Integer> rowWidths;
    private List<Integer> rowHeights;
    @Align private int align;
    @Direction private int direction;
    private int itemSpacing;
    @LastRowAlign private int lastRowAlign;
    private int rowSpacing;
    @VerticalAlign private int verticalAlign;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        align = typedArray.getInt(R.styleable.FlowLayout_fl_align, ALIGN_LEFT);
        direction = typedArray.getInt(R.styleable.FlowLayout_fl_direction, DIRECTION_LTR);
        itemSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_fl_item_spacing, DEFAULT_ITEM_SPACING);
        lastRowAlign = typedArray.getInt(R.styleable.FlowLayout_fl_last_row_align, ALIGN_INHERIT);
        rowSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_fl_row_spacing, DEFAULT_ROW_SPACING);
        verticalAlign = typedArray.getInt(R.styleable.FlowLayout_fl_vertical_align, VERTICAL_ALIGN_TOP);
        typedArray.recycle();

        itemCounts = new ArrayList<>();
        rowHeights = new ArrayList<>();
        rowWidths = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        itemCounts.clear();
        rowHeights.clear();
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
                rowHeights.add(rowHeight);

                rowWidth = 0;
                rowHeight = 0;
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
        rowHeights.add(rowHeight);

        layoutWidth = widthMode == MeasureSpec.EXACTLY ? layoutWidth : maxWidth;
        layoutHeight = heightMode == MeasureSpec.EXACTLY ? layoutHeight : maxHeight;
        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutWidth = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int currentLayoutHeight = paddingTop;
        int rowCount = itemCounts.size();
        int viewCount = 0;
        for (int i = 0; i < rowCount; i++) {
            int itemCount = itemCounts.get(i);
            int rowWidth = rowWidths.get(i);
            int rowHeight = rowHeights.get(i);
            int currentRowWidth = 0;

            // align
            int currentRowAlign = i == rowCount - 1 && lastRowAlign != ALIGN_INHERIT ? lastRowAlign : align;
            int leftSpacing = 0;
            if (currentRowAlign == ALIGN_RIGHT) {
                leftSpacing = layoutWidth - rowWidth;
            }
            else if (currentRowAlign == ALIGN_CENTER) {
                leftSpacing = (layoutWidth - rowWidth) / 2;
            }
            int intervalSpacing = 0;
            if (currentRowAlign == ALIGN_JUSTIFY && itemCount != 1) {
                intervalSpacing = (layoutWidth - rowWidth) / (itemCount - 1);
            }

            for (int j = 0; j < itemCount; j++) {
                View child = getChildAt(viewCount + (direction == DIRECTION_LTR ? j : itemCount - j - 1));

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
                    MarginLayoutParams marginLayoutParams = (MarginLayoutParams)layoutParams;
                    marginLeft = marginLayoutParams.leftMargin;
                    marginTop = marginLayoutParams.topMargin;
                    marginRight = marginLayoutParams.rightMargin;
                    marginBottom = marginLayoutParams.bottomMargin;
                }

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                // vertical align
                int topSpacing = 0;
                if (verticalAlign == VERTICAL_ALIGN_MIDDLE) {
                    topSpacing = (rowHeight - (marginTop + height + marginBottom)) / 2;
                }
                else if (verticalAlign == VERTICAL_ALIGN_BOTTOM) {
                    topSpacing = rowHeight - (marginTop + height + marginBottom);
                }

                // measure location
                int left = paddingLeft + leftSpacing + currentRowWidth + (j > 0 ? itemSpacing + intervalSpacing : 0) + marginLeft;
                int top = currentLayoutHeight + marginTop + topSpacing;
                child.layout(left, top, left + width, top + height);

                currentRowWidth += (j > 0 ? itemSpacing + intervalSpacing : 0) + marginLeft + width + marginRight;
            }

            viewCount += itemCount;
            currentLayoutHeight += rowHeight + rowSpacing;
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

    @Align
    public int getAlign() {
        return align;
    }

    public void setAlign(@Align int align) {
        this.align = align;
        requestLayout();
    }

    @Direction
    public int getDirection() {
        return direction;
    }

    public void setDirection(@Direction int direction) {
        this.direction = direction;
        requestLayout();
    }

    public int getItemSpacing() {
        return itemSpacing;
    }

    public void setItemSpacing(int itemSpacing) {
        this.itemSpacing = itemSpacing;
        requestLayout();
    }

    @LastRowAlign
    public int getLastRowAlign() {
        return lastRowAlign;
    }

    public void setLastRowAlign(@LastRowAlign int lastRowAlign) {
        this.lastRowAlign = lastRowAlign;
        requestLayout();
    }

    public int getRowSpacing() {
        return rowSpacing;
    }

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
        requestLayout();
    }

    @VerticalAlign
    public int getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(@VerticalAlign int verticalAlign) {
        this.verticalAlign = verticalAlign;
        requestLayout();
    }

    @IntDef({ALIGN_LEFT, ALIGN_RIGHT, ALIGN_CENTER, ALIGN_JUSTIFY})
    @Retention(RetentionPolicy.SOURCE)
    @interface Align {
    }

    @IntDef({DIRECTION_LTR, DIRECTION_RTL})
    @Retention(RetentionPolicy.SOURCE)
    @interface Direction {
    }

    @IntDef({ALIGN_LEFT, ALIGN_RIGHT, ALIGN_CENTER, ALIGN_JUSTIFY, ALIGN_INHERIT})
    @Retention(RetentionPolicy.SOURCE)
    @interface LastRowAlign {
    }

    @IntDef({VERTICAL_ALIGN_TOP, VERTICAL_ALIGN_MIDDLE, VERTICAL_ALIGN_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface VerticalAlign {
    }
}
