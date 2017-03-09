package com.example.bntwng.scriptcalculater;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    Button doButton;
    EditText inputFormula;
    EditText memoEditor;
    //TextView resultView;

    InputMethodManager inputMethodManager;

    protected void calculate(){
        try{
            node n = new node(inputFormula.getText().toString());

            StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
            sb.append(n.getFormula());
            sb.append(" =");
            sb.append(System.getProperty("line.separator"));
            sb.append(BigDecimal.valueOf(n.getValue()).toPlainString());
            sb.append(System.getProperty("line.separator"));

            memoEditor.append(sb.toString());
        }catch(Exception e){
            StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
            sb.append(e.getMessage());
            sb.append(System.getProperty("line.separator"));
            memoEditor.append(sb.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        memoEditor = (EditText) findViewById(R.id.memoEditor);

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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

