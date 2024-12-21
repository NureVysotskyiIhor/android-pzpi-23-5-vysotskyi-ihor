package ihor.vysotskyi.nure;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge;
    private TextView displayText;
    private Button saveButton, loadButton, clearButton, writeFileButton, readFileButton, saveToFileButton, loadFromFileButton;
    private ListView listView;
    private DBHelper dbHelper;
    private TextView textViewFileContent;
    private final String FILE_NAME = "user_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        displayText = findViewById(R.id.displayText);
        saveButton = findViewById(R.id.saveButton);
        saveToFileButton = findViewById(R.id.saveToFileButton);
        loadFromFileButton = findViewById(R.id.loadFromFileButton);

        // Load shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "Default Name");
        String age = sharedPreferences.getString("age", "0");
        displayText.setText("Name: " + name + "\nAge: " + age);

        // Save data when button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();

                // Save to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("age", age);
                editor.apply();

                // Update displayed values
                displayText.setText("Name: " + name + "\nAge: " + age);
            }
        });

        // Save data to a file
        saveToFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();
                String data = "Name: " + name + "\nAge: " + age;

                writeToFile(FILE_NAME, data);
            }
        });

        // Load data from a file
        loadFromFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileData = readFromFile(FILE_NAME, true);
                if (fileData != null) {
                    displayText.setText(fileData);
                }
            }
        });*/
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);
        listView = findViewById(R.id.listView);
        clearButton = findViewById(R.id.clearButton);
        writeFileButton = findViewById(R.id.writeFileButton);
        readFileButton = findViewById(R.id.readFileButton);
        textViewFileContent = findViewById(R.id.textViewFileContent);

        dbHelper = new DBHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());
                dbHelper.addUser(name, age);
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getUsers();
                ArrayList<String> userList = new ArrayList<>();

                if (cursor != null && cursor.moveToFirst()) {
                    // Отримуємо індекси колонок
                    int nameIndex = cursor.getColumnIndex("name");
                    int ageIndex = cursor.getColumnIndex("age");

                    // Перевірка індексів
                    if (nameIndex != -1 && ageIndex != -1) {
                        do {
                            String name = cursor.getString(nameIndex);
                            int age = cursor.getInt(ageIndex);
                            userList.add("Name: " + name + ", Age: " + age);
                        } while (cursor.moveToNext());
                    }
                    cursor.close(); // Закриваємо курсор
                } else {
                    userList.add("No data available");
                }

                // Встановлюємо адаптер для ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        userList
                );

                listView.setAdapter(adapter);
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.clearDatabase();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        new ArrayList<>()
                );
                listView.setAdapter(adapter); // Очищаємо список
            }
        });

        writeFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFile("users_data.txt");
            }
        });

        readFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = readFromFile("users_data.txt");
                textViewFileContent.setText(content);
            }
        });
    }
    private void writeToFile(String fileName, String content) {
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Data written to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error writing to file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Method to read data from a file
    private String readFromFile(String fileName, boolean overload) {
        try (FileInputStream fis = openFileInput(fileName)) {
            StringBuilder data = new StringBuilder();
            int c;
            while ((c = fis.read()) != -1) {
                data.append((char) c);
            }
            Toast.makeText(this, "Data loaded from file", Toast.LENGTH_SHORT).show();
            return data.toString();
        } catch (IOException e) {
            Toast.makeText(this, "Error reading from file: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void writeToFile(String fileName) {
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            Cursor cursor = dbHelper.getUsers();
            StringBuilder data = new StringBuilder();

            int nameIndex = cursor.getColumnIndex("name");
            int ageIndex = cursor.getColumnIndex("age");

            if (nameIndex == -1 || ageIndex == -1) {
                Toast.makeText(this, "Error: Invalid column name", Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                int age = cursor.getInt(ageIndex);
                data.append("Name: ").append(name).append(", Age: ").append(age).append("\n");
            }
            cursor.close();

            fos.write(data.toString().getBytes());
            Toast.makeText(this, "Data written to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error writing to file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String readFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = openFileInput(fileName)) {
            int c;
            while ((c = fis.read()) != -1) {
                content.append((char) c);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error reading from file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return content.toString();
    }
}
