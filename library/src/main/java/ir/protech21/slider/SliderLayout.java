package ir.protech21.slider;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import ir.protech21.R;
import ir.protech21.slider.Animations.BaseAnimationInterface;
import ir.protech21.slider.Indicators.PagerIndicator;
import ir.protech21.slider.SliderTypes.BaseSliderView;
import ir.protech21.slider.Transformers.AccordionTransformer;
import ir.protech21.slider.Transformers.BackgroundToForegroundTransformer;
import ir.protech21.slider.Transformers.BaseTransformer;
import ir.protech21.slider.Transformers.CubeInTransformer;
import ir.protech21.slider.Transformers.DefaultTransformer;
import ir.protech21.slider.Transformers.DepthPageTransformer;
import ir.protech21.slider.Transformers.FadeTransformer;
import ir.protech21.slider.Transformers.FlipHorizontalTransformer;
import ir.protech21.slider.Transformers.FlipPageViewTransformer;
import ir.protech21.slider.Transformers.ForegroundToBackgroundTransformer;
import ir.protech21.slider.Transformers.RotateDownTransformer;
import ir.protech21.slider.Transformers.RotateUpTransformer;
import ir.protech21.slider.Transformers.StackTransformer;
import ir.protech21.slider.Transformers.TabletTransformer;
import ir.protech21.slider.Transformers.ZoomInTransformer;
import ir.protech21.slider.Transformers.ZoomOutSlideTransformer;
import ir.protech21.slider.Transformers.ZoomOutTransformer;
import ir.protech21.slider.Tricks.FixedSpeedScroller;
import ir.protech21.slider.Tricks.InfinitePagerAdapter;
import ir.protech21.slider.Tricks.InfiniteViewPager;
import ir.protech21.slider.Tricks.ViewPagerEx;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class SliderLayout extends RelativeLayout {
    private Context mContext;
    public InfiniteViewPager mViewPager;
    private SliderAdapter mSliderAdapter;
    private PagerIndicator mIndicator;
    private Timer mCycleTimer;
    private TimerTask mCycleTask;
    private Timer mResumingTimer;
    private TimerTask mResumingTask;
    private boolean mCycling;
    private boolean mAutoRecover;
    private int mTransformerId;
    private int mTransformerSpan;
    private boolean mAutoCycle;
    private long mSliderDuration;
    private PagerIndicator.IndicatorVisibility mIndicatorVisibility;
    private BaseTransformer mViewPagerTransformer;
    private BaseAnimationInterface mCustomAnimation;
    private Handler mh;

    public SliderLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SliderStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    public SliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAutoRecover = true;
        this.mTransformerSpan = 1100;
        this.mSliderDuration = 4000L;
        this.mIndicatorVisibility = PagerIndicator.IndicatorVisibility.Visible;
        this.mh = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SliderLayout.this.moveNextPosition(true);
            }
        };
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.slider_layout, this, true);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SliderLayout, defStyle, 0);
        this.mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span, 1100);
        this.mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, SliderLayout.Transformer.Default.ordinal());
        this.mAutoCycle = attributes.getBoolean(R.styleable.SliderLayout_auto_cycle, true);
        int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility, 0);
        PagerIndicator.IndicatorVisibility[] arr$ = PagerIndicator.IndicatorVisibility.values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            PagerIndicator.IndicatorVisibility v = arr$[i$];
            if (v.ordinal() == visibility) {
                this.mIndicatorVisibility = v;
                break;
            }
        }

        this.mSliderAdapter = new SliderAdapter(this.mContext);
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(this.mSliderAdapter);
        this.mViewPager = this.findViewById(R.id.daimajia_slider_viewpager);
        this.mViewPager.setAdapter(wrappedAdapter);
        this.mViewPager.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch(action) {
                case 1:
                    SliderLayout.this.recoverCycle();
                default:
                    return false;
            }
        });
        attributes.recycle();
        this.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        this.setPresetTransformer(this.mTransformerId);
        this.setSliderTransformDuration(this.mTransformerSpan, (Interpolator)null);
        this.setIndicatorVisibility(this.mIndicatorVisibility);
        if (this.mAutoCycle) {
            this.startAutoCycle();
        }

    }

    public void addOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener != null) {
            this.mViewPager.addOnPageChangeListener(onPageChangeListener);
        }

    }

    public void removeOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        this.mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }

    public void setCustomIndicator(PagerIndicator indicator) {
        if (this.mIndicator != null) {
            this.mIndicator.destroySelf();
        }

        this.mIndicator = indicator;
        this.mIndicator.setIndicatorVisibility(this.mIndicatorVisibility);
        this.mIndicator.setViewPager(this.mViewPager);
        this.mIndicator.redraw();
    }

    public <T extends BaseSliderView> void addSlider(T imageContent) {
        this.mSliderAdapter.addSlider(imageContent);
    }

    public void startAutoCycle() {
        this.startAutoCycle(1000L, this.mSliderDuration, this.mAutoRecover);
    }

    public void startAutoCycle(long delay, long duration, boolean autoRecover) {
        if (this.mCycleTimer != null) {
            this.mCycleTimer.cancel();
        }

        if (this.mCycleTask != null) {
            this.mCycleTask.cancel();
        }

        if (this.mResumingTask != null) {
            this.mResumingTask.cancel();
        }

        if (this.mResumingTimer != null) {
            this.mResumingTimer.cancel();
        }

        this.mSliderDuration = duration;
        this.mCycleTimer = new Timer();
        this.mAutoRecover = autoRecover;
        this.mCycleTask = new TimerTask() {
            public void run() {
                SliderLayout.this.mh.sendEmptyMessage(0);
            }
        };
        this.mCycleTimer.schedule(this.mCycleTask, delay, this.mSliderDuration);
        this.mCycling = true;
        this.mAutoCycle = true;
    }

    private void pauseAutoCycle() {
        if (this.mCycling) {
            this.mCycleTimer.cancel();
            this.mCycleTask.cancel();
            this.mCycling = false;
        } else if (this.mResumingTimer != null && this.mResumingTask != null) {
            this.recoverCycle();
        }

    }

    public void setDuration(long duration) {
        if (duration >= 500L) {
            this.mSliderDuration = duration;
            if (this.mAutoCycle && this.mCycling) {
                this.startAutoCycle();
            }
        }

    }

    public void stopAutoCycle() {
        if (this.mCycleTask != null) {
            this.mCycleTask.cancel();
        }

        if (this.mCycleTimer != null) {
            this.mCycleTimer.cancel();
        }

        if (this.mResumingTimer != null) {
            this.mResumingTimer.cancel();
        }

        if (this.mResumingTask != null) {
            this.mResumingTask.cancel();
        }

        this.mAutoCycle = false;
        this.mCycling = false;
    }

    private void recoverCycle() {
        if (this.mAutoRecover && this.mAutoCycle) {
            if (!this.mCycling) {
                if (this.mResumingTask != null && this.mResumingTimer != null) {
                    this.mResumingTimer.cancel();
                    this.mResumingTask.cancel();
                }

                this.mResumingTimer = new Timer();
                this.mResumingTask = new TimerTask() {
                    public void run() {
                        SliderLayout.this.startAutoCycle();
                    }
                };
                this.mResumingTimer.schedule(this.mResumingTask, 6000L);
            }

        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch(action) {
            case 0:
                this.pauseAutoCycle();
            default:
                return false;
        }
    }

    public void setPagerTransformer(boolean reverseDrawingOrder, BaseTransformer transformer) {
        this.mViewPagerTransformer = transformer;
        this.mViewPagerTransformer.setCustomAnimationInterface(this.mCustomAnimation);
        this.mViewPager.setPageTransformer(reverseDrawingOrder, this.mViewPagerTransformer);
    }

    public void setSliderTransformDuration(int period, Interpolator interpolator) {
        try {
            Field mScroller = ViewPagerEx.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this.mViewPager.getContext(), interpolator, period);
            mScroller.set(this.mViewPager, scroller);
        } catch (Exception var5) {
        }

    }

    public void setPresetTransformer(int transformerId) {
        SliderLayout.Transformer[] arr$ = SliderLayout.Transformer.values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            SliderLayout.Transformer t = arr$[i$];
            if (t.ordinal() == transformerId) {
                this.setPresetTransformer(t);
                break;
            }
        }

    }

    public void setPresetTransformer(String transformerName) {
        SliderLayout.Transformer[] arr$ = SliderLayout.Transformer.values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            SliderLayout.Transformer t = arr$[i$];
            if (t.equals(transformerName)) {
                this.setPresetTransformer(t);
                return;
            }
        }

    }

    public void setCustomAnimation(BaseAnimationInterface animation) {
        this.mCustomAnimation = animation;
        if (this.mViewPagerTransformer != null) {
            this.mViewPagerTransformer.setCustomAnimationInterface(this.mCustomAnimation);
        }

    }

    public void setPresetTransformer(SliderLayout.Transformer ts) {
        BaseTransformer t = null;
        switch(ts) {
            case Default:
                t = new DefaultTransformer();
                break;
            case Accordion:
                t = new AccordionTransformer();
                break;
            case Background2Foreground:
                t = new BackgroundToForegroundTransformer();
                break;
            case CubeIn:
                t = new CubeInTransformer();
                break;
            case DepthPage:
                t = new DepthPageTransformer();
                break;
            case Fade:
                t = new FadeTransformer();
                break;
            case FlipHorizontal:
                t = new FlipHorizontalTransformer();
                break;
            case FlipPage:
                t = new FlipPageViewTransformer();
                break;
            case Foreground2Background:
                t = new ForegroundToBackgroundTransformer();
                break;
            case RotateDown:
                t = new RotateDownTransformer();
                break;
            case RotateUp:
                t = new RotateUpTransformer();
                break;
            case Stack:
                t = new StackTransformer();
                break;
            case Tablet:
                t = new TabletTransformer();
                break;
            case ZoomIn:
                t = new ZoomInTransformer();
                break;
            case ZoomOutSlide:
                t = new ZoomOutSlideTransformer();
                break;
            case ZoomOut:
                t = new ZoomOutTransformer();
        }

        this.setPagerTransformer(true, (BaseTransformer)t);
    }

    public void setIndicatorVisibility(PagerIndicator.IndicatorVisibility visibility) {
        if (this.mIndicator != null) {
            this.mIndicator.setIndicatorVisibility(visibility);
        }
    }

    public PagerIndicator.IndicatorVisibility getIndicatorVisibility() {
        return this.mIndicator == null ? this.mIndicator.getIndicatorVisibility() : PagerIndicator.IndicatorVisibility.Invisible;
    }

    public PagerIndicator getPagerIndicator() {
        return this.mIndicator;
    }

    public void setPresetIndicator(SliderLayout.PresetIndicators presetIndicator) {
        PagerIndicator pagerIndicator = (PagerIndicator)this.findViewById(presetIndicator.getResourceId());
        this.setCustomIndicator(pagerIndicator);
    }

    private InfinitePagerAdapter getWrapperAdapter() {
        PagerAdapter adapter = this.mViewPager.getAdapter();
        return adapter != null ? (InfinitePagerAdapter)adapter : null;
    }

    private SliderAdapter getRealAdapter() {
        PagerAdapter adapter = this.mViewPager.getAdapter();
        return adapter != null ? ((InfinitePagerAdapter)adapter).getRealAdapter() : null;
    }

    public int getCurrentPosition() {
        if (this.getRealAdapter() == null) {
            throw new IllegalStateException("You did not set a slider adapter");
        } else {
            return this.mViewPager.getCurrentItem() % this.getRealAdapter().getCount();
        }
    }

    public BaseSliderView getCurrentSlider() {
        if (this.getRealAdapter() == null) {
            throw new IllegalStateException("You did not set a slider adapter");
        } else {
            int count = this.getRealAdapter().getCount();
            int realCount = this.mViewPager.getCurrentItem() % count;
            return this.getRealAdapter().getSliderView(realCount);
        }
    }

    public void removeSliderAt(int position) {
        if (this.getRealAdapter() != null) {
            this.getRealAdapter().removeSliderAt(position);
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem(), false);
        }

    }

    public void removeAllSliders() {
        if (this.getRealAdapter() != null) {
            int count = this.getRealAdapter().getCount();
            this.getRealAdapter().removeAllSliders();
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + count, false);
        }

    }

    public void setCurrentPosition(int position, boolean smooth) {
        if (this.getRealAdapter() == null) {
            throw new IllegalStateException("You did not set a slider adapter");
        } else if (position >= this.getRealAdapter().getCount()) {
            throw new IllegalStateException("Item position is not exist");
        } else {
            int p = this.mViewPager.getCurrentItem() % this.getRealAdapter().getCount();
            int n = position - p + this.mViewPager.getCurrentItem();
            this.mViewPager.setCurrentItem(n, smooth);
        }
    }

    public void setCurrentPosition(int position) {
        this.setCurrentPosition(position, true);
    }

    public void movePrevPosition(boolean smooth) {
        if (this.getRealAdapter() == null) {
            throw new IllegalStateException("You did not set a slider adapter");
        } else {
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() - 1, smooth);
        }
    }

    public void movePrevPosition() {
        this.movePrevPosition(true);
    }

    public void moveNextPosition(boolean smooth) {
        if (this.getRealAdapter() == null) {
            throw new IllegalStateException("You did not set a slider adapter");
        } else {
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1, smooth);
        }
    }

    public void moveNextPosition() {
        this.moveNextPosition(true);
    }

    public static enum PresetIndicators {
        Center_Bottom("Center_Bottom", R.id.default_center_bottom_indicator),
        Right_Bottom("Right_Bottom", R.id.default_bottom_right_indicator),
        Left_Bottom("Left_Bottom", R.id.default_bottom_left_indicator),
        Center_Top("Center_Top", R.id.default_center_top_indicator),
        Right_Top("Right_Top", R.id.default_center_top_right_indicator),
        Left_Top("Left_Top", R.id.default_center_top_left_indicator);

        private final String name;
        private final int id;

        private PresetIndicators(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String toString() {
            return this.name;
        }

        public int getResourceId() {
            return this.id;
        }
    }

    public static enum Transformer {
        Default("Default"),
        Accordion("Accordion"),
        Background2Foreground("Background2Foreground"),
        CubeIn("CubeIn"),
        DepthPage("DepthPage"),
        Fade("Fade"),
        FlipHorizontal("FlipHorizontal"),
        FlipPage("FlipPage"),
        Foreground2Background("Foreground2Background"),
        RotateDown("RotateDown"),
        RotateUp("RotateUp"),
        Stack("Stack"),
        Tablet("Tablet"),
        ZoomIn("ZoomIn"),
        ZoomOutSlide("ZoomOutSlide"),
        ZoomOut("ZoomOut");

        private final String name;

        private Transformer(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(String other) {
            return other == null ? false : this.name.equals(other);
        }
    }
}
