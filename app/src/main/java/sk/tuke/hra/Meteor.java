package sk.tuke.hra;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Meteor extends View {
    private ConstraintLayout gameLayout;

    public Meteor(Context context, ConstraintLayout layout) {
        super(context);
        this.gameLayout = layout;
    }

    public void animateFalling() {
        ObjectAnimator fallAnimation = ObjectAnimator.ofFloat(this, "translationY", getY(), gameLayout.getHeight());
        fallAnimation.setDuration(1000);
        fallAnimation.start();
    }
}
