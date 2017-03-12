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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    static String ln = System.getProperty("line.separator");

    Button doButton;
    EditText inputFormula;
    EditText memoEditor;
    TextView messageView;

    InputMethodManager inputMethodManager;

    protected void backUp(){//メモを保存
        try {
            File file = new File(getFilesDir().getPath() + "/temp");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            pw.println(memoEditor.getText());
            pw.close();
        }catch (IOException e){
            messageView.setText("バックアップに失敗しました、念のためデータの外部への保存を推奨します");
        }
    }

    protected void calculate(){
        try{
            String s = inputFormula.getText().toString();
            if(s.equals(""))return;//何も入っていなければ何もせずリターン

            Node n = new Node(s);

            StringBuilder sb = new StringBuilder(ln);
            sb.append(s);
            sb.append(" =");
            sb.append(ln);
            sb.append(BigDecimal.valueOf(n.getValue()).toPlainString());
            sb.append(ln);

            memoEditor.append(sb.toString());
            messageView.setText("calculated");

            backUp();
        }catch(Exception e){
            messageView.setText(e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//アプリ開始時に呼ばれる
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        messageView = (TextView)  findViewById(R.id.messageView);

        memoEditor = (EditText) findViewById(R.id.memoEditor);
        try{//バックアップからの復元
            File file = new File(getFilesDir().getPath() + "/temp");
            BufferedReader br = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder(br.readLine());//null突っ込んでもsbがExceotion吐くのでちゃんと終わる
            String str = br.readLine();

            while(str!=null){
                sb.append(str);
                sb.append(ln);
                str = br.readLine();
            }
            memoEditor.setText(sb.toString());
            br.close();
        }catch (Exception e){//ファイルが無いとか読み込みミスったら何もしない
            //messageView.setText("onCreate:" + e.getMessage());//念のための出力、今はコメントアウト
        }

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
    protected void onDestroy(){
        backUp();
        super.onDestroy();
    }

    /*@Override//メニュー関連
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CHOSE_FILE_CODE && resultCode == RESULT_OK) {
                String filePath = data.getDataString().replace("file://", "");
                String decodedfilePath = URLDecoder.decode(filePath, "utf-8");
                //imageText.setText(decodedfilePath);
            }
        } catch (UnsupportedEncodingException e) {
            // いい感じに例外処理
        }
    }*/
    /*@Override//メニュー関連
    public boolean onCreateOptionsMenu(Menu menu) {//メニューの表示
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item){//メニューが選ばれた時の対応
        switch(item.getItemId()){
            case R.id.option_menu_item0://保存
                //saveMemo();
                break;
            case R.id.option_menu_item1://読み込み
                break;
            case R.id.option_menu_item2://ヘルプ？増やす予定で
                break;
        }
        return true;
    }*/
}

