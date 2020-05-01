package ir.protech21.slider.Indicators;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import androidx.viewpager.widget.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.protech21.R.styleable;
import ir.protech21.slider.Tricks.InfinitePagerAdapter;
import ir.protech21.slider.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.Iterator;

public class PagerIndicator extends LinearLayout implements ViewPagerEx.OnPageChangeListener {
    private Context mContext;
    private ViewPagerEx mPager;
    private ImageView mPreviousSelectedIndicator;
    private int mPreviousSelectedPosition;
    private int mUserSetUnSelectedIndicatorResId;
    private int mUserSetSelectedIndicatorResId;
    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;
    private int mItemCount;
    private PagerIndicator.Shape mIndicatorShape;
    private PagerIndicator.IndicatorVisibility mVisibility;
    private int mDefaultSelectedColor;
    private int mDefaultUnSelectedColor;
    private float mDefaultSelectedWidth;
    private float mDefaultSelectedHeight;
    private float mDefaultUnSelectedWidth;
    private float mDefaultUnSelectedHeight;
    private GradientDrawable mUnSelectedGradientDrawable;
    private GradientDrawable mSelectedGradientDrawable;
    private LayerDrawable mSelectedLayerDrawable;
    private LayerDrawable mUnSelectedLayerDrawable;
    private float mPadding_left;
    private float mPadding_right;
    private float mPadding_top;
    private float mPadding_bottom;
    private float mSelectedPadding_Left;
    private float mSelectedPadding_Right;
    private float mSelectedPadding_Top;
    private float mSelectedPadding_Bottom;
    private float mUnSelectedPadding_Left;
    private float mUnSelectedPadding_Right;
    private float mUnSelectedPadding_Top;
    private float mUnSelectedPadding_Bottom;
    private ArrayList<ImageView> mIndicators;
    private DataSetObserver dataChangeObserver;

    public PagerIndicator(Context context) {
        this(context, (AttributeSet)null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mItemCount = 0;
        this.mIndicatorShape = PagerIndicator.Shape.Oval;
        this.mVisibility = PagerIndicator.IndicatorVisibility.Visible;
        this.mIndicators = new ArrayList();
        this.dataChangeObserver = new DataSetObserver() {
            public void onChanged() {
                PagerAdapter adapter = PagerIndicator.this.mPager.getAdapter();
                int count;
                int countx;
                if (adapter instanceof InfinitePagerAdapter) {
                    countx = ((InfinitePagerAdapter)adapter).getRealCount();
                } else {
                    countx = adapter.getCount();
                }

                int i;
                if (countx > PagerIndicator.this.mItemCount) {
                    for(i = 0; i < countx - PagerIndicator.this.mItemCount; ++i) {
                        ImageView indicator = new ImageView(PagerIndicator.this.mContext);
                        indicator.setImageDrawable(PagerIndicator.this.mUnselectedDrawable);
                        indicator.setPadding((int) PagerIndicator.this.mUnSelectedPadding_Left, (int) PagerIndicator.this.mUnSelectedPadding_Top, (int) PagerIndicator.this.mUnSelectedPadding_Right, (int) PagerIndicator.this.mUnSelectedPadding_Bottom);
                        PagerIndicator.this.addView(indicator);
                        PagerIndicator.this.mIndicators.add(indicator);
                    }
                } else if (countx < PagerIndicator.this.mItemCount) {
                    for(i = 0; i < PagerIndicator.this.mItemCount - countx; ++i) {
                        PagerIndicator.this.removeView((View) PagerIndicator.this.mIndicators.get(0));
                        PagerIndicator.this.mIndicators.remove(0);
                    }
                }

                PagerIndicator.this.mItemCount = countx;
                PagerIndicator.this.mPager.setCurrentItem(PagerIndicator.this.mItemCount * 20 + PagerIndicator.this.mPager.getCurrentItem());
            }

            public void onInvalidated() {
                super.onInvalidated();
                PagerIndicator.this.redraw();
            }
        };
        this.mContext = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, styleable.PagerIndicator, 0, 0);
        int visibility = attributes.getInt(styleable.PagerIndicator_visibility, PagerIndicator.IndicatorVisibility.Visible.ordinal());
        PagerIndicator.IndicatorVisibility[] arr = PagerIndicator.IndicatorVisibility.values();
        int len$ = arr.length;

//        int len$;
        for(len$ = 0; len$ < len$; ++len$) {
            PagerIndicator.IndicatorVisibility v = arr[len$];
            if (v.ordinal() == visibility) {
                this.mVisibility = v;
                break;
            }
        }

        int shape = attributes.getInt(styleable.PagerIndicator_shape, PagerIndicator.Shape.Oval.ordinal());
        PagerIndicator.Shape[] arr$ = PagerIndicator.Shape.values();
        len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            PagerIndicator.Shape s = arr$[i$];
            if (s.ordinal() == shape) {
                this.mIndicatorShape = s;
                break;
            }
        }

