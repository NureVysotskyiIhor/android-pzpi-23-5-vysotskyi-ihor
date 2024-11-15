package ihor.vysotskyi.nure;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private boolean isOperatorClicked = false;
    //private boolean checker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result);


        if (savedInstanceState != null) {
            String restoredText = savedInstanceState.getString("RESULT_TEXT", "");
            resultTextView.setText(restoredText);
        }
        // Встановлюємо слухачів на кнопки
        setButtonListeners();
    }

    private void setButtonListeners() {
        findViewById(R.id.btn0).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn1).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn2).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn3).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn4).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn5).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn6).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn7).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn8).setOnClickListener(this::onNumberClick);
        findViewById(R.id.btn9).setOnClickListener(this::onNumberClick);
        // Додайте слухачі для всіх числових кнопок та дій
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperatorClick("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> onOperatorClick("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> onOperatorClick("*"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> onOperatorClick("/"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> onEqualsClick());
        findViewById(R.id.btnDot).setOnClickListener(v -> onDotClick());
        // Додаємо обробку кнопки очищення
        findViewById(R.id.btnClear).setOnClickListener(v -> onClearClick());
    }

    private void onNumberClick(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        if (isOperatorClicked) {
            resultTextView.setText("");
            isOperatorClicked = false;
            //checker = true;
        }

        String text = resultTextView.getText().toString();
        if (text.equals("0")) {
            resultTextView.setText(number);
        } else {
            resultTextView.append(number);
        }

    }

    private void onOperatorClick(String operator) {
        //if (!checker){
        firstNumber = Double.parseDouble(resultTextView.getText().toString());
        this.operator = operator;
        isOperatorClicked = true;
/*       }else{
           onEqualsClick();
       }
        checker = false;*/
    }

    private void onEqualsClick() {
        secondNumber = Double.parseDouble(resultTextView.getText().toString());

        double result = 0;
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                result = secondNumber != 0 ? firstNumber / secondNumber : 0;
                break;
            default:
                result = Double.parseDouble(resultTextView.getText().toString());
                break;
        }

        resultTextView.setText(String.valueOf(result));
        isOperatorClicked = true;
/*        if (checker){
            firstNumber = result;
        }
        checker = false;*/

    }

    private void onClearClick() {
        resultTextView.setText("0");
        firstNumber = 0;
        secondNumber = 0;
        operator = "";
        isOperatorClicked = false;
    }

    private void onDotClick() {
        String currentText = resultTextView.getText().toString();

        // Перевіряємо, чи вже є десятковий роздільник у поточному числі
        if (!currentText.contains(".")) {
            resultTextView.append(".");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Зберігаємо текст із TextView
        String currentText = resultTextView.getText().toString();
        outState.putString("RESULT_TEXT", currentText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Відновлюємо текст у TextView
        if (savedInstanceState != null) {
            String restoredText = savedInstanceState.getString("RESULT_TEXT", "");
            resultTextView.setText(restoredText);
        }
    }

}
