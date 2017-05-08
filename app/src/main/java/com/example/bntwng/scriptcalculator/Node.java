package com.example.bntwng.scriptcalculator;

import com.example.bntwng.scriptcalculator.functions.*;

import java.util.ArrayList;

//関数の大雑把な解説
//Analysis系:一つのノードを計算して返す
//calculating系:行を入力すると演算子とノードを喰って計算していく
//calculate:Stringを入力すると、文全て、もしくはカンマで区切られた部分までを喰ってdouble値を返す

public class Node {
    protected int left,right;
    protected double value = 0;

    protected final Node nodeAnalysis(String formula) throws IllegalArgumentException{
        //とりあえず通常カッコで括られている場合のみを分析
        //予定ではこのノードが何になるかを判定して、Nodeを作って返す
        int depth = 1;
        char c;
        Node n = null;
        left = right-1;
        //カッコの内容の切り出し
        while( (0 < depth) && (-1<left) ){
            c = formula.charAt(left);

            if(c == '(')depth--;
            else if(c == ')')depth++;

            left--;
        }
        if(depth!=0)throw new IllegalArgumentException("カッコの数が合いません");
        //ノードが関数かどうかを判定
        //4文字っぽく切り出す
        String s = formula.substring(Math.max(0,left-3),left+1);
        if(s.length()==4){//4文字切り出せた場合
            //逆三角関数
            if(s.equals("asin")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ASIN);
                left -= 4;
            }else if(s.equals("acos")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ACOS);
                left -= 4;
            }else if(s.equals("atan")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ATAN);
                left -= 4;
            }else if(s.equals("acsc")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ACSC);
                left -= 4;
            }else if(s.equals("asec")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ASEC);
                left -= 4;
            }else if(s.equals("acot")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ACOT);
                left -= 4;
            }
            //双曲線関数
            else if(s.equals("sinh")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.SINH);
                left -= 4;
            }else if(s.equals("cosh")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.COSH);
                left -= 4;
            }else if(s.equals("tanh")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.TANH);
                left -= 4;
            }else if(s.equals("csch")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.CSCH);
                left -= 4;
            }else if(s.equals("sech")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.SECH);
                left -= 4;
            }else if(s.equals("coth")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.COTH);
                left -= 4;
            }
            //ルート
            else if(s.equals("sqrt")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.SQRT);
                left -= 4;
            }else if(s.equals("root")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions4.ROOT);
                left -= 4;
            }
            else s = s.substring(1,4);//4文字から該当なしなら先頭一文字を消して次へ渡す
        }
        if(s.length()==3) {
            //三角関数類
            if (s.equals("sin")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.SIN);
                left -= 3;
            } else if (s.equals("cos")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.COS);
                left -= 3;
            } else if (s.equals("tan")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.TAN);
                left -= 3;
            } else if (s.equals("csc")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.CSC);
                left -= 3;
            } else if (s.equals("sec")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.SEC);
                left -= 3;
            } else if (s.equals("cot")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.COT);
                left -= 3;
            }
            //基礎的な算術関数
            else if (s.equals("log")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.LOG);
                left -= 3;
            } else if (s.equals("pow")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.POW);
                left -= 3;
            }
            //その他関数
            else if (s.equals("abs")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.ABS);
                left -= 3;
            } else if (s.equals("exp")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.EXP);
                left -= 3;
            } else if (s.equals("max")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.MAX);
                left -= 3;
            } else if (s.equals("min")) {
                n = new FunctionNode(formula.substring(Math.min(left + 2, right - 1), right), FunctionNode.functions3.MIN);
                left -= 3;
            }
        }
        //長さが4でも3でもなかった時,何も入っていないはずなのでnull検査する
        if(n == null)n = new Node(formula.substring(Math.min(left + 2, right - 1), right));//カッコ閉じの次まで読んでしまうのでleft+2しないと上手く取れない

        right = left;//カッコ閉じの次の文字を指す
        return n;
    }
    protected final Node numberAnalysis(String formula) throws IllegalArgumentException{
        //数字オンリーのノードを作って返す
        boolean isDecimal = false;
        char c;

        left = right;

        while(left > -1){
            c = formula.charAt(left);
            CharMap.nonterminalSymbol ns = CharMap.symbolInspection(c);

            if(ns != CharMap.nonterminalSymbol.NUMBER)break;//別のものが入ったので終了、ホントはノード作成に移る
            else if(c == '.'){
                if(isDecimal)throw new IllegalArgumentException("1つの数に小数点が複数含まれています");
                isDecimal = true;
            }
            left--;//数字なら続行
        }
        //数字の時はright, left共にを+1してsubstringを取らないとケツが切れる
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
                int n_r = n-r;
                while(n > n_r){
                    ans *= n;
                    n--;
                }
                if(c == 'C'){
                    while(r > 1){
                        ans = ans/r;
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

        char c = 0;
        CharMap.nonterminalSymbol ns;
        boolean isNode = false;//ノードか演算子かの判定

        try{
            while(right > -1&&(c = formula.charAt(right)) != ','){
                //c = formula.charAt(right);
                ns = CharMap.symbolInspection(c);

                if(ns == CharMap.nonterminalSymbol.OPERATOR){
                    //オペレータ処理、!演算子の処理がくっそ怠い、取りあえず暫定で全部オペレーター配列に突っ込む感じで処理
                    //ケツから計算かまして!を消していくしかないか
                    //オペレータの有無の判定はHashMapとか使うといいかもね
                    if(c == '!'){
                        if(isNode)throw new IllegalArgumentException("演算子の整合性が取れていません");
                        operatorArray.add((int)c);
                        needsCalculation[0] = true;
                        right--;
                    }else if(!isNode)throw new IllegalArgumentException("演算子の整合性が取れていません");
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
                    if(isNode)throw new IllegalArgumentException("演算子無しで値が連続しています");
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
                    if(isNode && nodeMaked)throw new IllegalArgumentException("演算子無しで値が連続しています");
                    else if(nodeMaked)isNode = true;
                }
            }

            //演算開始
            if(needsCalculation[0])calculatingFactorial(nodeArray,operatorArray);
            //単項演算子が消えたので、ここで式の先頭に有る符号になっている演算子を処理する
            if(nodeArray.size() == operatorArray.size()){
                int o = operatorArray.get(operatorArray.size()-1);
                if(o == '+')operatorArray.remove(operatorArray.size()-1);//+なら無視
                else if(o == '-'){
                    nodeArray.set(nodeArray.size()-1, new Node(0 - nodeArray.get(nodeArray.size()-1).getValue()));
                    operatorArray.remove(operatorArray.size()-1);
                }else throw new IllegalArgumentException("不正な演算子が先頭にあります");
            }

            if(needsCalculation[1])calculatingPower(nodeArray,operatorArray);
            if(needsCalculation[2])calculatingMultiplication(nodeArray,operatorArray);
            if(needsCalculation[3])calculatingPermutationAndCombination(nodeArray,operatorArray);
            if(needsCalculation[4])calculatingAddition(nodeArray,operatorArray);

            return nodeArray.get(0).getValue();//ここの前に確認はした方がいいかも
        }catch (Exception e){
            throw e;
        }
    }

    public Node(String formula)throws IllegalArgumentException{
        try{
            value = calculate(formula);
            if(right >= 0)throw new IllegalArgumentException(",が先頭に有ります");
        }catch (Exception e){
            throw e;
        }
    }
    public Node(double d){
        this.value = d;
    }
    public Node(){//なんかこれが無いと継承時に面倒そうなのでとりあえず実装
    }
    public final double getValue() {
        return this.value;
    }
}