package com.example.bntwng.scriptcalculater;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button doButton;
    EditText inputFormula;
    TextView resultView;

    InputMethodManager inputMethodManager;

    protected void calculate(){
        try{
            node n = new node(inputFormula.getText().toString());
            resultView.setText(String.valueOf(n.getValue()));
        }catch(Exception e){
            resultView.setText("error!");
        }
        //String text = inputFormula.getText().toString();
        //resultView.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = (TextView)findViewById(R.id.resultView);

        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputFormula = (EditText) findViewById(R.id.inputFormula);
        inputFormula.setOnKeyListener(new View.OnKeyListener() {
            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(inputFormula.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    //計算
                    calculate();

                    return true;
                }
                return false;
            }
        });

        doButton =  (Button) findViewById(R.id.doButton);
        doButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
               // エディットテキストのテキストを取得
                //String text = inputFormula.getText().toString();
                // 取得したテキストを TextView に張り付ける

                //doButton.setText(text);
                //resultView.setText(text);
            }
        });
    }
}

