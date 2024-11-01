package ihor.vysotskyi.nure;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second); // Підключення макета другого Activity

        // Знаходимо кнопку завершення Activity
        Button finishButton = findViewById(R.id.finishButton1);

        // Додаємо обробник натискання на кнопку
        finishButton.setOnClickListener(v -> {
            // Закриваємо це Activity
            finish();
        });
    }
}

