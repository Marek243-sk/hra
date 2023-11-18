package sk.tuke.hra;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;
import java.util.Random;

public class EasyLevelActivity extends AppCompatActivity {

    Context context;
    Velocity velocity = new Velocity(10, 20);
    final long UPDATE_MILLIS = 30;
    float TEXT_SIZE = 120;
    //Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    private ConstraintLayout gameLayout;
    private ImageView spaceship, meteor;
    float spaceshipX, spaceshipY;
    private TextView score;
    private DrawView drawView;
    private Handler meteorHandler = new Handler();
    private final int METEOR_INTERVAL = 2000;

    @SuppressLint("ClickableViewAccessibility") //kvôli "performClick()"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Skryť stavový riadok
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_easy_level);

        gameLayout = findViewById(R.id.easy_layout);
        score = findViewById(R.id.score_easy);

        spaceship = findViewById(R.id.spaceship_easy);

        meteor = findViewById(R.id.meteor_easy);

        drawView = findViewById(R.id.draw_view_easy);
        drawView.setSpaceship(spaceship);

        spaceship.setOnTouchListener((v, event) -> {
            touchHandler(event);
            return true;
        });



        //meteorHandler.postDelayed(meteorGenerator, METEOR_INTERVAL);

        /*new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                meteorsFalling();
                handler.postDelayed(this, 3000);
            }
        }, 3000);;*/

    }

    //Ovládanie vesmírnej lode
    private void spaceshipMove(float x1, float y1) {

        float x2 = spaceship.getX() + x1;
        float y2 = spaceship.getY() + y1;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        //Obrázok neprejde cez hranice obrazovky
        x2 = (x2 < 0) ? 0 : (x2 + spaceship.getWidth() > screenWidth) ? screenWidth - spaceship.getWidth() : x2;
        y2 = (y2 < 0) ? 0 : (y2 + spaceship.getHeight() > screenHeight) ? screenHeight - spaceship.getHeight() : y2;

        //Nastavenie nových hodnôt
        spaceship.setX(x2);
        spaceship.setY(y2);
    }

    private void touchHandler(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                spaceshipX = event.getRawX();
                spaceshipY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float x1 = event.getRawX() - spaceshipX;
                float y1 = event.getRawY() - spaceshipY;

                spaceshipMove(x1, y1);
                spaceshipX = event.getRawX();
                spaceshipY = event.getRawY();
                break;
        }
    }

    //Padanie meteoritov
/*
    private Runnable meteorGenerator = new Runnable() {
        @Override
        public void run() {
            meteorsFalling();
            meteorHandler.postDelayed(this, METEOR_INTERVAL);
        }
    };

    private void meteorsFalling() {
        ImageView newMeteor = new ImageView(context);
        newMeteor.setImageResource(R.drawable.meteor);
        gameLayout.addView(newMeteor);

        //Náhodná pozícia meteoritu vzhľadom na os x
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int randomX = new Random().nextInt(screenWidth - newMeteor.getWidth());
        newMeteor.setX(randomX);

        //Spustenie animácie
        ObjectAnimator meteorAnimator = ObjectAnimator.ofFloat(newMeteor, "translationY", 0f, 2000f);
        meteorAnimator.setDuration(3000);
        meteorAnimator.start();

        //Listener pre zistenie, kedy meteorit dosiahol spodok obrazovky
        meteorAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                gameLayout.addView(newMeteor);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                gameLayout.removeView(newMeteor); //odstránenie meteoru
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        meteorAnimator.start();
    }
    */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ImageView newMeteor = new ImageView(context);
            newMeteor.setImageResource(R.drawable.meteor);
            gameLayout.addView(newMeteor);

            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            Random random = new Random();
            int randomX = random.nextInt(screenWidth - newMeteor.getWidth());
            newMeteor.setX(randomX);

            ObjectAnimator meteorAnimator =
                    ObjectAnimator.ofFloat(newMeteor, "translationY", 0f, 2000f);
            meteorAnimator.setDuration(3000);
            meteorAnimator.start();

            meteorAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    gameLayout.addView(newMeteor);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    gameLayout.removeView(newMeteor); //odstránenie meteoru
                }
            });
            meteorAnimator.start();
        }
    };

    /*import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 3000; // Animation duration in milliseconds
    private static final int SCREEN_WIDTH = 1080; // Screen width in pixels
    private static final int SCREEN_HEIGHT = 1920; // Screen height in pixels
    private static final int METEOR_WIDTH = 100; // Meteor width in pixels
    private static final int METEOR_HEIGHT = 100; // Meteor height in pixels

    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        // Create and start the animation
        startAnimation();
    }

    private void startAnimation() {
        // Create a container for the animators
        AnimatorSet animatorSet = new AnimatorSet();

        // Create an animator for each meteor
        for (int i = 0; i < 10; i++) {
            ImageView meteor = createMeteor();
            Animator animator = createAnimator(meteor);
            animatorSet.playTogether(animator);
        }

        // Set the animation duration
        animatorSet.setDuration(ANIMATION_DURATION);

        // Start the animation
        animatorSet.start();
    }

    private ImageView createMeteor() {
        // Create a new ImageView for the meteor
        ImageView meteor = new ImageView(this);
        meteor.setImageResource(R.drawable.meteor); // Set the meteor image resource
        meteor.setLayoutParams(new ViewGroup.LayoutParams(METEOR_WIDTH, METEOR_HEIGHT)); // Set the meteor size

        // Set the initial position of the meteor
        int x = random.nextInt(SCREEN_WIDTH - METEOR_WIDTH);
        int y = -METEOR_HEIGHT;
        meteor.setX(x);
        meteor.setY(y);

        // Add the meteor to the layout
        ViewGroup layout = findViewById(R.id.layout);
        layout.addView(meteor);

        return meteor;
    }

    private Animator createAnimator(View view) {
        // Create an ObjectAnimator for the meteor
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", SCREEN_HEIGHT);

        // Set the interpolator to create an accelerating effect
        animator.setInterpolator(new AccelerateInterpolator());

        return animator;
    }
}
*/
}
