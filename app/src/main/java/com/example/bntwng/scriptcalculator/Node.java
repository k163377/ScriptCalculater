package com.example.bntwng.scriptcalculator;

/**
 * Created by bntwng on 2017/03/12.
 */

import java.util.ArrayList;

public class Node {
    protected int left,right;
    protected double value = 0;

    protected final Node nodeAnalysis(String formula) throws IllegalArgumentException{
        //とりあえず通常カッコで括られている場合のみを分析
        //予定ではこのノードが何になるかを判定して、Nodeを作って返す
        int depth = 1;
        char c;

        left = right-1;

        while( (0 < depth) && (-1<left) ){
            c = formula.charAt(left);

            if(c == '(')depth--;
            else if(c == ')')depth++;

            left--;
        }
        if(depth!=0)throw new IllegalArgumentException("カッコの数が合いません");

        Node n = new Node(formula.substring(left + 2,right));//カッコ閉じの次まで読んでしまうのでleft+2しないと上手く取れない
        right = left;//カッコ閉じの次の文字を指す
        return n;
    }
    protected final Node numberAnalysis(String formula) throws IllegalArgumentException{
        //とりあえず数字のみを分析
        //予定では数字オンリーのノードを作って返す
        boolean isDecimal = false;
        char c;

        left = right;

        while(left > -1){
            c = formula.charAt(left);
            CharMap.nonterminalSymbol ns = CharMap.symbolInspection(c);

            if(ns != CharMap.nonterminalSymbol.NUMBER)break;//別のものが入ったので終了、ホントはノード作成に移る
            else if(c == '.'){
                if(isDecimal == true)throw new IllegalArgumentException("1つの数に小数点が複数含まれています");
                isDecimal = true;
            }
            left--;//数字なら続行
        }
        //数字の時はrightを+1してsubstringを取らないとケツが切れる
        Node n = new Node(Double.parseDouble(formula.substring(left+1, right+1)));
        right = left;//最初の数字でないものもしくは-1とかが入る
        return n;
    }
    //計算系
    protected final void calculatingFactorial(ArrayList<Node> nodeArray,ArrayList<Integer> operatorArray) throws IllegalArgumentException{//階乗
        int i = 0;
        while(i < operatorArray.size()){
            if(operatorArray.get(i) == '!'){
                double d = nodeArray.get(i).getValue();
                int n = (int)d;
                if(d == n){
                    long lng = 1;
                    while(n > 1){
                        lng *= n;
                        n--;
                    }

                    nodeArray.set(i, new Node((double)lng));
                    operatorArray.remove(i);
                }else throw new IllegalArgumentException("!演算子が自然数でない数を計算しようとしています");
            }else i++;
        }
    }
    protected final void calculatingPower(ArrayList<Node> nodeArray,ArrayList<Integer> operatorArray){//累乗
        int i = 0;
        while(i < operatorArray.size()){
            if(operatorArray.get(i) == '^'){
                nodeArray.set(i, new Node(Math.pow(nodeArray.get(i+1).getValue(), nodeArray.get(i).getValue())));
                nodeArray.remove(i+1);
                operatorArray.remove(i);
            }else i++;
        }
    }
    protected final void calculatingMultiplication(ArrayList<Node> nodeArray,ArrayList<Integer> operatorArray){//掛け割剰余
        for(int i = operatorArray.size()-1;i >= 0;i--){
            int c = operatorArray.get(i);
            if(c == '*'){
                nodeArray.set(i, new Node(nodeArray.get(i+1).getValue() * nodeArray.get(i).getValue()));
                nodeArray.remove(i+1);
                operatorArray.remove(i);
            }else if(c == '/'){
                nodeArray.set(i, new Node(nodeArray.get(i+1).getValue() / nodeArray.get(i).getValue()));
                nodeArray.remove(i+1);
                operatorArray.remove(i);
            }else if(c == '%'){
                nodeArray.set(i, new Node(nodeArray.get(i+1).getValue() % nodeArray.get(i).getValue()));
                nodeArray.remove(i+1);
                operatorArray.remove(i);
            }
        }
    }
    protected final void calculatingPermutationAndCombination(ArrayList<Node> nodeArray,ArrayList<Integer> operatorArray) throws IllegalArgumentException{//順列組み合わせ
        for(int i = operatorArray.size()-1;i >= 0;i--){
            int c = operatorArray.get(i);
            if(c =='P'||c == 'C'){
                double N = nodeArray.get(i+1).getValue();
                double R = nodeArray.get(i).getValue();
                int n = (int)N;
                int r = (int)R;

                if(N!=n||R!=r||n<0||r<0)throw new IllegalArgumentException((char)c + "演算子が自然数でない数を計算しようとしています");
                if(n < r)throw new IllegalArgumentException((char)c + "演算子について、nがrより小さいです");

                long ans = 1;
                while(n > r+1){
                    ans*=n;
                    n--;
                }
                if(c == 'C'){
                    while(r > 1){
                        ans /= r;
                        r--;
                    }
                }
                nodeArray.set(i, new Node((double)ans));
                nodeArray.remove(i+1);
                operatorArray.remove(i);
            }
        }
    }
    protected final void calculatingAddition(ArrayList<Node> nodeArray,ArrayList<Integer> operatorArray){//足し算引き算
        for(int i = operatorArray.size()-1;i > -1; i--){
            int c = operatorArray.get(i);
            if(c == '+')nodeArray.set(i, new Node(nodeArray.get(i + 1).getValue() + nodeArray.get(i).getValue()));
            else if(c == '-')nodeArray.set(i, new Node(nodeArray.get(i + 1).getValue() - nodeArray.get(i).getValue()));
        }
    }

