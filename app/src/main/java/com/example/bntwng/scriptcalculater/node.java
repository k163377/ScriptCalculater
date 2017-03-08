package com.example.bntwng.scriptcalculater;

/**
 * Created by bntwng on 2017/03/07.
 */
import java.util.ArrayList;

public class node {
    protected int left,right;

    protected String Formula;
    protected ArrayList<node> nodeArray;
    ArrayList<Character> operatorArray;

    protected double value = 0;

    protected void nodeAnalysis() throws IllegalArgumentException{
        //カッコを検知してnodeArrayに一個のnodeを追加
        //カッコ直後にカッコが来る場合は？
        int depth = 1;

        this.right = this.left+1;//今読んでいる場所の一個右からスタート
        while(0<depth){
            char c = Formula.charAt(right);
            if(c=='(')depth++;
            else if(c==')')depth--;

            this.right++;

            if(Formula.length()<right){//後で例外を投げるように改変
                System.out.println("カッコの数が合いません");
                throw new IllegalArgumentException("カッコの数が合いません");
            }
        }

        nodeArray.add(new node(Formula.substring(left+1,right-1)));
        left = right;

        //return new node(Formula.substring(left+1,right-1));
    }

    protected void valueAnalysis()
            throws IllegalArgumentException{//数字入力をdoubleの変数として記憶
        boolean isDecimal;//このノードが小数かを判定するフラグ
        char c = Formula.charAt(this.left);

        if(c == '.')isDecimal = true;
        else isDecimal = false;

        String s = String.valueOf(c);

        while((left+=1) < Formula.length()){
            c = Formula.charAt(this.left);
            //小数点が入った時
            if(c == '.'){
                if(isDecimal == true){
                    System.out.println("1変数に小数点が複数含まれています");
                    throw new IllegalArgumentException("1つの数に小数点が複数含まれています");
                }
                else{
                    isDecimal = true;
                    s += c;
                }
            }
            //数字以外が来た時
            else if(CharMap.symbolInspection(c)!= CharMap.nonterminalSymbol.NUMBER){
                //return new FormulaAnalysis(Double.parseDouble(s));
                break;
            }
            //それ以外ならappend
            else s += c;
        }

        nodeArray.add(new node(Double.parseDouble(s)));
        //return new node(Double.parseDouble(s));
    }

    protected void calculatingPower(){//累乗
        for(int i = 0;i < operatorArray.size(); i++){
            char c = operatorArray.get(i);
            double d;

            if(c =='^'){
                d = Math.pow(nodeArray.get(i).getValue(),nodeArray.get(i+1).getValue());
                node f = new node(d);
                nodeArray.set(i,f);
                nodeArray.remove(i+1);
                operatorArray.remove(i);
                i--;
            }
        }
    }

    protected void calculatingMultiplication(){//掛け割剰余
        for(int i = 0;i < operatorArray.size(); i++){
            char c = operatorArray.get(i);

            double d;

            if(c =='*'){
                d = nodeArray.get(i).getValue() * nodeArray.get(i+1).getValue();
                node f = new node(d);
                nodeArray.set(i,f);
                nodeArray.remove(i+1);
                operatorArray.remove(i);
                i--;
            }else if(c == '/'){
                d = nodeArray.get(i).getValue() / nodeArray.get(i+1).getValue();
                node f = new node(d);
                nodeArray.set(i,f);
                nodeArray.remove(i+1);
                operatorArray.remove(i);
                i--;
            }else if(c == '%'){
                d = nodeArray.get(i).getValue() % nodeArray.get(i+1).getValue();
                node f = new node(d);
                nodeArray.set(i,f);
                nodeArray.remove(i+1);
                operatorArray.remove(i);
                i--;
            }
        }
    }

    protected void calculatingPermutationAndCombination(){//組み合わせと順列
        for(int i = 0;i < operatorArray.size(); i++){
            char c = operatorArray.get(i);

            long n,r,ans;
            ans=1;

            if(c =='P'||c == 'p'||c == 'C'||c == 'c'){//自然数かとnがrより大きいかのチェック
                n = (long)nodeArray.get(i).getValue();
                if(n < 0 || ((double)n != nodeArray.get(i).getValue())){
                    throw new IllegalArgumentException("n" + c + "rのnが不正です");
                }
                r = (long)nodeArray.get(i+1).getValue();
                if(r < 0 || ((double)r != nodeArray.get(i+1).getValue())){
                    throw new IllegalArgumentException("n" + c + "rのrが不正です");
                }
                if(n < r)throw new IllegalArgumentException("n" + c + "rでnよりrが大きいです");

                for(;(n-r) > 0;n--)ans*=n;//npr計算

                if(c == 'C'||c=='c'){//ncr計算
                    for(;r > 0;r--)ans/=r;
                }

                node f = new node((double)ans);
                nodeArray.set(i,f);
                nodeArray.remove(i+1);
                operatorArray.remove(i);
                i--;
            }
        }
    }

