package ir.protech21.slider.Tricks;


import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;

import androidx.core.os.ParcelableCompat;
import androidx.core.os.ParcelableCompatCreatorCallbacks;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.core.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ViewPagerEx extends ViewGroup {
    private static final String TAG = "ViewPagerEx";
    private static final boolean DEBUG = false;
    private static final boolean USE_CACHE = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private int mExpectedAdapterCount;
    private static final Comparator<ViewPagerEx.ItemInfo> COMPARATOR = new Comparator<ViewPagerEx.ItemInfo>() {
        public int compare(ViewPagerEx.ItemInfo lhs, ViewPagerEx.ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            --t;
            return t * t * t * t * t + 1.0F;
        }
    };
    private final ArrayList<ViewPagerEx.ItemInfo> mItems = new ArrayList();
    private final ViewPagerEx.ItemInfo mTempItem = new ViewPagerEx.ItemInfo();
    private final Rect mTempRect = new Rect();
    private PagerAdapter mAdapter;
    private int mCurItem;
    private int mRestoredCurItem = -1;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private Scroller mScroller;
    private ViewPagerEx.PagerObserver mObserver;
    private int mPageMargin;
    private Drawable mMarginDrawable;
    private int mTopPageBounds;
    private int mBottomPageBounds;
    private float mFirstOffset = -3.4028235E38F;
    private float mLastOffset = 3.4028235E38F;
    private int mChildWidthMeasureSpec;
    private int mChildHeightMeasureSpec;
    private boolean mInLayout;
    private boolean mScrollingCacheEnabled;
    private boolean mPopulatePending;
    private int mOffscreenPageLimit = 1;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private boolean mIgnoreGutter;
    private int mDefaultGutterSize;
    private int mGutterSize;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = -1;
    private static final int INVALID_POINTER = -1;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mCloseEnough;
    private static final int CLOSE_ENOUGH = 2;
    private boolean mFakeDragging;
    private long mFakeDragBeginTime;
    private EdgeEffectCompat mLeftEdge;
    private EdgeEffectCompat mRightEdge;
    private boolean mFirstLayout = true;
    private boolean mNeedCalculatePageOffsets = false;
    private boolean mCalledSuper;
    private int mDecorChildCount;
    private ArrayList<ViewPagerEx.OnPageChangeListener> mOnPageChangeListeners = new ArrayList();
    private ViewPagerEx.OnPageChangeListener mInternalPageChangeListener;
    private ViewPagerEx.OnAdapterChangeListener mAdapterChangeListener;
    private ViewPagerEx.PageTransformer mPageTransformer;
    private Method mSetChildrenDrawingOrderEnabled;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private static final ViewPagerEx.ViewPositionComparator sPositionComparator = new ViewPagerEx.ViewPositionComparator();
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private Runnable mEndScrollRunnable;
    private int mScrollState;

    private void triggerOnPageChangeEvent(int position) {
        Iterator i$ = this.mOnPageChangeListeners.iterator();

        while(i$.hasNext()) {
            ViewPagerEx.OnPageChangeListener eachListener = (ViewPagerEx.OnPageChangeListener)i$.next();
            if (eachListener != null) {
                InfinitePagerAdapter infiniteAdapter = (InfinitePagerAdapter)this.mAdapter;
                if (infiniteAdapter.getRealCount() == 0) {
                    return;
                }

                int n = position % infiniteAdapter.getRealCount();
                eachListener.onPageSelected(n);
            }
        }

        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(position);
        }

    }

    public ViewPagerEx(Context context) {
        super(context);
//        this.mEndScrollRunnable = new NamelessClass_1();
        this.mScrollState = 0;
        this.initViewPager();
    }

    public ViewPagerEx(Context context, AttributeSet attrs) {
        super(context, attrs);

        class NamelessClass_1 implements Runnable {
            NamelessClass_1() {
            }

            public void run() {
                ViewPagerEx.this.setScrollState(0);
                ViewPagerEx.this.populate();
            }
        }

        this.mEndScrollRunnable = new NamelessClass_1();
        this.mScrollState = 0;
        this.initViewPager();
    }

    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = (int)(400.0F * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        this.mFlingDistance = (int)(25.0F * density);
        this.mCloseEnough = (int)(2.0F * density);
        this.mDefaultGutterSize = (int)(16.0F * density);
        ViewCompat.setAccessibilityDelegate(this, new ViewPagerEx.MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }

    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    private void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                this.enableLayers(newState != 0);
            }

            Iterator i$ = this.mOnPageChangeListeners.iterator();

            while(i$.hasNext()) {
                ViewPagerEx.OnPageChangeListener eachListener = (ViewPagerEx.OnPageChangeListener)i$.next();
                if (eachListener != null) {
                    eachListener.onPageScrollStateChanged(newState);
                }
            }

        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);

            for(int i = 0; i < this.mItems.size(); ++i) {
                ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(i);
                this.mAdapter.destroyItem(this, ii.position, ii.object);
            }

            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }

        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new ViewPagerEx.PagerObserver();
            }

            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!wasFirstLayout) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }

        if (this.mAdapterChangeListener != null && oldAdapter != adapter) {
            this.mAdapterChangeListener.onAdapterChanged(oldAdapter, adapter);
        }

    }

    private void removeNonDecorViews() {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
            if (!lp.isDecor) {
                this.removeViewAt(i);
                --i;
            }
        }

    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    void setOnAdapterChangeListener(ViewPagerEx.OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        this.setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            if (!always && this.mCurItem == item && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
            } else {
                if (item < 0) {
                    item = 0;
                } else if (item >= this.mAdapter.getCount()) {
                    item = this.mAdapter.getCount() - 1;
                }

                int pageLimit = this.mOffscreenPageLimit;
                if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                    for(int i = 0; i < this.mItems.size(); ++i) {
                        ((ViewPagerEx.ItemInfo)this.mItems.get(i)).scrolling = true;
                    }
                }

                boolean dispatchSelected = this.mCurItem != item;
                if (this.mFirstLayout) {
                    this.mCurItem = item;
                    this.triggerOnPageChangeEvent(item);
                    this.requestLayout();
                } else {
                    this.populate(item);
                    this.scrollToItem(item, smoothScroll, velocity, dispatchSelected);
                }

            }
        } else {
            this.setScrollingCacheEnabled(false);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ViewPagerEx.ItemInfo curInfo = this.infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            int width = this.getClientWidth();
            destX = (int)((float)width * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }

        if (smoothScroll) {
            this.smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                this.triggerOnPageChangeEvent(item);
            }
        } else {
            if (dispatchSelected) {
                this.triggerOnPageChangeEvent(item);
            }

            this.completeScroll(false);
            this.scrollTo(destX, 0);
            this.pageScrolled(destX);
        }

    }

    public void addOnPageChangeListener(ViewPagerEx.OnPageChangeListener listener) {
        if (!this.mOnPageChangeListeners.contains(listener)) {
            this.mOnPageChangeListeners.add(listener);
        }

    }

    public void removeOnPageChangeListener(ViewPagerEx.OnPageChangeListener listener) {
        this.mOnPageChangeListeners.remove(listener);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPagerEx.PageTransformer transformer) {
        boolean hasTransformer = transformer != null;
        boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
        this.mPageTransformer = transformer;
        this.setChildrenDrawingOrderEnabledCompat(hasTransformer);
        if (hasTransformer) {
            this.mDrawingOrder = reverseDrawingOrder ? 2 : 1;
        } else {
            this.mDrawingOrder = 0;
        }

        if (needsPopulate) {
            this.populate();
        }

    }

    void setChildrenDrawingOrderEnabledCompat(boolean enable) {
        if (VERSION.SDK_INT >= 7) {
            if (this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
                } catch (NoSuchMethodException var4) {
                    Log.e("ViewPagerEx", "Can't find setChildrenDrawingOrderEnabled", var4);
                }
            }

            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, enable);
            } catch (Exception var3) {
                Log.e("ViewPagerEx", "Error changing children drawing order", var3);
            }
        }

    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int index = this.mDrawingOrder == 2 ? childCount - 1 - i : i;
        int result = ((ViewPagerEx.LayoutParams)((View)this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
        return result;
    }

    ViewPagerEx.OnPageChangeListener setInternalPageChangeListener(ViewPagerEx.OnPageChangeListener listener) {
        ViewPagerEx.OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w("ViewPagerEx", "Requested offscreen page limit " + limit + " too small; defaulting to " + 1);
            limit = 1;
        }

        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            this.populate();
        }

    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = this.getWidth();
        this.recomputeScrollPosition(width, width, marginPixels, oldMargin);
        this.requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            this.refreshDrawableState();
        }

        this.setWillNotDraw(d == null);
        this.invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(this.getDrawableState());
        }

    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f = (float)((double)f * 0.4712389167638204D);
        return (float)Math.sin((double)f);
    }

    void smoothScrollTo(int x, int y) {
        this.smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
        } else {
            int sx = this.getScrollX();
            int sy = this.getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if (dx == 0 && dy == 0) {
                this.completeScroll(false);
                this.populate();
                this.setScrollState(0);
            } else {
                this.setScrollingCacheEnabled(true);
                this.setScrollState(2);
                int width = this.getClientWidth();
                int halfWidth = width / 2;
                float distanceRatio = Math.min(1.0F, 1.0F * (float)Math.abs(dx) / (float)width);
                float distance = (float)halfWidth + (float)halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
//                boolean duration = false;
                velocity = Math.abs(velocity);
                int duration;
                if (velocity > 0) {
                    duration = 4 * Math.round(1000.0F * Math.abs(distance / (float)velocity));
                } else {
                    float pageWidth = (float)width * this.mAdapter.getPageWidth(this.mCurItem);
                    float pageDelta = (float)Math.abs(dx) / (pageWidth + (float)this.mPageMargin);
                    duration = (int)((pageDelta + 1.0F) * 100.0F);
                }

                duration = Math.min(duration, 600);
                this.mScroller.startScroll(sx, sy, dx, dy, duration);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    ViewPagerEx.ItemInfo addNewItem(int position, int index) {
        ViewPagerEx.ItemInfo ii = new ViewPagerEx.ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.add(index, ii);
        } else {
            this.mItems.add(ii);
        }

        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < adapterCount;
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;

        int childCount;
        for(childCount = 0; childCount < this.mItems.size(); ++childCount) {
            ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(childCount);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(childCount);
                    --childCount;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate(this);
                        isUpdating = true;
                    }

                    this.mAdapter.destroyItem(this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }

                    ii.position = newPos;
                    needPopulate = true;
                }
            }
        }

        if (isUpdating) {
            this.mAdapter.finishUpdate(this);
        }

        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            childCount = this.getChildCount();

            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0F;
                }
            }

            this.setCurrentItemInternal(newCurrItem, false, true);
            this.requestLayout();
        }

    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        ViewPagerEx.ItemInfo oldCurInfo = null;
        int focusDirection = 2;
        if (this.mCurItem != newCurrentItem) {
            focusDirection = this.mCurItem < newCurrentItem ? 66 : 17;
            oldCurInfo = this.infoForPosition(this.mCurItem);
            this.mCurItem = newCurrentItem;
        }

        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
        } else if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
        } else if (this.getWindowToken() != null) {
            this.mAdapter.startUpdate(this);
            int pageLimit = this.mOffscreenPageLimit;
            int startPos = Math.max(0, this.mCurItem - pageLimit);
            int N = this.mAdapter.getCount();
            int endPos = Math.min(N - 1, this.mCurItem + pageLimit);
            if (N != this.mExpectedAdapterCount) {
                String resName;
                try {
                    resName = this.getResources().getResourceName(this.getId());
                } catch (NotFoundException var18) {
                    resName = Integer.toHexString(this.getId());
                }

                throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + N + " Pager id: " + resName + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
            } else {
//                int curIndex = true;
                ViewPagerEx.ItemInfo curItem = null;

                int curIndex;
                for(curIndex = 0; curIndex < this.mItems.size(); ++curIndex) {
                    ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(curIndex);
                    if (ii.position >= this.mCurItem) {
                        if (ii.position == this.mCurItem) {
                            curItem = ii;
                        }
                        break;
                    }
                }

                if (curItem == null && N > 0) {
                    curItem = this.addNewItem(this.mCurItem, curIndex);
                }

                int itemIndex;
                ViewPagerEx.ItemInfo ii;
                int i;
                if (curItem != null) {
                    float extraWidthLeft = 0.0F;
                    itemIndex = curIndex - 1;
                    ii = itemIndex >= 0 ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                    i = this.getClientWidth();
                    float leftWidthNeeded = i <= 0 ? 0.0F : 2.0F - curItem.widthFactor + (float)this.getPaddingLeft() / (float)i;

                    for(int pos = this.mCurItem - 1; pos >= 0; --pos) {
                        if (extraWidthLeft >= leftWidthNeeded && pos < startPos) {
                            if (ii == null) {
                                break;
                            }

                            if (pos == ii.position && !ii.scrolling) {
                                this.mItems.remove(itemIndex);
                                this.mAdapter.destroyItem(this, pos, ii.object);
                                --itemIndex;
                                --curIndex;
                                ii = itemIndex >= 0 ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                            }
                        } else if (ii != null && pos == ii.position) {
                            extraWidthLeft += ii.widthFactor;
                            --itemIndex;
                            ii = itemIndex >= 0 ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                        } else {
                            ii = this.addNewItem(pos, itemIndex + 1);
                            extraWidthLeft += ii.widthFactor;
                            ++curIndex;
                            ii = itemIndex >= 0 ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                        }
                    }

                    float extraWidthRight = curItem.widthFactor;
                    itemIndex = curIndex + 1;
                    if (extraWidthRight < 2.0F) {
                        ii = itemIndex < this.mItems.size() ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                        float rightWidthNeeded = i <= 0 ? 0.0F : (float)this.getPaddingRight() / (float)i + 2.0F;

                        for(int pos = this.mCurItem + 1; pos < N; ++pos) {
                            if (extraWidthRight >= rightWidthNeeded && pos > endPos) {
                                if (ii == null) {
                                    break;
                                }

                                if (pos == ii.position && !ii.scrolling) {
                                    this.mItems.remove(itemIndex);
                                    this.mAdapter.destroyItem(this, pos, ii.object);
                                    ii = itemIndex < this.mItems.size() ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                                }
                            } else if (ii != null && pos == ii.position) {
                                extraWidthRight += ii.widthFactor;
                                ++itemIndex;
                                ii = itemIndex < this.mItems.size() ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                            } else {
                                ii = this.addNewItem(pos, itemIndex);
                                ++itemIndex;
                                extraWidthRight += ii.widthFactor;
                                ii = itemIndex < this.mItems.size() ? (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex) : null;
                            }
                        }
                    }

                    this.calculatePageOffsets(curItem, curIndex, oldCurInfo);
                }

                this.mAdapter.setPrimaryItem(this, this.mCurItem, curItem != null ? curItem.object : null);
                this.mAdapter.finishUpdate(this);
                int childCount = this.getChildCount();

                for(itemIndex = 0; itemIndex < childCount; ++itemIndex) {
                    View child = this.getChildAt(itemIndex);
                    ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                    lp.childIndex = itemIndex;
                    if (!lp.isDecor && lp.widthFactor == 0.0F) {
                        ii = this.infoForChild(child);
                        if (ii != null) {
                            lp.widthFactor = ii.widthFactor;
                            lp.position = ii.position;
                        }
                    }
                }

                this.sortChildDrawingOrder();
                if (this.hasFocus()) {
                    View currentFocused = this.findFocus();
                    ii = currentFocused != null ? this.infoForAnyChild(currentFocused) : null;
                    if (ii == null || ii.position != this.mCurItem) {
                        for(i = 0; i < this.getChildCount(); ++i) {
                            View child = this.getChildAt(i);
                            ii = this.infoForChild(child);
                            if (ii != null && ii.position == this.mCurItem && child.requestFocus(focusDirection)) {
                                break;
                            }
                        }
                    }
                }

            }
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }

            int childCount = this.getChildCount();

            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                this.mDrawingOrderedChildren.add(child);
            }

            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }

    }

    private void calculatePageOffsets(ViewPagerEx.ItemInfo curItem, int curIndex, ViewPagerEx.ItemInfo oldCurInfo) {
        int N = this.mAdapter.getCount();
        int width = this.getClientWidth();
        float marginOffset = width > 0 ? (float)this.mPageMargin / (float)width : 0.0F;
        int itemCount;
        if (oldCurInfo != null) {
            itemCount = oldCurInfo.position;
            int itemIndex;
            ViewPagerEx.ItemInfo ii;
            float offset;
            int pos;
            if (itemCount < curItem.position) {
                itemIndex = 0;
                ii = null;
                offset = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;

                for(pos = itemCount + 1; pos <= curItem.position && itemIndex < this.mItems.size(); ++pos) {
                    for(ii = (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex); pos > ii.position && itemIndex < this.mItems.size() - 1; ii = (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex)) {
                        ++itemIndex;
                    }

                    while(pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        ++pos;
                    }

                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                }
            } else if (itemCount > curItem.position) {
                itemIndex = this.mItems.size() - 1;
                ii = null;
                offset = oldCurInfo.offset;

                for(pos = itemCount - 1; pos >= curItem.position && itemIndex >= 0; --pos) {
                    for(ii = (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex); pos < ii.position && itemIndex > 0; ii = (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex)) {
                        --itemIndex;
                    }

                    while(pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        --pos;
                    }

                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                }
            }
        }

        itemCount = this.mItems.size();
        float offset = curItem.offset;
        int pos = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38F;
        this.mLastOffset = curItem.position == N - 1 ? curItem.offset + curItem.widthFactor - 1.0F : 3.4028235E38F;

        int i;
        ViewPagerEx.ItemInfo ii;
        for(i = curIndex - 1; i >= 0; --pos) {
            for(ii = (ViewPagerEx.ItemInfo)this.mItems.get(i); pos > ii.position; offset -= this.mAdapter.getPageWidth(pos--) + marginOffset) {
            }

            offset -= ii.widthFactor + marginOffset;
            ii.offset = offset;
            if (ii.position == 0) {
                this.mFirstOffset = offset;
            }

            --i;
        }

        offset = curItem.offset + curItem.widthFactor + marginOffset;
        pos = curItem.position + 1;

        for(i = curIndex + 1; i < itemCount; ++pos) {
            for(ii = (ViewPagerEx.ItemInfo)this.mItems.get(i); pos < ii.position; offset += this.mAdapter.getPageWidth(pos++) + marginOffset) {
            }

            if (ii.position == N - 1) {
                this.mLastOffset = offset + ii.widthFactor - 1.0F;
            }

            ii.offset = offset;
            offset += ii.widthFactor + marginOffset;
            ++i;
        }

        this.mNeedCalculatePageOffsets = false;
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        ViewPagerEx.SavedState ss = new ViewPagerEx.SavedState(superState);
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }

        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof ViewPagerEx.SavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            ViewPagerEx.SavedState ss = (ViewPagerEx.SavedState)state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                this.setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }

        }
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!this.checkLayoutParams(params)) {
            params = this.generateLayoutParams(params);
        }

        ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)params;
        lp.isDecor |= child instanceof ViewPagerEx.Decor;
        if (this.mInLayout) {
            if (lp != null && lp.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }

            lp.needsMeasure = true;
            this.addViewInLayout(child, index, params);
        } else {
            super.addView(child, index, params);
        }

    }

    public void removeView(View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
        } else {
            super.removeView(view);
        }

    }

    ViewPagerEx.ItemInfo infoForChild(View child) {
        for(int i = 0; i < this.mItems.size(); ++i) {
            ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }

        return null;
    }

    ViewPagerEx.ItemInfo infoForAnyChild(View child) {
        while(true) {
            ViewParent parent;
            if ((parent = child.getParent()) != this) {
                if (parent != null && parent instanceof View) {
                    child = (View)parent;
                    continue;
                }

                return null;
            }

            return this.infoForChild(child);
        }
    }

    ViewPagerEx.ItemInfo infoForPosition(int position) {
        for(int i = 0; i < this.mItems.size(); ++i) {
            ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }

        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = this.getMeasuredWidth();
        int maxGutterSize = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize, this.mDefaultGutterSize);
        int childWidthSize = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        int childHeightSize = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int size = this.getChildCount();

        int i;
        View child;
        ViewPagerEx.LayoutParams lp;
        int widthSpec;
        for(i = 0; i < size; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    widthSpec = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = -2147483648;
                    int heightMode = -2147483648;
                    boolean consumeVertical = vgrav == 48 || vgrav == 80;
                    boolean consumeHorizontal = widthSpec == 3 || widthSpec == 5;
                    if (consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        heightMode = 1073741824;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != -2) {
                        widthMode = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }

                    if (lp.height != -2) {
                        heightMode = 1073741824;
                        if (lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }

                    widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec, heightSpec);
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }

        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        size = this.getChildCount();

        for(i = 0; i < size; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    widthSpec = MeasureSpec.makeMeasureSpec((int)((float)childWidthSize * lp.widthFactor), 1073741824);
                    child.measure(widthSpec, this.mChildHeightMeasureSpec);
                }
            }
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            this.recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }

    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        int scrollPos;
        if (oldWidth > 0 && !this.mItems.isEmpty()) {
            int widthWithMargin = width - this.getPaddingLeft() - this.getPaddingRight() + margin;
            int oldWidthWithMargin = oldWidth - this.getPaddingLeft() - this.getPaddingRight() + oldMargin;
            scrollPos = this.getScrollX();
            float pageOffset = (float)scrollPos / (float)oldWidthWithMargin;
            int newOffsetPixels = (int)(pageOffset * (float)widthWithMargin);
            this.scrollTo(newOffsetPixels, this.getScrollY());
            if (!this.mScroller.isFinished()) {
                int newDuration = this.mScroller.getDuration() - this.mScroller.timePassed();
                ViewPagerEx.ItemInfo targetInfo = this.infoForPosition(this.mCurItem);
                this.mScroller.startScroll(newOffsetPixels, 0, (int)(targetInfo.offset * (float)width), 0, newDuration);
            }
        } else {
            ViewPagerEx.ItemInfo ii = this.infoForPosition(this.mCurItem);
            float scrollOffset = ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0F;
            scrollPos = (int)(scrollOffset * (float)(width - this.getPaddingLeft() - this.getPaddingRight()));
            if (scrollPos != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(scrollPos, this.getScrollY());
            }
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int scrollX = this.getScrollX();
        int decorCount = 0;

        int childWidth;
        int loff;
        int childLeft;
        for(childWidth = 0; childWidth < count; ++childWidth) {
            View child = this.getChildAt(childWidth);
            if (child.getVisibility() != 8) {
                ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
//                int childLeft = false;
                int childTop;
                if (lp.isDecor) {
                    loff = lp.gravity & 7;
                    childLeft = lp.gravity & 112;
//                    int childLeft;
                    switch(loff) {
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 2:
                        case 4:
                        default:
                            childLeft = paddingLeft;
                            break;
                        case 3:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case 5:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                    }

//                    int childTop;
                    switch(childLeft) {
                        case 16:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case 48:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case 80:
                            childTop = height - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                        default:
                            childTop = paddingTop;
                    }

                    childLeft += scrollX;
                    child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
                    ++decorCount;
                }
            }
        }

        childWidth = width - paddingLeft - paddingRight;

        for(int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                ViewPagerEx.ItemInfo ii;
                if (!lp.isDecor && (ii = this.infoForChild(child)) != null) {
                    loff = (int)((float)childWidth * ii.offset);
                    childLeft = paddingLeft + loff;
                    if (lp.needsMeasure) {
                        lp.needsMeasure = false;
                        int widthSpec = MeasureSpec.makeMeasureSpec((int)((float)childWidth * lp.widthFactor), 1073741824);
                        int heightSpec = MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, 1073741824);
                        child.measure(widthSpec, heightSpec);
                    }

                    child.layout(childLeft, paddingTop, childLeft + child.getMeasuredWidth(), paddingTop + child.getMeasuredHeight());
                }
            }
        }

        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }

        this.mFirstLayout = false;
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = this.getScrollX();
            int oldY = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                this.scrollTo(x, y);
                if (!this.pageScrolled(x)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, y);
                }
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.completeScroll(true);
        }
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0F, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return false;
            }
        } else {
            ViewPagerEx.ItemInfo ii = this.infoForCurrentScrollPosition();
            int width = this.getClientWidth();
            int widthWithMargin = width + this.mPageMargin;
            float marginOffset = (float)this.mPageMargin / (float)width;
            int currentPage = ii.position;
            float pageOffset = ((float)xpos / (float)width - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int)(pageOffset * (float)widthWithMargin);
            this.mCalledSuper = false;
            this.onPageScrolled(currentPage, pageOffset, offsetPixels);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return true;
            }
        }
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int childCount;
        int i;
        if (this.mDecorChildCount > 0) {
            scrollX = this.getScrollX();
            childCount = this.getPaddingLeft();
            i = this.getPaddingRight();
            int width = this.getWidth();
             childCount = this.getChildCount();

            for( i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                if (lp.isDecor) {
                    int hgrav = lp.gravity & 7;
//                    boolean childLeft = false;
                    int childLeft;
                    switch(hgrav) {
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, childCount);
                            break;
                        case 2:
                        case 4:
                        default:
                            childLeft = childCount;
                            break;
                        case 3:
                            childLeft = childCount;
                            childCount += child.getWidth();
                            break;
                        case 5:
                            childLeft = width - i - child.getMeasuredWidth();
                            i += child.getMeasuredWidth();
                    }

                    childLeft += scrollX;
                    int childOffset = childLeft - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }

        Iterator i$ = this.mOnPageChangeListeners.iterator();

        while(i$.hasNext()) {
            ViewPagerEx.OnPageChangeListener eachListener = (ViewPagerEx.OnPageChangeListener)i$.next();
            if (eachListener != null) {
                eachListener.onPageScrolled(position, offset, offsetPixels);
            }
        }

        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if (this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            childCount = this.getChildCount();

            for(i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                ViewPagerEx.LayoutParams lp = (ViewPagerEx.LayoutParams)child.getLayoutParams();
                if (!lp.isDecor) {
                    float transformPos = (float)(child.getLeft() - scrollX) / (float)this.getClientWidth();
                    this.mPageTransformer.transformPage(child, transformPos);
                }
            }
        }

        this.mCalledSuper = true;
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        int i;
        if (needPopulate) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            i = this.getScrollX();
            int oldY = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (i != x || oldY != y) {
                this.scrollTo(x, y);
            }
        }

        this.mPopulatePending = false;

        for(i = 0; i < this.mItems.size(); ++i) {
            ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }

        if (needPopulate) {
            if (postEvents) {
                ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }

    }

    private boolean isGutterDrag(float x, float dx) {
        return x < (float)this.mGutterSize && dx > 0.0F || x > (float)(this.getWidth() - this.mGutterSize) && dx < 0.0F;
    }

    private void enableLayers(boolean enable) {
        int childCount = this.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            int layerType = enable ? 2 : 0;
            ViewCompat.setLayerType(this.getChildAt(i), layerType, (Paint)null);
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if (action != 3 && action != 1) {
            if (action != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }

                if (this.mIsUnableToDrag) {
                    return false;
                }
            }

            switch(action) {
                case 0:
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    this.mIsUnableToDrag = false;
                    this.mScroller.computeScrollOffset();
                    if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        this.mIsBeingDragged = true;
                        this.requestParentDisallowInterceptTouchEvent(true);
                        this.setScrollState(1);
                    } else {
                        this.completeScroll(false);
                        this.mIsBeingDragged = false;
                    }
                    break;
                case 2:
                    int activePointerId = this.mActivePointerId;
                    if (activePointerId != -1) {
                        int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                        float x = MotionEventCompat.getX(ev, pointerIndex);
                        float dx = x - this.mLastMotionX;
                        float xDiff = Math.abs(dx);
                        float y = MotionEventCompat.getY(ev, pointerIndex);
                        float yDiff = Math.abs(y - this.mInitialMotionY);
                        if (dx != 0.0F && !this.isGutterDrag(this.mLastMotionX, dx) && this.canScroll(this, false, (int)dx, (int)x, (int)y)) {
                            this.mLastMotionX = x;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }

                        if (xDiff > (float)this.mTouchSlop && xDiff * 0.5F > yDiff) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.setScrollState(1);
                            this.mLastMotionX = dx > 0.0F ? this.mInitialMotionX + (float)this.mTouchSlop : this.mInitialMotionX - (float)this.mTouchSlop;
                            this.mLastMotionY = y;
                            this.setScrollingCacheEnabled(true);
                        } else if (yDiff > (float)this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }

                        if (this.mIsBeingDragged && this.performDrag(x)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                        }
                    }
                    break;
                case 6:
                    this.onSecondaryPointerUp(ev);
            }

            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            return this.mIsBeingDragged;
        } else {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }

            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mFakeDragging) {
            return true;
        } else if (ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return false;
        } else if (this.mAdapter != null && this.mAdapter.getCount() != 0) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            int action = ev.getAction();
            boolean needsInvalidate = false;
            int activePointerIndex;
            float x;
            switch(action & 255) {
                case 0:
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    break;
                case 1:
                    if (this.mIsBeingDragged) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                        int initialVelocity = (int)VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                        this.mPopulatePending = true;
                        int width = this.getClientWidth();
                        int scrollX = this.getScrollX();
                        ViewPagerEx.ItemInfo ii = this.infoForCurrentScrollPosition();
                        int currentPage = ii.position;
                        float pageOffset = ((float)scrollX / (float)width - ii.offset) / ii.widthFactor;
                        activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                        x = MotionEventCompat.getX(ev, activePointerIndex);
                        int totalDelta = (int)(x - this.mInitialMotionX);
                        int nextPage = this.determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);
                        this.setCurrentItemInternal(nextPage, true, true, initialVelocity);
                        this.mActivePointerId = -1;
                        this.endDrag();
                        needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                    }
                    break;
                case 2:
                    if (!this.mIsBeingDragged) {
                        activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                        x = MotionEventCompat.getX(ev, activePointerIndex);
                        float xDiff = Math.abs(x - this.mLastMotionX);
                        float y = MotionEventCompat.getY(ev, activePointerIndex);
                        float yDiff = Math.abs(y - this.mLastMotionY);
                        if (xDiff > (float)this.mTouchSlop && xDiff > yDiff) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.mLastMotionX = x - this.mInitialMotionX > 0.0F ? this.mInitialMotionX + (float)this.mTouchSlop : this.mInitialMotionX - (float)this.mTouchSlop;
                            this.mLastMotionY = y;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            ViewParent parent = this.getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }

                    if (this.mIsBeingDragged) {
                        activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                        x = MotionEventCompat.getX(ev, activePointerIndex);
                        needsInvalidate |= this.performDrag(x);
                    }
                    break;
                case 3:
                    if (this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        this.mActivePointerId = -1;
                        this.endDrag();
                        needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                    }
                case 4:
                default:
                    break;
                case 5:
                    activePointerIndex = MotionEventCompat.getActionIndex(ev);
                    x = MotionEventCompat.getX(ev, activePointerIndex);
                    this.mLastMotionX = x;
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, activePointerIndex);
                    break;
                case 6:
                    this.onSecondaryPointerUp(ev);
                    this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
            }

            if (needsInvalidate) {
                ViewCompat.postInvalidateOnAnimation(this);
            }

            return true;
        } else {
            return false;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float oldScrollX = (float)this.getScrollX();
        float scrollX = oldScrollX + deltaX;
        int width = this.getClientWidth();
        float leftBound = (float)width * this.mFirstOffset;
        float rightBound = (float)width * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ViewPagerEx.ItemInfo firstItem = (ViewPagerEx.ItemInfo)this.mItems.get(0);
        ViewPagerEx.ItemInfo lastItem = (ViewPagerEx.ItemInfo)this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * (float)width;
        }

        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * (float)width;
        }

        float over;
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                over = leftBound - scrollX;
                needsInvalidate = this.mLeftEdge.onPull(Math.abs(over) / (float)width);
            }

            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                over = scrollX - rightBound;
                needsInvalidate = this.mRightEdge.onPull(Math.abs(over) / (float)width);
            }

            scrollX = rightBound;
        }

        this.mLastMotionX += scrollX - (float)((int)scrollX);
        this.scrollTo((int)scrollX, this.getScrollY());
        this.pageScrolled((int)scrollX);
        return needsInvalidate;
    }

    private ViewPagerEx.ItemInfo infoForCurrentScrollPosition() {
        int width = this.getClientWidth();
        float scrollOffset = width > 0 ? (float)this.getScrollX() / (float)width : 0.0F;
        float marginOffset = width > 0 ? (float)this.mPageMargin / (float)width : 0.0F;
        int lastPos = -1;
        float lastOffset = 0.0F;
        float lastWidth = 0.0F;
        boolean first = true;
        ViewPagerEx.ItemInfo lastItem = null;

        for(int i = 0; i < this.mItems.size(); ++i) {
            ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(i);
            if (!first && ii.position != lastPos + 1) {
                ii = this.mTempItem;
                ii.offset = lastOffset + lastWidth + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                --i;
            }

            float offset = ii.offset;
            float rightBound = offset + ii.widthFactor + marginOffset;
            if (!first && scrollOffset < offset) {
                return lastItem;
            }

            if (scrollOffset < rightBound || i == this.mItems.size() - 1) {
                return ii;
            }

            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
        }

        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) > this.mFlingDistance && Math.abs(velocity) > this.mMinimumVelocity) {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        } else {
            float truncator = currentPage >= this.mCurItem ? 0.4F : 0.6F;
            targetPage = (int)((float)currentPage + pageOffset + truncator);
        }

        if (this.mItems.size() > 0) {
            ViewPagerEx.ItemInfo firstItem = (ViewPagerEx.ItemInfo)this.mItems.get(0);
            ViewPagerEx.ItemInfo lastItem = (ViewPagerEx.ItemInfo)this.mItems.get(this.mItems.size() - 1);
            targetPage = Math.max(firstItem.position, Math.min(targetPage, lastItem.position));
        }

        return targetPage;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if (overScrollMode != 0 && (overScrollMode != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        } else {
            int restoreCount;
            int width;
            int height;
            if (!this.mLeftEdge.isFinished()) {
                restoreCount = canvas.save();
                width = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                height = this.getWidth();
                canvas.rotate(270.0F);
                canvas.translate((float)(-width + this.getPaddingTop()), this.mFirstOffset * (float)height);
                this.mLeftEdge.setSize(width, height);
                needsInvalidate |= this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }

            if (!this.mRightEdge.isFinished()) {
                restoreCount = canvas.save();
                width = this.getWidth();
                height = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                canvas.rotate(90.0F);
                canvas.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0F) * (float)width);
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        }

        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = this.getScrollX();
            int width = this.getWidth();
            float marginOffset = (float)this.mPageMargin / (float)width;
            int itemIndex = 0;
            ViewPagerEx.ItemInfo ii = (ViewPagerEx.ItemInfo)this.mItems.get(0);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ViewPagerEx.ItemInfo)this.mItems.get(itemCount - 1)).position;

            for(int pos = firstPos; pos < lastPos; ++pos) {
                while(pos > ii.position && itemIndex < itemCount) {
                    ++itemIndex;
                    ii = (ViewPagerEx.ItemInfo)this.mItems.get(itemIndex);
                }

                float drawAt;
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * (float)width;
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * (float)width;
                    offset += widthFactor + marginOffset;
                }

                if (drawAt + (float)this.mPageMargin > (float)scrollX) {
                    this.mMarginDrawable.setBounds((int)drawAt, this.mTopPageBounds, (int)(drawAt + (float)this.mPageMargin + 0.5F), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }

                if (drawAt > (float)(scrollX + width)) {
                    break;
                }
            }
        }

    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        } else {
            this.mFakeDragging = true;
            this.setScrollState(1);
            this.mInitialMotionX = this.mLastMotionX = 0.0F;
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                this.mVelocityTracker.clear();
            }

            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0F, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
            this.mFakeDragBeginTime = time;
            return true;
        }
    }

    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            int initialVelocity = (int)VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            ViewPagerEx.ItemInfo ii = this.infoForCurrentScrollPosition();
            int currentPage = ii.position;
            float pageOffset = ((float)scrollX / (float)width - ii.offset) / ii.widthFactor;
            int totalDelta = (int)(this.mLastMotionX - this.mInitialMotionX);
            int nextPage = this.determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);
            this.setCurrentItemInternal(nextPage, true, true, initialVelocity);
            this.endDrag();
            this.mFakeDragging = false;
        }
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            this.mLastMotionX += xOffset;
            float oldScrollX = (float)this.getScrollX();
            float scrollX = oldScrollX - xOffset;
            int width = this.getClientWidth();
            float leftBound = (float)width * this.mFirstOffset;
            float rightBound = (float)width * this.mLastOffset;
            ViewPagerEx.ItemInfo firstItem = (ViewPagerEx.ItemInfo)this.mItems.get(0);
            ViewPagerEx.ItemInfo lastItem = (ViewPagerEx.ItemInfo)this.mItems.get(this.mItems.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * (float)width;
            }

            if (lastItem.position != this.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * (float)width;
            }

            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }

            this.mLastMotionX += scrollX - (float)((int)scrollX);
            this.scrollTo((int)scrollX, this.getScrollY());
            this.pageScrolled((int)scrollX);
            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, time, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }

    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }

    }

    public boolean canScrollHorizontally(int direction) {
        if (this.mAdapter == null) {
            return false;
        } else {
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            if (direction < 0) {
                return scrollX > (int)((float)width * this.mFirstOffset);
            } else if (direction > 0) {
                return scrollX < (int)((float)width * this.mLastOffset);
            } else {
                return false;
            }
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();

            for(int i = count - 1; i >= 0; --i) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && this.canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == 0) {
            switch(event.getKeyCode()) {
                case 21:
                    handled = this.arrowScroll(17);
                    break;
                case 22:
                    handled = this.arrowScroll(66);
                    break;
                case 61:
                    if (VERSION.SDK_INT >= 11) {
//                        if ((Boolean) KeyEvent.hasNoModifiers(event)) {
//                            handled = this.arrowScroll(2);
//                        } else if ((Boolean) KeyEvent.hasModifiers(event, 1)) {
//                            handled = this.arrowScroll(1);
//                        }
                    }
            }
        }

        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = this.findFocus();
        boolean handled;
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            handled = false;

            for(ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                if (parent == this) {
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());

                for(ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                    sb.append(" => ").append(parent.getClass().getSimpleName());
                }

                Log.e("ViewPagerEx", "arrowScroll tried to find focus based on non-child current focused view " + sb.toString());
                currentFocused = null;
            }
        }

        handled = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused != null && nextFocused != currentFocused) {
            int currLeft;
            int nextLeft;
            if (direction == 17) {
                nextLeft = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
                currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if (currentFocused != null && nextLeft >= currLeft) {
                    handled = this.pageLeft();
                } else {
                    handled = nextFocused.requestFocus();
                }
            } else if (direction == 66) {
                nextLeft = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
                currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if (currentFocused != null && nextLeft <= currLeft) {
                    handled = this.pageRight();
                } else {
                    handled = nextFocused.requestFocus();
                }
            }
        } else if (direction != 17 && direction != 1) {
            if (direction == 66 || direction == 2) {
                handled = this.pageRight();
            }
        } else {
            handled = this.pageLeft();
        }

        if (handled) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }

        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }

        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();

            ViewGroup group;
            for(ViewParent parent = child.getParent(); parent instanceof ViewGroup && parent != this; parent = group.getParent()) {
                group = (ViewGroup)parent;
                outRect.left += group.getLeft();
                outRect.right += group.getRight();
                outRect.top += group.getTop();
                outRect.bottom += group.getBottom();
            }

            return outRect;
        }
    }

    boolean pageLeft() {
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        } else {
            return false;
        }
    }

    boolean pageRight() {
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        } else {
            return false;
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for(int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    ViewPagerEx.ItemInfo ii = this.infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }

        if (descendantFocusability != 262144 || focusableCount == views.size()) {
            if (!this.isFocusable()) {
                return;
            }

            if ((focusableMode & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }

            if (views != null) {
                views.add(this);
            }
        }

    }

    public void addTouchables(ArrayList<View> views) {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                ViewPagerEx.ItemInfo ii = this.infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }

    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = this.getChildCount();
        int index;
        byte increment;
        int end;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for(int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                ViewPagerEx.ItemInfo ii = this.infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        } else {
            int childCount = this.getChildCount();

            for(int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    ViewPagerEx.ItemInfo ii = this.infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewPagerEx.LayoutParams();
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return this.generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewPagerEx.LayoutParams && super.checkLayoutParams(p);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewPagerEx.LayoutParams(this.getContext(), attrs);
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            ViewPagerEx.LayoutParams llp = (ViewPagerEx.LayoutParams)lhs.getLayoutParams();
            ViewPagerEx.LayoutParams rlp = (ViewPagerEx.LayoutParams)rhs.getLayoutParams();
            if (llp.isDecor != rlp.isDecor) {
                return llp.isDecor ? 1 : -1;
            } else {
                return llp.position - rlp.position;
            }
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public boolean isDecor;
        public int gravity;
        float widthFactor = 0.0F;
        boolean needsMeasure;
        int position;
        int childIndex;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPagerEx.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPagerEx.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPagerEx.this.dataSetChanged();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPagerEx.class.getName());
            AccessibilityRecordCompat recordCompat = AccessibilityRecordCompat.obtain();
            recordCompat.setScrollable(this.canScroll());
            if (event.getEventType() == 4096 && ViewPagerEx.this.mAdapter != null) {
                recordCompat.setItemCount(ViewPagerEx.this.mAdapter.getCount());
                recordCompat.setFromIndex(ViewPagerEx.this.mCurItem);
                recordCompat.setToIndex(ViewPagerEx.this.mCurItem);
            }

        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPagerEx.class.getName());
            info.setScrollable(this.canScroll());
            if (ViewPagerEx.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }

            if (ViewPagerEx.this.canScrollHorizontally(-1)) {
                info.addAction(8192);
            }

        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            } else {
                switch(action) {
                    case 4096:
                        if (ViewPagerEx.this.canScrollHorizontally(1)) {
                            ViewPagerEx.this.setCurrentItem(ViewPagerEx.this.mCurItem + 1);
                            return true;
                        }

                        return false;
                    case 8192:
                        if (ViewPagerEx.this.canScrollHorizontally(-1)) {
                            ViewPagerEx.this.setCurrentItem(ViewPagerEx.this.mCurItem - 1);
                            return true;
                        }

                        return false;
                    default:
                        return false;
                }
            }
        }

        private boolean canScroll() {
            return ViewPagerEx.this.mAdapter != null && ViewPagerEx.this.mAdapter.getCount() > 1;
        }
    }

    public static class SavedState extends BaseSavedState {
        int position;
        Parcelable adapterState;
        ClassLoader loader;
        public static final Creator<ViewPagerEx.SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<ViewPagerEx.SavedState>() {
            public ViewPagerEx.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new ViewPagerEx.SavedState(in, loader);
            }

            public ViewPagerEx.SavedState[] newArray(int size) {
                return new ViewPagerEx.SavedState[size];
            }
        });

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }

            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    interface Decor {
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter var1, PagerAdapter var2);
    }

    public interface PageTransformer {
        void transformPage(View var1, float var2);
    }

    public static class SimpleOnPageChangeListener implements ViewPagerEx.OnPageChangeListener {
        public SimpleOnPageChangeListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int var1, float var2, int var3);

        void onPageSelected(int var1);

        void onPageScrollStateChanged(int var1);
    }

    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;
        float widthFactor;
        float offset;

        ItemInfo() {
        }
    }
}
