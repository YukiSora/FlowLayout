package moe.yukisora.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
    private static final int DEFAULT_ITEM_SPACING = 0;
    private static final int DEFAULT_ROW_SPACING = 0;
    private Shapes shapes;

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

        shapes = new Shapes();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        int contentLeft = getPaddingLeft();
        int contentTop = getPaddingTop();
        int contentRight = layoutWidth - getPaddingRight();

        int maxWidth = 0;
        int rowWidth = contentLeft;
        int maxHeight = contentTop;
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
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            int childWidth = marginLeft + w + marginRight;
            int childHeight = marginTop + h + marginBottom;
            if (rowWidth + (itemCount > 0 ? itemSpacing : 0) + childWidth > contentRight) {
                maxWidth = Math.max(maxWidth, rowWidth);
                maxHeight += rowHeight + rowSpacing;

                rowWidth = contentLeft;
                rowHeight = 0;
                itemCount = 0;
            }

            // view shape
            int l = rowWidth + (itemCount > 0 ? itemSpacing : 0) + marginLeft;
            int t = maxHeight + marginTop;
            shapes.set(i, l, t, w, h);

            rowWidth += (itemCount > 0 ? itemSpacing : 0) + childWidth;
            rowHeight = Math.max(rowHeight, childHeight);
            itemCount++;
        }
        maxWidth = Math.max(maxWidth, rowWidth);
        maxHeight += rowHeight;
        maxHeight += getPaddingBottom();

        layoutWidth = widthMode == MeasureSpec.EXACTLY ? layoutWidth : maxWidth;
        layoutHeight = heightMode == MeasureSpec.EXACTLY ? layoutHeight : maxHeight;
        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            Shape shape = shapes.get(i);
            child.layout(shape.x, shape.y, shape.x + shape.w, shape.y + shape.h);
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
