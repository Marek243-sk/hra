package sk.tuke.hra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class DrawView extends View {
    private ImageView spaceship, meteor;
    private void init() {
    }
    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public boolean isIntersecting(float x, float y) {
        int[] location = new int[2];
        spaceship.getLocationOnScreen(location);

        int spaceshipX = location[0];
        int spaceshipY = location[1];

        return (x > spaceshipX && x < (spaceshipX + spaceship.getWidth()) && y > spaceshipY && y < (spaceshipY + spaceship.getHeight()));
    }

    @SuppressLint("ClickableViewAccessibility")//kvôli performClick()
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    public void setSpaceship(ImageView spaceship) {
        this.spaceship = spaceship;
    }

    public void setMeteor(ImageView meteor) {
        this.meteor = meteor;
    }
}
