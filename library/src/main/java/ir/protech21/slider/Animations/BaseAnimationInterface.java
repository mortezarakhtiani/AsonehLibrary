package ir.protech21.slider.Animations;


import android.view.View;

public interface BaseAnimationInterface {
    void onPrepareCurrentItemLeaveScreen(View var1);

    void onPrepareNextItemShowInScreen(View var1);

    void onCurrentItemDisappear(View var1);

    void onNextItemAppear(View var1);
}
