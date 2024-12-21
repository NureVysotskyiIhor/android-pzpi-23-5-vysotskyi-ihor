package ihor.vysotskyi.nure;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Handler mainHandler;
    private Handler backgroundHandler;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ініціалізуємо RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Заповнюємо дані
        dataset = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            dataset.add("Item " + i);
        }
        // Створюємо адаптер і встановлюємо його для RecyclerView
        adapter = new MyAdapter(dataset);
        recyclerView.setAdapter(adapter);
        // AlertDialog
        Button showDialogButton = findViewById(R.id.showDialogButton);
        showDialogButton.setOnClickListener(v -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("Діалог")
                .setMessage("Це приклад AlertDialog.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Дія при натисканні OK
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Дія при натисканні Cancel
                })
                .show());

        // DatePickerDialog
        Button showDatePickerButton = findViewById(R.id.showDatePickerButton);
        showDatePickerButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Обробка вибраної дати
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Custom Dialog
        Button showCustomDialogButton = findViewById(R.id.showCustomDialogButton);
        showCustomDialogButton.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);

            new AlertDialog.Builder(MainActivity.this)
                    .setView(dialogView)
                    .setPositiveButton("OK", (dialog, id) -> {
                        // Обробка даних
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        // Закриття діалогу
                    })
                    .create()
                    .show();
        });

        // Handler для головного потоку
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                TextView textView = findViewById(R.id.handlerMessageTextView);
                textView.setText("Message received: " + msg.what);
            }
        };

        // Фоновий Handler
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

        // Запуск Handler з затримкою
        Button startHandlerButton = findViewById(R.id.startHandlerButton);
        startHandlerButton.setOnClickListener(v -> {
            // Використання поста з затримкою
            mainHandler.postDelayed(() -> {
                TextView textView = findViewById(R.id.handlerMessageTextView);
                textView.setText("Handler executed after delay");
            }, 5000); // 5 секунди

            // Виконання задачі у фоновому потоці
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Імітація довгої задачі
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Відправка повідомлення в головний потік
                mainHandler.post(() -> {
                    TextView textView = findViewById(R.id.handlerMessageTextView);
                    textView.setText("Updated from background thread");
                });
            }).start();

            // Використання HandlerThread
            backgroundHandler.post(() -> {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = mainHandler.obtainMessage();
                msg.what = 3; // Код повідомлення
                mainHandler.sendMessage(msg);
            });
        });
    }
}
