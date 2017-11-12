package moe.yukisora.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private static final int DEFAULT_ITEM_SPACING = 0;
    private static final int DEFAULT_ROW_SPACING = 0;
    private List<Integer> itemCounts;
    private List<Integer> rowTops;

    private int itemSpacing;
    private int rowSpacing;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        itemSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_fl_item_spacing, DEFAULT_ITEM_SPACING);
        rowSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_fl_row_spacing, DEFAULT_ROW_SPACING);

        itemCounts = new ArrayList<>();
        rowTops = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        itemCounts.clear();
        rowTops.clear();

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
            int rowTop = rowTops.get(i);
            int rowWidth = 0;

            int itemCount = itemCounts.get(i);
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
                int left = paddingLeft + rowWidth + (j > 0 ? itemSpacing : 0) + marginLeft;
                int top = rowTop + marginTop;
                child.layout(left, top, left + width, top + height);

                rowWidth += (j > 0 ? itemSpacing : 0) + marginLeft + width + marginRight;
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
    }

    public int getRowSpacing() {
        return rowSpacing;
    }

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
    }
}