        this.mUserSetSelectedIndicatorResId = attributes.getResourceId(styleable.PagerIndicator_selected_drawable, 0);
        this.mUserSetUnSelectedIndicatorResId = attributes.getResourceId(styleable.PagerIndicator_unselected_drawable, 0);
        this.mDefaultSelectedColor = attributes.getColor(styleable.PagerIndicator_selected_color, Color.rgb(255, 255, 255));
        this.mDefaultUnSelectedColor = attributes.getColor(styleable.PagerIndicator_unselected_color, Color.argb(33, 255, 255, 255));
        this.mDefaultSelectedWidth = attributes.getDimension(styleable.PagerIndicator_selected_width, (float)((int)this.pxFromDp(6.0F)));
        this.mDefaultSelectedHeight = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_selected_height, (int)this.pxFromDp(6.0F));
        this.mDefaultUnSelectedWidth = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_unselected_width, (int)this.pxFromDp(6.0F));
        this.mDefaultUnSelectedHeight = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_unselected_height, (int)this.pxFromDp(6.0F));
        this.mSelectedGradientDrawable = new GradientDrawable();
        this.mUnSelectedGradientDrawable = new GradientDrawable();
        this.mPadding_left = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_padding_left, (int)this.pxFromDp(3.0F));
        this.mPadding_right = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_padding_right, (int)this.pxFromDp(3.0F));
        this.mPadding_top = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_padding_top, (int)this.pxFromDp(0.0F));
        this.mPadding_bottom = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_padding_bottom, (int)this.pxFromDp(0.0F));
        this.mSelectedPadding_Left = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_selected_padding_left, (int)this.mPadding_left);
        this.mSelectedPadding_Right = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_selected_padding_right, (int)this.mPadding_right);
        this.mSelectedPadding_Top = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_selected_padding_top, (int)this.mPadding_top);
        this.mSelectedPadding_Bottom = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_selected_padding_bottom, (int)this.mPadding_bottom);
        this.mUnSelectedPadding_Left = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_unselected_padding_left, (int)this.mPadding_left);
        this.mUnSelectedPadding_Right = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_unselected_padding_right, (int)this.mPadding_right);
        this.mUnSelectedPadding_Top = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_unselected_padding_top, (int)this.mPadding_top);
        this.mUnSelectedPadding_Bottom = (float)attributes.getDimensionPixelSize(styleable.PagerIndicator_unselected_padding_bottom, (int)this.mPadding_bottom);
        this.mSelectedLayerDrawable = new LayerDrawable(new Drawable[]{this.mSelectedGradientDrawable});
        this.mUnSelectedLayerDrawable = new LayerDrawable(new Drawable[]{this.mUnSelectedGradientDrawable});
        this.setIndicatorStyleResource(this.mUserSetSelectedIndicatorResId, this.mUserSetUnSelectedIndicatorResId);
        this.setDefaultIndicatorShape(this.mIndicatorShape);
        this.setDefaultSelectedIndicatorSize(this.mDefaultSelectedWidth, this.mDefaultSelectedHeight, PagerIndicator.Unit.Px);
        this.setDefaultUnselectedIndicatorSize(this.mDefaultUnSelectedWidth, this.mDefaultUnSelectedHeight, PagerIndicator.Unit.Px);
        this.setDefaultIndicatorColor(this.mDefaultSelectedColor, this.mDefaultUnSelectedColor);
        this.setIndicatorVisibility(this.mVisibility);
        attributes.recycle();
    }

    @SuppressLint("WrongConstant")
    public void setDefaultIndicatorShape(PagerIndicator.Shape shape) {
        if (this.mUserSetSelectedIndicatorResId == 0) {
            if (shape == PagerIndicator.Shape.Oval) {
                this.mSelectedGradientDrawable.setShape(1);
            } else {
                this.mSelectedGradientDrawable.setShape(0);
            }
        }

        if (this.mUserSetUnSelectedIndicatorResId == 0) {
            if (shape == PagerIndicator.Shape.Oval) {
                this.mUnSelectedGradientDrawable.setShape(1);
            } else {
                this.mUnSelectedGradientDrawable.setShape(0);
            }
        }

        this.resetDrawable();
    }

    public void setIndicatorStyleResource(int selected, int unselected) {
        this.mUserSetSelectedIndicatorResId = selected;
        this.mUserSetUnSelectedIndicatorResId = unselected;
        if (selected == 0) {
            this.mSelectedDrawable = this.mSelectedLayerDrawable;
        } else {
            this.mSelectedDrawable = this.mContext.getResources().getDrawable(this.mUserSetSelectedIndicatorResId);
        }

        if (unselected == 0) {
            this.mUnselectedDrawable = this.mUnSelectedLayerDrawable;
        } else {
            this.mUnselectedDrawable = this.mContext.getResources().getDrawable(this.mUserSetUnSelectedIndicatorResId);
        }

        this.resetDrawable();
    }

    public void setDefaultIndicatorColor(int selectedColor, int unselectedColor) {
        if (this.mUserSetSelectedIndicatorResId == 0) {
            this.mSelectedGradientDrawable.setColor(selectedColor);
        }

        if (this.mUserSetUnSelectedIndicatorResId == 0) {
            this.mUnSelectedGradientDrawable.setColor(unselectedColor);
        }

        this.resetDrawable();
    }

    public void setDefaultSelectedIndicatorSize(float width, float height, PagerIndicator.Unit unit) {
        if (this.mUserSetSelectedIndicatorResId == 0) {
            float w = width;
            float h = height;
            if (unit == PagerIndicator.Unit.DP) {
                w = this.pxFromDp(width);
                h = this.pxFromDp(height);
            }

            this.mSelectedGradientDrawable.setSize((int)w, (int)h);
            this.resetDrawable();
        }

    }

    public void setDefaultUnselectedIndicatorSize(float width, float height, PagerIndicator.Unit unit) {
        if (this.mUserSetUnSelectedIndicatorResId == 0) {
            float w = width;
            float h = height;
            if (unit == PagerIndicator.Unit.DP) {
                w = this.pxFromDp(width);
                h = this.pxFromDp(height);
            }

            this.mUnSelectedGradientDrawable.setSize((int)w, (int)h);
            this.resetDrawable();
        }

    }

    public void setDefaultIndicatorSize(float width, float height, PagerIndicator.Unit unit) {
        this.setDefaultSelectedIndicatorSize(width, height, unit);
        this.setDefaultUnselectedIndicatorSize(width, height, unit);
    }

    private float dpFromPx(float px) {
        return px / this.getContext().getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(float dp) {
        return dp * this.getContext().getResources().getDisplayMetrics().density;
    }

    public void setIndicatorVisibility(PagerIndicator.IndicatorVisibility visibility) {
        if (visibility == PagerIndicator.IndicatorVisibility.Visible) {
            this.setVisibility(View.VISIBLE);
        } else {
            this.setVisibility(View.INVISIBLE);
        }

        this.resetDrawable();
    }

    public void destroySelf() {
        if (this.mPager != null && this.mPager.getAdapter() != null) {
            InfinitePagerAdapter wrapper = (InfinitePagerAdapter)this.mPager.getAdapter();
            PagerAdapter adapter = wrapper.getRealAdapter();
            if (adapter != null) {
                adapter.unregisterDataSetObserver(this.dataChangeObserver);
            }

            this.removeAllViews();
        }
    }

    public void setViewPager(ViewPagerEx pager) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("Viewpager does not have adapter instance");
        } else {
            this.mPager = pager;
            this.mPager.addOnPageChangeListener(this);
            ((InfinitePagerAdapter)this.mPager.getAdapter()).getRealAdapter().registerDataSetObserver(this.dataChangeObserver);
        }
    }

    private void resetDrawable() {
        Iterator i$ = this.mIndicators.iterator();

        while(true) {
            while(i$.hasNext()) {
                View i = (View)i$.next();
                if (this.mPreviousSelectedIndicator != null && this.mPreviousSelectedIndicator.equals(i)) {
                    ((ImageView)i).setImageDrawable(this.mSelectedDrawable);
                } else {
                    ((ImageView)i).setImageDrawable(this.mUnselectedDrawable);
                }
            }

            return;
        }
    }

    public void redraw() {
        this.mItemCount = this.getShouldDrawCount();
        this.mPreviousSelectedIndicator = null;
        Iterator i$ = this.mIndicators.iterator();

        while(i$.hasNext()) {
            View i = (View)i$.next();
            this.removeView(i);
        }

        for(int i = 0; i < this.mItemCount; ++i) {
            ImageView indicator = new ImageView(this.mContext);
            indicator.setImageDrawable(this.mUnselectedDrawable);
            indicator.setPadding((int)this.mUnSelectedPadding_Left, (int)this.mUnSelectedPadding_Top, (int)this.mUnSelectedPadding_Right, (int)this.mUnSelectedPadding_Bottom);
            this.addView(indicator);
            this.mIndicators.add(indicator);
        }

        this.setItemAsSelected(this.mPreviousSelectedPosition);
    }

    private int getShouldDrawCount() {
        return this.mPager.getAdapter() instanceof InfinitePagerAdapter ? ((InfinitePagerAdapter)this.mPager.getAdapter()).getRealCount() : this.mPager.getAdapter().getCount();
    }

    private void setItemAsSelected(int position) {
        if (this.mPreviousSelectedIndicator != null) {
            this.mPreviousSelectedIndicator.setImageDrawable(this.mUnselectedDrawable);
            this.mPreviousSelectedIndicator.setPadding((int)this.mUnSelectedPadding_Left, (int)this.mUnSelectedPadding_Top, (int)this.mUnSelectedPadding_Right, (int)this.mUnSelectedPadding_Bottom);
        }

        ImageView currentSelected = (ImageView)this.getChildAt(position + 1);
        if (currentSelected != null) {
            currentSelected.setImageDrawable(this.mSelectedDrawable);
            currentSelected.setPadding((int)this.mSelectedPadding_Left, (int)this.mSelectedPadding_Top, (int)this.mSelectedPadding_Right, (int)this.mSelectedPadding_Bottom);
            this.mPreviousSelectedIndicator = currentSelected;
        }

        this.mPreviousSelectedPosition = position;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public PagerIndicator.IndicatorVisibility getIndicatorVisibility() {
        return this.mVisibility;
    }

    public void onPageSelected(int position) {
        if (this.mItemCount != 0) {
            this.setItemAsSelected(position - 1);
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    public int getSelectedIndicatorResId() {
        return this.mUserSetSelectedIndicatorResId;
    }

    public int getUnSelectedIndicatorResId() {
        return this.mUserSetUnSelectedIndicatorResId;
    }

    public static enum Unit {
        DP,
        Px;

        private Unit() {
        }
    }

    public static enum Shape {
        Oval,
        Rectangle;

        private Shape() {
        }
    }

    public static enum IndicatorVisibility {
        Visible,
        Invisible;

        private IndicatorVisibility() {
        }
    }
}