    protected void calculatingAddition(){//加算減算
        this.value = nodeArray.get(0).getValue();
        for(int i=1; i<nodeArray.size();i++){
            switch(operatorArray.get(i-1)){
                case '+':
                    this.value += nodeArray.get(i).getValue();
                    break;
                case '-':
                    this.value -= nodeArray.get(i).getValue();
                    break;
            }
        }
    }

    public node(String formula)
            throws IllegalArgumentException{

        Formula = formula;
        nodeArray = new ArrayList<node>();
        operatorArray = new ArrayList<Character>();

        boolean isNode = false;

        this.left=0;

        try{
            while(this.left<Formula.length()){
                char c = Formula.charAt(left);
                CharMap.nonterminalSymbol t = CharMap.symbolInspection(c);

                if(t == null){
                    switch(c){
                        //ノード系
                        case '(':
                            if(isNode == true)throw new IllegalArgumentException("演算子無しで値が連続しています");
                            nodeAnalysis();
                            isNode = true;
                            break;
                        case ')':
                            throw new IllegalArgumentException("カッコの数が合いません");

                            //定数系
                        case 'e':
                            if(isNode == true)throw new IllegalArgumentException("演算子無しで値が連続しています");
                            nodeArray.add(new node(Math.E));
                            left++;
                            isNode = true;
                            break;
                        case 'p':
                            if(Formula.charAt(left+1)=='i'){//piは2文字喰うので検査、それ以外はゴミ
                                if(isNode == true)throw new IllegalArgumentException("演算子無しで値が連続しています");
                                nodeArray.add(new node(Math.PI));
                                left+=2;
                                isNode = true;
                            }else{
                                throw new IllegalArgumentException(c + "の直後の値が不正です");
                            }
                            break;
                        default:
                            left++;
                    }
                }else{
                    switch(t){
                        case OPERATOR://演算子
                            //単項演算子
                            if (c == '!') {
                                if(nodeArray.isEmpty())throw new IllegalArgumentException(c + "が先頭にあります");
                                else if(isNode == false)throw new IllegalArgumentException("演算子が連続しています");

                                long x,y;
                                x = 1;
                                y = (long)nodeArray.get(nodeArray.size()-1).getValue();
                                if((double)y != nodeArray.get(nodeArray.size()-1).getValue()||y < 0)throw new IllegalArgumentException(c + "が非整数に適用されました");
                                while(y > 1){
                                    x *= y;
                                    y--;
                                }
                                node n = new node((double)x);
                                nodeArray.set(nodeArray.size()-1,n);
                                left++;
                                break;
                            }
                            //二項演算子
                            if(c == '-'){//マイナスが初めに来たら0から引く動作を追加
                                if(nodeArray.isEmpty())nodeArray.add(new node(0));
                                else if(isNode == false)throw new IllegalArgumentException("演算子が連続しています");
                            }else if(c == '+'){//プラスが初めに来たら無視してbreak
                                if(nodeArray.isEmpty())break;
                                else if(isNode == false)throw new IllegalArgumentException("演算子が連続しています");
                            }else{//それ以外は異常
                                if(nodeArray.isEmpty())throw new IllegalArgumentException(c + "が先頭にあります");
                                else if(isNode == false)throw new IllegalArgumentException("演算子が連続しています");
                            }
                            operatorArray.add(c);
                            left++;

                            isNode = false;

                            break;
                        case NUMBER://数字
                            if(isNode == true)throw new IllegalArgumentException("演算子無しで値が連続しています");
                            valueAnalysis();
                            isNode = true;
                            break;
                    }
                }
            }
        }catch(Exception e){
            throw e;
        }

        //演算前の整合性チェック
        if(nodeArray.size() != operatorArray.size()+1){
            throw new IllegalArgumentException("演算子の整合性が不正です");
        }

        //最終計算
        //まず累乗を消す
        calculatingPower();

        //掛け算と割り算と剰余を先に消しておく
        calculatingMultiplication();

        //次にnPrとnCrを消す
        calculatingPermutationAndCombination();

        //最後に加算と減算を処理
        calculatingAddition();

        //System.out.println("end");
    }
    public node(double v){//定数を挿入するときはこっち
        this.value = v;
    }

    public String getFormula(){
        return this.Formula;
    }
    public double getValue(){
        return this.value;
    }
}