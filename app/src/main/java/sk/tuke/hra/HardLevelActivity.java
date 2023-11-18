package sk.tuke.hra;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HardLevelActivity extends AppCompatActivity {

    private ConstraintLayout gameLayout;
    private ImageView spaceship, meteor;
    float spaceshipX, spaceshipY;
    private TextView score;
    private DrawView drawView;

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
}
