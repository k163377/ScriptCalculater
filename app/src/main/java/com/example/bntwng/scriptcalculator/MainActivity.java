package com.example.bntwng.scriptcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    static String ln = System.getProperty("line.separator");
    //画面のパーツ
    AdView mAdView;
    TextView messageView;
    EditText inputFormula;
    Button doButton;
    EditText memoEditor;

    InputMethodManager inputMethodManager;
    //webiew関連
    WebView wv;
    boolean webViewIsEnable = false;//ウェブビューかどうかの判定
    //バックアップと復元関連
    private void backUp(){//メモを保存
        try {
            File file = new File(getFilesDir(),"temp");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            pw.println(memoEditor.getText());
            pw.close();
        }catch (IOException e){
            messageView.setText("バックアップに失敗しました、念のためデータの外部への保存を推奨します");
        }
    }
    private void initMemoEditor(){//メモエディタの初期化と以前の内容からの復元
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
        }
    }
    //AdViewの初期化
    private void initAdView(){
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    //説明用htmlを作成するためのスレッド
    private class loadDescription extends Thread{
        public String url = "";//暫定でpublicに
        int index;

        public loadDescription(int i){
            index = i;
            switch (i){//どのURLを読むかを参照
                case 0:
                    url = (String)getResources().getText(R.string.constants_url);
                    break;
                case 1:
                    url = (String)getResources().getText(R.string.operators_url);
                    break;
                case 2:
                    url = (String)getResources().getText(R.string.functions_url);
                    break;
            }
        }
        public void run(){
            if(url.equals(""))return;
            try {
                URL u = new URL(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(u.openConnection().getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder("<html><body><meta http-equiv=\"Content-Type\" content=\"text/html\"; charset=\"UTF-8\";><div style=\"border: 1px solid rgb(0, 0, 0);rules: 1px solid rgb(0, 0, 0;)\"><table frame=\"border\">");
                String s;

                s = in.readLine();
                while(s.indexOf("markdown-body") == -1)s = in.readLine();
                //while(!s.equals("<table>"))s = in.readLine();
                while(s.indexOf("</div>") == -1){
                    if(s.indexOf("<table>")!=-1){
                        sb.append("<table frame=\"border\" rules=\"all\" >");
                    }else sb.append(s);
                    sb.append(System.getProperty("line.separator"));
                    s = in.readLine();
                }
                in.close();
                sb.append("</div></body></html>");

                File file = new File(getFilesDir(),String.valueOf(index)+".html");
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                pw.print(sb.toString());
                pw.close();
            }catch (Exception e){
            }
        }
    }
    //計算
    private void calculate(){
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
        //解説のファイルを読み込み、多分ネット接続とストレージアクセスする都合で初期化中一番時間喰うので一番上で宣言
        File f = new File(getFilesDir(),"0.html");
        loadDescription ld = new loadDescription(0);
        ld.start();
        f = new File(getFilesDir(),"1.html");
        loadDescription ld2 = new loadDescription(1);
        ld2.start();
        f = new File(getFilesDir(),"2.html");
        loadDescription ld3 = new loadDescription(2);
        ld3.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//レイアウト読み込み
        //広告
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8906600258681229~8490126796");
        initAdView();
        //入力の監視
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //メッセージ表示部
        messageView = (TextView)findViewById(R.id.messageView);
        //計算入力部
        inputFormula = (EditText)findViewById(R.id.inputFormula);
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
        //計算開始ボタン
        doButton = (Button) findViewById(R.id.doButton);
        doButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
        //メモ帳部
        initMemoEditor();//バックアップからの復元
    }

    @Override
    public void onDestroy(){
        backUp();
        if(mAdView != null) mAdView.destroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if(mAdView != null) mAdView.pause();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) mAdView.resume();
    }

    @Override//メニュー関連
    public boolean onCreateOptionsMenu(Menu menu) {//メニューの表示
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){//メニューが選ばれた時の対応
        switch(item.getItemId()){
            case R.id.option_menu_item0://定数
                if(!webViewIsEnable) {//webViewじゃなかったら初期化
                    setContentView(R.layout.web_view);
                    wv = (WebView)findViewById(R.id.webView);//webView初期化
                    webViewIsEnable = true;
                    initAdView();
                }
                wv.loadUrl("file://" + getFilesDir().toString()+"/0.html");
                break;
            case R.id.option_menu_item1://演算子
                if(!webViewIsEnable) {//webViewじゃなかったら初期化
                    setContentView(R.layout.web_view);
                    wv = (WebView)findViewById(R.id.webView);//webView初期化
                    webViewIsEnable = true;
                    initAdView();
                }
                wv.loadUrl("file://" + getFilesDir().toString()+"/1.html");
                break;
            case R.id.option_menu_item2://関数
                if(!webViewIsEnable) {//webViewじゃなかったら初期化
                    setContentView(R.layout.web_view);
                    wv = (WebView)findViewById(R.id.webView);//webView初期化
                    webViewIsEnable = true;
                    initAdView();
                }
                wv.loadUrl("file://" + getFilesDir().toString()+"/2.html");
                break;
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){//webViewを起動したときの戻るキーの挙動制御
        if(keyCode==KeyEvent.KEYCODE_BACK && webViewIsEnable){
            setContentView(R.layout.activity_main);
            //ここでAdViewを読み直さないとadViewが消えたままになる
            initAdView();
            webViewIsEnable = false;

            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}