package com.example.bntwng.scriptcalculator.functions;

import com.example.bntwng.scriptcalculator.Node;

public class FunctionNode extends Node {
    public enum functions3{
        //三角関数
        SIN,
        COS,
        TAN,
        CSC,
        SEC,
        COT,
        //基礎的な関数
        LOG,
        POW,
        //その他関数
        ABS,
        EXP,
        MAX,
        MIN,
    }
    public enum functions4{
        //逆三角関数
        ASIN,
        ACOS,
        ATAN,
        ACSC,
        ASEC,
        ACOT,
        //双曲線関数
        SINH,
        COSH,
        TANH,
        CSCH,
        SECH,
        COTH,
        //ルート
        SQRT,
        ROOT,
    }
    //3文字系関数
    protected double logCalculate(String formula) throws IllegalArgumentException{
        try {
            double d1 = calculate(formula);//とりあえず消しきれなかったときはエラーを吐いておく、後で,区切りに関して実装
            if (right >= 0) {
                double d2 = calculate(formula.substring(0,right));//カンマ後はsubstringを取り、自動でrightが減るため、ここでrightを触る必要は無い
                if(right>=0)throw new IllegalArgumentException("log関数内に文法エラー");
                return Math.log10(d1) / Math.log10(d2);
            }
            return Math.log(d1);
        }catch (Exception e){
            throw e;
        }
    }
    protected double powCalculate(String formula)throws IllegalArgumentException{
        try{
            double d1 = calculate(formula);
            if (right >= 0) {
                double d2 = calculate(formula.substring(0,right));//カンマ後はsubstringを取り、自動でrightが減るため、ここでrightを触る必要は無い
                if(right>=0)throw new IllegalArgumentException("pow関数内に文法エラー");
                return Math.pow(d2,d1);
            }else{
                throw new IllegalArgumentException("pow関数への入力が少ないです");
            }

        }catch (Exception e){
            throw e;
        }
    }
    protected double maxCalculate(String formula) throws IllegalArgumentException {
        try {
            if(formula.charAt(0)==',')throw new IllegalArgumentException("max関数の先頭に,");
            double d1 = calculate(formula);
            double d2;
            while (right>=0){
                if(d1 < (d2 = calculate(formula.substring(0,right))))d1 = d2;
            }
            return d1;
        }catch (Exception e){
            throw e;
        }
    }
    protected double minCalculate(String formula) throws IllegalArgumentException {
        try {
            if(formula.charAt(0)==',')throw new IllegalArgumentException("max関数の先頭に,");
            double d1 = calculate(formula);
            double d2;
            while (right>=0){
                if(d1 > (d2 = calculate(formula.substring(0,right))))d1 = d2;
            }
            return d1;
        }catch (Exception e){
            throw e;
        }
    }
    //4文字系関数
    protected double rootCalculate(String formula) throws IllegalArgumentException{
        try {
            double d1 = calculate(formula);//とりあえず消しきれなかったときはエラーを吐いておく、後で,区切りに関して実装
            if (right >= 0) {
                double d2 = calculate(formula.substring(0,right));//カンマ後はsubstringを取り、自動でrightが減るため、ここでrightを触る必要は無い
                if(right>=0)throw new IllegalArgumentException("root関数内に文法エラー");
                return Math.pow(d1,1/d2);
            }
            return Math.sqrt(d1);
        }catch (Exception e){
            throw e;
        }
    }

    public FunctionNode(String formula,functions3 f) throws IllegalArgumentException{
        try {
            switch (f) {
                //三角関数
                case SIN:
                    value = Math.sin(calculate(formula)%(2*Math.PI));
                    break;
                case COS:
                    value = Math.cos(calculate(formula)%(2*Math.PI));
                    break;
                case TAN:
                    value = Math.tan(calculate(formula)%(2*Math.PI));
                    break;
                case CSC:
                    value = 1/Math.sin(calculate(formula)%(2*Math.PI));
                    break;
                case SEC:
                    value = 1/Math.cos(calculate(formula)%(2*Math.PI));
                    break;
                case COT:
                    value = 1/Math.tan(calculate(formula)%(2*Math.PI));
                    break;
                //log
                case LOG:
                    value = logCalculate(formula);
                    break;
                //pow
                case POW:
                    value = powCalculate(formula);
                    break;
                //その他関数
                case ABS:
                    value = Math.abs(calculate(formula));
                    break;
                case EXP:
                    value = Math.exp(calculate(formula));
                    break;
                case MAX:
                    value = maxCalculate(formula);
                    break;
                case MIN:
                    value = minCalculate(formula);
                    break;
            }
        }catch (Exception e){
            throw e;
        }
    }
    public FunctionNode(String formula,functions4 f) throws IllegalArgumentException {
        try {
            switch (f){
                //逆三角関数
                case ASIN:
                    value = Math.asin(calculate(formula));
                    break;
                case ACOS:
                    value = Math.acos(calculate(formula));
                    break;
                case ATAN:
                    value = Math.atan(calculate(formula));
                    break;
                case ACSC:
                    value = 1/Math.asin(calculate(formula));
                    break;
                case ASEC:
                    value = 1/Math.acos(calculate(formula));
                    break;
                case ACOT:
                    value = 1/Math.atan(calculate(formula));
                    break;
                //双曲線関数
                case SINH:
                    value = Math.sinh(calculate(formula));
                    break;
                case COSH:
                    value = Math.cosh(calculate(formula));
                    break;
                case TANH:
                    value = Math.tanh(calculate(formula));
                    break;
                case CSCH:
                    value = 1/Math.sinh(calculate(formula));
                    break;
                case SECH:
                    value = 1/Math.cosh(calculate(formula));
                    break;
                case COTH:
                    value = 1/Math.tanh(calculate(formula));
                    break;
                //ルート、一応平方根も実装
                case SQRT:
                    value = Math.sqrt(calculate(formula));
                    break;
                case ROOT:
                    value = rootCalculate(formula);
                    break;
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
