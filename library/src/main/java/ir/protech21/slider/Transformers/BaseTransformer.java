package ir.protech21.slider.Transformers;


import android.view.View;

import com.nineoldandroids.view.ViewHelper;
import ir.protech21.slider.Animations.BaseAnimationInterface;
import ir.protech21.slider.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseTransformer implements ViewPagerEx.PageTransformer {
    private BaseAnimationInterface mCustomAnimationInterface;
    private HashMap<View, ArrayList<Float>> h = new HashMap();
    boolean isApp;
    boolean isDis;

    public BaseTransformer() {
    }

    protected abstract void onTransform(View var1, float var2);

    public void transformPage(View view, float position) {
        this.onPreTransform(view, position);
        this.onTransform(view, position);
        this.onPostTransform(view, position);
    }

    protected boolean hideOffscreenPages() {
        return true;
    }

    protected boolean isPagingEnabled() {
        return false;
    }

    protected void onPreTransform(View view, float position) {
        float width = (float)view.getWidth();
        ViewHelper.setRotationX(view, 0.0F);
        ViewHelper.setRotationY(view, 0.0F);
        ViewHelper.setRotation(view, 0.0F);
        ViewHelper.setScaleX(view, 1.0F);
        ViewHelper.setScaleY(view, 1.0F);
        ViewHelper.setPivotX(view, 0.0F);
        ViewHelper.setPivotY(view, 0.0F);
        ViewHelper.setTranslationY(view, 0.0F);
        ViewHelper.setTranslationX(view, this.isPagingEnabled() ? 0.0F : -width * position);
        if (this.hideOffscreenPages()) {
            ViewHelper.setAlpha(view, position > -1.0F && position < 1.0F ? 1.0F : 0.0F);
        } else {
            ViewHelper.setAlpha(view, 1.0F);
        }

        if (this.mCustomAnimationInterface != null && (!this.h.containsKey(view) || ((ArrayList)this.h.get(view)).size() == 1) && position > -1.0F && position < 1.0F) {
            if (this.h.get(view) == null) {
                this.h.put(view, new ArrayList());
            }

            ((ArrayList)this.h.get(view)).add(position);
            if (((ArrayList)this.h.get(view)).size() == 2) {
                float zero = (Float)((ArrayList)this.h.get(view)).get(0);
                float cha = (Float)((ArrayList)this.h.get(view)).get(1) - (Float)((ArrayList)this.h.get(view)).get(0);
                if (zero > 0.0F) {
                    if (cha > -1.0F && cha < 0.0F) {
                        this.mCustomAnimationInterface.onPrepareNextItemShowInScreen(view);
                    } else {
                        this.mCustomAnimationInterface.onPrepareCurrentItemLeaveScreen(view);
                    }
                } else if (cha > -1.0F && cha < 0.0F) {
                    this.mCustomAnimationInterface.onPrepareCurrentItemLeaveScreen(view);
                } else {
                    this.mCustomAnimationInterface.onPrepareNextItemShowInScreen(view);
                }
            }
        }

    }

    protected void onPostTransform(View view, float position) {
        if (this.mCustomAnimationInterface != null) {
            if (position != -1.0F && position != 1.0F) {
                if (position == 0.0F) {
                    this.mCustomAnimationInterface.onNextItemAppear(view);
                    this.isDis = true;
                }
            } else {
                this.mCustomAnimationInterface.onCurrentItemDisappear(view);
                this.isApp = true;
            }

            if (this.isApp && this.isDis) {
                this.h.clear();
                this.isApp = false;
                this.isDis = false;
            }
        }

    }

    public void setCustomAnimationInterface(BaseAnimationInterface animationInterface) {
        this.mCustomAnimationInterface = animationInterface;
    }
}
