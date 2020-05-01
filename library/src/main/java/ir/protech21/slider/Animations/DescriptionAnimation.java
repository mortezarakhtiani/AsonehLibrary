package ir.protech21.slider.Animations;


import android.view.View;



import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import ir.protech21.R.id;

public class DescriptionAnimation implements BaseAnimationInterface {
    public DescriptionAnimation() {
    }

    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(id.description_layout);
        if (descriptionLayout != null) {
            current.findViewById(id.description_layout).setVisibility(View.INVISIBLE);
        }

    }

    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(id.description_layout);
        if (descriptionLayout != null) {
            next.findViewById(id.description_layout).setVisibility(View.INVISIBLE);
        }

    }

    public void onCurrentItemDisappear(View view) {
    }

    public void onNextItemAppear(View view) {
        View descriptionLayout = view.findViewById(id.description_layout);
        if (descriptionLayout != null) {
            float layoutY = ViewHelper.getY(descriptionLayout);
            view.findViewById(id.description_layout).setVisibility(View.VISIBLE);
            ValueAnimator animator = ObjectAnimator.ofFloat(descriptionLayout, "y", new float[]{layoutY + (float)descriptionLayout.getHeight(), layoutY}).setDuration(500L);
            animator.start();
        }

    }
}
