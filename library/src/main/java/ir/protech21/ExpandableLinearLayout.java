package ir.protech21;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;


import carbon.widget.LinearLayout;



public class ExpandableLinearLayout extends LinearLayout {
    private boolean expanded;
    private int duration;

    public ExpandableLinearLayout(Context context) {
        super(context);
    }

    public ExpandableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attributeSet) {
        TypedArray customValues = getContext().obtainStyledAttributes(attributeSet, R.styleable.ExpandableLinearLayout);
        duration = customValues.getInt(R.styleable.ExpandableLinearLayout_Duration, -1);
        expanded = customValues.getBoolean(R.styleable.ExpandableLinearLayout_Expand, true);
        customValues.recycle();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void toggle() {
        if (expanded)
            expandView(this);
        else
            hideView(this);
    }


    public void hide() {
        hideView(ExpandableLinearLayout.this);
        expanded = false;

    }

    public void show() {
        expandView(this);

    }



    private void expandView(final View view) {
        view.measure(WindowManager.LayoutParams.MATCH_PARENT
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        final int targetHeight = view.getMeasuredHeight();
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1 ? targetHeight : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (duration == -1)
            a.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density * 1.5));
        else
            a.setDuration(duration);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animate().alpha(1).setDuration(duration==-1?500:duration).setListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(a);

    }

    private void hideView(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (duration == -1)
            a.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density * 1.5));
        else
            a.setDuration(duration);
        animate().alpha(0).setDuration(duration==-1?400:duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.startAnimation(a);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}