    protected final double calculate(String formula) throws IllegalArgumentException{//valueに入れるとこまでやる
        //rightとleftはこちらで極力触らないようにコードを組もう
        right = formula.length()-1;
        ArrayList<Node> nodeArray = new ArrayList<Node>();
        ArrayList<Integer> operatorArray = new ArrayList<Integer>();

        boolean[] needsCalculation = new boolean[5];
        for(short i = 0; i < 5; i++)needsCalculation[i]=false;

        char c;
        CharMap.nonterminalSymbol ns;
        boolean isNode = false;//ノードか演算子かの判定

        try{
            while(right > -1){
                c = formula.charAt(right);
                ns = CharMap.symbolInspection(c);

                if(ns == CharMap.nonterminalSymbol.OPERATOR){
                    //オペレータ処理、!演算子の処理がくっそ怠い、取りあえず暫定で全部オペレーター配列に突っ込む感じで処理
                    //ケツから計算かまして!を消していくしかないか
                    //オペレータの有無の判定はHashMapとか使うといいかもね
                    if(c == '!'){
                        if(isNode == true)throw new IllegalArgumentException("演算子の整合性が取れていません");
                        operatorArray.add((int)c);
                        needsCalculation[0] = true;
                        right--;
                    }else if(isNode == false)throw new IllegalArgumentException("演算子の整合性が取れていません");
                    else{
                        operatorArray.add((int)c);
                        isNode = false;
                        if(c == '^')needsCalculation[1] = true;
                        else if(c == '*'||c == '/'||c == '%')needsCalculation[2]=true;
                        else if(c == 'P'||c == 'C')needsCalculation[3]=true;
                        else if(c == '+'||c == '-')needsCalculation[4]=true;

                        right--;
                    }
                }else if(ns == CharMap.nonterminalSymbol.NUMBER){
                    //数字
                    if(isNode == true)throw new IllegalArgumentException("演算子無しで値が連続しています");
                    nodeArray.add(numberAnalysis(formula));
                    isNode = true;
                }else {//数字でも演算子でもない時、ノード系、定数系、ゴミがここに来る
                    boolean nodeMaked = false;
                    if(c == ')'){//ノード作り
                        nodeArray.add(nodeAnalysis(formula));//ノード
                        nodeMaked = true;
                    }
                    //定数系
                    else if(c == 'c'){//光速
                        nodeArray.add(new Node(physicalConstants.c));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'e'){//ネイピア数e
                        nodeArray.add(new Node(Math.E));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'l'&&formula.charAt(Math.max(0,right-1)) == 'e'){//電気素量e
                        nodeArray.add(new Node(physicalConstants.el));
                        nodeMaked = true;
                        right-=2;
                    }else if(c == 'f'){//ファラデー定数f
                        nodeArray.add(new Node(physicalConstants.f));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'g'){//重力定数
                        nodeArray.add(new Node(physicalConstants.g));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'h'){//プランク定数
                        nodeArray.add(new Node(physicalConstants.h));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'k'){//ボルツマン定数
                        nodeArray.add(new Node(physicalConstants.k));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'l'&&formula.charAt(Math.max(0,right-1)) != 'e'){//アボガドロ定数
                        nodeArray.add(new Node(physicalConstants.l));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'i'&&formula.charAt(Math.max(0,right-1)) == 'p'){//円周率
                        nodeArray.add(new Node(Math.PI));
                        nodeMaked = true;
                        right--;
                    }else if(c == 'r'){//モル気体定数
                        nodeArray.add(new Node(physicalConstants.r));
                        nodeMaked = true;
                        right--;
                    }
                    //エラーチェックとゴミの読み飛ばし
                    else if(c == '(')throw new IllegalArgumentException("カッコの数が合いません");//カッコの数が合わないのでエラー
                    else right--;//今はチェック無しでごみを読み飛ばす

                    //ノードが連続していないかチェック
                    if(isNode == true && nodeMaked == true)throw new IllegalArgumentException("演算子無しで値が連続しています");
                    else if(nodeMaked==true)isNode = true;
                }
            }

            //演算開始
            if(needsCalculation[0] == true)calculatingFactorial(nodeArray,operatorArray);
            //単項演算子が消えたので、ここで式の先頭に有る符号になっている演算子を処理する
            if(nodeArray.size() == operatorArray.size()){
                int o = operatorArray.get(operatorArray.size()-1);
                if(o == '+')operatorArray.remove(operatorArray.size()-1);//+なら無視
                else if(o == '-'){
                    nodeArray.set(nodeArray.size()-1, new Node(0 - nodeArray.get(nodeArray.size()-1).getValue()));
                    operatorArray.remove(operatorArray.size()-1);
                }else throw new IllegalArgumentException("不正な演算子が先頭にあります");
            }

            if(needsCalculation[1] == true)calculatingPower(nodeArray,operatorArray);
            if(needsCalculation[2] == true)calculatingMultiplication(nodeArray,operatorArray);
            if(needsCalculation[3] == true)calculatingPermutationAndCombination(nodeArray,operatorArray);
            if(needsCalculation[4] == true)calculatingAddition(nodeArray,operatorArray);

            return nodeArray.get(0).getValue();//ここの前に確認はした方がいいかも

            //値はコンストラクタで代入?
        }catch (Exception e){
            throw e;
        }
    }

    public Node(String formula)throws IllegalArgumentException{
        try{
            value = calculate(formula);
        }catch (Exception e){
            throw e;
        }
    }
    public Node(double d){
        this.value = d;
    }
    public final double getValue() {
        return this.value;
    }
}
