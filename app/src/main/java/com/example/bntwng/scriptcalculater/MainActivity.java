package com.example.bntwng.scriptcalculater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    Button doButton;
    EditText inputFormula;
    EditText memoEditor;
    //TextView resultView;

    InputMethodManager inputMethodManager;



    protected void calculate(){
        try{
            String s = inputFormula.getText().toString();
            if(s.equals(""))return;//何も入っていなければリターン

            node n = new node(s);

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

    protected void saveMemo(){//メモを保存、現在試験用の状態
        final EditText editView = new EditText(MainActivity.this);
        new AlertDialog.Builder(MainActivity.this)
                //.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ファイル名を入力")
                //setViewにてビューを設定します。
                .setView(editView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //入力した文字をトースト出力する
                        Toast.makeText(MainActivity.this,
                                editView.getText().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    /*
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("")))
        }catch (Exception e){
            StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
            sb.append(e.getMessage());
            sb.append(System.getProperty("line.separator"));
            memoEditor.append(sb.toString());
        }finally {
            try{
                if(bw != null)bw.close();
            }catch (Exception e){
                e.printStackTrace();//とりあえずエラーが出たら書いておく
            }
        }*/
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
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.option_menu_item0://保存
                saveMemo();
                break;
            case R.id.option_menu_item1://読み込み
                break;
            case R.id.option_menu_item2://ヘルプ？増やす予定で
                break;
        }
        return true;
    }
}

