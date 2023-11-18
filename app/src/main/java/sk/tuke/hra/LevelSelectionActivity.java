package sk.tuke.hra;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectionActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Skryť stavový riadok
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_level_selection);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Nastavenie tlačidiel pre obtiažnosti
        Button buttonEasy = findViewById(R.id.button_easy);
        Button buttonNormal = findViewById(R.id.button_normal);
        Button buttonHard = findViewById(R.id.button_hard);

        buttonEasy.setOnClickListener(v -> {
            startActivity(new Intent(LevelSelectionActivity.this, EasyLevelActivity.class));
            finish();
        });

        buttonNormal.setOnClickListener(v -> {
            startActivity(new Intent(LevelSelectionActivity.this, NormalLevelActivity.class));
            finish();
        });

        buttonHard.setOnClickListener(v -> {
            startActivity(new Intent(LevelSelectionActivity.this, HardLevelActivity.class));
            finish();
        });
    }
}
