package sk.tuke.hra;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class HardLevelActivity extends AppCompatActivity {

    private ConstraintLayout gameLayout;
    private ImageView spaceship, meteor;
    float spaceshipX, spaceshipY;
    private TextView score;
    private DrawView drawView;
    private int lives = 3;
    private TextView livesTextView;

    private Random random;
    private static final int ANIMATION_DURATION = 4000; // Animation duration in milliseconds
    private static final int SCREEN_WIDTH = 1080; // Screen width in pixels
    private static final int SCREEN_HEIGHT = 2920; // Screen height in pixels
    private static final int METEOR_WIDTH = 50; // Meteor width in pixels
    private static final int METEOR_HEIGHT = 50; // Meteor height in pixels
    private static  final int METEOR_FREQUENCY = 1000;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Skryť stavový riadok
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_hard_level);

        gameLayout = findViewById(R.id.hard_layout);
        score = findViewById(R.id.score_hard);

        spaceship = findViewById(R.id.spaceship_hard);

        drawView = findViewById(R.id.draw_view_hard);
        drawView.setSpaceship(spaceship);

        spaceship.setOnTouchListener((v, event) -> {
            touchHandler(event);
            return true;
        });

        random = new Random();

        meteorsFall();

        livesTextView = findViewById(R.id.life_hard);
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

    private void meteorsFall() {

        AnimatorSet animatorSet = new AnimatorSet();

        for (int i = 0; i < 10; i++) {
            ImageView meteor = createMeteor();
            Animator animator = createAnimator(meteor);

            animator.setStartDelay(i * METEOR_FREQUENCY);
            animatorSet.playTogether(animator);

            if (checkCollision(meteor, spaceship)) {
                updateLives();
            }
        }
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.start();
    }

    @NonNull
    private ImageView createMeteor() {

        ImageView meteor = new ImageView(this);
        meteor.setImageResource(R.drawable.meteor);
        meteor.setLayoutParams(new ViewGroup.LayoutParams(METEOR_WIDTH, METEOR_HEIGHT));

        int x = random.nextInt(SCREEN_WIDTH - METEOR_WIDTH);
        int y = -METEOR_HEIGHT;
        meteor.setX(x);
        meteor.setY(y);

        ViewGroup layout = findViewById(R.id.normal_layout);
        layout.addView(meteor);

        return meteor;
    }

    @NonNull
    private Animator createAnimator(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", SCREEN_HEIGHT);
        animator.setInterpolator(new LinearInterpolator());

        return animator;
    }

    private boolean checkCollision(ImageView meteor, ImageView spaceship) {
        Rect meteorRect = new Rect();
        meteor.getHitRect(meteorRect);

        Rect spaceshipRect = new Rect();
        spaceship.getHitRect(spaceshipRect);

        boolean collision = Rect.intersects(meteorRect, spaceshipRect);

        if (collision) {
            updateLives();
        } else if (meteor.getY() >= SCREEN_HEIGHT) {
            updateScore();
        }

        return collision;
    }

    @SuppressLint("SetTextI18n")
    private void updateLives() {
        lives--;
        livesTextView.setText("Lives: " + lives);
    }

    private void updateScore() {
        int currentScore = Integer.parseInt(score.getText().toString());
        int newScore = currentScore + 10;
        score.setText(String.valueOf(newScore));
    }
}
/*int currentScore = Integer.parseInt(score.getText().toString());
        int newScore = currentScore + 10;
        score.setText(String.valueOf(newScore));*/
