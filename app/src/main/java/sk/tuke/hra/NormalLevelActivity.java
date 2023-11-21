package sk.tuke.hra;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class NormalLevelActivity extends AppCompatActivity {

    private static final int SPACESHIP_WIDTH = 200;
    private static final int SPACESHIP_HEIGHT = 200;
    private ConstraintLayout gameLayout;
    private ImageView spaceship, meteor;
    float spaceshipX, spaceshipY;
    private TextView scoreTextView;
    private int score = 0;
    private int lives = 3;
    private TextView livesTextView;

    private Random random;
    private static final int ANIMATION_DURATION = 4000; // Animation duration in milliseconds
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private static final int METEOR_WIDTH = 50; // Meteor width in pixels
    private static final int METEOR_HEIGHT = 50; // Meteor height in pixels
    private static final int METEOR_FREQUENCY = 1000;

    private MediaPlayer mediaPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Skryť stavový riadok
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_normal_level);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels; // Screen width in pixels
        SCREEN_HEIGHT = displayMetrics.heightPixels; // Screen height in pixels

        gameLayout = findViewById(R.id.normal_layout);
        scoreTextView = findViewById(R.id.score_normal);

        //spaceship = findViewById(R.id.spaceship_normal);

/*
        spaceship = new ImageView(this);
        spaceship.setImageResource(R.drawable.spaceship);
        spaceship.setLayoutParams(new ViewGroup.LayoutParams(SPACESHIP_WIDTH, SPACESHIP_HEIGHT));

        int x = SCREEN_WIDTH / 2 - SPACESHIP_WIDTH / 2;
        int y = SCREEN_HEIGHT - SPACESHIP_HEIGHT;
        spaceship.setX(x);
        spaceship.setY(y);
*/
        @SuppressLint("CutPasteId") ViewGroup layout = findViewById(R.id.normal_layout);
        spaceship = createSpaceship();
        layout.addView(spaceship);
        //layout.addView(meteor);



        DrawView drawView = findViewById(R.id.draw_view_normal);
        drawView.setSpaceship(spaceship);
        drawView.setMeteor(meteor);

        spaceship.setOnTouchListener((v, event) -> {
            touchHandler(event);
            return true;
        });

        random = new Random();

        meteorsFall();

        livesTextView = findViewById(R.id.life_normal);

        mediaPlayer = MediaPlayer.create(this, R.raw.normal);
        mediaPlayer.setLooping(true);
    }

    @NonNull
    private Animator createAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", SCREEN_HEIGHT);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void spaceshipMove(float x1, float y1) {
        float x2 = spaceship.getX() + x1;
        float y2 = spaceship.getY() + y1;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        // Obrázok neprejde cez hranice obrazovky
        x2 = (x2 < 0) ? 0 : (x2 + spaceship.getWidth() > screenWidth) ? screenWidth - spaceship.getWidth() : x2;
        y2 = (y2 < 0) ? 0 : (y2 + spaceship.getHeight() > screenHeight) ? screenHeight - spaceship.getHeight() : y2;
        // Nastavenie nových hodnôt
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

    private ImageView createSpaceship() {
        ImageView spaceship = new ImageView(this);
        spaceship.setImageResource(R.drawable.spaceship);
        spaceship.setLayoutParams(new ViewGroup.LayoutParams(SPACESHIP_WIDTH, SPACESHIP_HEIGHT));

        int x = SCREEN_WIDTH / 2 - SPACESHIP_WIDTH / 2;
        int y = SCREEN_HEIGHT - SPACESHIP_HEIGHT;
        spaceship.setX(x);
        spaceship.setY(y);

        return spaceship;
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

    private void meteorsFall() {

        AnimatorSet animatorSet = new AnimatorSet();

        for (int i = 0; i < 100; i++) {
            ImageView meteor = createMeteor();
            Animator animator = createAnimator(meteor);

            animator.setStartDelay(i * METEOR_FREQUENCY);
            animatorSet.playTogether(animator);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (spaceship != null && meteor.getY() >= SCREEN_HEIGHT && !checkCollision(meteor, spaceship)) {
                        gameLayout.removeView(meteor);
                        score++;
                        updateScore();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.start();
    }


    private boolean checkCollision(ImageView meteor, ImageView spaceship) {
        Rect meteorRect = new Rect();
        meteor.getHitRect(meteorRect);

        Rect spaceshipRect = new Rect();
        spaceship.getHitRect(spaceshipRect);

        return Rect.intersects(meteorRect, spaceshipRect);
    }

    @SuppressLint("SetTextI18n")
    private void updateLives() {
        lives--;
        livesTextView.setText("Lives: " + lives);
    }

    @SuppressLint("SetTextI18n")
    private void updateScore() {
        scoreTextView.setText("SCORE: " + score);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}